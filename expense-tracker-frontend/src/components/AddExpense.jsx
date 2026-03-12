import { useState } from "react";
import API from "../services/api";

const CATEGORIES = [
  { value: "food", label: "🍔 Food & Dining", cls: "cat-food" },
  { value: "transport", label: "🚗 Transport", cls: "cat-transport" },
  { value: "health", label: "💊 Health", cls: "cat-health" },
  { value: "housing", label: "🏠 Housing", cls: "cat-housing" },
  { value: "entertainment", label: "🎬 Entertainment", cls: "cat-entertainment" },
  { value: "other", label: "📦 Other", cls: "cat-other" },
];

function AddExpense({ onAdd }) {
  const [title, setTitle] = useState("");
  const [amount, setAmount] = useState("");
  const [category, setCategory] = useState("other");
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState(null);

  const handleAdd = async () => {
    if (!title.trim() || !amount || isNaN(amount) || Number(amount) <= 0) {
      setMsg({ type: "error", text: "Please enter a valid title and amount." });
      return;
    }
    setLoading(true);
    setMsg(null);
    try {
      const token = localStorage.getItem("token");
      const res = await API.post(
        "/expenses",
        { title: title.trim(), amount: Number(amount), category },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setTitle("");
      setAmount("");
      setCategory("other");
      setMsg({ type: "success", text: "Expense added!" });
      if (onAdd) onAdd(res.data);
      setTimeout(() => setMsg(null), 2500);
    } catch {
      setMsg({ type: "error", text: "Failed to add expense." });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="add-expense-card">
      <div className="card-title">
        <div className="icon">➕</div>
        Add Expense
      </div>

      <div className="add-expense-form">
        <input
          placeholder="What did you spend on?"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />

        <input
          type="number"
          placeholder="Amount (₹)"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          min="0"
        />

        <select value={category} onChange={(e) => setCategory(e.target.value)}>
          {CATEGORIES.map((c) => (
            <option key={c.value} value={c.value}>{c.label}</option>
          ))}
        </select>

        {msg && <div className={`alert alert-${msg.type}`}>{msg.text}</div>}

        <button className="add-expense-btn" onClick={handleAdd} disabled={loading}>
          {loading ? "Adding..." : "➕ Add Expense"}
        </button>
      </div>
    </div>
  );
}

export default AddExpense;