<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="/resources/pics/favicon.ico">

    <title>Twitter Collage</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/signin.css" rel="stylesheet">

  </head>
  <body>
    <div class="container">
        <div style="text-align:center;">
          <img id="img" name="img" src="/collage/image" >
        </div>
        <form class="form-signin">
          <button class="btn btn-lg btn-primary btn-block" formaction="/back" formmethod="get">Назад</button>
        </form>
    </div>
  </body>
</html>
