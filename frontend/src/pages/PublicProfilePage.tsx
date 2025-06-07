import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getPublicProfile } from "../api/user";
import defaultAvatar from "../assets/avatar.jpg";

type UserProfile = {
  nickname: string;
  avatarUrl: string | null;
  bio: string | null;
};

const PublicProfilePage = () => {
  const { nickname } = useParams();
  const navigate = useNavigate();
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!nickname) return;
    getPublicProfile(nickname)
      .then(setProfile)
      .catch((err: any) => {
        console.error("User not found", err);
        setProfile(null);
      })
      .finally(() => setLoading(false));
  }, [nickname]);

  if (loading) return <div className="text-white p-6">Loading...</div>;
  if (!profile) return <div className="text-white p-6">User not found</div>;

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white">
      <div className="max-w-md w-full bg-gray-800 p-8 rounded-xl shadow space-y-6 text-center relative">
        {/* Кнопка "Назад" */}
        <button
          onClick={() => navigate(-1)}
          className="absolute left-4 top-4 text-white text-xl hover:text-purple-400"
        >
          ← Back
        </button>

        <h1 className="text-3xl font-bold">Public Profile</h1>
        <img
          src={profile.avatarUrl || defaultAvatar}
          alt="avatar"
          className="w-24 h-24 mx-auto rounded-full"
        />
        <p><strong>Nickname:</strong> {profile.nickname}</p>
        {profile.bio && <p><strong>Bio:</strong> {profile.bio}</p>}
      </div>
    </div>
  );
};

export default PublicProfilePage;