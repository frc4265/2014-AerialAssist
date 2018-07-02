/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dragonfly.subsystems;
import dragonfly.RobotMap;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author Matthew R.
 */
public class Manipulator extends Subsystem {
    
    Chopsticks direction;
    
    boolean running;
    
    Talon chopLeft;
    Talon chopRight;
    
    Relay position;
    
 
    
    public Manipulator(){
        
        direction = Chopsticks.Forward;
        running = false;
        
        chopLeft = RobotMap.leftChop;
        chopRight = RobotMap.rightChop;
        position = RobotMap.positionLights;
    }
    
    protected void initDefaultCommand() {
        
    }
    
    public void setPositionedLights(boolean state){
        if(state) position.set(Relay.Value.kOn);
        else position.set(Relay.Value.kOff);
    }
    
    public void setChopsticks(Chopsticks value){
        chopLeft.set(-value.value());
        chopRight.set(value.value());
    } 
    
    public static class Chopsticks {
        private int val;
        private double mult = 1; 
        private Chopsticks(int i){
            val = i;
        }
        private static final int iforward = 1;
        private static final int ireverse = -1;
        private static final int ioff = 0;
        public static final Chopsticks Forward = new Chopsticks(iforward);
        public static final Chopsticks Reverse = new Chopsticks(ireverse);
        public static final Chopsticks Off = new Chopsticks(ioff);
        public double value(){
            return val*mult;
        }
    }
    
}
