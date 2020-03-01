
package frc.robot.core751.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LightStrip extends SubsystemBase {

    private static AddressableLED LED;
    public int length;
    private int[][] preEffectLEDS;
    private int[][] postEffectLEDS;
    public static AddressableLEDBuffer buffer;
    public int tic;
    private boolean[] effects;
    private Orientation orientation;
    public int cycleCount = 1;
    public int hueShiftSpeed = 7;
    public int startIndex;

    public enum Orientation {
        FORWARD,
        BACKWARD,
        HORIZONTAL;
    }

    public enum PostProccessingEffects {
        WAVE,
        HUE_SHIFT;
    }

    public static void initializeStrip(int port, int length) {
        LightStrip.LED = new AddressableLED(port);
        LightStrip.LED.setLength(length);
        LightStrip.buffer = new AddressableLEDBuffer(length);
    }

    public static void pushBuffer() {
        LED.setData(buffer);
    }

    public LightStrip(int startIndex, int length, Orientation orientation) {
        this.length = length;
        this.effects = new boolean[PostProccessingEffects.values().length];
        this.preEffectLEDS = new int[length][3];
        this.postEffectLEDS = new int[length][3];
        this.cycleCount = length/5;
        this.tic = 0;
        this.postEffectLEDS = new int[length][3];
        this.preEffectLEDS = new int [length][3];
        this.orientation = orientation;
        this.effects = new boolean[PostProccessingEffects.values().length];
        this.startIndex = startIndex;
    }

    public static void start() {
        LED.start();
    }

    public static void stop() {
        LED.stop();
    }

    public void advanceTic(){
        this.tic++;
        this.tic%=(length*length);
    }

    public void update() {
        this.postProccessing();
        this.copyToBuffer();
        this.advanceTic();
    }

    public void setHSV(int i, int h, int s, int v) {
        buffer.setHSV(i, h, s, v);
    }

    private void postProccessing() {
        for (int i = 0; i < length; i++) {
            if (effects[PostProccessingEffects.WAVE.ordinal()]) {
                switch(this.orientation) {
                    case FORWARD:
                        this.postEffectLEDS[i][2] = (int)(this.preEffectLEDS[i][2] * this.getWaveMod(i * DifferentialDriveTrain.direction.getMod()));
                    break;
                    case BACKWARD:
                        this.postEffectLEDS[i][2] = (int)(this.preEffectLEDS[i][2] * this.getWaveMod(-i* DifferentialDriveTrain.direction.getMod()));
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

            this.postEffectLEDS[i][1] = this.preEffectLEDS[i][1];
        }

        
    }

    private double getWaveMod(int i) {
        //return ((Math.cos((i+(((float)tic/100f)/length)%length)/length)*cycleCount*2*Math.PI)+2/3);
        float m = i +  tic/5f;
        
        float cl = length/cycleCount;
        float o =  m%cl;
        float v = 0.25f;
        if (o < (cl/2.0)) {
            return 0.75+(v*o)/(cl/2);
        }else {
            return 0.75 + (v-(v/(cl/2)))*o;
        }
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
            //this.buffer.setHSV(i, this.preEffectLEDS[i][0], this.preEffectLEDS[i][0], this.preEffectLEDS[i][0]);
            buffer.setHSV(i + this.startIndex, this.postEffectLEDS[i][0], this.postEffectLEDS[i][1], this.postEffectLEDS[i][2]);
        }
    }

    public void setEffect(PostProccessingEffects e) {
        this.effects[e.ordinal()] = true;
    }

    public void clearEffects() {
        for (int i = 0; i < effects.length; i++) {
            this.effects[i] = false;
        }
    }
}