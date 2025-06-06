// src/pages/VerifyEmailPage.tsx

import React, { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import axios from "../api/axios";

const VerifyEmailPage = () => {
  const [searchParams] = useSearchParams();
  const [status, setStatus] = useState<"verifying" | "success" | "error">("verifying");
  const navigate = useNavigate();

  useEffect(() => {
    const token = searchParams.get("token");
    if (!token) {
      setStatus("error");
      return;
    }

    const verifyEmail = async () => {
      try {
        await axios.get(`/api/auth/verify?token=${token}`);
        setStatus("success");
        setTimeout(() => navigate("/login"), 3000);
      } catch (error) {
        setStatus("error");
      }
    };

    verifyEmail();
  }, [searchParams, navigate]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white">
      <div className="text-center">
        {status === "verifying" && <p>Verifying your email...</p>}
        {status === "success" && <p>Your email has been verified! Redirecting to login...</p>}
        {status === "error" && <p>Verification failed. Please try again or contact support.</p>}
      </div>
    </div>
  );
};

export default VerifyEmailPage;
