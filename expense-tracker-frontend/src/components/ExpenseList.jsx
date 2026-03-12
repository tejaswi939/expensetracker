import { useEffect, useState } from "react";
import API from "../services/api";

const CATEGORY_META = {
  food:          { icon: "🍔", cls: "cat-food" },
  transport:     { icon: "🚗", cls: "cat-transport" },
  health:        { icon: "💊", cls: "cat-health" },
  housing:       { icon: "🏠", cls: "cat-housing" },
  entertainment: { icon: "🎬", cls: "cat-entertainment" },
  other:         { icon: "📦", cls: "cat-other" },
};

function ExpenseList({ refresh }) {
  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchExpenses = async () => {
    try {
      const token = localStorage.getItem("token");
      const res = await API.get("/expenses", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setExpenses(res.data || []);
    } catch {
      setExpenses([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchExpenses(); }, [refresh]);

  const deleteExpense = async (id) => {
    try {
      const token = localStorage.getItem("token");
      await API.delete(`/expenses/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setExpenses((prev) => prev.filter((e) => e._id !== id));
    } catch {
      alert("Could not delete expense.");
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return "";
    return new Date(dateStr).toLocaleDateString("en-IN", {
      day: "numeric", month: "short",
    });
  };

  return (
    <div className="expense-list-card">
      <div className="expense-list-header">
        <div className="card-title" style={{ marginBottom: 0 }}>
          <div className="icon">📋</div>
          Recent Expenses
        </div>
        <span className="expense-count">{expenses.length} items</span>
      </div>

      {loading ? (
        <div className="empty-state">
          <div className="empty-state-icon">⏳</div>
          <p>Loading expenses...</p>
        </div>
      ) : expenses.length === 0 ? (
        <div className="empty-state">
          <div className="empty-state-icon">💸</div>
          <p>No expenses yet. Add your first one!</p>
        </div>
      ) : (
        <div className="expense-items">
          {expenses.map((exp) => {
            const meta = CATEGORY_META[exp.category] || CATEGORY_META.other;
            return (
              <div className="expense-item" key={exp._id}>
                <div className={`expense-icon ${meta.cls}`}>{meta.icon}</div>
                <div className="expense-info">
                  <div className="expense-name">{exp.title}</div>
                  <div className="expense-category">
                    {exp.category?.charAt(0).toUpperCase() + exp.category?.slice(1) || "Other"}
                  </div>
                </div>
                <div className="expense-right">
                  <div className="expense-amount">-₹{Number(exp.amount).toLocaleString("en-IN")}</div>
                  <div className="expense-date">{formatDate(exp.date || exp.createdAt)}</div>
                </div>
                <button
                  className="expense-delete"
                  onClick={() => deleteExpense(exp._id)}
                  title="Delete"
                >
                  ✕
                </button>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}

export default ExpenseList;