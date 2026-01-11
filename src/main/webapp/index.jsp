<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<link rel="stylesheet" href="styles/styles.css">
<body class="index">
    <h1 class="indexH1">Добро пожаловать!</h1>
    <h2>Начало Квеста.</h2>
    <h3>Выходные. Едем на дачу.</h3>

    <form action="start" method="post">
         <label for="playerName">Предстватесь:</label><br>
         <input type="text" id="playerName" name="playerName"
            placeholder="Введите ваше имя:" required
            value="${sessionScope.playerName != null ? sessionScope.playerName : ''}"><br> <br>
    <button type="submit">Поехали!</button>
    </form>
</body>
</html>
