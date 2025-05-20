<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Create Item</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: #eef2f3;
      padding: 40px;
      max-width: 600px;
      margin: auto;
    }
    h2 {
      text-align: center;
      color: #333;
    }
    form {
      background: white;
      padding: 25px;
      border-radius: 10px;
      box-shadow: 0 0 10px #ccc;
    }
    input[type="text"], input[type="submit"] {
      width: 100%;
      padding: 10px;
      margin: 15px 0;
      border-radius: 5px;
      border: 1px solid #ccc;
    }
    input[type="submit"] {
      background-color: #4CAF50;
      color: white;
      border: none;
      cursor: pointer;
    }
    input[type="submit"]:hover {
      background-color: #45a049;
    }
    a {
      display: inline-block;
      margin-top: 15px;
      text-decoration: none;
      color: #555;
    }
  </style>
</head>
<body>
  <h2>Create New Item</h2>
  <form action="${pageContext.request.contextPath}/createItem" method="post">
    <label>Item ID</label>
    <input type="text" name="itemId" id="itemId" required />
    <label>Item Name</label>
    <input type="text" name="itemName" id="itemName" required />
    <input type="submit" value="Add Item" />
  </form>
  <a href="${pageContext.request.contextPath}/cashier">Back to Menu</a>
</body>
</html>
