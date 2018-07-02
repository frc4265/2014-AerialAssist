/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dragonfly;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author GROFFTOD000
 */
public class AnalogCompressor{
    DigitalInput di;
    Relay spike;
    PressureTransducer pressureTransducer;
    
    boolean enabled = false;
    
    double limitLow = 118;
    double limitHigh = 120;
    
    Thread thread;
    
    public AnalogCompressor(int spikeport, int digitalsensor, int ptport){
        di = new DigitalInput(digitalsensor);
        spike = new Relay(spikeport);
        spike.setDirection(Relay.Direction.kForward);
        pressureTransducer = new PressureTransducer(ptport);
        thread = new Thread(new Runnable(){
            public void run(){
                while (true){
                   if(enabled){
                        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, 1, (di.get()?"true":"false"));
                        double p = pressureTransducer.getPressure();
                        if( p < limitLow || !di.get()){
                            spike.set(Relay.Value.kOn);
                            SmartDashboard.putBoolean("Compressing", true);
                        }else if(p > limitHigh){
                            spike.set(Relay.Value.kOff);
                            SmartDashboard.putBoolean("Compressing", false);
                        }Timer.delay(0.01);
                   }else{
                       spike.set(Relay.Value.kOff);
                       SmartDashboard.putBoolean("Compressing", false);
                   }
                }
            }
        });
        thread.start();
    }
    public void setPressureBounds(double low, double high){     //default 118psi low 120psi high
        limitLow = low;
        limitHigh = high;
    }
    public void start(){
        enabled = true;
    }
    public void stop(){
        enabled = false;
    }
    public PressureTransducer getPressureTransducer(){
        return pressureTransducer;
    }
}
