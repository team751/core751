package frc.robot.core751.wrappers;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.RobotController;

public class wAnalogPotentiometer extends AnalogPotentiometer {

    private double maxTurns;
    private double offSet;

    public wAnalogPotentiometer(int channel,double maxTurns,double offSet) {
        super(channel);
        this.maxTurns = maxTurns;
        this.offSet = offSet;
    }

    public double getAngle(){
        double adjustmentFactor = (360 * maxTurns) / RobotController.getVoltage5V();
        return (get() * adjustmentFactor) + offSet;
    }


    public double getOffSet() {
        return offSet;
    }

    
    
}
