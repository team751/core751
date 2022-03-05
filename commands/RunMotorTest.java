package frc.robot.core751.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.CoreConstants;
import frc.robot.core751.subsystems.MotorTest;

public class RunMotorTest extends CommandBase{
    
    public MotorTest mTest;
    public double speed = 0;

    public RunMotorTest(MotorTest mTest){
        this.mTest = mTest;
    }

    @Override
    public void initialize() {
        mTest.set(0);
        if(CoreConstants.driverStick.getRawButtonReleased(1)){
            speed += 0.2;
        }
        if(CoreConstants.driverStick.getRawButtonReleased(2)){
            speed = 0;
        }
    }
}
