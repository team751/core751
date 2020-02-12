/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.core751;

/**
 * Add your docs here.
 */
public class ValueTypes {
    //Wrappers For Pair Vaule actually works eeeeee
   
    
    
    
    
    
//I give up for now
    public class PairValue<T1 extends Parseable, T2 extends Parseable> /*Is there a way to make sure it's parsable or extends Number? */ implements Parseable{
        public T1 value1;
        public T2 value2;
       
        @Override
        public void parse(String string) {
          value1.parse(string.split("|")[0]);
          value2.parse(string.split("|")[1]);
        }
        @Override
        public String toString() {
            return value1.toString() + "|" + value2.toString();
        }
    }

}
