import { useEffect, useState } from "react";
import API from "../services/api";

function ReportView() {

const [monthly, setMonthly] = useState([]);
const [category, setCategory] = useState([]);

useEffect(() => {
  API.get("/expenses/report/monthly").then(res => setMonthly(res.data));
  API.get("/expenses/report/category").then(res => setCategory(res.data));
}, []);

const downloadCsv = () => {
  window.open("http://localhost:8080/expenses/export/csv", "_blank");
};

return (

<div>

<h2>Reports</h2>

<button onClick={downloadCsv}>Download CSV</button>

<div style={{ display: "flex", gap: "40px", marginTop: "10px" }}>

  <div>
    <h3>Monthly Spend</h3>
    {monthly.length === 0 && <p>No data</p>}
    {monthly.map(m => (
      <div key={m.month}>{m.month}: ₹{parseFloat(m.total).toFixed(2)}</div>
    ))}
  </div>

  <div>
    <h3>By Category</h3>
    {category.length === 0 && <p>No data</p>}
    {category.map(c => (
      <div key={c.category}>{c.category}: ₹{parseFloat(c.total).toFixed(2)}</div>
    ))}
  </div>

</div>

</div>

);

}

export default ReportView;
