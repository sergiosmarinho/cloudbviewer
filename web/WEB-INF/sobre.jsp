<%-- 
    Document   : sobre
    Created on : 25/10/2017, 21:48:58
    Author     : sergio
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@page session="false"%>
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
        <title>Sobre</title>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/sobre.css" rel="stylesheet">
        <link href="css/base.css" rel="stylesheet">

        <script src="js/jquery-3.2.1.min.js"></script>
        <script src="js/bootstrap.js"></script>
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
                    <a class="nav-link active">Sobre</a>
                  </li>
                </ul>
                <form class="form-inline my-2 my-lg-0" action="logout" method="post">
                  <button class="btn btn-danger my-2 my-sm-0" type="submit">Sair</button>
                </form>
            </div>
        </nav>

        <div class="container">
            <h2>Sobre</h2>
            <div class="modal-body row">
                <div class="col-md-7" id="colunagrafico">
                    <div class="divinfos">
                        <p class="lead">O CloudB Viewer é uma ferramenta desenvolvida para ser utilizada com bancos de dados replicados em nuvem. O seu propósito é de mostrar graficamente os tempos de respostas dos bancos de dados, bem como quantas réplicas de cada banco estão disponíveis. O usuário pode adicionar um Service Level Agreement(SLA), sendo considerado nesta aplicação somente o tempo de resposta, para acompanhar e ser alertado, caso ocorra uma violação. Como diferencial, a ferramenta utiliza o modelo Autoregressive Integrated Moving Average(ARIMA) para tentar prever os tempos de resposta futuros e alertar ao adminstrador se houver previsão de violação de SLA.</p>
                    </div>
                </div>
                <div class="col-md-5">
                    <img id="logosobre" src="img/logo.png"/>
                </div>
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