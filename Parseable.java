/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.core751;

/**
 * An interface for values to be passed to the MacroMaker that aren't an int, double, or bool
 */

public interface Parseable<T> {
    public void parse (String string);//Update Values of Parseable Datatype
    public String toString();
}
