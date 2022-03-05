package frc.robot.core751.auton;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class MillisecondWaitCommand extends WaitCommand{
    
    double timeMilliseconds;
    Timer timer; 

    public MillisecondWaitCommand(int timeMilliseconds){
        super(timeMilliseconds / 1000);
    }


}
