import { Link, useNavigate } from "react-router-dom";
import { session } from "../api";
import { useUser } from "../hooks/useUser";
import logo from "../assets/Images/logo.png";

export default function Layout({ children }) {
    const [user] = useUser();
    const navigate = useNavigate();

    function handleLogout() {
        session.clear();
        navigate("/");
    }

    const roleName = user?.userType
        ?.replaceAll("_", " ")
        ?.toLowerCase()
        ?.replace(/\b\w/g, (c) => c.toUpperCase());

    return (
        <div className="page">
            <header className="topbar">
                <Link to="/" className="brand">
                    <img
                        src={logo}
                        alt="SecondServe logo"
                        className="brand-logo"
                    />

                    <span className="brand-name">
                        <span className="brand-second">Second</span>
                        <span className="brand-serve">Serve</span>
                    </span>
                </Link>

                {user && (
                    <div className="topbar-user">
                        <span>
                            {user.organizationName || user.name} · {roleName}
                        </span>

                        <button onClick={handleLogout}>
                            Log out
                        </button>
                    </div>
                )}
            </header>

            <main className="content">
                {children}
            </main>
        </div>
    );
}