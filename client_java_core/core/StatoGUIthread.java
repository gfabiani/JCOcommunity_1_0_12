package client_java_core.core;

/***************************************************************************
 * 			                 StatoGUIthread.java                           *
 * 			              --------------------------                       *
 *   date          : Jul 26, 2004                                          *
 *   copyright     : (C) 2005 by Bticino S.p.A. Erba (CO) - Italy 	       *
 *   				 Embedded Software Development Laboratory              *
 *   license       : GPL                                                   *
 *   email         : 		             				                   *
 *   web site      : www.bticino.it; www.myhome-bticino.it                 *
 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/

/**
 * Description:
 * Richiama la funzione di garbage collector ogni 10 secondi 
 * 
 */
public class StatoGUIthread  extends Thread{
	String name;
	
	StatoGUIthread(String threadName){
		name = threadName;
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.gc();
		}
	}	
}
