import { useEffect, useState } from "react";
import API from "../services/api";

function ExpenseList({ refreshKey, onRefresh }) {

const [expenses, setExpenses] = useState([]);
const [editId, setEditId] = useState(null);
const [editData, setEditData] = useState({});

useEffect(() => {
  API.get("/expenses").then(res => setExpenses(res.data));
}, [refreshKey]);

const handleDelete = async (id) => {
  if (!window.confirm("Delete this expense?")) return;
  await API.delete("/expenses/" + id);
  onRefresh();
};

const startEdit = (expense) => {
  setEditId(expense.id);
  setEditData({
    title: expense.title,
    amount: expense.amount,
    category: expense.category,
    date: expense.date
  });
};

const handleEditSave = async (id) => {
  await API.put("/expenses/" + id, editData);
  setEditId(null);
  onRefresh();
};

return (

<div>

<h2>Expenses</h2>

{expenses.map(e => (
<div key={e.id} style={{ borderBottom: "1px solid #ccc", padding: "6px 0" }}>

  {editId === e.id ? (
    <span>
      <input value={editData.title} onChange={ev => setEditData({...editData, title: ev.target.value})} placeholder="Title"/>
      <input value={editData.amount} onChange={ev => setEditData({...editData, amount: ev.target.value})} placeholder="Amount"/>
      <input value={editData.category} onChange={ev => setEditData({...editData, category: ev.target.value})} placeholder="Category"/>
      <input type="date" value={editData.date} onChange={ev => setEditData({...editData, date: ev.target.value})}/>
      <button onClick={() => handleEditSave(e.id)}>Save</button>
      <button onClick={() => setEditId(null)}>Cancel</button>
    </span>
  ) : (
    <span>
      {e.title} — ₹{e.amount} [{e.category}] {e.date}
      <button onClick={() => startEdit(e)} style={{ marginLeft: "10px" }}>Edit</button>
      <button onClick={() => handleDelete(e.id)} style={{ marginLeft: "5px" }}>Delete</button>
    </span>
  )}

</div>
))}

</div>

);

}

export default ExpenseList;
