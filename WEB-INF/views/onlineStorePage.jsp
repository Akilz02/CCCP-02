<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>SYOS Online Store</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: #f4f4f4;
      padding: 50px;
      text-align: center;
    }

    .container {
      background: white;
      max-width: 900px;
      margin: auto;
      padding: 30px;
      border-radius: 12px;
      box-shadow: 0 0 10px rgba(0,0,0,0.15);
    }

    h2 {
      color: #333;
      margin-bottom: 20px;
    }

    table {
      width: 100%;
      margin-top: 20px;
      border-collapse: collapse;
      background-color: #fff;
      box-shadow: 0 0 10px #ddd;
    }

    th, td {
      padding: 12px;
      border: 1px solid #ddd;
      text-align: center;
    }

    th {
      background-color: #4CAF50;
      color: white;
    }

    ul {
      list-style: none;
      padding: 0;
      margin-top: 30px;
    }

    li {
      display: inline-block;
      margin: 10px;
    }

    a {
      background-color: #2196F3;
      color: white;
      padding: 10px 20px;
      text-decoration: none;
      border-radius: 6px;
      transition: 0.3s;
    }

    a:hover {
      background-color: #1976D2;
    }

    .logout {
      background-color: #f44336;
    }

    .logout:hover {
      background-color: #d32f2f;
    }

    .back {
      display: block;
      margin-top: 25px;
      color: #555;
      text-decoration: none;
    }
  </style>
</head>
<body>

<div class="container">
  <h2>Welcome to SYOS Online Store</h2>

  <!-- Embedded Available Items Table -->
  <h3>Available Items</h3>
  <table>
    <thead>
    <tr>
      <th>Item ID</th>
      <th>Item Name</th>
      <th>Unit Price</th>
    </tr>
    </thead>
    <tbody>
    <!-- Replace this with dynamic rows using JSP -->
    <c:forEach var="item" items="${items}">
      <tr>
        <td>${item.itemId}</td>
        <td>${item.itemName}</td>
        <td>LKR ${item.unitPrice}</td>
      </tr>
    </c:forEach>
    <!-- Add rows dynamically via JSP or JS -->
    </tbody>
  </table>

  <!-- Action Buttons -->
  <ul>
    <li><a href="${pageContext.request.contextPath}/addToCart">Add to Cart</a></li>
    <li><a href="${pageContext.request.contextPath}/viewCart">View Cart</a></li>
    <li><a href="${pageContext.request.contextPath}/checkout">Checkout</a></li>
    <li><a href="${pageContext.request.contextPath}/onlineStore" class="logout">Logout</a></li>
  </ul>
</div>

<a class="${pageContext.request.contextPath}/cashier"> Back to Main Menu</a>

</body>
</html>
