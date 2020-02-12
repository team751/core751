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

    public void read()throws FileFormatException{
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

    public void write() {// To a text file(The easy one)
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

    private class Macro extends CommandBase {
        private LinkedList<Controllable> subsystemList = new LinkedList<>();

        private LinkedList valueList = new LinkedList<>();
        private LinkedList<Integer> codeList = new LinkedList<>();
        // Maybe combine these two?
        // Maybe move these three to a diffrent nested class?

        private LinkedList<Long> timeList = new LinkedList<>();

        @Override
        public String toString() {
            String returnValue = "";
            for(int i = 0; i < subsystemList.size(); i++){
                returnValue += subsystemList.get(i) + "," + valueList.get(i) + "," + codeList.get(i) + "," + timeList.get(i) + ",;";//The comma before the semi-colon is very importaint
            }
            return returnValue;
        }

        public void add(Controllable subsystem, Object value) {
            add(subsystem, value, 0, 0);
        }

        public void add(Controllable subsystem, Object value, int code) {
            add(subsystem, value, code, 0);
        }

        public void add(Controllable subsystem, Object value, long time) {
            add(subsystem, value, 0, time);
        }

        public void add(Controllable subsystem, Object value, int code, long time) {// Is there a better way then using
                                                                                    // object for value?
            subsystemList.add(subsystem);
            valueList.add(value);
            codeList.add(code);
            timeList.add(time);
        }

        public void remove(Controllable subsystem) {
            if (subsystemList.contains(subsystem)) {
                int index = subsystemList.indexOf(subsystem);
                subsystemList.remove(index);
                valueList.remove(index);
                codeList.remove(index);
                timeList.remove(index);
            }
        }

        @Override
        public void execute() {

            subsystemList.add(null);
            valueList.add(null);
            codeList.add(null);
            timeList.add(null);// Better way?

            int index = 0;

            Controllable subsystem = subsystemList.get(index);
            Object value = valueList.get(index);
            int code = codeList.get(index);
            long delay = timeList.get(index);

            long time = System.currentTimeMillis();

            while (subsystem != null) {
                if (System.currentTimeMillis() >= time + delay) {
                    subsystem.execute(value, code);
                    index++;
                    subsystem = subsystemList.get(index);
                    value = valueList.get(index);
                    code = codeList.get(index);
                    delay = timeList.get(index);
                    time = System.currentTimeMillis();
                }
            }

        }
    }
    private class FileFormatException extends Exception{
        public FileFormatException(int line){
            super("" + line);
        }
    }
    public class PairNumberValue<T1 extends Parseable,T2 extends Parseable> implements Parseable{
        T1 value1;
        T2 value2;
        @Override
        public void parse(String string) {//change values of thing1 and thing2 beCuase nO stTic In IntErfAce
            
        }
        @Override
        public String toString() {
            // TODO Auto-generated method stub 
            return super.toString();
        }
    }
}
