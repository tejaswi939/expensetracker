import { useState } from "react";
import AddExpense from "./components/AddExpense";
import ExpenseList from "./components/ExpenseList";
import BudgetManager from "./components/BudgetManager";
import ReportView from "./components/ReportView";

function App() {

const [refreshKey, setRefreshKey] = useState(0);

const refresh = () => setRefreshKey(k => k + 1);

return (

<div style={{ padding: "20px", fontFamily: "sans-serif", maxWidth: "800px" }}>

<h1>Expense Tracker</h1>

<AddExpense onAdd={refresh} />

<hr/>

<ExpenseList refreshKey={refreshKey} onRefresh={refresh} />

<hr/>

<BudgetManager />

<hr/>

<ReportView />

</div>

);

}

export default App;
