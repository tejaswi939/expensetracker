import { useEffect,useState } from "react";
import API from "../services/api";

function ExpenseList({ refreshKey }){

const [expenses,setExpenses] = useState([]);

useEffect(()=>{

API.get("/expenses")
.then(res=>setExpenses(res.data));

},[refreshKey])

return(

<div>

<h2>Expenses</h2>

{
expenses.map(e=>(
<div key={e.id}>
{e.title} - ₹{e.amount}
</div>
))
}

</div>

)

}

export default ExpenseList;