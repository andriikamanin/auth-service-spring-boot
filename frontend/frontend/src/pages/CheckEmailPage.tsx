import React from "react";
import { useNavigate } from "react-router-dom";

const CheckEmailPage = () => {
  const navigate = useNavigate();

  const goToLogin = () => {
    navigate("/login");
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white px-4">
      <div className="max-w-md w-full bg-gray-800 p-8 rounded-xl shadow-lg text-center space-y-6">
        <h1 className="text-3xl font-bold">📩 Check your email</h1>
        <p className="text-gray-300">
          We’ve sent a confirmation link to your email address.
          <br />
          Please open your inbox and click the link to verify your account.
        </p>
        <button
          onClick={goToLogin}
          className="w-full py-2 px-6 bg-purple-600 hover:bg-purple-700 rounded-md text-white font-semibold transition"
        >
          Go to Login
        </button>
      </div>
    </div>
  );
};

export default CheckEmailPage;