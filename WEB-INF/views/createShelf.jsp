<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Create Shelf</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: #f2f2f2;
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
      box-shadow: 0 0 10px #bbb;
    }
    label {
      display: block;
      margin: 12px 0 5px;
    }
    input[type="text"], input[type="number"], input[type="submit"] {
      width: 100%;
      padding: 10px;
      margin: 10px 0;
      border: 1px solid #ccc;
      border-radius: 5px;
    }
    input[type="submit"] {
      background-color: #2196F3;
      color: white;
      border: none;
    }
    input[type="submit"]:hover {
      background-color: #1e88e5;
    }
    a {
      text-decoration: none;
      color: #555;
      display: inline-block;
      margin-top: 15px;
    }
  </style>
</head>
<body>
  <h2>Create New Shelf</h2>
  <form action="createShelf.jsp" method="post">
    <label>Shelf ID</label>
    <input type="text" name="shelfId" required />

    <label>Capacity</label>
    <input type="number" name="capacity" min="1" required />

    <input type="submit" value="Create Shelf" />
  </form>
  <a href="${pageContext.request.contextPath}/cashier">Back to Menu</a>
</body>
</html>
