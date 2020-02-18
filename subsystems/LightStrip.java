
package frc.robot.core751.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LightStrip extends SubsystemBase {

    private AddressableLED LED;
    public int length;
    private int[][] preEffectLEDS;
    private int[][] postEffectLEDS;
    public AddressableLEDBuffer buffer;
    public int tic;
    private boolean[] effects;
    private Orientation orientation;
    public int cycleCount = 2;
    public int hueShiftSpeed = 7;

    public enum Orientation {
        FORWARD,
        BACKWARD,
        HORIZONTAL,
    }

    public enum PostProccessingEffects {
        WAVE,
        HUE_SHIFT;
    }

    public LightStrip(int port, int length, Orientation orientation) {
        this.LED = new AddressableLED(port);
        this.LED.setLength(length);
        this.length = length;
        this.buffer = new AddressableLEDBuffer(length);
        this.effects = new boolean[PostProccessingEffects.values().length];
        this.preEffectLEDS = new int[length][3];
        this.postEffectLEDS = new int[length][3];
        this.cycleCount = length/5;
        this.tic = 0;
        this.start();
        this.orientation = orientation;
    }

    public void start() {
        this.LED.start();
    }

    public void stop() {
        this.LED.stop();
    }

    public void advanceTic(){
        this.tic++;
        this.tic%=2048;
    }

    public void update() {
        this.postProccessing();
        this.copyToBuffer();
        this.LED.setData(this.buffer);
        this.advanceTic();
    }

    public void setHSV(int i, int h, int s, int v) {
        this.buffer.setHSV(i, h, s, v);
    }

    public void clearEffects() {
        for (int i = 0; i < effects.length; i++) {
            effects[i] = false;
        }
    }

    private void postProccessing() {
        for (int i = 0; i < length; i++) {
            if (effects[PostProccessingEffects.WAVE.ordinal()]) {
                switch(this.orientation) {
                    case FORWARD:
                        this.postEffectLEDS[i][2] = (int)(this.preEffectLEDS[i][2] * this.getWaveMod(i));
                    break;
                    case BACKWARD:
                        this.postEffectLEDS[i][2] = (int)(this.preEffectLEDS[i][2] * this.getWaveMod(-i));
                    break;
                    case HORIZONTAL:
                        if (i < length/2) {
                            this.postEffectLEDS[i][2] = (int)(this.preEffectLEDS[i][2] * this.getWaveMod(i));
                        }else {
                            this.postEffectLEDS[i][2] = (int)(this.preEffectLEDS[i][2] * this.getWaveMod(-i));
                        }
                    break;
                }
            }
            if (effects[PostProccessingEffects.HUE_SHIFT.ordinal()]) {
                if (Math.abs(this.postEffectLEDS[i][0] - this.preEffectLEDS[i][0]) >= hueShiftSpeed) {
                    this.postEffectLEDS[i][0] = this.preEffectLEDS[i][0];
                }else {
                    this.postEffectLEDS[i][0] += hueShiftSpeed * this.postEffectLEDS[i][0]>this.preEffectLEDS[i][0]?-1:1;
                }
            } else {
                this.postEffectLEDS[i][0] = this.preEffectLEDS[i][0];
            }
        }
    }

    private double getWaveMod(int i) {
        return (Math.cos((i+(tic/length)%length)*cycleCount*2*Math.PI)+2/3);
    }

    public void fillHSV(int h, int s, int v) {
        for (int i = 0; i < length; i++) {
                this.preEffectLEDS[i][0] = h;
                this.preEffectLEDS[i][1] = s;
                this.preEffectLEDS[i][2] = v;
            
        }
    }

    private void copyToBuffer() {
        for (int i = 0; i < length; i++) {
            this.buffer.setHSV(i, this.postEffectLEDS[i][0], this.postEffectLEDS[i][1], this.postEffectLEDS[i][2]);
        }
    }

    public void setEffect(PostProccessingEffects e) {
        this.effects[e.ordinal()] = true;
    }


    

}