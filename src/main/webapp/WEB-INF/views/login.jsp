<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>ログイン</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
  <div class="card">
    <h1 class="page-title">ログイン</h1>

    <c:if test="${not empty error}">
      <div class="alert alert-error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/app/login" method="post" class="form">
      <div class="form-row">
        <label for="username">ユーザー名</label>
        <input type="text" id="username" name="username" value="${param.username}">
      </div>

      <div class="form-row">
        <label for="password">パスワード</label>
        <input type="password" id="password" name="password">
      </div>

      <div class="form-actions">
        <button type="submit" class="btn">ログイン</button>
      </div>
    </form>

    <div class="muted">
      ※ユーザー名・パスワードは管理者から案内されたものを入力してください。
    </div>
  </div>
</div>

</body>
</html>

