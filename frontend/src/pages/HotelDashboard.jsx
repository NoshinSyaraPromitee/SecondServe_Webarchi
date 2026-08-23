import { useEffect, useState } from "react";
import { api, session } from "../api";
import Layout from "../components/Layout";
import ErrorText from "../components/ErrorText";

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

export default function HotelDashboard() {
    const user = session.get();

    const [stats, setStats] = useState(null);
    const [pending, setPending] = useState([]);
    const [requests, setRequests] = useState([]);
    const [error, setError] = useState("");

    const [showLog, setShowLog] = useState(false);
    const [log, setLog] = useState([]);
    const [logError, setLogError] = useState("");
    const [logLoading, setLogLoading] = useState(false);
    const [logLoaded, setLogLoaded] = useState(false);

    async function loadAll() {
        try {
            const [statsRes, pendingRes, reqRes] =
                await Promise.all([
                    api.getDashboardStats(),
                    api.getPendingFoodItems(user.userId),
                    api.getHotelFoodRequests(
                        user.userId,
                        "PENDING"
                    ),
                ]);

            setStats(statsRes);
            setPending(pendingRes);
            setRequests(reqRes);
        } catch (err) {
            setError(
                err.message ||
                "Could not load dashboard."
            );
        }
    }

    useEffect(() => {
        loadAll();
    }, []);

    async function loadLog() {
        setLogLoading(true);
        setLogError("");

        try {
            const items = await api.getHotelFoodLog(user.userId);
            setLog(items);
            setLogLoaded(true);
        } catch (err) {
            setLogError(
                err.message ||
                "Could not load the food log."
            );
        } finally {
            setLogLoading(false);
        }
    }

    function toggleLog() {
        const next = !showLog;
        setShowLog(next);

        if (next && !logLoaded) {
            loadLog();
        }
    }

    async function approveItem(id) {
        await api.approveFoodItem(id);
        loadAll();
    }

    async function rejectItem(id) {
        await api.rejectFoodItem(id);
        loadAll();
    }

    async function approveRequest(id) {
        await api.approveFoodRequest(id);
        loadAll();
    }

    async function rejectRequest(id) {
        await api.rejectFoodRequest(id);
        loadAll();
    }

    async function completeRequest(id) {
        await api.completeFoodRequest(id);
        loadAll();
    }

    return (
        <Layout>
            <div className="dashboard-header">
                <div>
                    <h2>Hotel dashboard</h2>

                </div>
            </div>

            <ErrorText>{error}</ErrorText>

            {stats && (
                <div className="dashboard-grid">
                    <div className="stat-card">
                        <span>
                            {stats.totalDonatedThisWeek ?? 0}
                        </span>

                        <small>
                            Donated this week
                        </small>
                    </div>

                    <div
                        className="stat-card clickable"
                        onClick={toggleLog}
                        role="button"
                        tabIndex={0}
                    >
                        <span>
                            {stats.totalLoggedThisWeek ?? 0}
                        </span>

                        <small>
                            Food logged this week
                        </small>
                    </div>

                    <div className="stat-card">
                        <span>
                            {stats.hotelCode ?? "—"}
                        </span>

                        <small>
                            Hotel code
                        </small>
                    </div>
                </div>
            )}

            {showLog && (
                <section className="dashboard-section">
                    <div className="dashboard-section-header">
                        <div>
                            <h3>Food log</h3>
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
                            Nothing logged yet.
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
            )}

            <section className="dashboard-section">
                <div className="dashboard-section-header">
                    <div>
                        <h3>Surplus food awaiting review</h3>

                    </div>

                    <span className="badge pending">
                        {pending.length} pending
                    </span>
                </div>

                {pending.length === 0 ? (
                    <div className="empty-state">
                        ✓ Everything is reviewed.
                    </div>
                ) : (
                    <ul className="list">
                        {pending.map((item) => (
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
                                        Expires{" "}
                                        {item.expiryDate}
                                    </div>
                                </div>

                                <div className="row-actions">
                                    <button
                                        onClick={() =>
                                            approveItem(item.id)
                                        }
                                    >
                                        Approve
                                    </button>

                                    <button
                                        className="danger"
                                        onClick={() =>
                                            rejectItem(item.id)
                                        }
                                    >
                                        Reject
                                    </button>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </section>

            <section className="dashboard-section">
                <div className="dashboard-section-header">
                    <div>
                        <h3>NGO donation requests</h3>


                    </div>

                    <span className="badge pending">
                        {requests.length} pending
                    </span>
                </div>

                {requests.length === 0 ? (
                    <div className="empty-state">
                        No pending requests right now.
                    </div>
                ) : (
                    <ul className="list">
                        {requests.map((req) => (
                            <li
                                key={req.id}
                                className="list-row"
                            >
                                <div>
                                    <strong>
                                        {req.ngoName}
                                    </strong>

                                    <div className="muted">
                                        Wants{" "}
                                        {req.requestedQuantity}{" "}
                                        {req.unit} of{" "}
                                        {req.foodItemName}
                                    </div>

                                    {req.notes && (
                                        <div className="muted">
                                            {req.notes}
                                        </div>
                                    )}
                                </div>

                                <div className="row-actions">
                                    {req.requestStatus ===
                                        "PENDING" && (
                                            <>
                                                <button
                                                    onClick={() =>
                                                        approveRequest(
                                                            req.id
                                                        )
                                                    }
                                                >
                                                    Approve
                                                </button>

                                                <button
                                                    className="danger"
                                                    onClick={() =>
                                                        rejectRequest(
                                                            req.id
                                                        )
                                                    }
                                                >
                                                    Reject
                                                </button>
                                            </>
                                        )}

                                    {req.requestStatus ===
                                        "APPROVED" && (
                                            <button
                                                onClick={() =>
                                                    completeRequest(
                                                        req.id
                                                    )
                                                }
                                            >
                                                Mark complete
                                            </button>
                                        )}
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </section>
        </Layout>
    );
}