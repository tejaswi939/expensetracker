import { Link } from "react-router-dom";

function Home() {
  return (
    <div className="home">
      <div className="home-badge">
        ✦ Smart Financial Tracking
      </div>

      <h1>
        Control Every <span className="highlight">Rupee</span><br />
        You Spend
      </h1>

      <p>
        Track your spending habits, visualize where your money goes,
        and take back control of your finances — all in one place.
      </p>

      <div className="home-actions">
        <Link to="/signup" className="btn-primary">
          Start for Free →
        </Link>
        <Link to="/dashboard" className="btn-ghost">
          View Demo
        </Link>
      </div>

      <div className="home-stats">
        <div className="home-stat">
          <div className="home-stat-number">₹0</div>
          <div className="home-stat-label">Hidden Fees</div>
        </div>
        <div className="home-divider" />
        <div className="home-stat">
          <div className="home-stat-number">100%</div>
          <div className="home-stat-label">Private & Secure</div>
        </div>
        <div className="home-divider" />
        <div className="home-stat">
          <div className="home-stat-number">∞</div>
          <div className="home-stat-label">Transactions</div>
        </div>
      </div>
    </div>
  );
}

export default Home;