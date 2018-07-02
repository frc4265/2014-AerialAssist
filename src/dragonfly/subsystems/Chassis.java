/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dragonfly.subsystems;

import dragonfly.RobotMap;
import dragonfly.subsystems.Pneumatics.Gear;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author tag
 */
public class Chassis extends Subsystem implements PIDSource, PIDOutput{
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    Talon LF;
    Talon RF; 
    Talon LB;
    Talon RB;
    
    Encoder encLF;
    Encoder encRF;
    Encoder encLB;
    Encoder encRB;
    
    Gyro gyro;
    
    double threshold = 0.05;
    
    RobotDrive tractiondrive;
    RobotDrive mecanumdrive;
    
    //PID stuff
    PIDController angleController;
    private double kP = 0.1;
    private double kI = 0;
    private double kD = 0.05;
    
    public Chassis(){
        LF = RobotMap.LF;
        RF = RobotMap.RF;
        LB = RobotMap.LB;
        RB = RobotMap.RB;
        
        encLF = RobotMap.encLF;
        encRF = RobotMap.encRF;
        encLB = RobotMap.encLB;
        encRB = RobotMap.encRB;
        
        encLF.start();
        encRF.start();
        encLB.start();
        encRB.start();
        
        angleController = new PIDController(kP, kI, kD, this, this);
        angleController.setInputRange(-180, 180);
        angleController.setOutputRange(-1, 1);
        angleController.setPercentTolerance(0.01);
        SmartDashboard.putData("angle PID", angleController);
        
        gyro = RobotMap.gyro;
        
        tractiondrive = new RobotDrive(LF, LB, RF, RB);
        tractiondrive.setExpiration(0.03);
        mecanumdrive = new RobotDrive(LF, LB, RF, RB);
        mecanumdrive.setExpiration(0.03);
        tractiondrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, false);
        tractiondrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, false);
        tractiondrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
        tractiondrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
        mecanumdrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        mecanumdrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void mecanum_polar(double magnitude, double angle, double rotation, double mult){
        mecanumdrive.mecanumDrive_Polar((Math.abs(magnitude*mult) < threshold ? 0 : magnitude*mult), angle, -rotation);
    }
    public void mecanum_cartesian(double magnitude, double angle, double rotation, double gyro, double mult){
        mecanumdrive.mecanumDrive_Polar(magnitude, angle-gyro, -rotation);
    }
    public void wildDrive(double magnitude, double rotation, double mult){
        tractiondrive.arcadeDrive((Math.abs(magnitude*mult) < threshold ? 0 : magnitude*mult), -rotation, true);
    }
    
    public double pidGet(){
        double a = getGyroAngle();
        return (a  > 180?a-360:a); 
    }
    public void pidWrite(double output){
        output = (Math.abs(output) < 0.1?0:output);
        tractiondrive.tankDrive(output, -output);
        SmartDashboard.putNumber("PID Error", angleController.getError());
        SmartDashboard.putNumber("PID output", output);
    }
    public void enablePID(){
        angleController.enable();
    }
    public void disablePID(){
        angleController.disable();
    }
    public void setAngle(double angle){
        angleController.setSetpoint(angle);
    }
    /*public void dummyDrive(double x, double y){
        tractiondrive.arcadeDrive(x, y);
    }*/
    public double getGyroAngle(){
        double angle = gyro.getAngle();
        if (Math.abs(angle) >= 360 ) angle = angle%360;
        return angle;
    }
    public void resetGyro(){
        gyro.reset();
    }
    public double getEncoderAverage(){
        double average =   (Math.abs(encLF.getRate()) + 
                           Math.abs(encRF.getRate()) +
                           Math.abs(encLB.getRate()) +
                           Math.abs(encRB.getRate()) )/2;
        SmartDashboard.putNumber("FL", encLF.getRate());
        SmartDashboard.putNumber("FR", encRF.getRate());
        SmartDashboard.putNumber("BL", encLB.getRate());
        SmartDashboard.putNumber("BR", encRB.getRate());
        return average;
    }
    public void checkShift(){
        Gear gear;
        if(getEncoderAverage() > SmartDashboard.getNumber("Shift Threshold")){
            gear = Gear.High;
        }else if(getEncoderAverage() < 5){
            gear = Gear.Low;
        }else{
            return;
        }
        RobotMap.pneumatics.shift(gear);
    }
    public void checkShift(Gear gear){
        RobotMap.pneumatics.shift(gear);
    }
}
