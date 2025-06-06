// src/api/userApi.ts
import { userApi } from "./axios";

// Получить текущий профиль
export async function getMyProfile() {
  const res = await userApi.get("/api/users/me");
  return res.data;
}

// Получить публичный профиль по точному нику
export async function getPublicProfile(nickname: string) {
  const res = await userApi.get(`/api/users/nickname/${nickname}`);
  return res.data;
}

// Поиск пользователей по части ника
export async function searchProfiles(query: string) {
  const res = await userApi.get(`/api/users/search?query=${encodeURIComponent(query)}`);
  return res.data; // массив профилей
}

// Обновить профиль
export async function updateMyProfile(data: {
  nickname?: string;
  bio?: string;
  avatarUrl?: string;
  publicProfile?: boolean;
}) {
  return await userApi.put("/api/users/me", data);
}

// Загрузить аватар (multipart/form-data)
export async function uploadAvatar(file: File) {
  const formData = new FormData();
  formData.append("file", file);

  const res = await userApi.post("/api/users/me/avatar", formData, {
    headers: {
      // Axios сам установит правильный Content-Type
    },
  });

  return res.data; // avatarUrl string
}

