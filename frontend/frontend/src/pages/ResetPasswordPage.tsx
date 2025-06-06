import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "../api/axios";

const ResetPasswordPage = () => {
  const [newPassword, setNewPassword] = useState("");
  const navigate = useNavigate();

  const searchParams = new URLSearchParams(useLocation().search);
  const token = searchParams.get("token");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!token) return alert("Missing token");

    try {
      await axios.post("/api/auth/reset-password", {
        token,
        newPassword,
      });
      alert("Password reset successfully");
      navigate("/login");
    } catch (error) {
      console.error("Reset failed", error);
      alert("Failed to reset password");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white">
      <div className="max-w-md w-full bg-gray-800 p-8 rounded-xl shadow-xl space-y-6">
        <h2 className="text-2xl font-bold text-center">Reset Password</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block mb-1">New Password</label>
            <input
              type="password"
              required
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              className="w-full p-2 rounded bg-gray-700 text-white border border-gray-600"
            />
          </div>
          <button
            type="submit"
            className="w-full py-2 bg-purple-500 hover:bg-purple-700 rounded text-white font-semibold"
          >
            Reset Password
          </button>
        </form>
      </div>
    </div>
  );
};

export default ResetPasswordPage;