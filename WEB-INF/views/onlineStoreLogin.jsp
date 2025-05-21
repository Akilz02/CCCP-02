<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>SYOS Online Store Login/Register</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: linear-gradient(135deg, #e3f2fd, #e8f5e9);
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
      margin: 0;
    }

    .container {
      background: #fff;
      width: 420px;
      padding: 35px 30px;
      border-radius: 12px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    }

    h2 {
      text-align: center;
      margin-bottom: 30px;
      color: #2e7d32;
      font-size: 26px;
    }

    .tabs {
      display: flex;
      justify-content: space-between;
      margin-bottom: 25px;
    }

    .tabs button {
      flex: 1;
      background-color: #e0e0e0;
      border: none;
      padding: 10px 0;
      border-radius: 6px;
      font-weight: bold;
      cursor: pointer;
      font-size: 15px;
      transition: 0.3s ease;
      margin: 0 5px;
    }

    .tabs button.active {
      background-color: #4CAF50;
      color: white;
    }

    form {
      display: none;
      margin-top: 15px;
    }

    form.active {
      display: block;
    }

    label {
      display: block;
      margin-top: 12px;
      color: #333;
      font-size: 14px;
    }

    input[type="text"],
    input[type="password"] {
      width: 100%;
      padding: 10px;
      margin-top: 5px;
      border-radius: 6px;
      border: 1px solid #ccc;
      box-sizing: border-box;
    }

    input[type="submit"] {
      margin-top: 20px;
      width: 100%;
      padding: 10px;
      background-color: #4CAF50;
      color: white;
      border: none;
      font-size: 16px;
      border-radius: 6px;
      cursor: pointer;
      transition: background-color 0.3s ease;
    }

    input[type="submit"]:hover {
      background-color: #388e3c;
    }

    .home-button {
      display: block;
      width: 100%;
      padding: 10px;
      background-color: #2196F3;
      color: white;
      text-align: center;
      border: none;
      border-radius: 6px;
      margin-top: 20px;
      text-decoration: none;
      font-size: 16px;
      transition: background-color 0.3s;
    }

    .home-button:hover {
      background-color: #0b7dda;
    }
  </style>
</head>
<body>
<div class="container">
  <h2>SYOS Online Store</h2>

  <div class="tabs">
    <button id="loginTab" class="active" onclick="switchTab('login')">Login</button>
    <button id="registerTab" onclick="switchTab('register')">Register</button>
  </div>

  <!-- Login Form -->
  <form id="loginForm" class="active" action="${pageContext.request.contextPath}/onlineStoreLogin" method="post">
    <input type="hidden" name="action" value="login" />
    <label for="loginUsername">Username</label>
    <input type="text" name="username" id="loginUsername" value="akila" required />
    <label for="loginPassword">Password</label>
    <input type="password" name="password" id="loginPassword" value="123" required />
    <input type="submit" value="Login" />
  </form>

  <!-- Register Form -->
  <form id="registerForm" action="${pageContext.request.contextPath}/onlineStoreLogin" method="post">
    <input type="hidden" name="action" value="register" />
    <label for="registerUsername">Username</label>
    <input type="text" name="username" id="registerUsername" required />
    <label for="registerPassword">Password</label>
    <input type="password" name="password" id="registerPassword" required />
    <input type="submit" value="Register" />
  </form>

  <!-- Home Button -->
  <a href="${pageContext.request.contextPath}/home" class="home-button">Back to Home</a>
</div>

<script>
  function switchTab(tab) {
    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");
    const loginTab = document.getElementById("loginTab");
    const registerTab = document.getElementById("registerTab");

    if (tab === "login") {
      loginForm.classList.add("active");
      registerForm.classList.remove("active");
      loginTab.classList.add("active");
      registerTab.classList.remove("active");
    } else {
      loginForm.classList.remove("active");
      registerForm.classList.add("active");
      loginTab.classList.remove("active");
      registerTab.classList.add("active");
    }
  }
</script>
</body>
</html>
