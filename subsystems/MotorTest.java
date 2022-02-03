package frc.robot.core751.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;



public class MotorTest extends SubsystemBase{

    public enum motType {
        Falcon500,
        Neo,
        CIM;
    }

    int id;
    motType mType;
    public MotorTest(motType mType,int id){
        this.mType = mType;
        this.id = id;
    }

    public void set(double speed){
        switch(mType){
            case Falcon500:
                WPI_TalonFX talon = new WPI_TalonFX(id);
                talon.configFactoryDefault();
                talon.stopMotor();
                talon.set(ControlMode.PercentOutput, 0.5);
                System.out.println("uWu");
                break;
            case Neo:
                new CANSparkMax(id, MotorType.kBrushless).set(speed);
                break;
            case CIM:
                break;
        }
    }
}