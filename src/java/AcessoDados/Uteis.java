/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AcessoDados;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author sergio
 */
public class Uteis {
    public static ArrayList<DadoColeta> calculaMediaDados(ArrayList<DadoColeta> e){
        Date d = null;
        ArrayList<DadoColeta> resposta = new ArrayList<DadoColeta>();
        if(e==null || e.isEmpty()){
            return resposta;
        }
        for (DadoColeta aux : e) {
            if(d==null || !dataIgual(d,aux.getTime())){
                resposta.add(calculaMediaDeDataEspecifica(aux.getTime(),e));
                d = aux.getTime();
            }
        }
        return resposta;
    }
    
    private static DadoColeta calculaMediaDeDataEspecifica(Date d,ArrayList<DadoColeta> e){
        long soma=0;
        long media=0;
        int contador=0;
        for(DadoColeta aux:e){
            if(dataIgual(aux.getTime(),d)){
               soma+=aux.getResponseTime();
               contador++;
            }
        }
        media=soma/contador;
        DadoColeta retorno = new DadoColeta(d, media);
        return retorno;
    }
    
    public static double[] pegaValoresPredicao(ArrayList<DadoColeta> e){
        double[] retorno = new double[4];
        if(e.size()<4){
            return retorno;
        }
        else{
            for(int i=e.size()-4;i<e.size();i++){
                retorno[i]=e.get(i).getResponseTime();
            }
        }
        return retorno;
    }
    
    public static double predicaoSerie(double[] serie, int horizonte) throws Exception {
        ArrayList<String> a = new ArrayList<>();
           a.add("R");
           a.add("-e");
           a.add("library('forecast'); arimafit=auto.arima(c("+doubleToString(serie)+")); fcast=forecast(arimafit,h="+horizonte+"); fcast$mean;");
        return Double.parseDouble(Uteis.execute(a));
    }
    
    private static String execute(List<String> comandos) throws Exception {
        String resultado = "";
        ProcessBuilder pb = new ProcessBuilder(comandos);
        try {
            Process p = pb.start();
            p.waitFor();
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            while (br.ready()) {
                resultado += br.readLine() + "\n";
            }
        } catch (IOException ex) {
            throw new Exception(ex);
        } catch (InterruptedException ex) {
            throw new Exception(ex);
        }
        return trataResultado(resultado);
    }
    
    private static String doubleToString(double vector[]){
        String retorno = "";
        for(int i=0;i<vector.length;i++){
            if(i<vector.length-1){
                retorno+=vector[i]+",";
            }else{
                retorno+=vector[i];
            }
        }
        return retorno;
    }
    
    private static String trataResultado(String s){
        int pos = s.indexOf("[1]");
        s = s.substring(pos);
        s = s.replace("[1]", "");
        s = s.replace(">", "");
        s = s.replace(" ", "");
        s = s.replace("\n", "");
        return s;
    }

    public static boolean jaTemPrevisaoMinutoSeguinte(ArrayList<DadoColeta> dadosPrevistos, ArrayList<DadoColeta> dadosReais) {
        Date minutoSeguinte = getHorarioMinutoSeguinte(dadosReais.get(dadosReais.size()-1).getTime());
        for(DadoColeta d:dadosPrevistos){
            if(dataIgual(d.getTime(),minutoSeguinte)){
                return true;
            }
        }
        return false;
    }
    
    public static Date getHorarioMinutoSeguinte(Date d){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE)+1);
        return c.getTime();
    }
    
    //procura no array de valores previstos se ja há algum daquela data, retorna -1 se não
    public static double achaValorPrevistoAnterior(Date d,ArrayList<DadoColeta> previstos){
        for(DadoColeta aux:previstos){
            if(dataIgual(aux.getTime(),d)){
                return aux.getResponseTime();
            }
        }
        return -1;
    }
    
    public static boolean dataIgual(Date a,Date b){
        if(a==null||b==null){
            return false;
        }
        if(a.getYear()==b.getYear()){
            if(a.getMonth()==b.getMonth()){
                if(a.getDay()==b.getDay()){
                    if(a.getHours()==b.getHours()){
                        if(a.getMinutes()==b.getMinutes()){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
