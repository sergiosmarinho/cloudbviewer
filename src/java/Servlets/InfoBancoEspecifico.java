/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import AcessoDados.AcessoBdDadosEspecificos;
import AcessoDados.DadoColeta;
import AcessoDados.Uteis;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sergio
 */
public class InfoBancoEspecifico extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getSession(false)==null){
            response.sendRedirect("index.html");
        }
        if(request.getParameter("db")==null || request.getParameter("db").trim().compareTo("")==0){
            response.sendRedirect("bancos.html");
        }
        String db = request.getParameter("db");
        String endereco=null;
        String porta=null;
        String usuario=null; 
        String senha=null;
        double proximoValorPredito=-1;
        try{
            String sa = getDadosAcesso();
            String[] saux = sa.split(";");
            if(saux!=null && saux.length==4){
                if(saux[0]!=null){
                    endereco = saux[0];
                }
                if(saux[1]!=null){
                    porta = saux[1];
                }
                if(saux[2]!=null){
                    usuario = saux[2];
                }
                if(saux[3]!=null){
                    senha = saux[3];
                }
                if(endereco==null||porta==null||usuario==null||senha==null){
                    throw new Exception();
                }
                if(endereco.trim().compareTo("")==0 || porta.trim().compareTo("")==0 || usuario.trim().compareTo("")==0 || senha.trim().compareTo("")==0){
                    throw new Exception();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        AcessoBdDadosEspecificos ac = new AcessoBdDadosEspecificos();
        ArrayList<DadoColeta> dadosReais = ac.pegaTemposBanco(db, endereco, porta, usuario, senha);
        ServletContext context = request.getSession().getServletContext();
        ArrayList<DadoColeta> dadosPrevistos = (ArrayList<DadoColeta>)context.getAttribute("dadosPrevistos"); 
        if(dadosPrevistos==null){
            dadosPrevistos = new ArrayList<>();
            context.setAttribute("dadosPrevistos", dadosPrevistos);
        }
        if(dadosReais.size()>3 && !Uteis.jaTemPrevisaoMinutoSeguinte(dadosPrevistos,dadosReais)){//se tem o minimo de valores para fazer uma previsao e se os dados por algum motivo não estao sendo atualizados e ha uma previsao ja feita 
            try {
                proximoValorPredito = Uteis.predicaoSerie(Uteis.pegaValoresPredicao(dadosReais), 1);
                Date dataPrevisao = Uteis.getHorarioMinutoSeguinte(dadosReais.get(dadosReais.size()-1).getTime());
                dadosPrevistos.add(new DadoColeta(dataPrevisao, proximoValorPredito));
            } catch (Exception ex) {
                response.sendRedirect("/error.html");
                ex.printStackTrace();
            }
        }else{//se já tem a predição do ultimo minuto, entao pega essa do ultimo minuto, para que possa ser colocado no csv
            proximoValorPredito = dadosPrevistos.get(dadosPrevistos.size()-1).getResponseTime();
        }
        response.setContentType("text/csv");
        List<String> linha = new ArrayList<String>();
        List<List<String>> csv = new ArrayList<List<String>>();
        linha.add("date");
        linha.add("previsto");
        linha.add("real");
        csv.add(linha);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH:mm");
        
        for(int i=0;i<dadosReais.size();i++){
            DadoColeta auxiliar = dadosReais.get(i);
            if(i+1<dadosReais.size()){
                linha = new ArrayList<String>();
                linha.add(df.format(auxiliar.getTime()));//data
                if(Uteis.achaValorPrevistoAnterior(auxiliar.getTime(), dadosPrevistos)>0){
                   linha.add(Uteis.achaValorPrevistoAnterior(auxiliar.getTime(), dadosPrevistos)+"");//previsto 
                }else{
                   linha.add("null");//previsto 
                }
                linha.add(auxiliar.getResponseTime()+"");//real
                csv.add(linha);    
            }else{//se for o ultimo da sequencia
                if(dadosPrevistos.size()<2){//pra mostrar bonitinho o grafico de predição continuando o real, caso só tenha um valor
                    linha = new ArrayList<String>();
                    linha.add(df.format(auxiliar.getTime()));//data
                    if(Uteis.achaValorPrevistoAnterior(auxiliar.getTime(), dadosPrevistos)>0){
                       linha.add(Uteis.achaValorPrevistoAnterior(auxiliar.getTime(), dadosPrevistos)+"");//previsto 
                    }else{
                       linha.add(auxiliar.getResponseTime()+"");//previsto no ponto do real, só pra dar continuidade no grafico
                    }
                    linha.add(auxiliar.getResponseTime()+"");//real
                    csv.add(linha);
                }
            }
        }
        
        if(proximoValorPredito>0){
            linha = new ArrayList<String>();
            linha.add(df.format(Uteis.getHorarioMinutoSeguinte(dadosReais.get(dadosReais.size()-1).getTime())));//data
            linha.add(proximoValorPredito+"");//previsto
            linha.add("null");//real
            csv.add(linha);
        }
        writeCsv(csv, ',', response.getOutputStream());
        //codigo de teste do front end
        /*response.setContentType("text/csv");
        List<String> linha = new ArrayList<String>();
        List<List<String>> csv = new ArrayList<List<String>>();
        linha.add("date");
        linha.add("previsto");
        linha.add("real");
        csv.add(linha);
        linha = new ArrayList<String>();
        linha.add("1-01-2012-07:23");
        linha.add("68.13");
        linha.add("34.12");
        csv.add(linha);
        linha = new ArrayList<String>();;
        linha.add("1-01-2012-07:24");
        linha.add("63.98");
        linha.add("45.56");
        csv.add(linha);
        writeCsv(csv, ',', response.getOutputStream());
        /*response.setContentType("text/plain");
        try{
            double[] e = {17.0,21.0,32.0,43.0,54.0,65.0,76.0};
           AcessoBdDadosEspecificos abd = new AcessoBdDadosEspecificos();
           //ArrayList<String> a = new ArrayList<>();
           //a.add("R");
           //a.add("-e");
           //a.add("library('forecast'); arimafit=auto.arima(c(1,2,3,4,5,6,7)); fcast=forecast(arimafit,h=1); fcast$mean;");
           response.getOutputStream().print(abd.execute(e,1)); 
           //response.getOutputStream().print(abd.execute(e,1).toString());//abd.execute("R -e \"library('forecast'); arimafit=auto.arima(c(1,2,3,4,5,6,7)); fcast=forecast(arimafit,h=1); fcast$mean;\"")); 
        }catch(Exception e){
           e.printStackTrace();
        }*/
    }

public static <T> void writeCsv (List<List<T>> csv, char separator, OutputStream output) throws IOException {
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output,"UTF-8"));
    for (List<T> row : csv) {
        for (Iterator<T> iter = row.iterator(); iter.hasNext();) {
            String field = String.valueOf(iter.next()).replace("\"", "\"\"");
            if (field.indexOf(separator) > -1 || field.indexOf('"') > -1) {
                field = '"' + field + '"';
            }
            writer.append(field);
            if (iter.hasNext()) {
                writer.append(separator);
            }
        }
        writer.newLine();
    }
    writer.flush();
}

private String getDadosAcesso() throws Exception{
    String[] us=null;
    String linha=null;
    BufferedReader br = new BufferedReader(new FileReader(Configuracoes.Confs.PATHCSV_CONF));
    while ((linha = br.readLine()) != null) {
        return linha;
    }
    throw new Exception();
}

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
