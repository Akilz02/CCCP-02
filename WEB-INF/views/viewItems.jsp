<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>View Items</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background-color: #f2f2f2;
      padding: 30px;
    }
    h2 {
      text-align: center;
      margin-bottom: 20px;
    }
    table {
      width: 90%;
      margin: auto;
      border-collapse: collapse;
      background: #fff;
      box-shadow: 0 0 10px #ccc;
    }
    th, td {
      padding: 15px;
      border: 1px solid #ddd;
      text-align: center;
    }
    th {
      background-color: #4CAF50;
      color: white;
    }
    a {
      display: block;
      text-align: center;
      margin-top: 20px;
      text-decoration: none;
      color: #555;
    }
  </style>
</head>
<body>
  <h2>Available Items</h2>
  <table>
    <thead>
      <tr>
        <th>Item ID</th>
        <th>Name</th>
        <th>Unit Price</th>
      </tr>
    </thead>
    <tbody>
      <!-- Sample row -->
      <tr>
        <td>ITEM001</td>
        <td>Example Product</td>
        <td>12.99</td>
      </tr>
      <!-- Add dynamic rows using JSP -->
    </tbody>
  </table>
  <a href="${pageContext.request.contextPath}/cashier">Back to Online Menu</a>
</body>
</html>
