package frc.robot.core751.commands;

import java.io.File;
import java.io.PrintWriter;

import java.util.LinkedList;
import java.util.Scanner;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.core751.Controllable;
import frc.robot.core751.Parseable;
import frc.robot.core751.wrappers.OverrideableJoystick;

public class MacroMaker extends CommandBase {
    private Scanner scanner;
    private PrintWriter writer;
    private String file;//Maybe make this a constaint?

    private Controllable[] subsystems; // For building macros
    private Joystick joystick; // For joystick buttons
    private Boolean[] SmartDashButtons; // For smartdashboard buttons
    private Macro[] macros;

    public MacroMaker(Controllable[] subsystems, Joystick joystick) {
        this(subsystems, joystick, null);
    }

    public MacroMaker(Controllable[] subsystems, Boolean[] SmartDashButtons) {
        this(subsystems, null, SmartDashButtons);
    }

    public MacroMaker(Controllable[] subsystems, Joystick joystick, Boolean[] SmartDashButtons) {
        this.subsystems = subsystems;
        this.joystick = joystick;
        this.SmartDashButtons = SmartDashButtons;
        this.macros = new Macro[joystick.getButtonCount() + SmartDashButtons.length];

        SmartDashboard.putBoolean("Editing", true);
        SmartDashboard.putBoolean("Add", false);
        SmartDashboard.putBoolean("Remove", false);
        SmartDashboard.putNumber("Button", 0);

        for (Controllable subsystem : subsystems) {

            if (subsystem instanceof SubsystemBase) {
                addRequirements((SubsystemBase) subsystem);
            }

        }
        try{
            File file = new File(this.file);
            scanner = new Scanner(file);
            writer = new PrintWriter(file);

            scanner.useDelimiter(",|\\n");
        }catch(Exception e){//Good coding practice trust me
            System.out.println(e);
        }
    }

    @Override
    public void execute() {
        if (SmartDashboard.getBoolean("Editing", true)) {

            if (SmartDashboard.getBoolean("Add", false)) {
                SmartDashboard.putBoolean("Add", false);
                int button = (int) SmartDashboard.getNumber("Button", 0);
                if (button > 0 && button < macros.length) {
                    if (macros[button - 1] != null) {
                        // macros[button - 1].add(subsystem, value, code, time);
                    } else {
                        macros[button - 1] = new Macro();
                        // macros[button - 1].add(subsystem, value, code, time);
                    }
                }
            } else if (SmartDashboard.getBoolean("Remove", false)) {
                SmartDashboard.putBoolean("Remove", false);
                int button = (int) SmartDashboard.getNumber("Button", 0);
                if (button > 0 && button < macros.length) {
                    if (macros[button - 1] != null) {
                        //macros.remove(subsystem);
                    }
                }
            }

        } else {
            for (int i = 0; i < macros.length; i++) {// There has got to be a better way to do this.
                if (i <= joystick.getButtonCount()) {
                    if (joystick.getRawButtonPressed(i)) {
                        macros[i - 1].execute();
                    }
                } else if (SmartDashButtons[i]) {
                    macros[i - 1].execute();
                }
            }
        }

    }

    public void autoRead() {

    }

    public void autoWrite(){

    }

    public void read()throws FileFormatException{ //CHANGE ChANGE CHANGE CHANGE
       int line = 0;
       int count = 0;
       String temp;
       Controllable subsystem;
       while(scanner.hasNext()){
           temp = scanner.next();
           if(temp != null){
                macros[line] = new Macro();
                subsystem = subsystemLookup(temp);
                macros[line].add(subsystem, parseValue(subsystem, scanner.next()), Integer.parseInt(scanner.next()), Long.parseLong(scanner.next()));
           }
           line++;
           
       }
    }

    private Controllable subsystemLookup(String string){
        for(Controllable subsystem : subsystems){
            if(subsystem.name().equals(string)){
                return subsystem;
            }
        }
        return null;
    }

    private Object parseValue(Controllable subsystem, String string){
        switch(subsystem.parseType()){
            case Int :
                return Integer.parseInt(string);

            case Double :
                return Double.parseDouble(string);

            case Bool :
                return Boolean.parseBoolean(string);

            case Parseable: //Will create the interface later

        }
        return null;
    }

    public void write() {// To a text file(The easy one) CHAnGE CHAMNGE CHANGE CHANGE CHANGE CHANGE
        String temp = "";
        for(Macro macro : macros){
            if(macro != null){
                temp += macro;
            } else {
                temp += "null";
            }
            temp += "\\n";
        }
        writer.print(temp);
    }

    private abstract class Macro { public abstract void run(); @Override public abstract String toString();} //Just to have an abstract class to extend.


    public static enum pressModel{//I can't put this in button Macro ReeeeEEEEeeEe
        Press,
        Hold,
        Release
    }
    private class buttonMacro extends Macro{
        int button;
        pressModel pModel;
        OverrideableJoystick joystick;

        public buttonMacro(int button, pressModel pModel, OverrideableJoystick joystick){
            this.joystick = joystick;
            this.pModel = pModel;
            this.button = button;
        }

        @Override
        public void run() {
            switch(pModel){
                case Press:
                    joystick.press(button);
                    break;
                case Hold:
                    joystick.hold(button);
                    break;
                case Release:
                    joystick.release(button);
                    break;
            }
        }
        
        @Override
        public String toString() {
            return "B" + button + "," + pModel +",@";
        }
    }

    private class commandMacro extends Macro{

        private Controllable subsystem;
        private Object value;//Turn to Parsable when Wrappers are done
        private int code;

        public commandMacro(Controllable subsystem, Object value) {
            this(subsystem, value, 0);
        }
    
        public commandMacro(Controllable subsystem, Object value, int code) {                                                               
            this.subsystem = subsystem;
            this.value = value;
            this.code = code;
        }

        @Override
        public void run() {
            subsystem.execute(value, code);
        }
        @Override
        public String toString() {
            return "C" + subsystem + "," + value +"," + code +",@";
        }

    }
    private class MacroList extends CommandBase {
        
        private LinkedList<Macro> macros = new LinkedList<>();
        private LinkedList<Long> timeList = new LinkedList<>();

       public String toString() {
            String returnValue = "";
            int index = 0;
            for(Macro macro : macros){
                returnValue += macro.toString() + timeList.get(index) +";";
                index ++;
            }
            return returnValue;
        }
        public void add(Macro macro){
            add(macro,0L);
        }
        public void add(Macro macro,Long time){
            macros.add(macro);
            timeList.add(time);
        }
       
        public void remove() {
           macros.removeLast();
        }
        
        @Override
        public void execute() {
            // TODO Auto-generated method stub
            super.execute();
        }

    }
    private class FileFormatException extends Exception{
        public FileFormatException(int line){
            super("" + line);
        }
    }
   
}
