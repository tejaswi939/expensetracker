import { useState } from "react";
import { Link } from "react-router-dom";
import API from "../services/api";

function Signup() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState(null);

  const signup = async () => {
    if (!name || !email || !password) {
      setMsg({ type: "error", text: "Please fill in all fields." });
      return;
    }
    if (password.length < 6) {
      setMsg({ type: "error", text: "Password must be at least 6 characters." });
      return;
    }
    setLoading(true);
    setMsg(null);
    try {
      await API.post("/auth/register", { name, email, password });
      setMsg({ type: "success", text: "Account created! You can now log in." });
    } catch (err) {
      setMsg({ type: "error", text: err?.response?.data || "Signup failed. Try again." });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-card-header">
          <h2>Create account</h2>
          <p>Start tracking your expenses today — it's free</p>
        </div>

        <div className="form-group">
          <div className="input-wrapper">
            <label>Full Name</label>
            <input
              placeholder="Your Name"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>

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
              placeholder="Min. 6 characters"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && signup()}
            />
          </div>
        </div>

        {msg && (
          <div className={`alert alert-${msg.type}`}>{msg.text}</div>
        )}

        <button className="auth-btn" onClick={signup} disabled={loading}>
          {loading ? "Creating account..." : "Create Account →"}
        </button>

        <div className="auth-footer">
          Already have an account?{" "}
          <Link to="/login">Sign in</Link>
        </div>
      </div>
    </div>
  );
}

export default Signup;