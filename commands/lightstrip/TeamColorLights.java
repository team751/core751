package frc.robot.core751.commands.lightstrip;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.LightStrip;
import frc.robot.core751.subsystems.LightStrip.PostProccessingEffects;

public class TeamColorLights extends CommandBase{

    private LightStrip[] lightStrips;
    private Alliance alliance;
    private int[] allianceColor;

    public TeamColorLights(LightStrip[] lightStrips) {
        this.lightStrips = lightStrips;

        for (LightStrip l : lightStrips) {
            addRequirements(l);
        }
    }

    @Override
    public void initialize() {
        this.alliance = DriverStation.getInstance().getAlliance();
        switch (alliance) {
            case Red:
                this.allianceColor = new int[]{0, 255, 255};
            break;
            case Blue:
                this.allianceColor = new int[]{100, 255, 255};
            break;
            default:
                this.allianceColor = new int[]{300, 255, 255};
            break;
        }

        for (LightStrip l : lightStrips) {
            l.fillHSV(this.allianceColor[0], this.allianceColor[1], this.allianceColor[2]);
            l.clearEffects();
            l.setEffect(PostProccessingEffects.WAVE);
            l.update();
        }
        
    }

    @Override
    public void execute() {
        for (LightStrip l : lightStrips) {
            l.update();
        }
    }
}