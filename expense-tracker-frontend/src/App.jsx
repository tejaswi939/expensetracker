import { useState } from "react";
import AddExpense from "./components/AddExpense";
import ExpenseList from "./components/ExpenseList";

function App(){

const [refreshKey, setRefreshKey] = useState(0);

return(

<div>

<h1>Expense Tracker</h1>

<AddExpense onAdd={() => setRefreshKey(k => k + 1)}/>

<ExpenseList refreshKey={refreshKey}/>

</div>

)

}

export default App;