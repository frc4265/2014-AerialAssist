/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dragonfly.subsystems;

import dragonfly.AnalogCompressor;
import dragonfly.PressureTransducer;
import dragonfly.RobotMap;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author tag
 */
public class Pneumatics extends Subsystem {
    
    AnalogCompressor compressor = RobotMap.compressor;
    public DoubleSolenoid modules = RobotMap.modules;
    DoubleSolenoid shifters = RobotMap.shifters;
    DoubleSolenoid chopsticks = RobotMap.chop;
    Solenoid launcher1 = RobotMap.launcher1;
    Solenoid launcher2 = RobotMap.launcher2;
    public Mode mode = Mode.Traction;
    public Gear gear = Gear.Low;
    PressureTransducer lowSide;
    
    public Pneumatics(){
        lowSide = new PressureTransducer(4);
        compressor.getPressureTransducer().setPressureRange(0, 120);
        compressor.getPressureTransducer().setVoltageRange(0.5, 3.4);
        lowSide.setPressureRange(0, 60);
        lowSide.setVoltageRange(0.48, 2.17);
    }
    
    public void initDefaultCommand() {
    }
    public void startCompressor(){
        compressor.start();
    }
    public void stopCompressor(){
        compressor.stop();
    }
    
    public double getHighSidePressure(){
        double voltage = compressor.getPressureTransducer().getVoltage();
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, voltage+"");
        return compressor.getPressureTransducer().getPressure();
        
    }
    public double getLowSidePressure(){
        return lowSide.getPressure();
    }
    
    public void setMode(Mode mode){
        
        if (mode == Mode.Mecanum){
            modules.set(DoubleSolenoid.Value.kForward);
        }
        if (mode == Mode.Traction){
            modules.set(DoubleSolenoid.Value.kReverse);
        }
        this.mode = mode;
        RobotMap.mode = mode;
        
    }
    
    public void shift(Gear gear){
        if (gear == Gear.High) shifters.set(DoubleSolenoid.Value.kForward);
        if (gear == Gear.Low) shifters.set(DoubleSolenoid.Value.kReverse);
        this.gear = gear;
    }
    
    //Chopstick Acuation Code -M.R.
    public void chopsticksIn(){
        chopsticks.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void chopsticksOut(){
        chopsticks.set(DoubleSolenoid.Value.kForward);
    }
    
    public void fire(){
        if(RobotMap.ballSensor.get()){
            new Thread(){
                public void run(){
                    RobotMap.pneumatics.launcher1.set(true);
                    RobotMap.pneumatics.launcher2.set(true);
                    Timer.delay(SmartDashboard.getNumber("Pulse"));
                    RobotMap.pneumatics.launcher1.set(false);
                    RobotMap.pneumatics.launcher2.set(false);
                }
            }.start();
        }
    }
    
    public void hptruss(){
        if(RobotMap.ballSensor.get()){
            new Thread(){
                public void run(){
                    RobotMap.pneumatics.launcher1.set(true);
                    RobotMap.pneumatics.launcher2.set(true);
                    Timer.delay(fTruss(lowSide.getPressure())+0.02);
                    RobotMap.pneumatics.launcher1.set(false);
                    RobotMap.pneumatics.launcher2.set(false);
                }
            }.start();
        }
    }
    public void lob(){
        if(RobotMap.ballSensor.get()){
            new Thread(){
                public void run(){
                    RobotMap.pneumatics.launcher1.set(true);
                    RobotMap.pneumatics.launcher2.set(true);
                    //Timer.delay(SmartDashboard.getNumber("Close Pulse"));
                    Timer.delay(fLow(lowSide.getPressure()));
                    RobotMap.pneumatics.launcher1.set(false);
                    RobotMap.pneumatics.launcher2.set(false);
                }
            }.start();
        }
    }
    public void truss(){
        if(RobotMap.ballSensor.get()){
            new Thread(){
                public void run(){
                    RobotMap.pneumatics.launcher1.set(true);
                    RobotMap.pneumatics.launcher2.set(true);
                    //Timer.delay(SmartDashboard.getNumber("Truss Pulse"));
                    Timer.delay(fTruss(lowSide.getPressure()));
                    RobotMap.pneumatics.launcher1.set(false);
                    RobotMap.pneumatics.launcher2.set(false);
                }
            }.start();
        }
    }
    
    public Mode getMode(){
        return mode;
    }
    public Gear getGear(){
        return gear;
    }
    
    public static class Gear {      //DIY enums because Java ME doesn't have enums
        int value;
        public Gear(int id){
            value = id;
        }
        private static int iHigh = 1;
        private static int iLow = 0;
        private static int iNone = 2;
        public static Gear High = new Gear(iHigh);
        public static Gear Low = new Gear(iLow);
        public static Gear None = new Gear(iNone);
    }
    
    public static class Mode {
        int value;
        public Mode(int id){
            value = id;
        }
        private static int iMecanum = 1;
        private static int iTraction = 0;
        public static Mode Mecanum = new Mode(iMecanum);
        public static Mode Traction = new Mode(iTraction);
    }
    public static double fLow(double x){
        double a = 0.00000103941;
        double b = -0.000232921;
        double c = 0.0195589;
        double d = -0.73178;
        double e = 10.385;
        return a*(x*x*x*x) + b*(x*x*x) + c*(x*x) + d*x + e;
    }
    public static double fTruss(double x){
        double a = 0;
        double b = 0;
        double c = .000178248;
        double d = -.0213299;
        double e = .719513;
        double pulse = a*(x*x*x*x)+b*(x*x*x)+c*(x*x)+d*x+e;
        System.out.println(pulse);
        return pulse;
    }
    
}
