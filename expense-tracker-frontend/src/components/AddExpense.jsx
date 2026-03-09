import { useState } from "react";
import API from "../services/api";

function AddExpense(){

const [title,setTitle] = useState("");
const [amount,setAmount] = useState("");
const [category,setCategory] = useState("");

const submit = async () => {

await API.post("/expenses",{
title,
amount,
category
});

alert("Expense Added");

};

return(

<div>

<h2>Add Expense</h2>

<input
placeholder="Title"
onChange={(e)=>setTitle(e.target.value)}
/>

<input
placeholder="Amount"
onChange={(e)=>setAmount(e.target.value)}
/>

<input
placeholder="Category"
onChange={(e)=>setCategory(e.target.value)}
/>

<button onClick={submit}>Add</button>

</div>

)

}

export default AddExpense;