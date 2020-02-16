package frc.robot.core751.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LightGrid extends SubsystemBase {

    private AddressableLED strip;
    private AddressableLEDBuffer buffer;
    private int tic;
    private int[][][] ledColors;
    private int[][][] ledColorsFinal;
    public int rows;
    public int cols;
    public boolean[] effects;

    public enum PostProccessingEffects {
        WAVE;
    }

    public LightGrid (int port, int rows, int cols) {
        this.strip = new AddressableLED(port);
        this.buffer = new AddressableLEDBuffer(rows*cols);
        this.ledColors = new int[rows][cols][3];
        this.ledColorsFinal = new int[rows][cols][3];
        this.rows = rows;
        this.cols = cols;
        this.strip.start();
        this.effects = new boolean[PostProccessingEffects.values().length];
    }

    public void addEffect(PostProccessingEffects p) {
        this.effects[p.ordinal()] = true;
    }

    public void setHSV(int r, int c, int h, int s, int v) {
        this.ledColors[r][c][0] = h;
        this.ledColors[r][c][0] = s;
        this.ledColors[r][c][0] = v;
    }

    public void clearEffects() {
        for (int i = 0; i < effects.length; i++) {
            effects[i] = false;
        }
    }

    private void postProccessing() {
        if (effects[PostProccessingEffects.WAVE.ordinal()]) {
            for (int r = 0; r < rows; r++) {
                double mod = this.getWaveMod(2, r);
                for (int c = 0; c < cols; c++) {
                    this.ledColorsFinal[r][c][2] = (int)(this.ledColorsFinal[r][c][2] * mod);
                }
            }
        }
    }

    private void copyArrayToBuffer() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.buffer.setHSV((r*cols)+c, ledColorsFinal[r][c][0], ledColorsFinal[r][c][1], ledColorsFinal[r][c][2]);
            }
        }
    }

    private void copyArraytoFinalArray() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.ledColorsFinal[r][c][0] = this.ledColors[r][c][0];
                this.ledColorsFinal[r][c][1] = this.ledColors[r][c][1];
                this.ledColorsFinal[r][c][2] = this.ledColors[r][c][2];
            }
        }
    }

    public void update() {
        this.copyArraytoFinalArray();
        this.postProccessing();

        this.copyArrayToBuffer();
        this.strip.setData(this.buffer);
        this.tic = (tic+1)%2048;
    }

    private double getWaveMod(int cycleCount, int i) {
        return (Math.cos((i+(tic/rows)%rows)*cycleCount*2*Math.PI)+2/3);
    }

    public void fillHSV(int h, int s, int v) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.ledColors[r][c][0] = h;
                this.ledColors[r][c][1] = s;
                this.ledColors[r][c][2] = v;
            }
        }
    }


}