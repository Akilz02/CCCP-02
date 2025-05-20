<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Add to Stock</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: #f0f0f5;
      padding: 40px;
      max-width: 700px;
      margin: auto;
    }
    h2 {
      text-align: center;
      color: #444;
    }
    form {
      background: white;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 0 10px #ccc;
    }
    label {
      display: block;
      margin: 10px 0 5px;
    }
    input[type="text"], input[type="number"], input[type="date"], input[type="submit"] {
      width: 100%;
      padding: 10px;
      margin-top: 8px;
      margin-bottom: 20px;
      border: 1px solid #ccc;
      border-radius: 5px;
    }
    input[type="submit"] {
      background-color: #4CAF50;
      color: white;
      border: none;
      font-size: 16px;
    }
    input[type="submit"]:hover {
      background-color: #45a049;
    }
    a {
      text-decoration: none;
      color: #555;
      display: inline-block;
      margin-top: 20px;
    }
  </style>
</head>
<body>
  <h2>Add Batch to Stock</h2>
  <form action="${pageContext.request.contextPath}/addToStock" method="post">
    <label>Item ID</label>
    <input type="text" name="itemId" required />

    <label>Batch ID</label>
    <input type="text" name="batchId" required />

    <label>Quantity</label>
    <input type="number" name="quantity" min="1" required />

    <label>Unit Price</label>
    <input type="number" step="0.01" name="unitPrice" required />

    <label>Date of Purchase</label>
    <input type="date" name="purchaseDate" required />

    <label>Expiry Date</label>
    <input type="date" name="expiryDate" required />

    <input type="submit" value="Add to Stock" />
  </form>
  <a href="${pageContext.request.contextPath}/cashier">Back to Menu</a>
</body>
</html>
