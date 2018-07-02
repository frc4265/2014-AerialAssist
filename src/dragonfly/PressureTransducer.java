/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dragonfly;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 *
 * @author GROFFTOD000
 */
public class PressureTransducer extends AnalogChannel{
    private double vLow = 0.0;
    private double vHigh = 5.0;
    private double dV = vHigh-vLow;
    private double pLow = 0.0;
    private double pHigh = 150.0;
    private double dP = pHigh-pLow;
    private BufferedAverager averager = new BufferedAverager(75);
    
    public PressureTransducer(int port){
        super(port);
    }
    public PressureTransducer(int slot, int port){
        super(slot, port);
        this.setAverageBits(50);
    }
    public double getPressure(){
        double v = this.getVoltage();
        double scalar = (v-vLow)/dV;
        double pressure = scalar*dP+pLow;
        averager.addSample(pressure);
        return averager.getAverage();
    }
    public void setVoltageRange(double low, double high){
        vLow = low;
        vHigh = high;
        dV = vHigh-vLow;
    }
    public void setPressureRange(double low, double high){
        pLow = low;
        pHigh = high;
        dP = pHigh-pLow;
    }
}
