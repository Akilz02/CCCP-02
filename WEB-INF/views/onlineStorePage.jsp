<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html lang="en">--%>
<%--<head>--%>
<%--  <meta charset="UTF-8" />--%>
<%--  <title>SYOS Online Store</title>--%>
<%--  <style>--%>
<%--    body {--%>
<%--      font-family: 'Segoe UI', sans-serif;--%>
<%--      background: #f4f4f4;--%>
<%--      padding: 50px;--%>
<%--      text-align: center;--%>
<%--    }--%>

<%--    .container {--%>
<%--      background: white;--%>
<%--      max-width: 900px;--%>
<%--      margin: auto;--%>
<%--      padding: 30px;--%>
<%--      border-radius: 12px;--%>
<%--      box-shadow: 0 0 10px rgba(0,0,0,0.15);--%>
<%--    }--%>

<%--    h2 {--%>
<%--      color: #333;--%>
<%--      margin-bottom: 20px;--%>
<%--    }--%>

<%--    table {--%>
<%--      width: 100%;--%>
<%--      margin-top: 20px;--%>
<%--      border-collapse: collapse;--%>
<%--      background-color: #fff;--%>
<%--      box-shadow: 0 0 10px #ddd;--%>
<%--    }--%>

<%--    th, td {--%>
<%--      padding: 12px;--%>
<%--      border: 1px solid #ddd;--%>
<%--      text-align: center;--%>
<%--    }--%>

<%--    th {--%>
<%--      background-color: #4CAF50;--%>
<%--      color: white;--%>
<%--    }--%>

<%--    ul {--%>
<%--      list-style: none;--%>
<%--      padding: 0;--%>
<%--      margin-top: 30px;--%>
<%--    }--%>

<%--    li {--%>
<%--      display: inline-block;--%>
<%--      margin: 10px;--%>
<%--    }--%>

<%--    a {--%>
<%--      background-color: #2196F3;--%>
<%--      color: white;--%>
<%--      padding: 10px 20px;--%>
<%--      text-decoration: none;--%>
<%--      border-radius: 6px;--%>
<%--      transition: 0.3s;--%>
<%--    }--%>

<%--    a:hover {--%>
<%--      background-color: #1976D2;--%>
<%--    }--%>

<%--    .logout {--%>
<%--      background-color: #f44336;--%>
<%--    }--%>

<%--    .logout:hover {--%>
<%--      background-color: #d32f2f;--%>
<%--    }--%>

<%--    .back {--%>
<%--      display: block;--%>
<%--      margin-top: 25px;--%>
<%--      color: #555;--%>
<%--      text-decoration: none;--%>
<%--    }--%>
<%--  </style>--%>
<%--</head>--%>
<%--<body>--%>

<%--<div class="container">--%>
<%--  <h2>Welcome to SYOS Online Store</h2>--%>

<%--  <!-- Embedded Available Items Table -->--%>
<%--  <h3>Available Items</h3>--%>
<%--  <table>--%>
<%--    <thead>--%>
<%--    <tr>--%>
<%--      <th>Item ID</th>--%>
<%--      <th>Item Name</th>--%>
<%--      <th>Unit Price</th>--%>
<%--    </tr>--%>
<%--    </thead>--%>
<%--    <tbody>--%>
<%--    <!-- Replace this with dynamic rows using JSP -->--%>
<%--    <c:forEach var="item" items="${items}">--%>
<%--      <tr>--%>
<%--        <td>${item.itemId}</td>--%>
<%--        <td>${item.itemName}</td>--%>
<%--        <td>LKR ${item.unitPrice}</td>--%>
<%--      </tr>--%>
<%--    </c:forEach>--%>
<%--    <!-- Add rows dynamically via JSP or JS -->--%>
<%--    </tbody>--%>
<%--  </table>--%>

<%--  <form action="${pageContext.request.contextPath}/onlineStore" method="post">--%>
<%--    <input type="hidden" name="action" value="logout" />--%>
<%--    <input type="submit" value="Log Out" />--%>
<%--  </form>--%>


<%--  <!-- Action Buttons -->--%>
<%--  <ul>--%>
<%--    <li><a href="${pageContext.request.contextPath}/addToCart">Add to Cart</a></li>--%>
<%--    <li><a href="${pageContext.request.contextPath}/viewCart">View Cart</a></li>--%>
<%--    <li><a href="${pageContext.request.contextPath}/checkout">Checkout</a></li>--%>

<%--  </ul>--%>
<%--</div>--%>

<%--<a class="${pageContext.request.contextPath}/cashier"> Back to Main Menu</a>--%>

<%--</body>--%>
<%--</html>--%>



<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>SYOS Online Store</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: #f0f2f5;
      margin: 0;
      padding: 0;
    }

    .container {
      max-width: 1000px;
      margin: 50px auto;
      background: #fff;
      padding: 40px;
      border-radius: 12px;
      box-shadow: 0 0 15px rgba(0,0,0,0.1);
    }

    header {
      text-align: center;
      margin-bottom: 30px;
    }

    header h1 {
      font-size: 32px;
      color: #2e7d32;
      margin: 0;
    }

    header p {
      font-size: 16px;
      color: #555;
      margin-top: 8px;
    }

    h3 {
      text-align: center;
      color: #444;
      margin-bottom: 20px;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-bottom: 30px;
    }

    th, td {
      padding: 14px;
      border: 1px solid #e0e0e0;
      text-align: center;
      font-size: 15px;
    }

    th {
      background-color: #43a047;
      color: white;
      text-transform: uppercase;
      font-weight: normal;
    }

    tr:nth-child(even) {
      background-color: #f9f9f9;
    }

    .buttons {
      text-align: center;
      margin-bottom: 30px;
    }

    .buttons a {
      display: inline-block;
      background-color: #2196F3;
      color: white;
      padding: 12px 25px;
      margin: 10px;
      text-decoration: none;
      border-radius: 8px;
      font-size: 15px;
      transition: background-color 0.3s ease, transform 0.2s ease;
    }

    .buttons a:hover {
      background-color: #1976D2;
      transform: translateY(-2px);
    }

    .logout-form {
      text-align: center;
      margin-top: 10px;
    }

    .logout-form input[type="submit"] {
      background-color: #e53935;
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 6px;
      font-size: 15px;
      cursor: pointer;
      transition: background-color 0.3s ease;
    }

    .logout-form input[type="submit"]:hover {
      background-color: #c62828;
    }

    .back-link {
      display: block;
      text-align: center;
      margin-top: 30px;
      font-size: 14px;
    }

    .back-link a {
      color: #555;
      text-decoration: none;
    }

    .back-link a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>

<div class="container">

  <header>
    <h1>SYOS Online Store</h1>
    <p>Smart. Simple. Seamless Shopping Experience.</p>
  </header>

  <!-- Embedded Available Items Table -->
  <h3>Available Items</h3>
  <table>
    <thead>
    <tr>
      <th>Item ID</th>
      <th>Item Name</th>
      <th>Unit Price (LKR)</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="item" items="${items}">
      <tr>
        <td>${item.itemId}</td>
        <td>${item.itemName}</td>
        <td>${item.unitPrice}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

  <!-- Action Buttons -->
  <div class="buttons">
    <a href="${pageContext.request.contextPath}/addToCart">Add to Cart</a>
    <a href="${pageContext.request.contextPath}/viewCart">View Cart</a>
    <a href="${pageContext.request.contextPath}/checkout">Checkout</a>
  </div>

  <!-- Logout -->
  <div class="logout-form">
    <form action="${pageContext.request.contextPath}/onlineStore" method="post">
      <input type="hidden" name="action" value="logout" />
      <input type="submit" value="Log Out" />
    </form>
  </div>

  <!-- Back to Cashier -->
  <div class="back-link">
<%--    <a href="${pageContext.request.contextPath}/cashier">← Back to Main Menu</a>--%>
  </div>

</div>

</body>
</html>
