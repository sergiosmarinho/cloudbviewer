<%-- 
    Document   : configuracoes
    Created on : 25/10/2017, 21:47:41
    Author     : sergio
--%>

<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@ page session="false"%>
<% if(request.getSession(false)==null){
    response.sendRedirect("index.html");
}%>

<%String endereco = "";%>
<%String porta = "";%>
<%String usuario = "";%>
<%String senha = "";%>
<%
String[] us=null;
String linha=null;
BufferedReader br = new BufferedReader(new FileReader(Configuracoes.Confs.PATHCSV_CONF));
while ((linha = br.readLine()) != null) {
    us = linha.split(";");
}
if(us!=null && us.length==4){
    if(us[0]!=null){
        endereco = us[0];
    }
    if(us[1]!=null){
        porta = us[1];
    }
    if(us[2]!=null){
        usuario = us[2];
    }
    if(us[3]!=null){
        senha = us[3];
    }
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
        <title>Configurações</title>
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/sobre.css" rel="stylesheet">
        <link href="css/base.css" rel="stylesheet">
        
        <style type="text/css">
            #btnenviar{
                float: right;
            }
        </style>

        <script src="js/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.js"></script>
        <script src="js/sweetalert2.all.min.js"></script>
        <link href="css/sweetalert2.min.css" rel="stylesheet">
        <script>
            //checando se passaram para cá com o parâmetro de erro de login
            $(window).ready(function(){
                if(window.location.search.substring(1).indexOf("a=0")!==-1){
                    swal({
                        title: 'Erro',
                        text: "Um erro inesperado ocorreu, tente novamente!",
                        type: 'error',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Ok'
                    }).then(function(){
                        document.location.href = "configuracoes.html";
                    });
                }else if(window.location.search.substring(1).indexOf("a=1")!==-1){
                    swal({
                        title: 'Sucesso',
                        text: "Os dados foram alterados!",
                        type: 'success',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Ok'
                    }).then(function(){
                        document.location.href = "configuracoes.html";
                    });
                }
            });
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
                  <li class="nav-item">
                    <a class="nav-link" href="bancos.html">Bancos Disponíveis</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link active">Configurações</a>
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
            <h2>Configurações</h2>
            <form class="form-horizontal" action="/CloudBViewer/dadosconfiguracoes" method="POST">
                <div class="form-group">
                    <label class="control-label col-sm-3">Endereço do SGBD:</label>
                    <div class="col-sm-10">
                        <input type="text" autocomplete="off" class="form-control" id="endereco" required="" placeholder="insira o endereço aqui" name="endereco" value="<%=endereco%>">
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-3">Porta:</label>
                    <div class="col-sm-10">
                        <input type="text" autocomplete="off" class="form-control" id="porta" required="" placeholder="insira a porta aqui" name="porta" value="<%=porta%>">
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-3">Usuário:</label>
                    <div class="col-sm-10">
                        <input type="text" autocomplete="off" class="form-control" id="usuario" required="" placeholder="insira o usuario aqui" name="usuario" value="<%=usuario%>">
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2" for="pwd">Senha:</label>
                    <div class="col-sm-10">          
                        <input type="password" autocomplete="off" class="form-control" id="senha" required="" placeholder="insira a senha aqui" name="senha" value="<%=senha%>">
                    </div>
                </div>
                <div class="form-group">        
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-success" id="btnenviar">Salvar</button>
                    </div>
                </div>
            </form>
        </div>
        <!-- fim de conteudo principal, comeco do rodapé-->
        <footer>
          <div>
            <span class="text-muted">© 2017 CloudB Viewer All Rights Reserved</span>
          </div>
        </footer>
    </body>
</html>
