package client_java_core.core;

/***************************************************************************
 *                            AttivaTLCThread.java                         *
 *                    --------------------------                           *
 *   date          : Oct 19, 2005                                          *
 *   copyright     : (C) 2005 by Bticino S.p.A. Erba (CO) - Italy          *
 *                   Embedded Software Development Laboratory              *
 *   license       : GPL                                                   *
 *   email         :                                                       *
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
 * Description: Thread per l'invio di comandi per l'attivazione della telecamera
 * 
 * 
 */
public class AttivaTLCThread extends Thread{
    
    String open = null;
    
	/**
	 * Costruttore
	 * 
	 * @param comandoOpen Comando da inviare
	 */
	public AttivaTLCThread(String comandoOpen){
	  
		open = comandoOpen;
		
	}
	
	/**
	 * Avvia il Thread per l'invio di un comando open di attivazione telecamera
	 */
	public void run(){

	    PanelVideocontrollo.socketCom.invia(open);
    
	}
	
}