/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;

import robocode.*;
import robocode.util.*;
import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * Elemento105 - a robot by (your name here)
 */
public class robot_115 extends AdvancedRobot
{/**
	 * run: Prueba's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		setColors(Color.red,Color.red,Color.red); // body,gun,radar

		// Robot main loop
		while(true) {

       		mover_a(900,700);   
                turnGunRight(10);		
    	}
	}

	private void mover_a(double x, double y) {
	    /* Calcula la diferencia entre la pos actual y la posicion a la que se dirige. */
	    x = x - getX();
	    y = y - getY();
	    double goAngle = Utils.normalRelativeAngle(Math.atan2(x, y) - getHeadingRadians());
	    setTurnRightRadians(Math.atan(Math.tan(goAngle)));
	    setAhead(Math.cos(goAngle) * Math.hypot(x, y));
	}

	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	
}