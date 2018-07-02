/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dragonfly.commands;
import dragonfly.RobotMap;
import dragonfly.subsystems.Auto;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 *
 * @author GROFFTOD000
 */
public class AutoControl extends Command {
    
    private Auto sub;
    
    public AutoControl() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        sub = RobotMap.auto;
        requires(sub);
        RobotMap.gyro.reset();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(SmartDashboard.getBoolean("One")) sub.one();
        if(SmartDashboard.getBoolean("One Kinect")) sub.oneKinect();
        if(SmartDashboard.getBoolean("One Hot One Not")) sub.oneHotOneNot();
        if(SmartDashboard.getBoolean("Two Kinect")) sub.twoKinect();
        sub.isDone = true;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        System.out.println("Finializing execution...");
        return sub.isDone;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}