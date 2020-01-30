package frc.robot.core751.wrappers;

import com.revrobotics.CANEncoder;

import edu.wpi.first.wpilibj.Encoder;

public class RevEncoder extends Encoder {
    private CANEncoder encoder;

    public RevEncoder(CANEncoder encoder) {
        super(0, 0);

        this.encoder = encoder;
    }

    @Override
    public double getRate() {
        return encoder.getVelocity();
    }

    @Override
    public double getDistance() {
        return encoder.getPosition();
    }

    @Override
    public boolean getStopped() {
        return getRate() == 0;
    }

    @Override
    public boolean getDirection() {
        return getRate() >= 0;
    }
}