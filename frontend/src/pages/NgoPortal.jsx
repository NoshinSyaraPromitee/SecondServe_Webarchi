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

        // No push/websocket notifications exist in this app — the only way
        // the NGO finds out a hotel approved an item is by re-fetching.
        // Poll every 10s so newly approved food (and request status changes)
        // show up without the NGO having to manually refresh the page.
        const intervalId = setInterval(loadAll, 10000);
        return () => clearInterval(intervalId);
    }, []);

    async function requestItem(item) {
        setNotice("");
        setError("");

        const qty = window.prompt(
            `How much ${item.unit} of "${item.foodName}" would you like to request?`,
            item.quantity
        );

        if (!qty) return;

        try {
            await api.createFoodRequest({
                ngoId: user.userId,
                foodItemId: item.id,
                requestedQuantity: Number(qty),
                unit: item.unit,
            });

            setNotice(
                "Request sent successfully."
            );

            loadAll();
        } catch (err) {
            setError(
                err.message ||
                "Could not send request."
            );
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
                                    {
                                        item.currentUserRequestStatus
                                    }
                                </span>
                            ) : (
                                <button
                                    onClick={() =>
                                        requestItem(item)
                                    }
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
        </Layout>
    );
}