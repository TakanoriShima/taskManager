<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>ホーム</title>
</head>
<body>
  <%
    User loginUser = (User) request.getAttribute("loginUser");
  %>

  <h1>ホーム</h1>
  <p>ログイン中：<%= (loginUser != null ? loginUser.getUsername() : "未ログイン") %></p>
  
  <p>
    <a href="${pageContext.request.contextPath}/app/task/list">
        タスク一覧へ
    </a>
  </p> 

  <p><a href="<%= request.getContextPath() %>/app/logout">ログアウト</a></p>
</body>
</html>
