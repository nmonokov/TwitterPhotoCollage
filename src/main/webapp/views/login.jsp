<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html >
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="icon" href="/favicon.ico">

        <title>Twitter Collage | Login</title>
        <link href=${pageContext.request.contextPath}"/resources/css/styles.css" rel="stylesheet">
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/validation.js"></script>
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/signin.css" rel="stylesheet">
    </head>

    <body>
        <div class="container">

            <form class="form-signin" method="get">
                <h4 class="form-signin-heading" style="text-align:center">Давай створимо крутий коллаж!</h4>
                <c:if test="${error != null}">
                    <div style="color:red" id="error">${error}</div>
                </c:if>
                <label for="login" class="sr-only">Логін користувача</label>
                <input type="text" id="login" name="login" class="form-control" placeholder="Логін користувача" required autofocus>
                <label for="width" class="sr-only">Ширина комірки</label>
                <input type="number" id="width" name="width" class="form-control" style="width:50%; float:left;" placeholder="Ширина комірки" required>
                <label for="height" class="sr-only">Висота комірки</label>
                <input type="number" id="height" name="height" class="form-control" style="width:50%" placeholder="Висота комірки" required>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="diffSize"> Розмір зображень залежить від кількості твітів
                    </label>
                </div>
                <button class="btn btn-lg btn-primary btn-block" formaction="/collage">Створити</button>
            </form>
        </div>
    </body>
</html>

