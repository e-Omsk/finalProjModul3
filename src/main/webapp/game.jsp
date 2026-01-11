<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: vaceslavbragin
  Date: 19.12.2025
  Time: 00:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Квест </title>
    <link rel="stylesheet" href="styles/styles.css">
</head>
<body>
    <h1 class="gameH1">Квест</h1>
    <div>
        <p>Игрок: ${sessionScope.playerName}</p>
    </div>

    <hr>
    <h2 class="gameH2">${step.text}</h2>

    <div class="gameDiv">
    <c:choose>
        <c:when test="${not empty step.option1}">
            <form action="game" method="post">
                <input type="hidden" name="currentStep" value="${step.id}">
                <p class="gameP1">
                    <input type="radio" name="answer" value="1" required>
                    ${step.option1}
                </p>
                <p class="gameP2">
                    <input type="radio" name="answer" value="2" required>
                    ${step.option2}
                </p>
                <button type="submit">Ответить</button>
            </form>
        </c:when>

        <c:otherwise>
            <p>

                <a href="start">Играть сначала.</a>
            </p>
        </c:otherwise>
    </c:choose>
    </div>

    <hr>
    <div class="gameDiv1">
        <p>ID сессии: ${pageContext.session.id}</p>
        <p>Текущий шаг: ${step.id}</p>
        <p>Пройденных шагов: ${sessionScope.gamesPlayed != null ? sessionScope.gamesPlayed : 0 }</p>
    </div>
</body>
</html>
