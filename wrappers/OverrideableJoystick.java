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
    private Double[] axis = new Double[2];//Using Double not double so I can set it to null.


    public OverrideableJoystick(final int port){
        super(port);
    }
    
    public void setAxis(Double x,Double y) {
        this.axis[0] = x;
        this.axis[1] = y;
    }

    public void setX(Double x){
        setAxis(x, null);
    }

    public void setY(Double y){
        setAxis(null, y);
    }

    /**
    WHEN EVER YOU SET AXIS YOU MUST RUN THIS AFTER PLEASE
    */
    public void clearAixs(){
        setAxis(null, null);
    }

    

    public void press(int button){
        buttons[button] = true;
    } 
    public void hold(int button){
        held[button] = true;
        buttons[button] = true;
    }
    public void release(int button){
        held[button] = false;
        buttons[button] = false;
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
            return super.getRawButton(button);
        }
    }

    public int[] getButtonsPressed(){
        int[] temp = new int[buttons.length];
        int count = 0;
        for(int i = 0; i < buttons.length; i++){
            if(getRawButton(i)){
                temp[count++] = i;
            }
        }
        if(count == 0) return null;
        int[] returnArray = new int[count];
        for(int i = 0; i < count; i++){
            returnArray[i] = temp[i];
        }
        return returnArray;
    }

    @Override
    public double getRawAxis(int axisNum) {
        if(axisNum <= 1 && this.axis[axisNum] != null){
            return this.axis[axisNum];
        } else {
            return super.getRawAxis(axisNum);
        }    
    }
}

