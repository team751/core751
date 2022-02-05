package frc.robot.core751.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.MotorTest;

public class RunMotorTest extends CommandBase{
    
    public MotorTest mTest;

    public RunMotorTest(MotorTest mTest){
        this.mTest = mTest;
    }

    @Override
    public void initialize() {
        mTest.set(1);
    }
}
