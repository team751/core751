package frc.robot.core751.commands.lightgrid;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.LightGrid;
import frc.robot.core751.subsystems.LightStrip;

public class TeamColorGrid extends CommandBase{

    private LightGrid lightGrid;
    private Alliance alliance;
    private int[] allianceColor;

    public TeamColorGrid(LightGrid lightGrid) {
        this.lightGrid = lightGrid;

        addRequirements(lightGrid);

    }

    @Override
    public void initialize() {
        this.alliance = DriverStation.getInstance().getAlliance();
        switch (alliance) {
            case Red:
                this.allianceColor = new int[]{0, 255, 255};
            break;
            case Blue:
                this.allianceColor = new int[]{240, 255, 255};
            break;
            default:
                this.allianceColor = new int[]{300, 255, 255};
            break;
        }
        this.lightGrid.addEffect(LightGrid.PostProccessingEffects.WAVE);
        this.lightGrid.fillHSV(this.allianceColor[0], this.allianceColor[1], this.allianceColor[2]);
    }

    @Override
    public void execute() {
        this.lightGrid.update();
    }

    
}