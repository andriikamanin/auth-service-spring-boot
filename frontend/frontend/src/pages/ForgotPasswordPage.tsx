// src/pages/ForgotPasswordPage.tsx

import React, { useState } from "react";
import { authApi } from "../api/axios"; // ✅ Исправленный импорт

const ForgotPasswordPage = () => {
  const [email, setEmail] = useState("");
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await authApi.post("/api/auth/forgot-password", { email }); // ✅ Используем authApi
      setSubmitted(true);
    } catch (error) {
      console.error("Failed to send reset email", error);
      alert("Something went wrong. Please try again.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white">
      <div className="max-w-md w-full bg-gray-800 p-8 rounded-xl shadow-xl space-y-6">
        <h2 className="text-2xl font-bold text-center">Forgot Password</h2>
        {submitted ? (
          <p className="text-center">Check your email for the reset link.</p>
        ) : (
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block mb-1">Email Address</label>
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full p-2 rounded bg-gray-700 text-white border border-gray-600"
              />
            </div>
            <button
              type="submit"
              className="w-full py-2 bg-purple-500 hover:bg-purple-700 rounded text-white font-semibold"
            >
              Send Reset Link
            </button>
          </form>
        )}
      </div>
    </div>
  );
};

export default ForgotPasswordPage;