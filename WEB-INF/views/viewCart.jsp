<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Your Cart</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: #f4f4f4;
      padding: 30px;
    }
    h2 {
      text-align: center;
    }
    table {
      width: 90%;
      margin: auto;
      border-collapse: collapse;
      background: white;
      box-shadow: 0 0 10px #ccc;
    }
    th, td {
      padding: 12px;
      border: 1px solid #ddd;
      text-align: center;
    }
    th {
      background-color: #ff9800;
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
  <h2>Your Shopping Cart</h2>
  <table>
    <thead>
      <tr>
        <th>Item ID</th>
        <th>Name</th>
        <th>Quantity</th>
        <th>Unit Price</th>
      </tr>
    </thead>
    <tbody>
      <!-- Dynamic rows from backend -->
      <c:forEach var="item" items="${items}">
        <tr>
          <td>${item.itemId}</td>
          <td>${item.itemName}</td>
          <td>LKR ${item.unitPrice}</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  <a href="${pageContext.request.contextPath}/onlineStore">Back to Online Menu</a>
</body>
</html>
