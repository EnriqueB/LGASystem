package com.enrique;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

/**
 * Created by enrique on 27/08/16.
 */
public class LSystem {


    int angle;
    int rank;
    String g_commands;

    public LSystem() {
        angle = 0;
        g_commands = "";
    }

    public LSystem(int angle,  int rank, String g_commands) {
        this.angle = angle;
        this.rank = rank;
        this.g_commands = g_commands;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getG_commands() {
        return g_commands;
    }

    public void setG_commands(String g_commands) {
        this.g_commands = g_commands;
    }

    public void initialize(){
        Random r = new Random();
        angle = r.nextInt(360);
        while(angle<30 || angle > 330 || (angle >150 && angle < 210)){
            angle = r.nextInt(360);
        }

        g_commands = "";
        Stack stack = new Stack();
        String symbols [] = {"X", "Y", "Z", "C", "+", "-", "[", "]"};
        //int length = r.nextInt(90000)+ 5000;
        int length = r.nextInt(9000)+ 5000;
        int index;
        for(int i=0; i<length; i++){
            index = r.nextInt(symbols.length-2);
            if(r.nextDouble() > 0.7){
                index = r.nextInt(symbols.length);
            }
            if(symbols[index] == "]"){
                //check stack;
                if(stack.empty()){
                    index = r.nextInt(symbols.length-1);
                }
            }
            if(symbols[index].equals("[")) {
                stack.push(1);
            }
            g_commands+=symbols[index];
            if(symbols[index].equals("C")){
                g_commands+=(r.nextInt(10)+"");
            }
        }
        rank = r.nextInt(91)+10;
    }

    public LSystem crossover(LSystem p1, LSystem p2){
        Random r = new Random();

        //Angle crossover
        int angleP1 = p1.getAngle();
        int angleP2 = p2.getAngle();

        String angleP1Str = Integer.toBinaryString(angleP1);
        String angleP2Str = Integer.toBinaryString(angleP2);

        while(angleP1Str.length()<9){
            angleP1Str = "0"+angleP1Str;
        }
        while(angleP2Str.length()<9){
            angleP2Str = "0"+angleP2Str;
        }


        int index = r.nextInt(10);

        String newAngleStr = angleP1Str.substring(0, index)
                + angleP2Str.substring(index, angleP2Str.length());

        int newAngle = Integer.parseInt(newAngleStr, 2);

        String p1G_commands = p1.getG_commands();
        String p2G_commands = p2.getG_commands();

        int minimum = Math.min(p1G_commands.length(), p2G_commands.length());

        //ammount of crossover points
        int ammountXoverPoints = r.nextInt((minimum/1000))+1;
        int xoverPoints [] = new int [ammountXoverPoints + 1];
        xoverPoints[0] = 0;
        for(int i=1; i<ammountXoverPoints+1; i++){
            xoverPoints[i] = r.nextInt(minimum);
        }
        Arrays.sort(xoverPoints);
        String n_commands = "";
        for(int i=0; i<ammountXoverPoints; i++){
            if(i%2 == 0){
                System.out.println(xoverPoints[i] + " " + xoverPoints[i+1]);
                System.out.println(p1G_commands.substring(xoverPoints[i], xoverPoints[i+1]));
                n_commands+=p1G_commands.substring(xoverPoints[i], xoverPoints[i+1]);
            }
            else{
                n_commands+=p2G_commands.substring(xoverPoints[i], xoverPoints[i+1]);
            }
        }
        if(p1G_commands.length() > p2G_commands.length()){
            n_commands += p1G_commands.substring(xoverPoints[ammountXoverPoints]);
        }
        else{
            n_commands += p2G_commands.substring(xoverPoints[ammountXoverPoints]);
        }
        return new LSystem(newAngle, 5, n_commands);
    }

    public LSystem mutate(LSystem p){
        double mutationChance = 0.1;
        Random r = new Random();
        int angle = p.getAngle();
        String angleStr = Integer.toBinaryString(angle);
        while(angleStr.length() < 9){
            angleStr = "0"+angleStr;
        }
        String newAngleStr = "";
        for(int i =0; i<angleStr.length(); i++){
            if(r.nextDouble()<mutationChance){
                if(angleStr.charAt(i) == '0'){
                    newAngleStr +="1";
                }
                else{
                    newAngleStr +="0";
                }
            }
            else{
                newAngleStr+=angleStr.substring(i, i+1);
            }
        }
        int newAngle = Integer.parseInt(newAngleStr, 2);

        mutationChance = 0.05;

        String g = p.getG_commands();
        String symbols [] = {"X", "Y", "Z", "C", "+", "-", "[", "]"};
        String n_commands = "";
        for(int i=0; i<g.length() && i<99000; i++){
            if(r.nextDouble()<mutationChance){
                //mutate
                if(g.substring(i, i+1).equals("C")){
                    if(r.nextBoolean()) {
                        n_commands += "C" + (r.nextInt(10) + "");
                    }
                    else{
                        n_commands+=symbols[r.nextInt(symbols.length)];
                    }
                    i++;
                }
                else {
                    n_commands += symbols[r.nextInt(symbols.length)];
                }
                if(n_commands.substring(n_commands.length()-1).equals("C")){
                    n_commands+=(r.nextInt(10)+"");
                    i++;
                }
            }
            else{
                n_commands+=g.substring(i, i+1);
            }
            if(r.nextDouble()<mutationChance/2.0){
                i--;
            }
            if(r.nextDouble()<mutationChance/2.0){
                i++;
            }
        }
        if(n_commands.length()>99000){
            n_commands = n_commands.substring(0, 95000);
        }

        return new LSystem(newAngle, 5, n_commands);
    }
}
