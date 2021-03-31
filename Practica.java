package pkg_practica;
import robocode.*;
import robocode.AdvancedRobot;
import robocode.RobocodeFileOutputStream;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import static robocode.util.Utils.normalRelativeAngleDegrees;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * Practica - a robot by Gary y Sharolin
 */
public class Practica extends AdvancedRobot 
{
	/**
	 * run: Practica's default behavior
	 */
	
	float alpha=0;
	float gamma=0;
	int no_turnos=0;
	int modo_juego=0;
	//valores epsilon
	float epsilon = 1
	float epsilon_minimo = 0.05
	float tasa_decaimiento = 0.02
	List<Float> listado_recompensas=new ArrayList<Float>();  
	static int corner = 180; // Which corner we are currently using
	// static so that it keeps it between rounds.
	boolean stopWhenSeeRobot = false; // See goCorner()
	
	public void run() {
		// Initialization of the robot should be put here


		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		setColors(Color.red,Color.blue,Color.green); // body,gun,radar
		//read file		
		readfile();
		//leer hiperparametos
		readHpar();
		// Move to a corner
		goCorner();
		// Robot main loop
		// Initialize gun turn speed to 3
		int gunIncrement = 3;
		while(true) {
			for (int i = 0; i < 30; i++) {
				turnGunLeft(gunIncrement);
			}
			gunIncrement *= -1;
		}
		
	}
	public void readfile(){
		try (FileReader reader = new FileReader("C:\\robocode\\robots\\pkg_practica\\minas.txt");
				 BufferedReader br = new BufferedReader(reader)) {

				// read line by line
				String line;
				while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "(,)");
			while (st.hasMoreTokens()) {
				System.out.println("x: "+st.nextToken()+" y: "+st.nextToken());
				
				
			}
				}

			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
			}
	}
	public void readHpar(){
		try (FileReader reader = new FileReader("C:\\robocode\\robots\\pkg_practica\\200915609.par");
             BufferedReader br = new BufferedReader(reader)) {

            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				while (st.hasMoreTokens()) {
					alpha=Float.parseFloat(st.nextToken());
					gamma=Float.parseFloat(st.nextToken());
					no_turnos=Integer.parseInt(st.nextToken());
					modo_juego=Integer.parseInt(st.nextToken());
					
				}
				System.out.println(alpha+" "+gamma+" "+no_turnos+" "+modo_juego);
			}

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
	}
	public void goCorner() {
		// We don't want to stop when we're just turning...
		stopWhenSeeRobot = false;
		// turn to face the wall to the "right" of our desired corner.
		turnRight(normalRelativeAngleDegrees(corner - getHeading()));
		// Ok, now we don't want to crash into any robot in our way...
		stopWhenSeeRobot = true;
		// Move to that wall
		ahead(5000);
		// Turn to face the corner
		turnRight(90);
		// Move to the corner
		ahead(5000);
		// Turn gun to starting point
		turnGunLeft(90);
		
	
	}
	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		fire(1);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	
}
