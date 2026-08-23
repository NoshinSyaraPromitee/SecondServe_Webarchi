import { useState } from "react";
import { session } from "../api";

export function useUser() {
    const [user, setUser] = useState(session.get());
    return [user, setUser];
}