package dragonfly;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */

import dragonfly.subsystems.*;
import dragonfly.subsystems.Pneumatics.Mode;
import edu.wpi.first.wpilibj.*;
//import com.sun.squawk.util.ArrayHashtable;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.util.Hashtable;

public class RobotMap {
    
    public static final String macaddress = "ECA86BF3A9A5";
    
    //declare static subsystem objects here
    public static Pneumatics pneumatics;
    public static Chassis chassis;
    public static Manipulator manipulator;
    public static Auto auto;
    
    //public static ArrayHashtable robotTable;
    
    //declare static robot objects here
    
    //motor controllers
    public static Talon LF;
    public static Talon RF;
    public static Talon LB;
    public static Talon RB;
    public static Talon rightChop;
    public static Talon leftChop;
    
    //pneumatics
    public static AnalogCompressor compressor;
    public static DoubleSolenoid modules;
    public static DoubleSolenoid shifters;
    public static DoubleSolenoid chop;
    public static Solenoid launcher1;
    public static Solenoid launcher2;

    
    //sensors
    public static Encoder encLF;
    public static Encoder encRF;
    public static Encoder encLB;
    public static Encoder encRB;
    
    public static Relay positionLights;
    
    public static Gyro gyro;
    
    public static DigitalInput ballSensor;
    
    public static Mode mode = Mode.Traction;
    
    public static Hashtable robotTable;
    
    public static NetworkTable networkTable;
    
    public static Kinect kinect;
    
    public RobotMap(){
        
        //robotTable = new ArrayHashtable();
        
        //initialize static objects here
        
        //motor controllers
        LF = new Talon(1);                          //PWM port
        RF = new Talon(2);                          //NOTE: rules say that we must use one motor controller
        LB = new Talon(3);                          //      per motor (8), but we are using Y cables so we 
        RB = new Talon(4);                          //      can represent each ballshifter pair as one talon 
                                                    //      in the code.
        leftChop = new Talon(5);
        rightChop = new Talon(6);   
        
        //pneumatics
        compressor = new AnalogCompressor(1, 1, 2);          //pressure switch (I/O), spike (relay)
        modules = new DoubleSolenoid(3, 4);
        shifters = new DoubleSolenoid(2, 1);
        chop = new DoubleSolenoid(5, 6);
        launcher1 = new Solenoid(7);
        launcher2 = new Solenoid(8);
        
        //sensors
        encLF = new Encoder(1,7,1,8, true, EncodingType.k1X);                  //port A (I/O), port B (I/O)
        encLF.setDistancePerPulse(1.0/360/3);
        encLF.setSamplesToAverage(100);
        encRF = new Encoder(1,9,1,10, false, EncodingType.k1X);
        encRF.setDistancePerPulse(1.0/360/3);
        encRF.setSamplesToAverage(100);
        encLB = new Encoder(1,11,1,12, true, EncodingType.k1X);
        encLB.setDistancePerPulse(1.0/360/3);
        encLB.setSamplesToAverage(100);
        encRB = new Encoder(1,13,1,14, false, EncodingType.k1X);
        encRB.setDistancePerPulse(1.0/360/3);               
        encRB.setSamplesToAverage(100);
        
        gyro = new Gyro(1);                         //Analog Module port
        gyro.setSensitivity(0.007);
        
        ballSensor = new DigitalInput(2);
        
        positionLights = new Relay(1,2);
        positionLights.setDirection(Relay.Direction.kForward);
        
        kinect = Kinect.getInstance();
        
        initSubsystems();
        
        robotTable = new Hashtable();
        initTable();
        
        networkTable = NetworkTable.getTable("SmartDashboard");
                
    }
    
    private void initSubsystems(){
        //subsystems
        pneumatics = new Pneumatics();
        chassis = new Chassis();
        manipulator = new Manipulator();
        auto = new Auto();
    }
    
    public static Mode getMode(){
        return mode;
    }
    private void initTable(){
        robotTable.put("Speed Average", new Double(0.0));
    }
    
}
