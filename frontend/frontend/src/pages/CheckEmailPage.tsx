// src/pages/CheckEmailPage.tsx

import React from "react";

const CheckEmailPage = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white px-4">
      <div className="max-w-md text-center space-y-6">
        <h1 className="text-3xl font-bold">📩 Check your email</h1>
        <p>
          We’ve sent you a confirmation link. <br />
          Please check your inbox and follow the instructions to verify your account.
        </p>
      </div>
    </div>
  );
};

export default CheckEmailPage;