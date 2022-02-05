/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.core751.commands;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.core751.wrappers.OverrideableJoystick;


public class JoystickRecorder extends CommandBase {
    public OverrideableJoystick joystick;
    public LinkedList<Node> list = new LinkedList<>();

    public class Node{
        Double xAxis;
        Double yAxis;
        int[] buttons;
        public Node(Double xAxis,Double yAxis,int[] buttons){
            this.xAxis = xAxis;
            this.yAxis = yAxis;
            this.buttons = buttons;
        }
        @Override
        public String toString(){
            String temp = "{" + xAxis + "D," + yAxis;
            if(buttons != null){
                for(int num:buttons){
                    temp += "D," + num;
                }
            }
            return temp + "D}";
            }

    }

    public JoystickRecorder(OverrideableJoystick joystick){
        this.joystick = joystick;
    }

    @Override
    public void execute() {
            System.out.println(joystick.getX() + " " + joystick.getY() + " " + joystick.getButtonsPressed());
            list.add(new Node(joystick.getRawAxis(0),joystick.getRawAxis(1),joystick.getButtonsPressed()));
    }
    
    @Override 
    public String toString(){
        String temp = "{";
        for(Node node : list){
            temp += node.toString() + ",";
        } 
        temp = temp.substring(0,temp.length() - 1) + "}";
        return temp;
    }

    @Override
    public void end(boolean interrupted) {
        String fileContents = "public class Storage{public static Double[][] array = " + this.toString() + ";}";
        System.out.println(fileContents);
        try{
            File file = new File("Storage.java");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            } 
            FileWriter writer = new FileWriter("Storage.java");
            writer.write(fileContents);
            writer.close();
        }catch(Exception e){}
    }

   
}