/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dragonfly.commands;

import com.sun.squawk.util.MathUtils;
import dragonfly.OI;
import dragonfly.RobotMap;
import dragonfly.subsystems.Chassis;
import dragonfly.subsystems.Pneumatics;
import dragonfly.subsystems.Pneumatics.Mode;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author tag
 */
public class ChassisControl extends Command{
    
    Chassis sub;
    
    Joystick auxpad;
    Joystick drivepad;
    
    double threshold = 0.15;
    
    boolean gyroreset_prev = false;
    boolean gyroreset_curr = false;
    
    
    public ChassisControl() {
        requires(RobotMap.chassis);
        sub = RobotMap.chassis;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        auxpad = OI.auxpad;
        drivepad = OI.drivepad;
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double magnitude;
        double angle;
        double rotation;
        double mult;

        
        SmartDashboard.putNumber("RPM",sub.getEncoderAverage()*2);

        
        gyroreset_prev = gyroreset_curr;
        gyroreset_curr = drivepad.getRawButton(OI.Buttons.gyroreset);  //select button to reset gyro
        if (!gyroreset_prev && gyroreset_curr){
            sub.resetGyro();
        }
        if (RobotMap.mode == Mode.Traction){        //Drive code in traction mode
            magnitude = drivepad.getRawAxis(OI.Axis.magnitude);
            rotation = drivepad.getRawAxis(OI.Axis.rotation);
            mult = 1;
            sub.checkShift();
            if (Math.abs(magnitude) < threshold) magnitude = 0;

            sub.wildDrive(magnitude, rotation, mult);

        }else if (RobotMap.mode == Mode.Mecanum){   //Drive code in mecanum mode
        
            //Pythagorean theorem for polar magnitude
            magnitude = Math.sqrt(drivepad.getRawAxis(OI.Axis.strafe)*drivepad.getRawAxis(OI.Axis.strafe) + drivepad.getRawAxis(OI.Axis.magnitude)*drivepad.getRawAxis(OI.Axis.magnitude));
            
            //Polar angle correction
            angle = -(getAngle(drivepad.getRawAxis(OI.Axis.strafe), drivepad.getRawAxis(OI.Axis.magnitude))+180);
            
            rotation = drivepad.getRawAxis(OI.Axis.rotation);
            mult = 1;
            
            //threshold checking
            if (Math.abs(magnitude) < threshold) magnitude = 0;
            if (Math.abs(rotation) < threshold) rotation = 0;
            
            if (drivepad.getRawButton(5) || PneumaticsControl.inSmartMecanum){      //Robot Relative Override
                sub.mecanum_polar(magnitude, angle, rotation, mult);
            }else{
                sub.mecanum_cartesian(magnitude, angle, rotation, sub.getGyroAngle(), mult);
            }
        }
        
        if(drivepad.getRawButton(OI.Buttons.low_override)){     //Low gear manual override
            sub.checkShift(Pneumatics.Gear.Low);
        }else{
            sub.checkShift();
        }
        
        if(drivepad.getRawAxis(3) < -0.25){    //High gear manual override
            sub.checkShift(Pneumatics.Gear.High);
        }else{
            sub.checkShift();    
        }
        
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, 1, sub.getGyroAngle()+"");
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
    
    public double getAngle(double x, double y){ //given to rectilinear axis, compute polar angle
        double angle = 0;
        
        double absx = Math.abs(x);
        double absy = Math.abs(y);
        double theta = Math.toDegrees(MathUtils.atan(absy/absx));
        
        if(x>0 && y>0) angle = 90-theta;
        if(x>0 && y<0) angle = 90+theta;
        if(x<0 && y<0) angle = 270-theta;
        if(x<0 && y>0) angle = 270+theta;
        return angle;
    }
}
