package frc.robot.core751.commands.Drivetrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.SmartDifferentialDriveTrain;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SpeedCapAdjuster extends CommandBase {

    private Type type;
    private boolean state = false; //Only used with switch
    private SmartDifferentialDriveTrain differentialDriveTrain;
    private double amount;

    public static enum Type{//How it alters the Speed Cap
        Switch,
        Button
    }

    public SpeedCapAdjuster(SmartDifferentialDriveTrain differentialDriveTrain,int amount){
        this(differentialDriveTrain,Type.Button,amount);
    }

    public SpeedCapAdjuster(SmartDifferentialDriveTrain differentialDriveTrain,Type type,int amount) {
        this.differentialDriveTrain = differentialDriveTrain;
        this.type = type;
        this.amount = amount;
        addRequirements(differentialDriveTrain);
    }

    @Override
    public void execute() {
        if(type == Type.Switch){
            if(!state){
                differentialDriveTrain.setSpeedCap(amount);
                state = true;
            } else {
                differentialDriveTrain.setSpeedCap(SmartDashboard.getNumber("Speed Cap Max", 0.9));
                state = false;
            }
        } else if(type == Type.Button){
            differentialDriveTrain.setSpeedCap(amount);
        }
    }

    @Override
    public void end(boolean interrupted) {
        if(type == Type.Button){
            differentialDriveTrain.setSpeedCap(SmartDashboard.getNumber("Speed Cap Max", 0.9));
        }
    }

}