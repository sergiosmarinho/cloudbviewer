<%-- 
    Document   : dadosbd
    Created on : 25/10/2017, 21:48:22
    Author     : sergio
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@ page session="false" %>
<% if(request.getSession(false)==null){
    response.sendRedirect("index.html");
}
if(!(request.getParameter("db")!=null && request.getParameter("db").trim().compareTo("")!=0)){
    response.sendRedirect("bancos.html");
}
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=windows-1252">
        <meta charset="windows-1252">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="CloudB Viewer">
        <meta name="author" content="Sergio Marinho">
        <link rel="icon" href="favicon.ico">
        <title>Dados do banco</title>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/dadosbd.css" rel="stylesheet">
        <link id="css" href="css/base.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dadosbd.css">

        <script>
            var dbname="<%=request.getParameter("db")%>";
        </script>
        <script src="js/jquery-3.2.1.min.js"></script>
        <script src="js/d3.v4.min.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/sweetalert2.all.min.js"></script>
        <link href="css/sweetalert2.min.css" rel="stylesheet">
        <script src="js/dadosbd.js"></script>
        <style type="text/css">
            .swal2-modal *,.swal2-show *{
            	overflow: hidden;
            }
            #btnInSla{
            float: right;
            margin-left: 2%;
            margin-top: 2%;
            width: 150px;
          }
          #btnExSla{
            margin-top: 2%;
            float: right;
            width: 150px;
          }
          @media only screen and (max-width:767px){
            #btnInSla{
              width: 100%;
            }
            #btnExSla{
              width: 100%;
            }  
          }
        </style>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark" id="navdif">
            <a class="navbar-brand" href="bancos.html"><img src="img/logo-invertida.png" id="logo"/></a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent" >
                <ul class="navbar-nav mr-auto">
                  <li class="nav-item">
                    <a class="nav-link" href="bancos.html">Bancos Disponíveis</a>
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

<!-- conteudo principal-->
        <div class="container">
          <h2 id="titulobd"></h2>
          <div class="modal-body row">
           <div class="col-md-5">
             <h5 class="subtitulo">Réplicas</h5>
             <p class="textoseminfo">Sem informações disponíveis...</p>
             <ul class="list-group" id="gruporeplicas">
                <!--<li class="list-group-item">First item</li>-->
            </ul>
           </div>
           <div class="col-md-7" id="colunagrafico">
              <h5 class="subtitulo">Tempo de resposta geral</h5>
              <p class="textoseminfo">Sem informações disponíveis...</p>
              <svg>
              </svg>
              <p id="legenda"></p>
              <button type="button" class="btn btn-success" id="btnInSla" onclick="inserirSla()">Adicionar SLA</button>
              <button type="button" class="btn btn-danger" id="btnExSla" onclick="excluirSla()" disabled>Excluir SLA</button>
           </div>
         </div>
        </div>
        <audio id="audio" src="sound/alert.wav" autostart="true"></audio>
<!-- fim de conteudo principal, comeco do rodapé-->
        <footer>
          <div>
            <span class="text-muted">© 2017 CloudB Viewer All Rights Reserved</span>
          </div>
        </footer>
    </body>
</html>