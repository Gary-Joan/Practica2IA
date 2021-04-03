/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;

import robocode.*;
import robocode.AdvancedRobot;
import robocode.RobocodeFileOutputStream;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;
import java.util.StringTokenizer;

import static robocode.util.Utils.normalRelativeAngleDegrees;
import static robot.Rl_lut.intRandom;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * Practica - a robot by Gary y Sharolin
 */
public class Practica extends AdvancedRobot 
{
	/**
	 * run: Practica's default behavior
	 */
	
	// declare variables
	final double epsilon = 0;
	double gamma = 0.9;
	double alpha = 0.1;
        //LUT table initialization
        final int acciones=5;
        final int statesCount = 80;
        int[][] R = new int[statesCount][acciones]; // reward lookup
        
        //iniciamos tabla Q con valores 0 numero_de_accciones X numero_de_estados
        double[][] Q = new double[statesCount][acciones]; // Q learning
        int[] actions = new int[]{1,2,3,4,5};
        /*1 = mover arriba
          2 = mover abajo
          3 = mover izq
          4 = mover der
          5 = disporar o desenterrar*/
        //estado inicial del sistema
        String[][] estadoI = new String[8][10];
     
        
        int no_turnos=0;
	int modo_juego=0;
	
	double epsilon_minimo = 0.05;
	double tasa_decaimiento = 0.02;
	List<Float> listado_recompensas=new ArrayList<>();  
	static int corner = 180; // Which corner we are currently using
	// static so that it keeps it between rounds.
	boolean stopWhenSeeRobot = false; // See goCorner()
	//creacion de la tabla Q
	
	public void run() {
		// Initialization of the robot should be put here
                //iniciamos el sistemas inicial y final con valores
                estadoI[0][0]="X";
                estadoI[6][8]="F";

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:
                
		setColors(Color.blue,Color.blue,Color.green); // body,gun,radar
		//read file		
		readfile();
		//leer hiperparametos
		readHpar();
		// Move to a corner
		goCorner();
                int i,j=0;
                if(modo_juego == 0){

                                //initialize LUT for learning 
                                iniciar_entrenamiento();
 //                               save();

                                //load LUT for testing 
                                /*try {
                                        load();
                                        } 
                                catch (IOException e) {
                                        e.printStackTrace();
                                        }
                                save();*/
                    }

                

                while(true){
                    //turn gun to scan 
                        ahead(1);
                }
	

		
	}
	public void readfile(){
		try (FileReader reader = new FileReader("C:\\robocode\\robots\\pkg_practica\\minas.txt");
				 BufferedReader br = new BufferedReader(reader)) {

				// read line by line
				String line;
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "(,)");
                        //inicio lista de minas
                        

                        List<List<Integer>> minas = new ArrayList<List<Integer>>();
                        

                        minas.add(new ArrayList<Integer>());
                        minas.add(new ArrayList<Integer>());
			while (st.hasMoreTokens()) {
				minas.get(0).add(Integer.parseInt(st.nextToken()));
                                minas.get(1).add(Integer.parseInt(st.nextToken()));
				
			}
                        for (int i = 0; i <= minas.get(0).size() - 1; i++) {
                            System.out.println("X: " + minas.get(0).get(i) + " Y: " + minas.get(1).get(i));
                        }
                }

			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
			}
	}
	public void readHpar(){
	 try (FileReader reader = new FileReader("F:\\OneDrive\\Documentos\\NetBeansProjects\\Robot\\build\\classes\\robot\\200915609.par");
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
		System.out.println(e.getDistance());
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
		back(1);
	}	
//inicio de funcion para entregar robot
    private void iniciar_entrenamiento() {
        guardar_tabla();
        Random rand = new Random();
        for (int i = 0; i < no_turnos; i++) { // train episodes
            // Select random initial state
            int accion_ramdon = rand.nextInt(acciones);
        }
    }
    
    
    
    
    //retornamos el valor de la Tabla Q (estados, acciones)
    double Q(int s, int a) {
        return Q[s][a];
    }
    //seteamos el nuevo vamos en la Tabla Q
    void setQ(int s, int a, double value) {
        Q[s][a] = value;
    }
    //funcion la cual ejecuta la accion random del robot
    public void ejecuatar_accion(int x)
			{
	switch(x){
		case 1: 
			int moveDirection=+1;  
			setAhead(100);
			break;
		case 2: 
			int moveDirection1=-1;  
			setAhead(100);
			break;
		case 3: 
			ahead(100);
			break;
		case 4: 

			back(100);
			break;
                case 5:
                        fire(1);
                        break;
	}
}
//metodo para guardar la tabla Q en el archivo
    public void guardar_tabla() {
        int i,j=0;
	PrintStream S = null;
	try {
		S = new PrintStream(new RobocodeFileOutputStream("F:\\OneDrive\\Documentos\\NetBeansProjects\\Robot\\build\\classes\\robot\\200915609.tabla"));
                for (i = 0; i < Q.length; i++) { 

                        S.println(Q[i][0] + " "+Q[i][1] + " "+Q[i][2] + " "+Q[i][3] + " "+Q[i][4]);
                    
                    System.out.println();
                }
	} catch (IOException e) {
		e.printStackTrace();
	}finally {
		S.flush();
		S.close();
	}
}
    
}
