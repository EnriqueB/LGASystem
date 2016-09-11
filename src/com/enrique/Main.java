package com.enrique;

public class Main {

    public static void main(String[] args) {
	// write your code here
        LSystem l = new LSystem();
        l.initialize();
        LSystem l2 = new LSystem();
        l2.initialize();
        System.out.println("Angle: "+l.getAngle()+"\n G_commands: "+ l.getG_commands());
        System.out.println("Angle: "+l2.getAngle()+"\n G_commands: "+ l2.getG_commands());
        LSystem x = l.crossover(l, l2);
        System.out.print("Angle: "+x.getAngle() + "\n G_commands: " + x.getG_commands());
    }
}
