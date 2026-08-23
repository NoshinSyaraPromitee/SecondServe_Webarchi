import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute";
import RoleSelect from "./pages/RoleSelect";
import Login from "./pages/Login";
import SignupHotel from "./pages/SignupHotel";
import SignupNgo from "./pages/SignupNgo";
import SignupStaff from "./pages/SignupStaff";
import HotelDashboard from "./pages/HotelDashboard";
import KitchenDashboard from "./pages/KitchenDashboard";
import NgoPortal from "./pages/NgoPortal";

export default function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<RoleSelect />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup/hotel" element={<SignupHotel />} />
          <Route path="/signup/ngo" element={<SignupNgo />} />
          <Route path="/signup/staff" element={<SignupStaff />} />
          <Route path="/hotel" element={<ProtectedRoute userType="HOTEL_MANAGER"><HotelDashboard /></ProtectedRoute>} />
          <Route path="/kitchen" element={<ProtectedRoute userType="KITCHEN_STAFF"><KitchenDashboard /></ProtectedRoute>} />
          <Route path="/ngo" element={<ProtectedRoute userType="NGO"><NgoPortal /></ProtectedRoute>} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
  );
}