<%-- 
    Document   : index
    Created on : 26/10/2017, 00:41:33
    Author     : sergio
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@ page session="false" %>
<% if(request.getSession(false)!=null){
    response.sendRedirect("configuracoes.html");
}%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=windows-1252">
        <meta charset="windows-1252">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="CloudB Viewer">
        <meta name="author" content="Sergio Marinho">
        <link rel="icon" href="favicon.ico" type="image/x-icon">
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon"> 
        <title>Login</title>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/index.css" rel="stylesheet">
        <link href="css/base.css" rel="stylesheet">

        <script src="js/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.js"></script>
        <script src="js/sweetalert2.all.min.js"></script>
        <link href="css/sweetalert2.min.css" rel="stylesheet">
        <script>
            //checando se passaram para cá com o parâmetro de erro de login
            $(window).ready(function(){
                if(window.location.search.substring(1).indexOf("a=1")!==-1){
                    swal({
                        title: 'Falha de autenticação',
                        text: "Os dados inseridos estão incorretos, tente novamente!",
                        type: 'error',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Ok'
                    }).then(function(){
                        document.location.href = "index.html";
                    });
                }
            });
        </script>
    </head>
    <body>
        <div class="container">
          <form class="form-signin" action="login" method="POST">
            <h2 class="form-signin-heading">Login</h2>
            <label for="inputText" class="sr-only">Usuário</label>
            <input id="inputText" name="usuario" class="form-control" placeholder="Usuario" required="" autofocus="" type="text">
            <label for="inputPassword" class="sr-only">Senha</label>
            <input id="inputPassword" name="senha" class="form-control" placeholder="Senha" required="" type="password">
            <button class="btn btn-lg btn btn-outline-primary btn-block" type="submit">Entrar</button>
          </form>
        </div>
    </body>
</html>

