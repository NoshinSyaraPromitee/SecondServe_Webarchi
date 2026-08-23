import { Navigate } from "react-router-dom";
import { session } from "../api";

export default function ProtectedRoute({ userType, children }) {
    const user = session.get();
    if (!user) return <Navigate to="/login" replace />;
    if (userType && user.userType !== userType) return <Navigate to="/" replace />;
    return children;
}