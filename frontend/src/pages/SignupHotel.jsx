import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { api } from "../api";
import Layout from "../components/Layout";
import ErrorText from "../components/ErrorText";

export default function SignupHotel() {
    const navigate = useNavigate();

    const [form, setForm] = useState({
        hotelName: "",
        managerName: "",
        email: "",
        password: "",
        address: "",
        hotelLicense: "",
        phone: "",
        city: "",
        state: "",
        postalCode: "",
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
            await api.registerHotel(form);
            navigate("/login?type=HOTEL_MANAGER");
        } catch (err) {
            setError(
                err.message ||
                "Registration failed."
            );
        } finally {
            setLoading(false);
        }
    }

    return (
        <Layout>
            <div className="food-form-card">
                <div className="food-form-header">
                    <div className="hero-eyebrow">
                        Hotel registration
                    </div>

                    <h2>Register your hotel</h2>

                    <p>
                        Create a SecondServe account for your
                        hotel and start managing food donations.
                    </p>
                </div>

                <form
                    onSubmit={handleSubmit}
                    className="form"
                >
                    <div className="form-grid">
                        <label>
                            Hotel name

                            <input
                                required
                                placeholder="Your hotel name"
                                value={form.hotelName}
                                onChange={update("hotelName")}
                            />
                        </label>

                        <label>
                            Manager name

                            <input
                                required
                                placeholder="Manager's full name"
                                value={form.managerName}
                                onChange={update("managerName")}
                            />
                        </label>

                        <label>
                            Email

                            <input
                                type="email"
                                required
                                placeholder="hotel@example.com"
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
                            Phone

                            <input
                                placeholder="+880..."
                                value={form.phone}
                                onChange={update("phone")}
                            />
                        </label>

                        <label>
                            Hotel license #

                            <input
                                value={form.hotelLicense}
                                onChange={update("hotelLicense")}
                            />
                        </label>

                        <label className="full">
                            Address

                            <input
                                value={form.address}
                                onChange={update("address")}
                            />
                        </label>

                        <label>
                            City

                            <input
                                value={form.city}
                                onChange={update("city")}
                            />
                        </label>

                        <label>
                            State

                            <input
                                value={form.state}
                                onChange={update("state")}
                            />
                        </label>

                        <label>
                            Postal code

                            <input
                                value={form.postalCode}
                                onChange={update("postalCode")}
                            />
                        </label>
                    </div>

                    <ErrorText>{error}</ErrorText>

                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {loading
                            ? "Creating account..."
                            : "Create hotel account"}
                    </button>
                </form>
            </div>
        </Layout>
    );
}