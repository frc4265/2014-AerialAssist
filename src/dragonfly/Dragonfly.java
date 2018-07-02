/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package dragonfly;


import dragonfly.commands.AutoGroup;
import dragonfly.commands.TeleopGroup;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Watchdog;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Dragonfly extends IterativeRobot {

    CommandGroup auto;
    CommandGroup teleop;
    RobotMap robotmap;
    OI oi;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        
        robotmap = new RobotMap();
        oi = new OI();
        
        auto = new AutoGroup();
        teleop = new TeleopGroup();
        
        initSmartDashboard();
        RobotMap.compressor.start();
        Watchdog.getInstance().setExpiration(0.03);
        //new Thread(new CompressorControl()).start();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (teleop != null) teleop.cancel();
        if (auto != null) auto.start();
        SmartDashboard.putBoolean("Enable", true);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (auto != null) auto.cancel();
        if (teleop != null) teleop.start();
        RobotMap.pneumatics.chopsticksIn();
        SmartDashboard.putBoolean("Enabled", true);

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    public void disabledInit() {
        System.out.println("OVERLOAD ME!");
        SmartDashboard.putBoolean("Enabled", false);
    } 
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    public void initSmartDashboard(){
        SmartDashboard.putString("Chopsticks", "in");
        SmartDashboard.putNumber("Pulse", 0.135);
        SmartDashboard.putBoolean("Compressing", false);
        SmartDashboard.putBoolean("Enabled", false);
        SmartDashboard.putBoolean("One", false);
        SmartDashboard.putBoolean("One Kinect", false);
        SmartDashboard.putBoolean("One Hot One Not", false);
        SmartDashboard.putBoolean("Two Kinect", true);
        SmartDashboard.putNumber("Dropshot Timing", 0.25);
        SmartDashboard.putNumber("Auto Forward", 0.5);
        SmartDashboard.putNumber("Auto Wait", 1.5);
        SmartDashboard.putNumber("Close Pulse", 0.066);
        SmartDashboard.putNumber("Truss Pulse", 0.09);
        SmartDashboard.putNumber("Shift Threshold", 8);
    }
}
