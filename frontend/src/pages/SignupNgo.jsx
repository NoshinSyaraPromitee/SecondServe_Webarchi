import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { api } from "../api";
import Layout from "../components/Layout";
import ErrorText from "../components/ErrorText";

export default function SignupNgo() {
    const navigate = useNavigate();

    const [form, setForm] = useState({
        ngoName: "",
        contactPerson: "",
        email: "",
        password: "",
        phone: "",
        address: "",
        city: "",
        state: "",
        postalCode: "",
        licenseNumber: "",
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
            await api.registerNgo(form);
            navigate("/login?type=NGO");
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
                        NGO registration
                    </div>

                    <h2>Register your NGO</h2>

                    <p>
                        Join SecondServe and connect your
                        organization with surplus food donors.
                    </p>
                </div>

                <form
                    onSubmit={handleSubmit}
                    className="form"
                >
                    <div className="form-grid">
                        <label>
                            NGO name

                            <input
                                required
                                placeholder="Organization name"
                                value={form.ngoName}
                                onChange={update("ngoName")}
                            />
                        </label>

                        <label>
                            Contact person

                            <input
                                required
                                placeholder="Full name"
                                value={form.contactPerson}
                                onChange={update("contactPerson")}
                            />
                        </label>

                        <label>
                            Email

                            <input
                                type="email"
                                required
                                placeholder="ngo@example.com"
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
                            License #

                            <input
                                value={form.licenseNumber}
                                onChange={update("licenseNumber")}
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
                            : "Create NGO account"}
                    </button>
                </form>
            </div>
        </Layout>
    );
}