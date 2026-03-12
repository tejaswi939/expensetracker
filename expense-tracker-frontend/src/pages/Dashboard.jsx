import { useState, useEffect } from "react";
import AddExpense from "../components/AddExpense";
import ExpenseList from "../components/ExpenseList";
import API from "../services/api";

function Dashboard() {
  const [refresh, setRefresh] = useState(0);
  const [summary, setSummary] = useState({ total: 0, today: 0, count: 0 });

  const fetchSummary = async () => {
    try {
      const token = localStorage.getItem("token");
      const res = await API.get("/expenses", {
        headers: { Authorization: `Bearer ${token}` },
      });
      const expenses = res.data || [];
      const total = expenses.reduce((sum, e) => sum + Number(e.amount), 0);
      const todayStr = new Date().toDateString();
      const today = expenses
        .filter((e) => new Date(e.date || e.createdAt).toDateString() === todayStr)
        .reduce((sum, e) => sum + Number(e.amount), 0);
      setSummary({ total, today, count: expenses.length });
    } catch {
      // silent
    }
  };

  useEffect(() => { fetchSummary(); }, [refresh]);

  const handleAdd = () => setRefresh((r) => r + 1);

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <div>
          <h1>Expense Dashboard</h1>
          <p>Track and manage all your spending</p>
        </div>
      </div>

      <div className="summary-grid">
        <div className="summary-card red">
          <div className="summary-card-label">
            <span className="dot" />
            Total Spent
          </div>
          <div className="summary-card-value">
            ₹{summary.total.toLocaleString("en-IN")}
          </div>
          <div className="summary-card-sub">All time</div>
        </div>

        <div className="summary-card yellow">
          <div className="summary-card-label">
            <span className="dot" />
            Today
          </div>
          <div className="summary-card-value">
            ₹{summary.today.toLocaleString("en-IN")}
          </div>
          <div className="summary-card-sub">{new Date().toLocaleDateString("en-IN", { weekday: "long" })}</div>
        </div>

        <div className="summary-card green">
          <div className="summary-card-label">
            <span className="dot" />
            Transactions
          </div>
          <div className="summary-card-value">{summary.count}</div>
          <div className="summary-card-sub">Total recorded</div>
        </div>
      </div>

      <div className="dashboard-grid">
        <AddExpense onAdd={handleAdd} />
        <ExpenseList refresh={refresh} />
      </div>
    </div>
  );
}

export default Dashboard;