/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dragonfly;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author GROFFTOD000
 */
public class CompressorControl implements Runnable{
    DigitalInput compressorswitch = new DigitalInput(2);
    public void run(){
        while(true){
            if(!SmartDashboard.getBoolean("Enabled")){
                //System.out.println(compressorswitch.get());
                if(compressorswitch.get()){
                    RobotMap.compressor.start();
                }else{
                    RobotMap.compressor.start();
                }
            }
        }
    }
}
