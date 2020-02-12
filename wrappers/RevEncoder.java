package frc.robot.core751.wrappers;

import com.revrobotics.CANEncoder;

public class RevEncoder {
    private CANEncoder encoder;

    public RevEncoder(CANEncoder encoder) {
        this.encoder = encoder;

        // based off this output shaft measurement: https://www.revrobotics.com/content/docs/REV-21-1650-DS.pdf
        // converts RPM to meters per second, and then rotations to meters
        this.encoder.setVelocityConversionFactor(3.01592894745);
        this.encoder.setPositionConversionFactor(0.05026548246);
    }

    public double getRate() {
        return encoder.getVelocity();
    }

    public double getDistance() {
        return encoder.getPosition();
    }

    public boolean getStopped() {
        return getRate() == 0;
    }

    public boolean getDirection() {
        return getRate() >= 0;
    }
}