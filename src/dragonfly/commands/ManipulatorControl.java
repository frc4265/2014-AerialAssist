/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dragonfly.commands;

import dragonfly.Dragonfly;
import dragonfly.OI;
import dragonfly.RobotMap;
import dragonfly.subsystems.Manipulator;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author gfwilliams
 */
public class ManipulatorControl extends Command {
    
    Manipulator sub;
    
    Joystick drivepad = OI.drivepad;
    Joystick auxpad = OI.auxpad;
    
    private boolean chopsticks_toggle = false;
    
    //edge detector bools
    
    public ManipulatorControl() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        sub = RobotMap.manipulator;
        requires(sub);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
        if(auxpad.getRawButton(OI.Buttons.chopsticksForward)){  //chopsticks forward checking
            sub.setChopsticks(Manipulator.Chopsticks.Forward);
        }else{
            sub.setChopsticks(Manipulator.Chopsticks.Off);
        }
        
        if(auxpad.getRawButton(OI.Buttons.chopsticksReverse)){  //chopsticks reverse checking
            sub.setChopsticks(Manipulator.Chopsticks.Reverse);
        }
        if(drivepad.getRawButton(OI.Buttons.positioned)){
            sub.setPositionedLights(true);
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, "on   ");
        }else{
            sub.setPositionedLights(false);
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, "off  ");
        }
        DriverStationLCD.getInstance().updateLCD();
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
