package frc.robot.core751.wrappers;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.core751.CoreConstants;

public class AnalogDistanceSensor extends AnalogInput{ //Got deleted by git somehow, ill remake it when neccisary.
    
    public AnalogDistanceSensor(int channel) {
        super(channel);
    }

    public enum SensorType{

    }

    public double getDistance(){
        return 0;
    }

    public boolean hasTraget(){
        //return false;
        return CoreConstants.driverStick.getRawButton(4);
    }
}
