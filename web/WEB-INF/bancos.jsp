<%-- 
    Document   : bancos
    Created on : 25/10/2017, 21:46:51
    Author     : sergio
--%>

<%@ page contentType="text/html" pageEncoding="windows-1252"%>
<%@ page session="false" %>
<% if(request.getSession(false)==null){
    response.sendRedirect("index.html");
}%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=windows-1252">
        <meta charset="windows-1252">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="CloudB Viewer">
        <meta name="author" content="Sergio Marinho">
        <link rel="icon" href="favicon.ico">
        <title>Bancos disponíveis</title>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/listadbms.css" rel="stylesheet">
        <link href="css/base.css" rel="stylesheet">
        <script src="js/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.js"></script>
        <script>
            var t1= '<a href="dadosbd.html?db=';
            var t2= 'class="list-group-item">';
            var jqxhr = $.getJSON( "/CloudBViewer/bancosdisponiveis", function(data) {
                $.each(data,function(key,val){
                    //alert(t1+val.name+'" '+t2+val.nome+"</a>");
                    $("#listabancosdisponiveis").append(t1+val.name+'" '+t2+val.name+"</a>");
                });
            })
            .fail(function() {
                alert("Erro, não foi possível se comunicar a base de dados!");
            })
        </script>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark" id="navdif">
            <a class="navbar-brand" href="bancos.html"><img src="img/logo-invertida.png" id="logo"/></a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent" >
                <ul class="navbar-nav mr-auto">
                  <li class="nav-item active">
                    <a class="nav-link">Bancos Disponíveis</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="configuracoes.html">Configurações</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="sobre.html">Sobre</a>
                  </li>
                </ul>
                <form class="form-inline my-2 my-lg-0" action="logout" method="post">
                  <button class="btn btn-danger my-2 my-sm-0" type="submit" >Sair</button>
                </form>
            </div>
        </nav>

        <div class="container">
            <h2>Bancos de Dados disponíveis</h2>
            <div class="list-group" id="listabancosdisponiveis">
                <!--<a href="#" class="list-group-item">First item</a>-->
            </div>
        </div>
        <!-- fim de conteudo principal, comeco do rodapé-->
        <footer>
          <div>
            <span class="text-muted">© 2017 CloudB Viewer All Rights Reserved</span>
          </div>
        </footer>
    </body>
</html>