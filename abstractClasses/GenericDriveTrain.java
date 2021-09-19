package frc.robot.core751.abstractClasses;

public abstract class GenericDriveTrain{

    public static enum DriveTrainDirection {
        FORWARD(1),
        BACKWARD(-1);

        private int mod;
        public int getMod(){return this.mod;}
        private DriveTrainDirection(int mod) {
            this.mod = mod;
        }

    }

    public abstract DifferentialDriveTrain(int[] left, int[] right, driveMotor dm, boolean invertLeft, boolean invertRight);

    public abstract DifferentialDrive getDifferentialDrive();

    public DriveTrainDirection getDirection(){
        return DriveTrainDirection.FORWARD;
    }
    
    public void setDirection(DriveTrainDirection direction){
        return;
    }
}