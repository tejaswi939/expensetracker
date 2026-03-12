import { Link, useLocation } from "react-router-dom";

function Navbar() {
  const location = useLocation();

  return (
    <nav className="navbar">
      <Link to="/" style={{ textDecoration: "none" }}>
        <h2 className="logo">Expense<span>Tracker</span></h2>
      </Link>

      <div className="nav-links">
        <Link to="/" className={location.pathname === "/" ? "active" : ""}>Home</Link>
        <Link to="/dashboard" className={location.pathname === "/dashboard" ? "active" : ""}>Dashboard</Link>
        <Link to="/login" className={location.pathname === "/login" ? "active" : ""}>Login</Link>
        <Link to="/signup" style={{
          background: "var(--accent)",
          color: "white",
          padding: "7px 16px",
          borderRadius: "8px",
          fontSize: "0.85rem",
          fontWeight: 600,
          textDecoration: "none",
          marginLeft: "8px",
          transition: "all 0.2s",
        }}>Get Started</Link>
      </div>
    </nav>
  );
}

export default Navbar;