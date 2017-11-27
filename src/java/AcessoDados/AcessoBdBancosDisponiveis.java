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
import java.util.List;

/**
 *
 * @author lsbd
 */
public class AcessoBdBancosDisponiveis {

    private String endereco;
    private String porta;
    private String usuario;
    private String senha;
    
    public AcessoBdBancosDisponiveis(String endereco,String porta,String usuario,String senha) {
        this.endereco = endereco;
        this.porta=porta;
        this.usuario=usuario;
        this.senha=senha;
    }
        
    public ArrayList<String> consultarBancosDisponiveis() {
        ArrayList<String> retorno = new ArrayList<String>();
        try { 
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://"+endereco+":"+porta+"/qosdbc-catalog", usuario, senha);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select db_name from db_active");
            while (rs.next()) {
                if(rs.getString("db_name")!=null){
                    retorno.add(rs.getString("db_name"));
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
}
