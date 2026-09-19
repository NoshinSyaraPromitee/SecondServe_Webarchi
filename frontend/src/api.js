const BASE_URL = "http://localhost:8080/api";



function getToken() {
  return localStorage.getItem("token");
}

async function request(path, { method = "GET", body, auth = false } = {}) {
  const headers = { "Content-Type": "application/json" };
  if (auth) {
    const token = getToken();
    if (token) headers["Authorization"] = `Bearer ${token}`;
  }

  const res = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });

  if (!res.ok) {
    let message = `Request failed (${res.status})`;
    try {
      const data = await res.json();
      if (data?.message) message = data.message;
    } catch {
      // response had no JSON body
    }
    throw new Error(message);
  }

  if (res.status === 204) return null;
  const text = await res.text();
  return text ? JSON.parse(text) : null;
}

export const api = {


  // Auth
  login: (email, password, userType) =>
      request("/auth/login", { method: "POST", body: { email, password, userType } }),




  // Registration
  registerHotel: (payload) => request("/hotels/register", { method: "POST", body: payload }),
  registerNgo: (payload) => request("/ngos/register", { method: "POST", body: payload }),
  registerKitchenStaff: (payload) => request("/staff/register", { method: "POST", body: payload }),

  // Hotel dashboard
  getDashboardStats: () => request("/hotels/dashboard-stats", { auth: true }),
  getPendingFoodItems: (hotelId) => request(`/food-items/hotel/${hotelId}/pending`, { auth: true }),
  getTodaysFoodItems: (hotelId) => request(`/food-items/hotel/${hotelId}/today`, { auth: true }),
  getHotelFoodRequests: (hotelId, status) =>
      request(`/food-requests/hotel/${hotelId}${status ? `?status=${status}` : ""}`, { auth: true }),
  getHotelFoodLog: (hotelId) => request(`/food-items/hotel/${hotelId}/log`, { auth: true }),

  getAvailableFoodItems: () => request("/food-items/available", { auth: true }),
  createFoodItem: (payload) => request("/food-items", { method: "POST", body: payload, auth: true }),
  getMyFoodLog: () => request("/food-items/my-log", { auth: true }),
  approveFoodItem: (id) => request(`/food-items/${id}/approve`, { method: "PUT", auth: true }),
  rejectFoodItem: (id) => request(`/food-items/${id}`, { method: "DELETE", auth: true }),
  markFoodItemUnavailable: (id) => request(`/food-items/${id}/unavailable`, { method: "PUT", auth: true }),
  getCloudinarySignature: () => request("/cloudinary/signature", { auth: true }),


  //  Food requests
  createFoodRequest: (payload) => request("/food-requests", { method: "POST", body: payload, auth: true }),
  approveFoodRequest: (id) => request(`/food-requests/${id}/approve`, { method: "PUT", auth: true }),
  rejectFoodRequest: (id) => request(`/food-requests/${id}/reject`, { method: "PUT", auth: true }),
  completeFoodRequest: (id) => request(`/food-requests/${id}/complete`, { method: "PUT", auth: true }),
  getNgoFoodRequests: (ngoId) => request(`/food-requests/ngo/${ngoId}`, { auth: true }),
};

export const session = {
  save(authResponse) {
    localStorage.setItem("token", authResponse.token);
    localStorage.setItem("user", JSON.stringify(authResponse));
  },
  get() {
    const raw = localStorage.getItem("user");
    return raw ? JSON.parse(raw) : null;
  },
  clear() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  },

};
export async function uploadImageToCloudinary(file) {
  const sigRes = await request("/cloudinary/signature", { auth: true });

  const formData = new FormData();
  formData.append("file", file);
  formData.append("api_key", sigRes.apiKey);
  formData.append("timestamp", sigRes.timestamp);
  formData.append("signature", sigRes.signature);
  formData.append("folder", sigRes.folder);

  const uploadRes = await fetch(
    `https://api.cloudinary.com/v1_1/${sigRes.cloudName}/image/upload`,
    { method: "POST", body: formData }
  );

    if (!uploadRes.ok) {
      const errBody = await uploadRes.json().catch(() => null);
      console.error("Cloudinary upload error:", errBody);
      throw new Error(errBody?.error?.message || "Image upload failed. Please try again.");
    }
  const data = await uploadRes.json();
  return data.secure_url;
}
