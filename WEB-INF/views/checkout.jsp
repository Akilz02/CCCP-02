<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Checkout</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background-color: #e9f5ec;
      text-align: center;
      padding: 50px;
    }
    h2 {
      color: #444;
    }
    .box {
      background: white;
      padding: 30px;
      margin: auto;
      width: 400px;
      box-shadow: 0 0 10px #aaa;
      border-radius: 10px;
    }
    .total {
      font-size: 20px;
      margin: 20px 0;
    }
    form input[type="submit"] {
      background-color: #4CAF50;
      color: white;
      padding: 12px 25px;
      font-size: 16px;
      border: none;
      border-radius: 8px;
      cursor: pointer;
    }
    form input[type="submit"]:hover {
      background-color: #388e3c;
    }
    a {
      display: block;
      margin-top: 20px;
      text-decoration: none;
      color: #555;
    }
  </style>
</head>
<body>
  <div class="box">
    <h2>Confirm Checkout</h2>
    <p class="total">Total Amount: $45.97</p>
    <form action="checkout.jsp" method="post">
      <input type="submit" value="Confirm and Pay" />
    </form>
    <a href="${pageContext.request.contextPath}/cashier">Back to Menu</a>
  </div>
</body>
</html>
