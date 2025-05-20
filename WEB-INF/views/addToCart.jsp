<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Add to Cart</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: #f8f8f8;
      padding: 40px;
      max-width: 500px;
      margin: auto;
    }
    h2 {
      text-align: center;
      color: #444;
    }
    form {
      background: white;
      padding: 25px;
      border-radius: 10px;
      box-shadow: 0 0 10px #ccc;
    }
    label, input {
      display: block;
      width: 100%;
      margin-bottom: 15px;
    }
    input[type="text"], input[type="number"] {
      padding: 10px;
      border-radius: 5px;
      border: 1px solid #ccc;
    }
    input[type="submit"] {
      background-color: #2196F3;
      color: white;
      border: none;
      padding: 10px;
      font-size: 16px;
      cursor: pointer;
      border-radius: 5px;
    }
    input[type="submit"]:hover {
      background-color: #1976D2;
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
  <h2>Add Item to Cart</h2>
  <form action="${pageContext.request.contextPath}/addToCart" method="post">
    <label>Item ID</label>
    <input type="text" name="itemId" id="itemId" required />

    <label>Quantity</label>
    <input type="number" name="quantity" id="quantity" min="1" required />

    <input type="submit" value="Add to Cart" />
  </form>
  <a href="${pageContext.request.contextPath}/onlineStore">Back to Online Menu</a>
</body>
</html>
