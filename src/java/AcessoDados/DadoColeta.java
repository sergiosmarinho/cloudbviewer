/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AcessoDados;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author sergio
 */
public class DadoColeta {
    private Date time;
    private double responseTime;

    public DadoColeta(Date time, double responseTime) {
        this.time=time;
        this.responseTime = responseTime;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

    public Date getTime() {
        return time;
    }
    
    public void setTime(Date time) {
        //SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH:mm");
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(time);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        this.time = cal.getTime();
        //this.time = df.format(time);
    }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH:mm");
        return "date:"+df.format(time)+" response_time:"+responseTime;
    }
}
