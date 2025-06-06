// src/pages/CheckEmailPage.tsx

import React from "react";
import { useNavigate } from "react-router-dom";

const CheckEmailPage = () => {
  const navigate = useNavigate();

  const goToLogin = () => {
    navigate("/login");
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white px-4">
      <div className="max-w-md text-center space-y-6">
        <h1 className="text-3xl font-bold">📩 Check your email</h1>
        <p>
          We’ve sent you a confirmation link. <br />
          Please check your inbox and follow the instructions to verify your account.
        </p>
        <button
          onClick={goToLogin}
          className="mt-4 py-2 px-6 bg-purple-600 hover:bg-purple-700 rounded-md text-white font-semibold transition"
        >
          Go to Login
        </button>
      </div>
    </div>
  );
};

export default CheckEmailPage;