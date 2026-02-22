<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>タスク一覧</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

  <style>
    .pagination a, .pagination span { margin: 0 4px; }
    .current-page { font-weight: bold; }
    .active { font-weight: bold; text-decoration: underline; }
    .fav { text-decoration: none; font-size: 18px; }
    .table-wrap { overflow-x: auto; width: 100%; }
    .task-table { width: 100%; border-collapse: collapse; white-space: nowrap; }
  </style>
</head>
<body>

<h1>タスク一覧</h1>

<p class="muted">ログイン中：${loginUser.username}</p>

<c:if test="${not empty error}">
  <div class="alert alert-error">${error}</div>
</c:if>

<c:if test="${not empty message}">
  <div class="alert alert-info">${message}</div>
</c:if>

<form action="${pageContext.request.contextPath}/app/task/list" method="get" class="search">
  <input type="text" name="keyword" value="${keyword}" placeholder="タスク名で検索">
  <input type="hidden" name="sort" value="${sort}">
  <input type="hidden" name="page" value="1">
  <button type="submit" class="btn">検索</button>
</form>

<c:if test="${not empty keyword}">
  <p class="muted">
    検索キーワード：「${keyword}」
    <a href="${pageContext.request.contextPath}/app/task/list">クリア</a>
  </p>
</c:if>

<div class="sort">
  並べ替え：
  <a href="?keyword=${keyword}&sort=DESC&page=1" class="${sort == 'DESC' ? 'active' : ''}">新しい順</a> |
  <a href="?keyword=${keyword}&sort=ASC&page=1" class="${sort == 'ASC' ? 'active' : ''}">古い順</a>
</div>

<p>
  <a class="btn btn-sub" href="${pageContext.request.contextPath}/app/task/new">タスク新規作成</a>
</p>

<c:if test="${totalRecords > 0}">
  <div class="muted">
    全${totalRecords}件中 ${startRecord}-${endRecord}件目を表示（${currentPage}/${totalPages}ページ）
  </div>
</c:if>

<c:choose>
  <c:when test="${empty tasks}">
    <c:choose>
      <c:when test="${not empty keyword}">
        <p>「${keyword}」に該当するタスクはありません。</p>
      </c:when>
      <c:otherwise>
        <p>タスクが登録されていません。</p>
      </c:otherwise>
    </c:choose>
  </c:when>

  <c:otherwise>
    <div class="table-wrap">
      <table class="task-table">
        <tr>
          <th>★</th>
          <th>タイトル</th>
          <th>カテゴリ</th>
          <th>ステータス</th>
          <th>操作</th>
        </tr>

        <c:forEach var="task" items="${tasks}">
          <tr>
            <td>
              <a class="fav ${task.favorite ? 'favorite-active' : 'favorite-inactive'}"
                 href="${pageContext.request.contextPath}/app/favorite/toggle?taskId=${task.id}">
                <c:choose>
                  <c:when test="${task.favorite}">★</c:when>
                  <c:otherwise>☆</c:otherwise>
                </c:choose>
              </a>
            </td>

            <td>${task.title}</td>

            <td>
              <c:choose>
                <c:when test="${task.category == 'work'}">仕事</c:when>
                <c:when test="${task.category == 'private'}">プライベート</c:when>
                <c:when test="${task.category == 'study'}">勉強</c:when>
                <c:otherwise>${task.category}</c:otherwise>
              </c:choose>
            </td>

            <td>
              <c:choose>
                <c:when test="${task.status == 'pending'}">未着手</c:when>
                <c:when test="${task.status == 'doing'}">進行中</c:when>
                <c:when test="${task.status == 'done'}">完了</c:when>
                <c:otherwise>${task.status}</c:otherwise>
              </c:choose>
            </td>

            <td>
              <a href="${pageContext.request.contextPath}/app/task/edit?id=${task.id}">編集</a>
              |
              <a href="${pageContext.request.contextPath}/app/task/delete?id=${task.id}"
                 onclick="return confirm('削除していい？');">削除</a>
            </td>
          </tr>
        </c:forEach>
      </table>
    </div>
  </c:otherwise>
</c:choose>

<c:if test="${totalPages > 1}">
  <div class="pagination" style="margin-top: 12px;">
    <c:if test="${hasPrevious}">
      <a href="?keyword=${keyword}&sort=${sort}&page=${currentPage - 1}">＜前へ</a>
    </c:if>

    <c:forEach begin="1" end="${totalPages}" var="pageNum">
      <c:choose>
        <c:when test="${pageNum == currentPage}">
          <span class="current-page">${pageNum}</span>
        </c:when>
        <c:otherwise>
          <a href="?keyword=${keyword}&sort=${sort}&page=${pageNum}">${pageNum}</a>
        </c:otherwise>
      </c:choose>
    </c:forEach>

    <c:if test="${hasNext}">
      <a href="?keyword=${keyword}&sort=${sort}&page=${currentPage + 1}">次へ＞</a>
    </c:if>
  </div>
</c:if>

<br>
<a href="${pageContext.request.contextPath}/app/home">ホームへ</a>

</body>
</html>
