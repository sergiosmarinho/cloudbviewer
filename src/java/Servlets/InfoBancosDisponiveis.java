/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import AcessoDados.AcessoBdBancosDisponiveis;
import AcessoDados.AcessoBdDadosEspecificos;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sergio
 */
public class InfoBancosDisponiveis extends HttpServlet {

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
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            String endereco=null;
            String porta=null;
            String usuario=null; 
            String senha=null;
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
                //tratar a exceção aqui e devolver algo para ser mostrado que tem erro;
            }            
            AcessoBdBancosDisponiveis a = new AcessoBdBancosDisponiveis(endereco, porta, usuario, senha);
            ArrayList<String> disponiveis = a.consultarBancosDisponiveis();
            String estruturado = "[";
            for(int i=0;i<disponiveis.size();i++){
                estruturado+="{\"name\":"+"\""+disponiveis.get(i)+"\"}";
                if(i+1<disponiveis.size()){
                    estruturado+=",";
                }
            }
            estruturado+="]";
            out.print(estruturado);//"[{\"name\":\"jack\"},{\"name\":\"john\"},{\"name\":\"joe\"}]");
        }
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
