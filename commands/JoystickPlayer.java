/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.core751.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.wrappers.OverrideableJoystick;


public class JoystickPlayer extends CommandBase {
    private Double array[][];
    private OverrideableJoystick joystick;
    private int rover = 0;


    public JoystickPlayer(Double array[][], OverrideableJoystick joystick){
        this.array = array;
        this.joystick = joystick;
    }
    @Override
    public void execute() {
        if(rover < array.length){
            for(int i = 0; i < array[rover].length; i++){
                switch(i){
                    case 0:
                        joystick.setX(array[rover][0]);
                        break;
                    case 1:
                        joystick.setY(array[rover][1]);
                        break;
                    default:
                        joystick.press((int)(double)(array[rover][i])); // :)
                        break;
                }
            }
            rover++;
        }
    }
    @Override
    public void end(boolean interrupted) {
       joystick.clearAixs();
    }
}
