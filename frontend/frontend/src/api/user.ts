// src/api/userApi.ts

import { userApi } from "./axios";

// Получить текущий профиль
export async function getMyProfile() {
  const res = await userApi.get("/api/users/me");
  return res.data;
}

// Получить публичный профиль по нику
export async function getPublicProfile(nickname: string) {
  const res = await userApi.get(`/api/users/nickname/${nickname}`);
  return res.data;
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
      // ВАЖНО: НЕ УСТАНАВЛИВАЙ Content-Type ВРУЧНУЮ
      // Axios сам поставит правильный multipart/form-data + boundary
    },
  });

  return res.data; // URL строки с avatarUrl
}