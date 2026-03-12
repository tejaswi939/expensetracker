import { useEffect, useState } from "react";
import API from "../services/api";

function BudgetManager() {

const [category, setCategory] = useState("");
const [limit, setLimit] = useState("");
const [budgets, setBudgets] = useState([]);
const [status, setStatus] = useState([]);

const loadData = () => {
  API.get("/budgets").then(res => setBudgets(res.data));
  API.get("/budgets/status").then(res => setStatus(res.data));
};

useEffect(() => { loadData(); }, []);

const saveBudget = async () => {
  await API.post("/budgets", { category, monthlyLimit: parseFloat(limit) });
  alert("Budget saved!");
  loadData();
};

return (

<div>

<h2>Budget Limits</h2>

<input
  placeholder="Category"
  onChange={(e) => setCategory(e.target.value)}
/>
<input
  placeholder="Monthly Limit (₹)"
  onChange={(e) => setLimit(e.target.value)}
/>
<button onClick={saveBudget}>Set Budget</button>

{status.length > 0 && (
<div>
  <h3>This Month's Status</h3>
  {status.map(s => (
    <div key={s.category} style={{ color: s.exceeded ? "red" : "green", margin: "4px 0" }}>
      {s.category}: ₹{s.spent} / ₹{s.limit} — {s.exceeded ? "EXCEEDED" : `₹${s.remaining.toFixed(2)} remaining`}
    </div>
  ))}
</div>
)}

{budgets.length > 0 && (
<div>
  <h3>Configured Budgets</h3>
  {budgets.map(b => (
    <div key={b.id}>{b.category}: ₹{b.monthlyLimit}/month</div>
  ))}
</div>
)}

</div>

);

}

export default BudgetManager;
