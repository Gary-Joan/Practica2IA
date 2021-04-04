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
import robocode.util.Utils;

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
        //valores globales
	double alpha;
	double gamma;
	int no_turnos;
	int cont_turnos=0;
	int modo_juego;
	//valores epsilon
	double epsilon = 1;
	double epsilon_minimo = 0.05;
	double tasa_decaimiento = 0.02;
	
	static int corner = 180; // Which corner we are currently using
	// static so that it keeps it between rounds.
	boolean stopWhenSeeRobot = false; // See goCorner()
	//creacion de la tabla Q
	double[][] Qtable = new double[11][11];
        int[][] minas = new int[11][11];
	public void run() {

		setColors(Color.blue,Color.blue,Color.green); // body,gun,radar
		//read file		
		readfile();
		//leer hiperparametos
		readHpar();
		// Move to a corner
		goCorner();
                int i,j=0;


                
                int bandera=0;
                int x=80;
                int y=80;
                while(cont_turnos<=no_turnos){
                    System.out.println("-----Posicion X:"+(int)getX()/100+" Y:"+(int)getY()/100+" N#_TURNOS:"+cont_turnos);
                    if(bandera==0&&getX()!=80){
       		 	mover_a(x,y);   
                        turnGunRight(10);	         	
                    }else{
                          bandera=1;
                          if(modo_juego == 0){
                                Accion(cont_turnos);
                                Recompensa();
                               	if(Elemento()){
                                    System.out.println("Ha encontrado el ELEMENTO 105!");
                                    break;
                                }


                        }
                    }
                    cont_turnos++;
                }
	
        System.out.println("Juego Finalizado revise su archivo Q.table!");
        guardar_tabla();
		
	}
	public void readfile(){
		try (FileReader reader = new FileReader("F:\\OneDrive\\Documentos\\NetBeansProjects\\Robot\\src\\robot\\minas.txt");
                    BufferedReader br = new BufferedReader(reader)) {

				// read line by line
                    String line;
                    while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "(,)");
			while (st.hasMoreTokens()) {
				System.out.println("x: "+st.nextToken()+" y: "+st.nextToken());
				int x=Integer.parseInt(st.nextToken());
				int y=Integer.parseInt(st.nextToken());
				minas[x][y]=1;
				
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

	public void onHitByBullet(HitByBulletEvent e) {
		back(10);}
	public void onHitWall(HitWallEvent e) {
		back(1);
	}	

//metodo para guardar la tabla Q en el archivo
    public void guardar_tabla() {
	try {
            RobocodeFileWriter fileWriter = new RobocodeFileWriter("F:\\OneDrive\\Documentos\\NetBeansProjects\\Robot\\build\\classes\\robot\\200915609.tabla");
            for (int i = 0; i < Qtable.length; i++) {
            String s= "";
            for (int j = 0; j < Qtable[i].length; j++) {
            	if(i==Qtable.length-1&&j==Qtable[i].length-1){
                	s+= Qtable[i][j];	
				}else{
                	s+= Qtable[i][j] + ",";			
				}
            }
            fileWriter.write(s+"\n");
        }
	    fileWriter.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
}

    private void Accion(int cont_turnos) {
		double num = Math.random()*100;
		if(num/100 < epsilon){//Exploramos
                    int [] coor=MaxValor(Qtable);
                    mover_a(coor[0],coor[1]); 		 	
		}else{
			if(num>50){
		 		mover_a(getX()+100,getY());  
			}else{
				if(getX()>=800){
		 			mover_a(getX(),getY()-10); 
				}else{
		 			mover_a(getX(),getY()+10); 					
				}	
			}			
		}
        epsilon = epsilon_minimo + (1 - epsilon_minimo) * Math.exp(-tasa_decaimiento * cont_turnos);
        turnGunLeft(10);
    }

    private void Recompensa() {
       		double recompensa=0;
		int rwin=0;
		int rtiempo=0;
		int rmina=0;
		int rmuro=0;
		double rdistancia=0;
		if(getX()==900&&getY()==700){
                    rwin=10; 
                }
		rtiempo=-10;

		if(Minas()){ 
			rmina=-10; 
		}else{
			rmina=10; 
		}
		rdistancia=(900-getX())+(700-getY());
		recompensa=rwin+rtiempo+rmina+rmuro+rdistancia;
		int x = (int) Math.round(getX()/100);
		int y = (int) Math.round(getY()/100);

		int [] cor=MaxValor(Qtable);
	 	mover_a(cor[0],cor[1]); ;
		double maxtabq = Qtable[cor[0]][cor[1]];

		//FORMULA Q------------# Q(s, a) += alpha * [R + gamma * Max Q(s+1, a+1) - Q(s, a) ]
		Qtable[x][y]=alpha * (recompensa + gamma * maxtabq - Qtable[x][y]);

        System.out.println("recompensa:"+recompensa+" rtiempo:"+rtiempo+" rmina:"+rmina+" rdistancia:"+rdistancia);
    }

    private boolean Elemento() {
       		int x = (int) getX()/100;
		int y = (int) getY()/100;

		if((x==8||x==9)&&y==7){
			fire(4);
			return true;
		}
		return false;
    }

    private void mover_a(double x, double y) {
	    /* Calcula la diferencia entre la pos actual y la posicion a la que se dirige. */
	    x = x - getX();
	    y = y - getY();	    
	    /* Calculate the angle relative to the current heading. */
	    double goAngle = Utils.normalRelativeAngle(Math.atan2(x, y) - getHeadingRadians());		
	    /*  */
	    setTurnRightRadians(Math.atan(Math.tan(goAngle)));		
	    /* */
	    setAhead(Math.cos(goAngle) * Math.hypot(x, y));
    }

    private int[] MaxValor(double[][] Qtable) {
        int[]retorno=new int[2];
        double maxValue = Qtable[0][0];
        for (int j = 0; j < Qtable.length; j++) {
            for (int i = 0; i < Qtable[j].length; i++) {
                if (Qtable[j][i] > maxValue) {
                    maxValue = Qtable[j][i];
                    retorno[0]=j;
                    retorno[1]=i;
                }
            }
        }
        return retorno;
    }

    private boolean Minas() {
       		//Coordenadas actuales		
		int x = (int) getX()/100;
		int y = (int) getY()/100;

		//Verificamos si existe minas
		if(minas[x][y]==1){
                        System.out.println("Encontro una mina en X:"+x+" Y:"+y);
			return true;
		}
		return false;
    }
    
}
