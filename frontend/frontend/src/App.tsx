import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import CheckEmailPage from "./pages/CheckEmailPage";
import VerifyEmailPage from "./pages/VerifyEmailPage";
import ProfilePage from "./pages/ProfilePage";
import ChangePasswordPage from "./pages/ChangePasswordPage";
import ForgotPasswordPage from "./pages/ForgotPasswordPage";
import ResetPasswordPage from "./pages/ResetPasswordPage";
import EditProfilePage from "./pages/EditProfilePage";
import SearchProfilePage from "./pages/SearchProfilePage";
import HomePage from "./pages/HomePage";
import PublicProfilePage from "./pages/PublicProfilePage";
import ProfileSettingsPage from "./pages/ProfileSettingsPage";


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/check-email" element={<CheckEmailPage />} />
        <Route path="/verify-email" element={<VerifyEmailPage />} />
        <Route path="/me" element={<ProfilePage />} />
        <Route path="/me/edit" element={<EditProfilePage />} />
      
        <Route path="/change-password" element={<ChangePasswordPage />} />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />
        <Route path="/reset-password" element={<ResetPasswordPage />} />
        <Route path="/search" element={<SearchProfilePage />} />
        <Route path="/" element={<HomePage />} />
        <Route path="/u/:nickname" element={<PublicProfilePage />} />
        <Route path="/me/settings" element={<ProfileSettingsPage />} />
      </Routes>
    </Router>
  );
}




export default App;








