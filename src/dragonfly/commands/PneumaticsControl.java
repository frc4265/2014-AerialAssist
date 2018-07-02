/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dragonfly.commands;

import dragonfly.*;
import dragonfly.subsystems.Pneumatics;
import dragonfly.subsystems.Pneumatics.Gear;
import dragonfly.subsystems.Pneumatics.Mode;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author tag
 */
public class PneumaticsControl extends Command {
    
    private Pneumatics sub;
    Joystick drivepad;
    Joystick auxpad;
    private boolean mode_prev = false;
    private boolean mode_curr = false;
    private boolean fire_prev = false;
    private boolean fire_curr = false;
    private boolean hp_prev = false;
    private boolean hp_curr = false;
    private boolean lob_prev = false;
    private boolean lob_curr = false;
    private boolean truss_prev = false;
    private boolean truss_curr = false;
    
    private boolean smart_mecanum_prev = false;
    private boolean smart_mecanum_curr = false;
    
    private static final double smart_mecanum_threshold = 0.75;
    private static final double smart_mecanum_delay = 0.35;
    
    public boolean countdown = false;
    public static boolean inSmartMecanum = false;
    
    public PneumaticsControl() {
        // Use requires() here to declare subsystem dependencies
        requires(RobotMap.pneumatics);
        sub = RobotMap.pneumatics;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        drivepad = OI.drivepad;
        auxpad = OI.auxpad;
        sub.startCompressor();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {        
        double pHigh = sub.getHighSidePressure();
        double pLow = sub.getLowSidePressure();
        
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "High Side: "+pHigh+"psi");
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "Low Side: "+pLow+"psi");
        
        SmartDashboard.putNumber("High Side Pressure", pHigh);
        SmartDashboard.putNumber("Low Side Pressure", pLow);
        DriverStationLCD.getInstance().updateLCD();
        
        //update edge detectors
        mode_prev = mode_curr;
        mode_curr = drivepad.getRawButton(OI.Buttons.mode_toggle);
 
        smart_mecanum_prev = smart_mecanum_curr;
        //smart_mecanum_curr = Math.abs(drivepad.getRawAxis(OI.Axis.strafe)) > smart_mecanum_threshold;
        
        fire_prev = fire_curr;
        fire_curr = auxpad.getRawAxis(OI.Axis.fire) < -0.25;    //trigger fire threshold
        
        hp_prev = hp_curr;
        hp_curr = auxpad.getRawButton(2);
        
        lob_prev = lob_curr;
        //lob_curr = auxpad.getRawButton(6);
        
        truss_prev = truss_curr;
        truss_curr = auxpad.getRawButton(6);
        
        if (!fire_prev && fire_curr ){
            sub.fire();            
        }
        
        if (!hp_prev && hp_curr){
            sub.hptruss();
        }
        
        if (!lob_prev && lob_curr){
            sub.lob();
        }
        
        if(!truss_prev && truss_curr){
            sub.truss();
        }
        
        if(auxpad.getRawButton(OI.Buttons.chopsticks_extend)){
            sub.chopsticksOut();
        }else{
            sub.chopsticksIn();
        }
        if(sub.getMode() == Mode.Traction){
            if(!smart_mecanum_prev && smart_mecanum_curr){      //detect rising edge
                sub.setMode(Mode.Mecanum);
                countdown = false;
                inSmartMecanum = true;
            }
        }
        if(sub.getMode() == Mode.Mecanum && inSmartMecanum && smart_mecanum_curr){
            countdown = false;
        }
        if(smart_mecanum_prev && !smart_mecanum_curr && inSmartMecanum){      //detect falling edge
            countdown = true;
            Thread tCountdown = new Thread(new Runnable(){
               public void run(){
                   double tStart = Timer.getFPGATimestamp();
                   while(countdown){
                       if((Timer.getFPGATimestamp()-tStart) > smart_mecanum_delay){ //good ole system time timer
                           sub.setMode(Mode.Traction);
                           countdown = false;
                           inSmartMecanum = false;
                       }
                   }
               } 
            });
            tCountdown.start();
        }
        
        if(!mode_prev && mode_curr){        //Module Toggle
            if (RobotMap.mode == Mode.Traction) sub.setMode(Mode.Mecanum);
            else if (RobotMap.mode == Mode.Mecanum) sub.setMode(Mode.Traction);
        }
        
        
        double speedAverage = ((Double) RobotMap.robotTable.get("Speed Average")).doubleValue();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
