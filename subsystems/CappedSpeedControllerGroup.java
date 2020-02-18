/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.core751.subsystems;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.SpeedController;
import frc.robot.Constants;

public class CappedSpeedControllerGroup extends SpeedControllerGroup {

    private double speedCap;

    public CappedSpeedControllerGroup(SpeedController speedController, double speedCap, SpeedController... speedControllers) {
        super(speedController, speedControllers);
        this.speedCap = speedCap;
    }

    @Override
    public void set(double speed) {
        if (speed < 0) {
            speed = Math.max(speed, -speedCap);
        } else if (speed > 0) {
            speed = Math.min(speed, speedCap);
        }
        super.set(speed);
    }

    public double getSpeedCap() {
        return speedCap;
    }

    public void setSpeedCap(double speedCap) {
        this.speedCap = speedCap;
    }
}
