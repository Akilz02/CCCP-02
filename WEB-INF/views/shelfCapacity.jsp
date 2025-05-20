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
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  <head>
    <title>Shelf Item Capacity</title>
    <style>
      .error { color: red; }
      .success { color: green; }
    </style>
  </head>
  <body>
  <h2>Shelf Item Capacity</h2>

  <c:if test="${not empty error}">
    <div class="error">${error}</div>
  </c:if>

  <c:if test="${not empty success}">
    <div class="success">${success}</div>
  </c:if>

  <form method="post" action="shelfCapacity">
    <div>
      <label for="shelfId">Shelf ID:</label>
      <input type="text" id="shelfId" name="shelfId" required>
    </div>
    <div>
      <label for="itemId">Item ID:</label>
      <input type="text" id="itemId" name="itemId" required>
    </div>
    <div>
      <label for="capacityLimit">Capacity Limit:</label>
      <input type="number" id="capacityLimit" name="capacityLimit" required min="1">
    </div>
    <div>
      <button type="submit">Submit</button>
    </div>
  </form>
  </body>
  </html>
