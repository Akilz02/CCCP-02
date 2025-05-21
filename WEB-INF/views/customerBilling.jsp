<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>

<%--<!DOCTYPE html>--%>
<%--<html lang="en">--%>
<%--<head>--%>
<%--  <meta charset="UTF-8" />--%>
<%--  <title>Customer Billing</title>--%>
<%--  <style>--%>
<%--    body {--%>
<%--      font-family: 'Segoe UI', sans-serif;--%>
<%--      background: #f2f2f2;--%>
<%--      padding: 40px;--%>
<%--      max-width: 900px;--%>
<%--      margin: auto;--%>
<%--    }--%>

<%--    h2 {--%>
<%--      text-align: center;--%>
<%--      color: #444;--%>
<%--    }--%>

<%--    form {--%>
<%--      background: #fff;--%>
<%--      padding: 25px;--%>
<%--      border-radius: 10px;--%>
<%--      box-shadow: 0 0 10px #ccc;--%>
<%--      margin-bottom: 30px;--%>
<%--    }--%>

<%--    label {--%>
<%--      display: block;--%>
<%--      margin-top: 10px;--%>
<%--      margin-bottom: 5px;--%>
<%--    }--%>

<%--    input[type="text"], input[type="number"] {--%>
<%--      width: 100%;--%>
<%--      padding: 10px;--%>
<%--      border: 1px solid #ccc;--%>
<%--      border-radius: 5px;--%>
<%--      margin-bottom: 15px;--%>
<%--    }--%>

<%--    input[type="submit"] {--%>
<%--      background-color: #4CAF50;--%>
<%--      color: white;--%>
<%--      border: none;--%>
<%--      padding: 12px 20px;--%>
<%--      border-radius: 5px;--%>
<%--      cursor: pointer;--%>
<%--      font-size: 16px;--%>
<%--    }--%>

<%--    input[type="submit"]:hover {--%>
<%--      background-color: #45a049;--%>
<%--    }--%>

<%--    table {--%>
<%--      width: 100%;--%>
<%--      border-collapse: collapse;--%>
<%--      margin-top: 30px;--%>
<%--      background: #fff;--%>
<%--      box-shadow: 0 0 10px #ccc;--%>
<%--    }--%>

<%--    th, td {--%>
<%--      padding: 12px;--%>
<%--      text-align: center;--%>
<%--      border: 1px solid #ddd;--%>
<%--    }--%>

<%--    th {--%>
<%--      background-color: #2196F3;--%>
<%--      color: white;--%>
<%--    }--%>

<%--    .total {--%>
<%--      text-align: right;--%>
<%--      font-size: 18px;--%>
<%--      margin-top: 20px;--%>
<%--    }--%>

<%--    .back-link {--%>
<%--      text-align: center;--%>
<%--      margin-top: 25px;--%>
<%--    }--%>

<%--    .back-link a {--%>
<%--      text-decoration: none;--%>
<%--      color: #555;--%>
<%--    }--%>
<%--  </style>--%>
<%--</head>--%>
<%--<body>--%>

<%--  <h2>Customer Billing</h2>--%>

<%--  <form action="${pageContext.request.contextPath}/customerBilling" method="post">--%>
<%--    <label for="itemId">Item ID</label>--%>
<%--    <input type="text" id="itemId" name="itemId" required />--%>

<%--    <label for="batchId">Batch ID</label>--%>
<%--    <input type="text" id="batchId" name="batchId" required />--%>

<%--    <label for="quantity">Quantity</label>--%>
<%--    <input type="number" id="quantity" name="quantity" min="1" required />--%>

<%--    <input type="submit" value="Add to Bill" />--%>
<%--  </form>--%>

<%--  <!-- Placeholder for Bill Display -->--%>
<%--  <table>--%>
<%--    <thead>--%>
<%--      <tr>--%>
<%--        <th>Item ID</th>--%>
<%--        <th>Name</th>--%>
<%--        <th>Quantity</th>--%>
<%--        <th>Unit Price</th>--%>
<%--        <th>Subtotal</th>--%>
<%--      </tr>--%>
<%--    </thead>--%>
<%--    <tbody>--%>
<%--      <c:if test="${empty bill}">--%>
<%--        <tr>--%>
<%--          <td colspan="5">No items added to bill yet</td>--%>
<%--        </tr>--%>
<%--      </c:if>--%>
<%--      <c:forEach var="item" items="${bill}">--%>
<%--        <tr>--%>
<%--          <td>${item.itemId}</td>--%>
<%--          <td>${item.name}</td>--%>
<%--          <td>${item.quantity}</td>--%>
<%--          <td>$${item.unitPrice}</td>--%>
<%--          <td>$${item.getSubtotal()}</td>--%>
<%--        </tr>--%>
<%--      </c:forEach>--%>
<%--    </tbody>--%>
<%--  </table>--%>

<%--  <div class="total">--%>
<%--    <strong>Total:</strong> $${total}--%>
<%--  </div>--%>

<%--  <div class="back-link">--%>
<%--    <a href="${pageContext.request.contextPath}/cashier">Back to Main Menu</a>--%>
<%--  </div>--%>

<%--</body>--%>
<%--</html>--%>




<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Customer Billing</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: #f0f0f0;
      padding: 40px;
      max-width: 900px;
      margin: auto;
    }

    h2 {
      text-align: center;
      color: #2e7d32;
      margin-bottom: 30px;
    }

    form {
      background: #ffffff;
      padding: 25px;
      border-radius: 10px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
      margin-bottom: 40px;
    }

    label {
      display: block;
      margin-top: 10px;
      margin-bottom: 5px;
      font-weight: 500;
    }

    input[type="text"], input[type="number"] {
      width: 100%;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 6px;
      margin-bottom: 15px;
      font-size: 15px;
    }

    input[type="submit"] {
      background-color: #4CAF50;
      color: white;
      border: none;
      padding: 12px 20px;
      border-radius: 6px;
      cursor: pointer;
      font-size: 16px;
      margin-top: 10px;
    }

    input[type="submit"]:hover {
      background-color: #45a049;
    }

    .bill-container {
      background-color: #fff;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 0 12px rgba(0,0,0,0.1);
    }

    table {
      width: 100%;
      border-collapse: collapse;
      font-size: 15px;
      margin-bottom: 20px;
    }

    th, td {
      padding: 14px;
      text-align: center;
      border: 1px solid #ddd;
    }

    th {
      background-color: #2196F3;
      color: white;
    }

    tr.total-row td {
      font-weight: bold;
      background-color: #e3f2fd;
    }

    .back-link {
      text-align: center;
      margin-top: 25px;
    }

    .back-link a {
      text-decoration: none;
      color: #555;
      font-weight: 500;
    }

    .empty-row {
      font-style: italic;
      color: #777;
    }
  </style>
</head>
<body>

<h2>Customer Billing</h2>

<!-- Input form -->
<form action="${pageContext.request.contextPath}/customerBilling" method="post">
  <label for="itemId">Item ID</label>
  <input type="text" id="itemId" name="itemId" required />

  <label for="batchId">Batch ID</label>
  <input type="text" id="batchId" name="batchId" required />

  <label for="quantity">Quantity</label>
  <input type="number" id="quantity" name="quantity" min="1" required />

  <input type="submit" value="Add to Bill" />
</form>

<!-- Billing table -->
<div class="bill-container">
  <table>
    <thead>
    <tr>
      <th>Item ID</th>
      <th>Name</th>
      <th>Quantity</th>
      <th>Unit Price</th>
      <th>Subtotal</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
      <c:when test="${empty bill}">
        <tr><td colspan="5" class="empty-row">No items added to bill yet.</td></tr>
      </c:when>
      <c:otherwise>
        <c:forEach var="item" items="${bill}">
          <tr>
            <td>${item.itemId}</td>
            <td>${item.name}</td>
            <td>${item.quantity}</td>
            <td>
              LKR <fmt:formatNumber value="${item.unitPrice}" type="number" minFractionDigits="2" />
            </td>
            <td>
              LKR <fmt:formatNumber value="${item.subtotal}" type="number" minFractionDigits="2" />
            </td>
          </tr>
        </c:forEach>
        <!-- Total row inside table -->
        <tr class="total-row">
          <td colspan="4" style="text-align: right;">Total</td>
          <td>LKR <fmt:formatNumber value="${total}" type="number" minFractionDigits="2" /></td>
        </tr>
      </c:otherwise>
    </c:choose>
    </tbody>
  </table>
</div>

<div class="back-link">
  <a href="${pageContext.request.contextPath}/cashier">Back to Main Menu</a>
</div>

</body>
</html>
