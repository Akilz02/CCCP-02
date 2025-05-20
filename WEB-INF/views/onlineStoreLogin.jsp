<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>SYOS Online Store Login/Register</title>
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background: #f0f2f5;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
    .container {
      background: white;
      width: 400px;
      padding: 30px;
      border-radius: 12px;
      box-shadow: 0 0 15px rgba(0,0,0,0.1);
    }
    h2 {
      text-align: center;
      margin-bottom: 25px;
      color: #333;
    }
    .tabs {
      display: flex;
      justify-content: space-around;
      margin-bottom: 20px;
    }
    .tabs button {
      background-color: #e0e0e0;
      border: none;
      padding: 10px 20px;
      border-radius: 5px;
      font-weight: bold;
      cursor: pointer;
      transition: 0.3s;
    }
    .tabs button.active {
      background-color: #4CAF50;
      color: white;
    }
    form {
      display: none;
    }
    form.active {
      display: block;
    }
    label {
      display: block;
      margin-top: 10px;
      color: #333;
    }
    input[type="text"], input[type="password"], input[type="email"] {
      width: 100%;
      padding: 10px;
      margin-top: 5px;
      border-radius: 5px;
      border: 1px solid #ccc;
    }
    input[type="submit"] {
      margin-top: 20px;
      width: 100%;
      padding: 10px;
      background-color: #4CAF50;
      color: white;
      border: none;
      font-size: 16px;
      border-radius: 5px;
      cursor: pointer;
    }
    input[type="submit"]:hover {
      background-color: #45a049;
    }
    .back-link {
      text-align: center;
      margin-top: 15px;
    }
    .back-link a {
      text-decoration: none;
      color: #555;
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
      <input type="text" name="username" id="loginUsername" required />
      <label for="loginPassword">Password</label>
      <input type="password" name="password" id="loginPassword" required />
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

<%--    <div class="back-link">--%>
<%--      <a href="${pageContext.request.contextPath}">Back to Main Menu</a>--%>
<%--    </div>--%>
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
