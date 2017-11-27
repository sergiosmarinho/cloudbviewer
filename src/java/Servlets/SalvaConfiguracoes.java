/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import static Servlets.InfoBancoEspecifico.writeCsv;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sergio
 */
public class SalvaConfiguracoes extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

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
        response.sendRedirect("configuracoes.html");
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
        if(request.getSession(false)==null){
            response.sendRedirect("index.html");
        }
        if(request.getParameter("endereco")==null || request.getParameter("porta")==null || request.getParameter("usuario")==null || request.getParameter("porta")==null){
            response.sendRedirect("configuracoes.html?a=0");
        }
        String endereco = request.getParameter("endereco");
        String porta = request.getParameter("porta");
        String senha = request.getParameter("senha");
        String usuario = request.getParameter("usuario");
        
        File file = new File(Configuracoes.Confs.PATHCSV_CONF);
        try (FileOutputStream fop = new FileOutputStream(file)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            List<String> linha = new ArrayList<String>();
            List<List<String>> csv = new ArrayList<List<String>>();
            linha.add(endereco);
            linha.add(porta);
            linha.add(usuario);
            linha.add(senha);
            csv.add(linha);
            writeCsv(csv, ';', fop);
        } catch (IOException e) {
            response.sendRedirect("configuracoes.html?a=0");
        }
        response.sendRedirect("configuracoes.html?a=1");
    }
    
    public static <T> void writeCsv (List<List<T>> csv, char separator, OutputStream output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
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
