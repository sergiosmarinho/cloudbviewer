/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AcessoDados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author sergio
 */
public class AcessoBdDadosEspecificos {
        
    public ArrayList<String> pegaReplicasBanco(String nomeBanco, String endereco, String porta, String usuario, String senha){
        ArrayList<String> retorno = new ArrayList<String>();
        String idBanco = getIdBanco(nomeBanco,endereco,porta,usuario,senha);
        if(idBanco==null){
            return retorno;
        }
        retorno.add(idBanco);
        try { 
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://"+endereco+":"+porta+"/qosdbc-catalog", usuario, senha);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select vm_id from db_active_replica where master="+"\'"+idBanco+"\'");
            while (rs.next()) {
                if(rs.getString("vm_id")!=null){
                    retorno.add(rs.getString("vm_id"));
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return retorno;
    }

    private String getIdBanco(String nomeBanco, String endereco, String porta, String usuario, String senha) {
        String retorno=null;
        try { 
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://"+endereco+":"+porta+"/qosdbc-catalog", usuario, senha);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select vm_id from db_active where db_name=\'"+nomeBanco+"\'");
            while (rs.next()) {
                if(rs.getString("vm_id")!=null){
                    retorno=rs.getString("vm_id");
                }
            }
            rs.close();
            stmt.close();
            conn.close();
            return retorno;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public ArrayList<DadoColeta> pegaTemposBanco(String nomeBanco, String endereco, String porta, String usuario, String senha){
        ArrayList<DadoColeta> dados = new ArrayList<DadoColeta>();
        try { 
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://"+endereco+":"+porta+"/qosdbc-log", usuario, senha);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select time,response_time from sql_log where db_name="+"\'"+nomeBanco+"\'"+" order by time asc");
            while (rs.next()) {
                DadoColeta ab = new DadoColeta(new Date(rs.getLong("time")), rs.getLong("response_time"));
                dados.add(ab);
                System.out.println(ab);
            }
            rs.close();
            stmt.close();
            conn.close();
            ArrayList<DadoColeta> retorno = Uteis.calculaMediaDados(dados);
            return retorno;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
}
