/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dragonfly.subsystems;

import dragonfly.RobotMap;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Kinect;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables2.type.NumberArray;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author tag
 */
public class Auto extends Subsystem {

    public boolean isDone = false;
    
    Chassis chassis;
    Pneumatics pneumatics;
    Manipulator manipulator;
    
    double turn_scalar = 0;//-37.750;
    
    public Auto(){
        chassis = RobotMap.chassis;
        pneumatics = RobotMap.pneumatics;
        manipulator = RobotMap.manipulator;
    } 
    public void one(){
        double num = SmartDashboard.getNumber("Auto Forward");
        for(int i = 0; i < num*100; i++){
            Timer.delay(0.01);
            chassis.tractiondrive.arcadeDrive(-1,(-RobotMap.gyro.getAngle()/18000)*turn_scalar);
        }
        chassis.tractiondrive.arcadeDrive(0, 0);
        Timer.delay(SmartDashboard.getNumber("Auto Wait"));
        pneumatics.chopsticksOut();
        Timer.delay(1.5);
        pneumatics.fire();
        Timer.delay(1);
        pneumatics.chopsticksIn();   
    }
    public void oneKinect(){
        double num = SmartDashboard.getNumber("Auto Forward");
        turn_scalar = SmartDashboard.getNumber("Close Pulse");
        for(int i = 0; i < num*100; i++){
            Timer.delay(0.01);
            //chassis.tractiondrive.arcadeDrive((-1+(i-50)/100),0);
            chassis.tractiondrive.arcadeDrive(-1+(i-50)/100,(-RobotMap.gyro.getAngle()/18000)*turn_scalar);
        }
        chassis.tractiondrive.arcadeDrive(0, 0);
        Timer.delay(SmartDashboard.getNumber("Auto Wait"));
        pneumatics.chopsticksOut();
        Timer.delay(1.5);
        while(RobotMap.kinect.getSkeleton().GetWristRight().getY() < 0.25 && DriverStation.getInstance().getMatchTime() < 7 ){
            Timer.delay(0.01);
        }
        pneumatics.fire();
        Timer.delay(1);
        pneumatics.chopsticksIn();        
    }
    public void oneHotOneNot(){
        RobotMap.gyro.reset();
        
        pneumatics.chopsticksOut();     //pick up second ball
        Timer.delay(0.5);
        manipulator.setChopsticks(Manipulator.Chopsticks.Forward);
        Timer.delay(0.2);
        manipulator.setChopsticks(Manipulator.Chopsticks.Off);
        pneumatics.chopsticksIn();
        
        double num = SmartDashboard.getNumber("Auto Forward");  //move forward
        for(int i = 0; i < num*100; i++){
            Timer.delay(0.01);
            chassis.tractiondrive.arcadeDrive(-1+(i-50)/100,(-RobotMap.gyro.getAngle()/18000)*turn_scalar);
        }
        chassis.tractiondrive.arcadeDrive(0, 0);
        
        pneumatics.chopsticksOut(); //extend choptsticks and fire
        Timer.delay(2);
        pneumatics.fire();
        
        Timer.delay(1);      //load second ball
        manipulator.setChopsticks(Manipulator.Chopsticks.Forward);
        Timer.delay(0.7);
        manipulator.setChopsticks(Manipulator.Chopsticks.Off);
        pneumatics.chopsticksIn();
        Timer.delay(0.18);
        pneumatics.chopsticksOut();     //extend chopsticks and fire
        Timer.delay(2.5);
        pneumatics.fire();
        
        Timer.delay(0.5);
        pneumatics.chopsticksIn();
    }
    public void twoKinect(){
        RobotMap.gyro.reset();
        
        pneumatics.chopsticksOut();     //pick up second ball
        Timer.delay(0.5);
        manipulator.setChopsticks(Manipulator.Chopsticks.Forward);
        Timer.delay(0.2);
        manipulator.setChopsticks(Manipulator.Chopsticks.Off);
        //pneumatics.chopsticksIn();
        
        double num = SmartDashboard.getNumber("Auto Forward");  //move forward
        for(int i = 0; i < num*100; i++){
            Timer.delay(0.01);
            chassis.tractiondrive.arcadeDrive(-1+(i-50)/100,(-RobotMap.gyro.getAngle()/18000)*turn_scalar);
        }
        chassis.tractiondrive.arcadeDrive(0, 0);
        
        //pneumatics.chopsticksOut(); //extend choptsticks and fire
        Timer.delay(2);
        while(RobotMap.kinect.getSkeleton().GetWristRight().getY() < 0.25 && DriverStation.getInstance().getMatchTime() < 5.5 );
        pneumatics.fire();
        
        Timer.delay(1);      //load second ball
        manipulator.setChopsticks(Manipulator.Chopsticks.Forward);
        Timer.delay(0.7);
        manipulator.setChopsticks(Manipulator.Chopsticks.Off);
        pneumatics.chopsticksIn();
        Timer.delay(0.18);
        pneumatics.chopsticksOut();     //extend chopsticks and fire
        Timer.delay(2.5);
        pneumatics.fire();
        
        Timer.delay(0.5);
        pneumatics.chopsticksIn();
    }
    protected void initDefaultCommand() {
        
    }
}
