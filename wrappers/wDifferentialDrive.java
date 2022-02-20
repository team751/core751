package frc.robot.core751.wrappers;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public interface wDifferentialDrive {
    public enum DriveTrainDirection {
        FORWARD(1),
        BACKWARD(-1);

        private int mod;
        public int getMod(){return this.mod;}
        private DriveTrainDirection(int mod) {
            this.mod = mod;
        }

    }

    public DriveTrainDirection getDirection();
    public void setDirection(DriveTrainDirection direction);

    public DifferentialDrive getDifferentialDrive();

    public static class SmartControllerProfile {
        public IdleMode idle;
        public double rate;
        public int limit;

        public SmartControllerProfile(IdleMode idle, double rate, int limit) {
            this.idle = idle;
            this.rate = rate;
            this.limit = limit;
        }

        public SmartControllerProfile(IdleMode idle, double rate) {
            this.idle = idle;
            this.rate = rate;
        }

        public SmartControllerProfile(IdleMode idle) {
            this.idle = idle;
        }

        public SmartControllerProfile(double rate, int limit) {
            this.rate = rate;
            this.limit = limit;
        }

        public SmartControllerProfile(int limit) {
            this.limit = limit;
        }

        public SmartControllerProfile(IdleMode idle, int limit) {
            this.idle = idle;
            this.limit = limit;
        }

        public SmartControllerProfile(double rate) {
            this.rate = rate;
        }

    }
}
