<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Reports</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background-color: #f0f0f0;
      padding: 40px;
      max-width: 500px;
      margin: auto;
      text-align: center;
    }
    h2 {
      color: #444;
    }
    form {
      background: white;
      padding: 25px;
      border-radius: 10px;
      box-shadow: 0 0 10px #ccc;
    }
    select, input[type="submit"] {
      width: 100%;
      padding: 10px;
      margin-top: 15px;
      border-radius: 5px;
      border: 1px solid #ccc;
    }
    input[type="submit"] {
      background-color: #673ab7;
      color: white;
      border: none;
    }
    input[type="submit"]:hover {
      background-color: #512da8;
    }
    a {
      display: block;
      margin-top: 20px;
      text-decoration: none;
      color: #555;
    }
  </style>
</head>
<body>
  <h2>Generate Reports</h2>
  <form action="reports.jsp" method="post">
    <label>Select Report Type:</label>
    <select name="reportType" required>
      <option value="dailysales">Daily Sales</option>
      <option value="reorder">Reorder Level</option>
      <option value="stock">Stock Report</option>
      <option value="bill">Bill Report</option>
    </select>
    <input type="submit" value="Generate Report" />
  </form>
  <a href="${pageContext.request.contextPath}/cashier">Back to Menu</a>
</body>
</html>
