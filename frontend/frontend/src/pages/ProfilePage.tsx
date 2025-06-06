import React from "react";
import { jwtDecode } from "jwt-decode";
import { useNavigate } from "react-router-dom";

type DecodedToken = {
  sub: string;
  email: string;
  roles: string[];
  iat: number;
  exp: number;
};

const ProfilePage = () => {
  const navigate = useNavigate();
  const accessToken = localStorage.getItem("accessToken");

  const decoded: DecodedToken | null = accessToken
    ? jwtDecode<DecodedToken>(accessToken)
    : null;

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white">
      <div className="max-w-md w-full p-8 bg-gray-800 rounded-xl shadow-xl space-y-6">
        <h1 className="text-3xl font-bold text-center">Your Profile</h1>
        {decoded ? (
          <div className="space-y-2">
            <p><strong>Email:</strong> {decoded.email}</p>
            <p><strong>Roles:</strong> {decoded.roles.join(", ")}</p>
            <p><strong>User ID:</strong> {decoded.sub}</p>

            <button
              className="mt-4 w-full py-2 px-4 bg-purple-500 hover:bg-purple-700 rounded-md text-white font-semibold"
              onClick={() => navigate("/change-password")}
            >
              Change Password
            </button>
          </div>
        ) : (
          <p>Invalid or missing token</p>
        )}
      </div>
    </div>
  );
};

export default ProfilePage;