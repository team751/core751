package frc.robot.core751.commands.camera.LimeLight;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.camera.LimeLight;
import frc.robot.core751.wrappers.OverrideableJoystick;

public class SwitchCameraMode extends CommandBase {
    
    LimeLight limeLight;
    Joystick joystick;
    int switchButton;

    public SwitchCameraMode(LimeLight limeLight,Joystick joystick,int switchButton){
        this.joystick = joystick;
        this.switchButton = switchButton;
        this.limeLight = limeLight;
        addRequirements(limeLight);
    }

    @Override
    public void execute() {
        if(joystick.getRawButtonReleased(switchButton)){
            limeLight.switchCamMode();
        }
    }
}
