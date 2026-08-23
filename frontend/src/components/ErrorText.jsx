export default function ErrorText({ children }) {
    if (!children) return null;
    return <p className="error">{children}</p>;
}