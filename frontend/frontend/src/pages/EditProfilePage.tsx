import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { userApi } from "../api/axios";
import defaultAvatar from "../assets/avatar.jpg";

type ProfileData = {
  nickname: string;
  bio: string | null;
  avatarUrl: string | null;
  publicProfile: boolean;
};

const EditProfilePage = () => {
  const navigate = useNavigate();
  const [profile, setProfile] = useState<ProfileData>({
    nickname: "",
    bio: "",
    avatarUrl: null,
    publicProfile: true,
  });
  const [avatarFile, setAvatarFile] = useState<File | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadProfile = async () => {
      try {
        const res = await userApi.get("/api/users/me");
        setProfile({
          nickname: res.data.nickname,
          bio: res.data.bio || "",
          avatarUrl: res.data.avatarUrl || null,
          publicProfile: res.data.publicProfile,
        });
      } catch (err) {
        console.error("Failed to load profile", err);
        alert("Failed to load profile");
      } finally {
        setLoading(false);
      }
    };
    loadProfile();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      let avatarUrl = profile.avatarUrl;

      if (avatarFile) {
        const formData = new FormData();
        formData.append("file", avatarFile);
        const res = await userApi.post("/api/users/me/avatar", formData);
        avatarUrl = res.data;
      }

      await userApi.put("/api/users/me", {
        nickname: profile.nickname,
        bio: profile.bio,
        publicProfile: profile.publicProfile,
        avatarUrl,
      });

      alert("Profile updated!");
      navigate("/me");
    } catch (err) {
      console.error("Failed to update profile", err);
      alert("Error updating profile");
    }
  };

  if (loading) return <div className="text-white p-8">Loading...</div>;

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white">
      <div className="max-w-md w-full bg-gray-800 p-8 rounded-xl shadow-xl space-y-6">
        <h2 className="text-2xl font-bold text-center">Edit Profile</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="flex flex-col items-center">
            <img
              src={
                avatarFile
                  ? URL.createObjectURL(avatarFile)
                  : profile.avatarUrl || defaultAvatar
              }
              alt="avatar"
              className="w-24 h-24 rounded-full mb-2"
            />
            <input
              type="file"
              accept="image/*"
              onChange={(e) => {
                if (e.target.files && e.target.files.length > 0) {
                  setAvatarFile(e.target.files[0]);
                }
              }}
              className="text-sm text-gray-300"
            />
          </div>

          <div>
            <label className="block mb-1">Nickname</label>
            <input
              type="text"
              value={profile.nickname}
              onChange={(e) => setProfile({ ...profile, nickname: e.target.value })}
              className="w-full p-2 rounded bg-gray-700 text-white border border-gray-600"
              required
            />
          </div>

          <div>
            <label className="block mb-1">Bio</label>
            <textarea
              value={profile.bio || ""}
              onChange={(e) => setProfile({ ...profile, bio: e.target.value })}
              className="w-full p-2 rounded bg-gray-700 text-white border border-gray-600"
            />
          </div>

          <div className="flex items-center space-x-2">
            <input
              type="checkbox"
              checked={profile.publicProfile}
              onChange={(e) =>
                setProfile({ ...profile, publicProfile: e.target.checked })
              }
              className="form-checkbox text-purple-600 bg-gray-700 border-gray-600"
            />
            <label>Make profile public</label>
          </div>

          <button
            type="submit"
            className="w-full py-2 bg-purple-500 hover:bg-purple-700 rounded text-white font-semibold"
          >
            Save Changes
          </button>
        </form>
      </div>
    </div>
  );
};

export default EditProfilePage;