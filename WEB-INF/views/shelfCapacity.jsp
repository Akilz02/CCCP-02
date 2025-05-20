<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Shelf Item Capacity</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background-color: #f8f8f8;
      padding: 40px;
      max-width: 600px;
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
    label {
      display: block;
      margin: 12px 0 5px;
    }
    input[type="text"], input[type="number"], input[type="submit"] {
      width: 100%;
      padding: 10px;
      margin-top: 8px;
      margin-bottom: 15px;
      border: 1px solid #ccc;
      border-radius: 5px;
    }
    input[type="submit"] {
      background-color: #ff9800;
      color: white;
      border: none;
    }
    input[type="submit"]:hover {
      background-color: #fb8c00;
    }
    a {
      text-decoration: none;
      color: #666;
      display: inline-block;
      margin-top: 20px;
    }
  </style>
</head>
<body>
  <h2>Set Shelf Item Capacity</h2>
  <form action="shelfCapacity.jsp" method="post">
    <label>Shelf ID</label>
    <input type="text" name="shelfId" required />

    <label>Item ID</label>
    <input type="text" name="itemId" required />

    <label>Capacity Limit</label>
    <input type="number" name="capacity" min="1" required />

    <input type="submit" value="Set Capacity" />
  </form>
  <a href="${pageContext.request.contextPath}/cashier">Back to Menu</a>
</body>
</html>
