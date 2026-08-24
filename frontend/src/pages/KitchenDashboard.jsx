import { useEffect, useState } from "react";

import Layout from "../components/Layout";
import ErrorText from "../components/ErrorText";
import { api, uploadImageToCloudinary } from "../api";

const CONDITION_LABELS = {
    FRESH: "Fresh",
    GOOD: "Good",
    NEAR_EXPIRY: "Near expiry",
};

function formatDateTime(value) {
    if (!value) return "—";
    const d = new Date(value);
    if (Number.isNaN(d.getTime())) return value;
    return d.toLocaleString([], {
        dateStyle: "medium",
        timeStyle: "short",
    });
}

export default function KitchenDashboard() {
    const [form, setForm] = useState({
        foodName: "",
        quantity: "",
        unit: "kg",
        expiryDate: "",
        description: "",
        category: "PREPARED_FOOD",
        condition: "FRESH",
    });
    const [imageFile, setImageFile] = useState(null);
    const [imagePreview, setImagePreview] = useState(null);
    const [uploading, setUploading] = useState(false);

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);

    const [log, setLog] = useState([]);
    const [logError, setLogError] = useState("");
    const [logLoading, setLogLoading] = useState(true);

    async function loadLog() {
        setLogLoading(true);
        setLogError("");

        try {
            const items = await api.getMyFoodLog();
            setLog(items);
        } catch (err) {
            setLogError(
                err.message ||
                "Could not load the surplus food log."
            );
        } finally {
            setLogLoading(false);
        }
    }

    useEffect(() => {
        loadLog();
    }, []);

    function update(field) {
        return (e) => {
            setForm({
                ...form,
                [field]: e.target.value,
            });
        };
    }
    function handleImageChange(e) {
        const file = e.target.files[0];
        if (!file) return;
        setImageFile(file);
        setImagePreview(URL.createObjectURL(file));
    }
    async function handleSubmit(e) {
        e.preventDefault();

        setError("");
        setSuccess("");
        setLoading(true);

        try {
            let imageUrl = null;
            if (imageFile) {
                setUploading(true);
                try{
                imageUrl = await uploadImageToCloudinary(imageFile);}
                finally{
                setUploading(false);}
            }
            await api.createFoodItem({
                ...form,
                quantity: Number(form.quantity),
                imageUrl
            });

            setSuccess(
                "Food logged successfully. Your hotel manager will review it shortly."
            );

            setForm({
                foodName: "",
                quantity: "",
                unit: "kg",
                expiryDate: "",
                description: "",
                category: "PREPARED_FOOD",
                condition: "FRESH",
            });
            setImageFile(null);
            setImagePreview(null);
            loadLog();
        } catch (err) {
            setError(
                err.message ||
                "Could not log this item."
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
                        Kitchen inventory
                    </div>

                    <div>

                    </div>
                    <h2>Log surplus food</h2>


                </div>

                <form
                    onSubmit={handleSubmit}
                    className="form"
                >
                    <div className="form-grid">
                        <label>
                            Food name

                            <input
                                required
                                placeholder="e.g. Vegetable rice"
                                value={form.foodName}
                                onChange={update("foodName")}
                            />
                        </label>

                        <label>
                            Quantity

                            <input
                                type="number"
                                step="0.1"
                                min="0"
                                required
                                placeholder="0.0"
                                value={form.quantity}
                                onChange={update("quantity")}
                            />
                        </label>

                        <label>
                            Unit

                            <select
                                value={form.unit}
                                onChange={update("unit")}
                            >
                                <option value="kg">
                                    Kilograms
                                </option>

                                <option value="pieces">
                                    Pieces
                                </option>

                                <option value="liters">
                                    Liters
                                </option>

                                <option value="servings">
                                    Servings
                                </option>
                            </select>
                        </label>

                        <label>
                            Expiry date

                            <input
                                type="date"
                                required
                                value={form.expiryDate}
                                onChange={update("expiryDate")}
                            />
                        </label>

                        <label>
                            Category

                            <select
                                value={form.category}
                                onChange={update("category")}
                            >
                                <option value="PREPARED_FOOD">
                                    Prepared food
                                </option>

                                <option value="INGREDIENTS">
                                    Ingredients
                                </option>
                            </select>
                        </label>

                        <label>
                            Condition

                            <select
                                value={form.condition}
                                onChange={update("condition")}
                            >
                                <option value="FRESH">
                                    Fresh
                                </option>

                                <option value="GOOD">
                                    Good
                                </option>

                                <option value="NEAR_EXPIRY">
                                    Near expiry
                                </option>
                            </select>
                        </label>

                        <label className="full">
                            Notes

                            <textarea
                                placeholder="Optional notes about the food..."
                                value={form.description}
                                onChange={update("description")}
                            />
                        </label>
                        <label className="full">
                            Food photo

                            <input
                                type="file"
                                accept="image/*"
                                onChange={handleImageChange}
                            />
                        </label>

                        {imagePreview && (
                            <img
                                src={imagePreview}
                                alt="Preview"
                                style={{ maxWidth: "150px", borderRadius: "8px" }}
                            />
                        )}
                    </div>

                    <ErrorText>{error}</ErrorText>

                    {success && (
                        <p className="success">
                            {success}
                        </p>
                    )}

                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {uploading
                            ? "Uploading image..."
                            : loading
                            ? "Logging food..."
                            : "Log surplus food"}
                    </button>
                </form>
            </div>

            <section className="dashboard-section">
                <div className="dashboard-section-header">
                    <div>
                        <h3>Surplus food log</h3>

                    </div>

                    <span className="badge pending">
                        {log.length} entries
                    </span>
                </div>

                <ErrorText>{logError}</ErrorText>

                {logLoading ? (
                    <div className="empty-state">
                        Loading log...
                    </div>
                ) : log.length === 0 ? (
                    <div className="empty-state">
                        You haven't logged any food yet.
                    </div>
                ) : (
                    <ul className="list">
                        {log.map((item) => (
                            <li
                                key={item.id}
                                className="list-row"
                            >
                                <div>
                                    <strong>
                                        {item.foodName}
                                    </strong>

                                    <div className="muted">
                                        {item.quantity}{" "}
                                        {item.unit}
                                        {" · "}
                                        Condition:{" "}
                                        {CONDITION_LABELS[
                                            item.condition
                                            ] || item.condition}
                                        {" · "}
                                        Expires{" "}
                                        {item.expiryDate}
                                    </div>

                                    <div className="muted">
                                        Logged{" "}
                                        {formatDateTime(
                                            item.createdDate
                                        )}
                                    </div>
                                </div>

                                <span
                                    className={`badge ${
                                        item.isAvailable
                                            ? "approved"
                                            : "pending"
                                    }`}
                                >
                                    {item.isAvailable
                                        ? "Available to NGOs"
                                        : "Pending review"}
                                </span>
                            </li>
                        ))}
                    </ul>
                )}
            </section>
        </Layout>
    );
}