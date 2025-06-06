import React from "react";
import { jwtDecode } from "jwt-decode";
import { useNavigate } from "react-router-dom";
import axios from "../api/axios";
import { getAccessToken } from "../util/tokenStorage";

type DecodedToken = {
  sub: string;
  email: string;
  roles: string[];
  iat: number;
  exp: number;
};

const ProfilePage = () => {
  const navigate = useNavigate();
  const accessToken = getAccessToken();

  const decoded: DecodedToken | null = accessToken
    ? jwtDecode<DecodedToken>(accessToken)
    : null;

  const handleLogout = async () => {
    try {
      if (accessToken) {
        await axios.post("/api/auth/logout", null, {
          headers: { Authorization: `Bearer ${accessToken}` },
        });
      }
    } catch (error) {
      console.error("Logout request failed", error);
    } finally {
      clearTokens();
      navigate("/login");
    }
  };

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
              className="w-full py-2 px-4 bg-purple-500 hover:bg-purple-700 rounded-md text-white font-semibold"
              onClick={() => navigate("/change-password")}
            >
              Change Password
            </button>

            <button
              className="w-full mt-2 py-2 px-4 bg-red-500 hover:bg-red-700 rounded-md text-white font-semibold"
              onClick={handleLogout}
            >
              Logout
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