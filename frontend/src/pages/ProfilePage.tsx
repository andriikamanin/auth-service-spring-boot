import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { userApi, authApi } from "../api/axios";
import { getAccessToken, clearTokens } from "../util/tokenStorage";
import defaultAvatar from "../assets/avatar.jpg";

type UserProfile = {
  id: string;
  email: string | null;
  nickname: string;
  bio: string | null;
  avatarUrl: string | null;
  roles: string[] | null;
  publicProfile: boolean;
};

const ProfilePage = () => {
  const navigate = useNavigate();
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [loading, setLoading] = useState(true);

  const loadProfile = async () => {
    try {
      const res = await userApi.get("/api/users/me");
      setProfile(res.data);
    } catch (err) {
      console.error("❌ Failed to load profile", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (getAccessToken()) loadProfile();
    else setLoading(false);
  }, []);

  const handleLogout = async () => {
    try {
      await authApi.post("/api/auth/logout");
    } catch (err) {
      console.error("Logout failed", err);
    } finally {
      clearTokens();
      navigate("/login");
    }
  };

  if (loading) return <div className="text-white p-8">Loading...</div>;
  if (!profile) return <div className="text-white p-8">Unauthorized</div>;

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6 relative">
      <button
        onClick={() => navigate(-1)}
        className="absolute left-6 top-6 text-white text-xl hover:text-purple-400"
      >
        ← Back
      </button>

      <div className="max-w-md mx-auto mt-12 p-8 bg-gray-800 rounded-xl shadow-xl space-y-6">
        <h1 className="text-3xl font-bold text-center">Your Profile</h1>

        <img
          src={profile.avatarUrl || defaultAvatar}
          alt="avatar"
          className="w-32 h-32 rounded-full mx-auto"
        />

        <div className="space-y-2 text-sm text-center">
          <p><strong>Nickname:</strong> {profile.nickname}</p>
          {profile.email && <p><strong>Email:</strong> {profile.email}</p>}
          {profile.roles && <p><strong>Roles:</strong> {profile.roles.join(", ")}</p>}
          {profile.bio && <p><strong>Bio:</strong> {profile.bio}</p>}
        </div>

        <div className="space-y-2">
          <button
            className="w-full py-2 px-4 bg-blue-500 hover:bg-blue-700 rounded-md text-white font-semibold"
            onClick={() => navigate("/me/edit")}
          >
            Edit Profile
          </button>

          <button
            className="w-full py-2 px-4 bg-purple-500 hover:bg-purple-700 rounded-md text-white font-semibold"
            onClick={() => navigate("/change-password")}
          >
            Change Password
          </button>

          <button
            className="w-full py-2 px-4 bg-red-500 hover:bg-red-700 rounded-md text-white font-semibold"
            onClick={handleLogout}
          >
            Logout
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;