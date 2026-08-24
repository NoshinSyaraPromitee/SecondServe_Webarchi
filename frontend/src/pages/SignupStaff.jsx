import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { api, session } from "../api";
import Layout from "../components/Layout";
import ErrorText from "../components/ErrorText";

export default function SignupStaff() {
    const navigate = useNavigate();

    const [form, setForm] = useState({
        staffName: "",
        email: "",
        password: "",
        hotelCode: "",
        position: "",
    });

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    function update(field) {
        return (e) =>
            setForm({
                ...form,
                [field]: e.target.value,
            });
    }

    async function handleSubmit(e) {
        e.preventDefault();

        setError("");
        setLoading(true);

        try {
            const res =
                await api.registerKitchenStaff(form);

            session.save(res);
            navigate("/kitchen");
        } catch (err) {
            setError(
                err.message ||
                "Registration failed. Check the hotel code."
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
                        <div className="hero-eyebrow">
                            Kitchen team
                        </div>


                    </div>

                    <form
                        onSubmit={handleSubmit}
                        className="form"
                    >
                        <label>
                            Name

                            <input
                                required
                                placeholder="Your full name"
                                value={form.staffName}
                                onChange={update("staffName")}
                            />
                        </label>

                        <label>
                            Email

                            <input
                                type="email"
                                required
                                placeholder="you@example.com"
                                value={form.email}
                                onChange={update("email")}
                            />
                        </label>

                        <label>
                            Password

                            <input
                                type="password"
                                required
                                placeholder="Create a password"
                                value={form.password}
                                onChange={update("password")}
                            />
                        </label>

                        <label>
                            Hotel code

                            <input
                                required
                                placeholder="Enter your hotel's code"
                                value={form.hotelCode}
                                onChange={update("hotelCode")}
                            />
                        </label>

                        <label>
                            Position

                            <input
                                placeholder="e.g. Chef"
                                value={form.position}
                                onChange={update("position")}
                            />
                        </label>

                        <p className="muted">
                            Ask your hotel manager for the
                            hotel's registration code.
                        </p>

                        <ErrorText>{error}</ErrorText>

                        <button
                            type="submit"
                            disabled={loading}
                        >
                            {loading
                                ? "Creating account..."
                                : "Create staff account"}
                        </button>
                    </form>
                </div>
            </div>
        </Layout>
    );
}