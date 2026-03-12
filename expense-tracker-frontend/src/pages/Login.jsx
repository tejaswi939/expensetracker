import { useState } from "react";
import { Link } from "react-router-dom";
import API from "../services/api";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState(null);

  const login = async () => {
    if (!email || !password) {
      setMsg({ type: "error", text: "Please fill in all fields." });
      return;
    }
    setLoading(true);
    setMsg(null);
    try {
      const res = await API.post("/auth/login", { email, password });
      localStorage.setItem("token", res.data);
      setMsg({ type: "success", text: "Login successful! Redirecting..." });
    } catch (err) {
      setMsg({ type: "error", text: err?.response?.data || "Login failed. Check your credentials." });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-card-header">
          <h2>Welcome back</h2>
          <p>Sign in to your account to continue</p>
        </div>

        <div className="form-group">
          <div className="input-wrapper">
            <label>Email Address</label>
            <input
              type="email"
              placeholder="you@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          <div className="input-wrapper">
            <label>Password</label>
            <input
              type="password"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && login()}
            />
          </div>
        </div>

        {msg && (
          <div className={`alert alert-${msg.type}`}>{msg.text}</div>
        )}

        <button className="auth-btn" onClick={login} disabled={loading}>
          {loading ? "Signing in..." : "Sign In →"}
        </button>

        <div className="auth-footer">
          Don't have an account?{" "}
          <Link to="/signup">Create one free</Link>
        </div>
      </div>
    </div>
  );
}

export default Login;