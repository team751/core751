package frc.robot.core751.wrappers;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WCANSparkMax implements SpeedController {
    private CANSparkMax mot;
    private double speedCap;

    public WCANSparkMax(int deviceID, CANSparkMaxLowLevel.MotorType type){
        this.mot = new CANSparkMax(deviceID, type);
        this.speedCap = 1.0;

        SmartDashboard.putNumber("Speed Cap", this.speedCap);
    }

    @Override
    public void pidWrite(double output) {
        mot.pidWrite(output);
    }

    @Override
    public void set(double speed) {
        double speedCap = SmartDashboard.getNumber("Speed Cap", 1.0);

        if(speed < 0) {
            speedCap = -speedCap;
        }

        mot.set(Math.min(speed, speedCap));
    }
   
    public CANEncoder getEncoder(){
        return mot.getEncoder();
    }


    public void setSoftLimit(CANSparkMax.SoftLimitDirection direction, float limit){
        mot.setSoftLimit(direction, limit);
    }

    @Override
    public double get() {
        return mot.get();
    }

    @Override
    public void setInverted(boolean isInverted) {
        mot.setInverted(isInverted);
    }

    @Override
    public boolean getInverted() {
        return mot.getInverted();
    }

    @Override
    public void disable() {
        mot.disable();
    }

    @Override
    public void stopMotor() {
        mot.stopMotor();
    }

    public CANPIDController getPIDController(){
        return mot.getPIDController();
    }

    public void setBreakMode(IdleMode mode) {
        this.mot.setIdleMode(mode);
    }

    public void setOpenLoopRampRate(double rate) {
        this.mot.setOpenLoopRampRate(rate);
    }

    public void setClosedLoopRampRate(double rate) {
        this.mot.setClosedLoopRampRate(rate);
    }

    public void setCurrentLimit(int limit) {
        this.mot.setSmartCurrentLimit(limit);
    }

}