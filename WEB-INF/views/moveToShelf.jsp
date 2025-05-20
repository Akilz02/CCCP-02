<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Move Stock to Shelf</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background-color: #f9f9f9;
      text-align: center;
      padding: 100px;
    }
    h2 {
      color: #444;
    }
    form {
      display: inline-block;
      margin-top: 30px;
    }
    input[type="submit"] {
      padding: 12px 25px;
      font-size: 16px;
      background-color: #009688;
      color: white;
      border: none;
      border-radius: 8px;
      cursor: pointer;
    }
    input[type="submit"]:hover {
      background-color: #00796b;
    }
    a {
      display: block;
      margin-top: 20px;
      color: #555;
      text-decoration: none;
    }
  </style>
</head>
<body>
  <h2>Move Available Stock to Shelf Automatically</h2>
  <form action="${pageContext.request.contextPath}/moveToShelf" method="post">
    <input type="submit" value="Execute Move Operation" />
  </form>
  <a href="${pageContext.request.contextPath}/cashier">Back to Menu</a>
</body>
</html>
