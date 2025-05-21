<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>SYOS - Entry Page</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: linear-gradient(135deg, #e3f2fd, #fce4ec);
      height: 100vh;
      margin: 0;
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
    }

    h1 {
      font-size: 32px;
      color: #333;
      margin-bottom: 40px;
    }

    .button-container {
      display: flex;
      gap: 40px;
    }

    a {
      text-decoration: none;
      padding: 20px 40px;
      font-size: 18px;
      font-weight: bold;
      border-radius: 10px;
      transition: background-color 0.3s ease, transform 0.2s ease;
      box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    }

    .cashier-btn {
      background-color: #4CAF50;
      color: white;
    }

    .cashier-btn:hover {
      background-color: #45a049;
      transform: translateY(-2px);
    }

    .online-btn {
      background-color: #2196F3;
      color: white;
    }

    .online-btn:hover {
      background-color: #1976D2;
      transform: translateY(-2px);
    }
  </style>
</head>
<body>

<h1>Welcome to SYOS - Synex Outlet Store</h1>

<div class="button-container">
  <a href="${pageContext.request.contextPath}/cashier" class="cashier-btn">SYOS Cashier</a>
  <a href="${pageContext.request.contextPath}/onlineStoreLogin" class="online-btn">SYOS Online Store</a>
</div>

</body>
</html>
