/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.core751.wrappers;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Add your docs here.
 */
public class OverrideableJoystick extends Joystick{

    public boolean[] buttons = new boolean[super.getButtonCount() + 1];//The index's start at 1
    public boolean[] held = new boolean[super.getButtonCount() + 1];

    public OverrideableJoystick(final int port){
        super(port);
    }

    public void press(int button){
        buttons[button] = true;
    } 
    public void hold(int button){
        held[button] = true;
    }
    public void release(int button){
        held[button] = false;
    }


    @Override
    public boolean getRawButtonPressed(int button) {
        if(buttons[button]){
            buttons[button] = held[button];
            return true;
        } else {
            return super.getRawButtonPressed(button);
        }
    }

    @Override
    public boolean getRawButton(int button) {
        if(held[button]){
            return true;
        } else {
            return super.getRawButtonPressed(button);
        }
    }




}
