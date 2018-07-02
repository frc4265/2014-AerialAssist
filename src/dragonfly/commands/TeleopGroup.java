/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dragonfly.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author tag
 */
public class TeleopGroup extends CommandGroup {
    
    public TeleopGroup() {
        addParallel(new PneumaticsControl());
        addParallel(new ChassisControl());
        addParallel(new ManipulatorControl());
    }
}
