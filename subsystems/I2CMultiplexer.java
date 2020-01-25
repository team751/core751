package frc.robot.core751.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.I2C;

public class I2CMultiplexer extends SubsystemBase {
    private final I2C i2c;
    
    private int lastSetDevice = -1;

    public I2CMultiplexer(I2C.Port port) {
        i2c = new I2C(port, 0x70);
    }

    public void switchI2CDevice(int toDeviceNum) {
        lastSetDevice = toDeviceNum;

        i2c.writeBulk(new byte[]{ (byte)(1 << toDeviceNum) });
    }

    public int getLastSetDevice() {
        return lastSetDevice;
    }
}
