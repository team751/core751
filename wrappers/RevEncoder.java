package frc.robot.core751.wrappers;

import com.revrobotics.CANEncoder;

import edu.wpi.first.wpilibj.Encoder;

public class RevEncoder {
    private CANEncoder encoder;

    public RevEncoder(CANEncoder encoder) {
        this.encoder = encoder;
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