package dragonfly;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //this is where driver console objects are stored
    public static Joystick drivepad;
    public static Joystick auxpad;

    public static Buttons buttons;
    public static Axis axis;
    
    public OI(){
        drivepad = new Joystick(2);
        auxpad = new Joystick(1);
        
        buttons = new Buttons();
        axis = new Axis();
        
    }
    
    public static class Buttons {
        
        //Driver Gamepad Buttons
        public static final int low_override = 6;
        public static final int mode_toggle = 10;
        public static final int gyroreset = 7;
        public static final int robotorient = 5;
        public static final int shift_toggle = 1;           // Temporary, just to make the program happy.
        public static final int positioned = 3;
        
        //Axiliary Gamepad Buttons
        public static final int chopsticks_extend = 5;
        public static final int chopsticksReverse = 4;
        public static final int chopsticksForward = 3;
        public static final int computershutoff = 8;
        public static final int computerturnon = 7;
    }
    
    public static class Axis {
        
        //Driver Gamepad Axis
        public static final int magnitude = 2;
        public static final int strafe = 1;
        public static final int rotation = 4;
        
        //Auxiliary Gamepad Axis
        public static final int fire = 3;
    }
    
}

