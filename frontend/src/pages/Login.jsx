import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { api, session } from "../api";
import Layout from "../components/Layout";
import ErrorText from "../components/ErrorText";

export default function Login() {
    const navigate = useNavigate();

    const params = new URLSearchParams(window.location.search);

    const [userType, setUserType] = useState(
        params.get("type") || "HOTEL_MANAGER"
    );

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    async function handleSubmit(e) {
        e.preventDefault();

        setError("");
        setLoading(true);

        try {
            const res = await api.login(email, password, userType);

            session.save(res);

            if (userType === "HOTEL_MANAGER") {
                navigate("/hotel");
            } else if (userType === "KITCHEN_STAFF") {
                navigate("/kitchen");
            } else {
                navigate("/ngo");
            }
        } catch (err) {
            setError(
                err.message ||
                "Login failed. Please check your credentials."
            );
        } finally {
            setLoading(false);
        }
    }

    return (
        <Layout>
            <div className="auth-shell">
                <div className="auth-card">
                    <div className="auth-header">


                        <h2>Sign in to SecondServe</h2>

                    </div>

                    <form onSubmit={handleSubmit} className="form">
                        <label>
                            I am a

                            <select
                                value={userType}
                                onChange={(e) =>
                                    setUserType(e.target.value)
                                }
                            >
                                <option value="HOTEL_MANAGER">
                                    Hotel Manager
                                </option>

                                <option value="KITCHEN_STAFF">
                                    Kitchen Staff
                                </option>

                                <option value="NGO">
                                    NGO
                                </option>
                            </select>
                        </label>

                        <label>
                            Email

                            <input
                                type="email"
                                placeholder="you@example.com"
                                required
                                value={email}
                                onChange={(e) =>
                                    setEmail(e.target.value)
                                }
                            />
                        </label>

                        <label>
                            Password

                            <input
                                type="password"
                                placeholder="Enter your password"
                                required
                                value={password}
                                onChange={(e) =>
                                    setPassword(e.target.value)
                                }
                            />
                        </label>

                        <ErrorText>{error}</ErrorText>

                        <button type="submit" disabled={loading}>
                            {loading
                                ? "Signing in..."
                                : "Sign in"}
                        </button>
                    </form>
                    <div className="auth-footer">
                        <p className="muted">
                            Don't have an account?
                        </p>

                        <div className="auth-options">
                            <Link to="/signup/hotel" className="auth-option">
                                Register a hotel
                            </Link>

                            <Link to="/signup/ngo" className="auth-option">
                                Register an NGO
                            </Link>

                            <Link to="/signup/staff" className="auth-option">
                                Join as staff
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </Layout>
    );
}