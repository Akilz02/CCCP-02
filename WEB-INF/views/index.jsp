<%--<!DOCTYPE html>--%>
<%--<html lang="en">--%>
<%--<head>--%>
<%--  <meta charset="UTF-8" />--%>
<%--  <title>SYOS - main.Main Menu</title>--%>
<%--  <style>--%>
<%--    body {--%>
<%--      font-family: 'Segoe UI', sans-serif;--%>
<%--      background-color: #f9f9f9;--%>
<%--      text-align: center;--%>
<%--      padding: 50px;--%>
<%--    }--%>
<%--    h2 {--%>
<%--      color: #333;--%>
<%--    }--%>
<%--    ul {--%>
<%--      list-style-type: none;--%>
<%--      padding: 0;--%>
<%--    }--%>
<%--    li {--%>
<%--      margin: 15px 0;--%>
<%--    }--%>
<%--    a {--%>
<%--      display: inline-block;--%>
<%--      background-color: #4CAF50;--%>
<%--      color: white;--%>
<%--      padding: 10px 25px;--%>
<%--      text-decoration: none;--%>
<%--      border-radius: 8px;--%>
<%--      transition: background-color 0.3s ease;--%>
<%--    }--%>
<%--    a:hover {--%>
<%--      background-color: #45a049;--%>
<%--    }--%>
<%--  </style>--%>
<%--</head>--%>
<%--<body>--%>
<%--  <h2>Welcome to SYOS - Your Shopping System</h2>--%>
<%--  <ul>--%>
<%--    <li><a href="${pageContext.request.contextPath}/customerBilling">Customer Billing</a></li>--%>
<%--    <li><a href="${pageContext.request.contextPath}/createItem">Create Item</a></li>--%>
<%--    <li><a href="${pageContext.request.contextPath}/addToStock">Add to Stock</a></li>--%>
<%--    <li><a href="${pageContext.request.contextPath}/shelfCapacity">Shelf Item Capacity</a></li>--%>
<%--    <li><a href="${pageContext.request.contextPath}/moveToShelf">Move Stock to Shelf</a></li>--%>
<%--    <li><a href="${pageContext.request.contextPath}/createShelf">Create Shelf</a></li>--%>
<%--&lt;%&ndash;    <li><a href="onlineStore.html">Online Store</a></li>&ndash;%&gt;--%>
<%--    <li><a href="${pageContext.request.contextPath}/reports">Reports</a></li>--%>
<%--  </ul>--%>
<%--  <ul--%>
<%--  <li><a href="${pageContext.request.contextPath}/home">Back to Home</a></li>--%>
<%--    </ul>--%>
<%--</body>--%>
<%--</html>--%>




<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>SYOS - Cashier Main Menu</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background-color: #f0f2f5;
      margin: 0;
      padding: 0;
    }

    .container {
      max-width: 800px;
      margin: 60px auto;
      background: #fff;
      padding: 40px;
      border-radius: 12px;
      box-shadow: 0 0 15px rgba(0,0,0,0.1);
      text-align: center;
    }

    h2 {
      color: #2e7d32;
      margin-bottom: 30px;
      font-size: 28px;
    }

    ul {
      list-style: none;
      padding: 0;
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
      margin-bottom: 30px;
    }

    li {
      display: flex;
      justify-content: center;
    }

    a {
      display: block;
      width: 100%;
      background-color: #4CAF50;
      color: white;
      padding: 14px;
      text-decoration: none;
      border-radius: 8px;
      font-weight: 600;
      transition: background-color 0.3s ease, transform 0.2s ease;
    }

    a:hover {
      background-color: #388e3c;
      transform: translateY(-2px);
    }

    .back {
      background-color: #2196F3;
    }

    .back:hover {
      background-color: #0b7dda;
    }
  </style>
</head>
<body>

<div class="container">
  <h2>Welcome to SYOS - Cashier Control Panel</h2>

  <ul>
    <li><a href="${pageContext.request.contextPath}/customerBilling">Customer Billing</a></li>
    <li><a href="${pageContext.request.contextPath}/createItem">Create Item</a></li>
    <li><a href="${pageContext.request.contextPath}/addToStock">Add to Stock</a></li>
    <li><a href="${pageContext.request.contextPath}/shelfCapacity">Shelf Item Capacity</a></li>
    <li><a href="${pageContext.request.contextPath}/moveToShelf">Move Stock to Shelf</a></li>
    <li><a href="${pageContext.request.contextPath}/createShelf">Create Shelf</a></li>
    <li><a href="${pageContext.request.contextPath}/reports">Reports</a></li>
  </ul>

  <a href="${pageContext.request.contextPath}/home" class="back">Back to Home</a>
</div>

</body>
</html>
