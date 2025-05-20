<%--<!DOCTYPE html>--%>
<%--<html lang="en">--%>
<%--<head>--%>
<%--  <meta charset="UTF-8" />--%>
<%--  <title>Reports</title>--%>
<%--  <style>--%>
<%--    body {--%>
<%--      font-family: 'Segoe UI', sans-serif;--%>
<%--      background-color: #f0f0f0;--%>
<%--      padding: 40px;--%>
<%--      max-width: 500px;--%>
<%--      margin: auto;--%>
<%--      text-align: center;--%>
<%--    }--%>
<%--    h2 {--%>
<%--      color: #444;--%>
<%--    }--%>
<%--    form {--%>
<%--      background: white;--%>
<%--      padding: 25px;--%>
<%--      border-radius: 10px;--%>
<%--      box-shadow: 0 0 10px #ccc;--%>
<%--    }--%>
<%--    select, input[type="submit"] {--%>
<%--      width: 100%;--%>
<%--      padding: 10px;--%>
<%--      margin-top: 15px;--%>
<%--      border-radius: 5px;--%>
<%--      border: 1px solid #ccc;--%>
<%--    }--%>
<%--    input[type="submit"] {--%>
<%--      background-color: #673ab7;--%>
<%--      color: white;--%>
<%--      border: none;--%>
<%--    }--%>
<%--    input[type="submit"]:hover {--%>
<%--      background-color: #512da8;--%>
<%--    }--%>
<%--    a {--%>
<%--      display: block;--%>
<%--      margin-top: 20px;--%>
<%--      text-decoration: none;--%>
<%--      color: #555;--%>
<%--    }--%>
<%--  </style>--%>
<%--</head>--%>
<%--<body>--%>
<%--  <h2>Generate Reports</h2>--%>
<%--  <form action="${pageContext.request.contextPath}/reports" method="post">--%>
<%--    <label>Select Report Type:</label>--%>
<%--    <select name="reportType" required>--%>
<%--&lt;%&ndash;      <option value="dailysales">Daily Sales</option>&ndash;%&gt;--%>
<%--      <option value="reorder">Reorder Level</option>--%>
<%--      <option value="stock">Stock Report</option>--%>
<%--      <option value="bill">Bill Report</option>--%>
<%--    </select>--%>
<%--    <input type="submit" value="Generate Report" />--%>
<%--  </form>--%>
<%--  <a href="${pageContext.request.contextPath}/cashier">Back to Menu</a>--%>
<%--</body>--%>
<%--</html>--%>



<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
            max-width: 900px;
            margin: auto;
        }
        h2, h3 {
            color: #444;
            text-align: center;
        }
        form {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 10px #ccc;
            text-align: center;
            max-width: 500px;
            margin: 0 auto 20px;
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
            text-align: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #673ab7;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        .report-section {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px #ccc;
            margin-bottom: 30px;
        }
        .error {
            color: red;
            font-weight: bold;
            text-align: center;
        }
        .success {
            color: green;
            font-weight: bold;
            text-align: center;
        }
    </style>
</head>
<body>
<h2>Generate Reports</h2>
<form action="${pageContext.request.contextPath}/reports" method="post">
    <label>Select Report Type:</label>
    <select name="reportType" required>
        <%--      <option value="dailysales">Daily Sales</option>--%>
        <option value="reorder">Reorder Level</option>
        <option value="stock">Stock Report</option>
        <option value="bill">Bill Report</option>
    </select>
    <input type="submit" value="Generate Report" />
</form>

<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>

<c:if test="${not empty success}">
    <div class="success">${success}</div>
</c:if>

<!-- Stock Report Section -->
<c:if test="${not empty stockItems}">
    <div class="report-section">
        <h3>Stock Report</h3>
        <table>
            <thead>
            <tr>
                <th>Item ID</th>
                <th>Item Name</th>
                <th>Batch ID</th>
                <th>Quantity</th>
                <th>Unit Price</th>
                <th>Date of Purchase</th>
                <th>Expiry Date</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${stockItems}">
                <tr>
                    <td>${item.itemId}</td>
                    <td>${item.itemName}</td>
                    <td>${item.batchId}</td>
                    <td>${item.quantity}</td>
                    <td>${item.unitPrice}</td>
                    <td>${item.dateOfPurchase}</td>
                    <td>${item.expiryDate}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>

<!-- Reorder Report Section -->
<c:if test="${not empty reorderItems}">
    <div class="report-section">
        <h3>Reorder Report</h3>
        <table>
            <thead>
            <tr>
                <th>Item ID</th>
                <th>Item Name</th>
                <th>Quantity</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${reorderItems}">
                <tr>
                    <td>${item.itemId}</td>
                    <td>${item.itemName}</td>
                    <td>${item.quantity}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>

<!-- Bill Reports Section -->
<c:if test="${not empty normalBills || not empty onlineBills}">
    <div class="report-section">
        <h3>Bill Reports</h3>

        <c:if test="${not empty normalBills}">
            <h4>In-Store Transactions</h4>
            <table>
                <thead>
                <tr>
                    <th>Bill ID</th>
                    <th>Total Bill</th>
                    <th>Cash Tendered</th>
                    <th>Change Amount</th>
                    <th>Bill Date</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="bill" items="${normalBills}">
                    <tr>
                        <td>${bill.billId}</td>
                        <td>${bill.totalBill}</td>
                        <td>${bill.cashTendered}</td>
                        <td>${bill.changeAmount}</td>
                        <td>${bill.billDate}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

        <c:if test="${not empty onlineBills}">
            <h4>Online Store Transactions</h4>
            <table>
                <thead>
                <tr>
                    <th>Online Bill ID</th>
                    <th>Total Bill</th>
                    <th>Bill Date</th>
                    <th>Username</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="bill" items="${onlineBills}">
                    <tr>
                        <td>${bill.onlineBillId}</td>
                        <td>${bill.totalBill}</td>
                        <td>${bill.billDate}</td>
                        <td>${bill.username}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</c:if>

<a href="${pageContext.request.contextPath}/cashier">Back to Menu</a>
</body>
</html>