<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>SYOS - main.Main Menu</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background-color: #f9f9f9;
      text-align: center;
      padding: 50px;
    }
    h2 {
      color: #333;
    }
    ul {
      list-style-type: none;
      padding: 0;
    }
    li {
      margin: 15px 0;
    }
    a {
      display: inline-block;
      background-color: #4CAF50;
      color: white;
      padding: 10px 25px;
      text-decoration: none;
      border-radius: 8px;
      transition: background-color 0.3s ease;
    }
    a:hover {
      background-color: #45a049;
    }
  </style>
</head>
<body>
  <h2>Welcome to SYOS - Your Shopping System</h2>
  <ul>
    <li><a href="${pageContext.request.contextPath}/billing">Customer Billing</a></li>
    <li><a href="${pageContext.request.contextPath}/createItem">Create Item</a></li>
    <li><a href="${pageContext.request.contextPath}/addToStock">Add to Stock</a></li>
    <li><a href="${pageContext.request.contextPath}/shelfCapacity">Shelf Item Capacity</a></li>
    <li><a href="${pageContext.request.contextPath}/moveToShelf">Move Stock to Shelf</a></li>
    <li><a href="${pageContext.request.contextPath}/createShelf">Create Shelf</a></li>
<%--    <li><a href="onlineStore.html">Online Store</a></li>--%>
    <li><a href="${pageContext.request.contextPath}/reports">Reports</a></li>
  </ul>
</body>
</html>
