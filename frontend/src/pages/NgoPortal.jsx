import { useEffect, useState } from "react";
import { api, session } from "../api";
import Layout from "../components/Layout";
import ErrorText from "../components/ErrorText";

export default function NgoPortal() {
    const user = session.get();

    const [items, setItems] = useState([]);
    const [myRequests, setMyRequests] = useState([]);
    const [error, setError] = useState("");
    const [notice, setNotice] = useState("");

    const [requestModalItem, setRequestModalItem] = useState(null);
    const [requestQty, setRequestQty] = useState("");
    const [submitting, setSubmitting] = useState(false);

    async function loadAll() {
        try {
            const [available, mine] =
                await Promise.all([
                    api.getAvailableFoodItems(),
                    api.getNgoFoodRequests(user.userId),
                ]);

            setItems(available);
            setMyRequests(mine);
        } catch (err) {
            setError(
                err.message ||
                "Could not load available food."
            );
        }
    }

    useEffect(() => {
        loadAll();

        const intervalId = setInterval(loadAll, 10000);
        return () => clearInterval(intervalId);
    }, []);

    function openRequestModal(item) {
        setError("");
        setNotice("");
        setRequestQty(String(item.quantity));
        setRequestModalItem(item);
    }

    function closeRequestModal() {
        setRequestModalItem(null);
        setRequestQty("");
    }

    async function confirmRequest() {
        const qty = Number(requestQty);

        if (!requestQty || Number.isNaN(qty) || qty <= 0) {
            setError("Enter a valid quantity.");
            return;
        }

        setSubmitting(true);

        try {
            await api.createFoodRequest({
                ngoId: user.userId,
                foodItemId: requestModalItem.id,
                requestedQuantity: qty,
                unit: requestModalItem.unit,
            });

            setNotice("Request sent successfully.");
            closeRequestModal();
            loadAll();
        } catch (err) {
            setError(
                err.message ||
                "Could not send request."
            );
        } finally {
            setSubmitting(false);
        }
    }

    return (
        <Layout>
            <div className="dashboard-header">
                <div>
                    <h2>Available food</h2>
                </div>

                <span className="badge approved">
                    {items.length} available
                </span>
            </div>

            <ErrorText>{error}</ErrorText>

            {notice && (
                <p className="success">
                    {notice}
                </p>
            )}

            {items.length === 0 ? (
                <div className="dashboard-section">
                    <div className="empty-state">
                        <div style={{ fontSize: "2rem" }}>
                            🍽️
                        </div>

                        <h3>No food available right now</h3>

                        <p className="muted">
                            Check back soon for new
                            donations from hotels.
                        </p>
                    </div>
                </div>
            ) : (
                <div className="food-grid">
                    {items.map((item) => (
                        <div
                            key={item.id}
                            className="food-card"
                        >
                            <div className="food-card-top">
                                <div>
                                    <h3>
                                        {item.foodName}
                                    </h3>

                                    <p className="muted">
                                        {item.hotelName}
                                    </p>
                                </div>

                                <div className="food-quantity">
                                    {item.quantity}{" "}
                                    {item.unit}
                                </div>
                            </div>

                            {item.imageUrl && (
                                <img
                                    src={item.imageUrl}
                                    alt={item.foodName}
                                    style={{ width: "100%", height: "140px", objectFit: "cover", borderRadius: "8px", marginBottom: "8px" }}
                                />
                            )}

                            <div className="food-meta">
                                <span
                                    style={{
                                        border: "1px solid #dc2626",
                                        color: "#dc2626",
                                        backgroundColor: "#fef2f2",
                                        padding: "4px 8px",
                                        borderRadius: "6px",
                                        display: "inline-block",
                                        fontSize: "13px",
                                        fontWeight: "500",
                                    }}
                                >
                                    Expires {item.expiryDate}
                                </span>

                                {item.description && (
                                    <span>
                                        {item.description}
                                    </span>
                                )}
                            </div>

                            {item.currentUserRequestStatus ? (
                                <span
                                    className={`badge ${item.currentUserRequestStatus.toLowerCase()}`}
                                >
                                    {item.currentUserRequestStatus}
                                </span>
                            ) : (
                                <button
                                    onClick={() => openRequestModal(item)}
                                >
                                    Request this food
                                </button>
                            )}
                        </div>
                    ))}
                </div>
            )}

            <section className="dashboard-section">
                <div className="dashboard-section-header">
                    <div>
                        <h3>My requests</h3>
                    </div>
                </div>

                {myRequests.length === 0 ? (
                    <div className="empty-state">
                        You haven't requested any food yet.
                    </div>
                ) : (
                    <ul className="list">
                        {myRequests.map((req) => (
                            <li
                                key={req.id}
                                className="list-row"
                            >
                                <div>
                                    <strong>
                                        {req.foodItemName}
                                    </strong>

                                    <div className="muted">
                                        {req.requestedQuantity}{" "}
                                        {req.unit} ·{" "}
                                        {req.hotelName}
                                    </div>
                                </div>

                                <span
                                    className={`badge ${req.requestStatus?.toLowerCase()}`}
                                >
                                    {req.requestStatus}
                                </span>
                            </li>
                        ))}
                    </ul>
                )}
            </section>

            {requestModalItem && (
                <div
                    className="modal-overlay"
                    onClick={closeRequestModal}
                >
                    <div
                        className="modal-card"
                        onClick={(e) => e.stopPropagation()}
                    >
                        <h3>Request food</h3>
                        <p>
                            How much {requestModalItem.unit} of "{requestModalItem.foodName}" would you like to request?
                        </p>

                        <input
                            type="number"
                            step="0.1"
                            min="0"
                            autoFocus
                            value={requestQty}
                            onChange={(e) => setRequestQty(e.target.value)}
                            onKeyDown={(e) => {
                                if (e.key === "Enter") confirmRequest();
                            }}
                        />

                        <ErrorText>{error}</ErrorText>

                        <div className="modal-actions">
                            <button
                                className="secondary"
                                onClick={closeRequestModal}
                                disabled={submitting}
                            >
                                Cancel
                            </button>
                            <button
                                onClick={confirmRequest}
                                disabled={submitting}
                            >
                                {submitting ? "Sending..." : "Send request"}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </Layout>
    );
}