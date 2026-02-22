<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>タスク新規作成</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<h1>タスク新規作成</h1>

<c:if test="${not empty error}">
  <div class="alert alert-error">${error}</div>
</c:if>

<c:if test="${not empty errors}">
  <div class="alert alert-error">入力内容を確認してください</div>
</c:if>

<form action="${pageContext.request.contextPath}/app/task/new" method="post">

  <div>
    <label>タイトル *</label><br>
    <input type="text" name="title" value="${title}">
    <c:if test="${not empty errors.title}">
      <div class="field-error">
        <c:forEach items="${errors.title}" var="e"><div>${e}</div></c:forEach>
      </div>
    </c:if>
  </div>

  <div>
    <label>説明</label><br>
    <textarea name="description">${description}</textarea>
    <c:if test="${not empty errors.description}">
      <div class="field-error">
        <c:forEach items="${errors.description}" var="e"><div>${e}</div></c:forEach>
      </div>
    </c:if>
  </div>

  <div>
    <label>カテゴリ</label><br>
    <select name="category">
      <option value="work" ${category == 'work' || empty category ? 'selected' : ''}>仕事</option>
      <option value="private" ${category == 'private' ? 'selected' : ''}>プライベート</option>
      <option value="study" ${category == 'study' ? 'selected' : ''}>勉強</option>
    </select>
    <c:if test="${not empty errors.category}">
      <div class="field-error">
        <c:forEach items="${errors.category}" var="e"><div>${e}</div></c:forEach>
      </div>
    </c:if>
  </div>

  <div>
    <label>ステータス</label><br>
    <select name="status">
      <option value="pending" ${status == 'pending' || empty status ? 'selected' : ''}>未着手</option>
      <option value="doing" ${status == 'doing' ? 'selected' : ''}>進行中</option>
      <option value="done" ${status == 'done' ? 'selected' : ''}>完了</option>
    </select>
    <c:if test="${not empty errors.status}">
      <div class="field-error">
        <c:forEach items="${errors.status}" var="e"><div>${e}</div></c:forEach>
      </div>
    </c:if>
  </div>

  <button type="submit" class="btn">作成</button>
  <a class="btn btn-sub" href="${pageContext.request.contextPath}/app/task/list">戻る</a>

</form>
</body>
</html>
