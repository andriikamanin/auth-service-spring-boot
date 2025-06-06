import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { authApi } from "../api/axios";
import { getAccessToken } from "../util/tokenStorage";

const ChangePasswordPage = () => {
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const token = getAccessToken();
      await authApi.post(
        "/api/auth/change-password",
        { currentPassword, newPassword },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      alert("Password changed successfully");
      navigate("/me");
    } catch (error: any) {
      console.error("Failed to change password", error);
      const msg =
        error?.response?.data?.message || error?.response?.data?.error || "Error changing password";
      alert(msg);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white">
      <div className="max-w-md w-full bg-gray-800 p-8 rounded-xl shadow-xl space-y-6">
        <h2 className="text-2xl font-bold text-center">Change Password</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block mb-1 text-sm">Current Password</label>
            <input
              type="password"
              value={currentPassword}
              onChange={(e) => setCurrentPassword(e.target.value)}
              className="w-full p-2 rounded bg-gray-700 text-white border border-gray-600"
              required
            />
          </div>
          <div>
            <label className="block mb-1 text-sm">New Password</label>
            <input
              type="password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              className="w-full p-2 rounded bg-gray-700 text-white border border-gray-600"
              required
            />
          </div>
          <button
            type="submit"
            className="w-full py-2 bg-purple-500 hover:bg-purple-700 rounded text-white font-semibold"
          >
            Change Password
          </button>
        </form>
      </div>
    </div>
  );
};

export default ChangePasswordPage;