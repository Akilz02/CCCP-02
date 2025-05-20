<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Customer Billing</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: #f2f2f2;
      padding: 40px;
      max-width: 900px;
      margin: auto;
    }

    h2 {
      text-align: center;
      color: #444;
    }

    form {
      background: #fff;
      padding: 25px;
      border-radius: 10px;
      box-shadow: 0 0 10px #ccc;
      margin-bottom: 30px;
    }

    label {
      display: block;
      margin-top: 10px;
      margin-bottom: 5px;
    }

    input[type="text"], input[type="number"] {
      width: 100%;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 5px;
      margin-bottom: 15px;
    }

    input[type="submit"] {
      background-color: #4CAF50;
      color: white;
      border: none;
      padding: 12px 20px;
      border-radius: 5px;
      cursor: pointer;
      font-size: 16px;
    }

    input[type="submit"]:hover {
      background-color: #45a049;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 30px;
      background: #fff;
      box-shadow: 0 0 10px #ccc;
    }

    th, td {
      padding: 12px;
      text-align: center;
      border: 1px solid #ddd;
    }

    th {
      background-color: #2196F3;
      color: white;
    }

    .total {
      text-align: right;
      font-size: 18px;
      margin-top: 20px;
    }

    .back-link {
      text-align: center;
      margin-top: 25px;
    }

    .back-link a {
      text-decoration: none;
      color: #555;
    }
  </style>
</head>
<body>

  <h2>Customer Billing</h2>

  <form action="billing.jsp" method="post">
    <label for="itemId">Item ID</label>
    <input type="text" id="itemId" name="itemId" required />

    <label for="quantity">Quantity</label>
    <input type="number" id="quantity" name="quantity" min="1" required />

    <input type="submit" value="Add to Bill" />
  </form>

  <!-- Placeholder for Bill Display -->
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
      <!-- Example row (replace with backend data using JSP or Servlet) -->
      <tr>
        <td>ITEM001</td>
        <td>Sample Item</td>
        <td>2</td>
        <td>10.00</td>
        <td>20.00</td>
      </tr>
    </tbody>
  </table>

  <div class="total">
    <strong>Total:</strong> $20.00
  </div>

  <div class="back-link">
    <a href="index.html">← Back to Main Menu</a>
  </div>

</body>
</html>
