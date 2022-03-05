package frc.robot.core751.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PneumaticsController extends SubsystemBase{


    PneumaticsControlModule pcm;
    PneumaticHub ph;
    Compressor compressor;

    public PneumaticsController(PneumaticsModuleType type, int CANId){
        switch(type){
            case CTREPCM: 
                this.pcm = new PneumaticsControlModule(CANId);
                compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
                break;
            case REVPH: 
                this.ph = new PneumaticHub(CANId);
                compressor = new Compressor(1, PneumaticsModuleType.CTREPCM);
                break;
        }
    }

    public void setSingleSolinoid(int id, boolean on){
        if(pcm != null){
            
        }else if(ph != null){
            
        }
    }

    public void setDoubleSolinoid(int forwardId, int reverseId, DoubleSolenoid.Value value){
        if(pcm != null){
            switch(value){
                case kOff:
                    break;
                case kForward:
                    break;
                case kReverse:
                    break;
            }
        }else if(ph != null){
            switch(value){
                
            }
        }
    }

    public void setCompressor(boolean on){
        if(on){
            compressor.enableDigital();
        }else{
            compressor.disable();
        }
    }
    
}
