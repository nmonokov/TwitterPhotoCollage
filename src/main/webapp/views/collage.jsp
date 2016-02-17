<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/resources/pics/favicon.ico" rel="icon">

    <title>Twitter Collage</title>
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/css/signin.css" rel="stylesheet">
    <link href="/resources/css/sticky-footer.css" rel="stylesheet"/>

  </head>
  <body>
    <div class="container">
        <div style="text-align:center;">
          <img id="img" name="img" src="/image/collage.png" >
        </div>
        <form class="form-signin">
          <button class="btn btn-lg btn-primary btn-block" formaction="/back" formmethod="get">Назад</button>
        </form>
    </div>
    <footer class="footer">
        <div class="container">
            <p class="text-muted"><a href="https://github.com/nmonokov/TwitterPhotoCollage">GitHub</a> | <a href="/logs">Logs</a></p>
        </div>
    </footer>
  </body>
</html>
