/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.core751;

/**
 * Controlable Interface for MacroMaker
 */
public interface Controllable<T> {
    public static enum type{
        Int,
        Double,
        Bool,
        Parseable
    }
    public default void execute(){};
    public default void execute(T n){execute();};
    public default void execute(T n ,int code){if(code == 0){execute(n);}else{execute();}};
   
    public String name();
    public type parseType();//Is their a way to make this automatic?

    /* Example

    @Override
    public void execute(Integer n, int code) {
        switch(code){
            case 0:
                Intake(n);
                break;
            case 1:
                setOutputMotor(n);
                break;
            case 2:
                setIntakeMotor(n);;
                break;
            case 3:
                setPolycordMotor(n);
                break;
       }
    }
    @Override
    public String name() {
        return "Ball";
    }
    @Override
    public type parseType() {
        return Controllable.type.Int;
    }
    */
}
