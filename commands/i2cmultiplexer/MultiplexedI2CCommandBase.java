package frc.robot.core751.commands.i2cmultiplexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.subsystems.I2CMultiplexer;

public abstract class MultiplexedI2CCommandBase extends CommandBase {
    private I2CMultiplexer multiplexer;
    private int multiplexerI2CDeviceId;

    protected MultiplexedI2CCommandBase(I2CMultiplexer multiplexer,
                                        int multiplexerI2CDeviceId) {
        this.multiplexer = multiplexer;
        this.multiplexerI2CDeviceId = multiplexerI2CDeviceId;

        switchDeviceId();

        addRequirements(multiplexer);
    }

    private void switchDeviceId() {
        if(multiplexer.getLastSetDevice() != multiplexerI2CDeviceId) {
            multiplexer.switchI2CDevice(multiplexerI2CDeviceId);
            System.out.println("Switched to " + multiplexerI2CDeviceId);
        }
    }

    @Override
    public void execute() {
        switchDeviceId();
    }
}