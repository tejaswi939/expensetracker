import { useState } from "react";
import API from "../services/api";

function AddExpense({ onAdd }) {

const [title, setTitle] = useState("");
const [amount, setAmount] = useState("");
const [category, setCategory] = useState("");
const [date, setDate] = useState("");

const submit = async () => {
  const res = await API.post("/expenses", { title, amount, category, date });
  const data = res.data;

  if (data.budgetAlert) {
    alert("Expense Added!\n\n⚠ " + data.budgetAlert);
  } else {
    alert("Expense Added!");
  }
  onAdd();
};

return (

<div>

<h2>Add Expense</h2>

<input
  placeholder="Title"
  onChange={(e) => setTitle(e.target.value)}
/>

<input
  placeholder="Amount"
  onChange={(e) => setAmount(e.target.value)}
/>

<input
  placeholder="Category"
  onChange={(e) => setCategory(e.target.value)}
/>

<input
  type="date"
  onChange={(e) => setDate(e.target.value)}
/>

<button onClick={submit}>Add</button>

</div>

);

}

export default AddExpense;
