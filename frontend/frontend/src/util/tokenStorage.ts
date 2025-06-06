export function getAccessToken(): string | null {
  return localStorage.getItem("accessToken") || sessionStorage.getItem("accessToken");
}

export function getRefreshToken(): string | null {
  return localStorage.getItem("refreshToken") || sessionStorage.getItem("refreshToken");
}

export function saveTokens(access: string, refresh: string) {
  localStorage.setItem("accessToken", access);
  localStorage.setItem("refreshToken", refresh);
}

export function clearTokens() {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  sessionStorage.removeItem("accessToken");
  sessionStorage.removeItem("refreshToken");
}