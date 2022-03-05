package frc.robot.core751.wrappers;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class wTalonFX extends WPI_TalonFX{

    public wTalonFX(int deviceNumber) {
        super(deviceNumber);
        this.configFactoryDefault();
        this.stopMotor();
    }

    public void set(double speed){
        this.set(TalonFXControlMode.PercentOutput, speed);
    }

    /**
     * @return Velocity in revolutions per second.
     */
    public double getVelocity(){
        return this.getSelectedSensorVelocity() / 2048 * 10;
    }


}