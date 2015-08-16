package client_java_core.core;

/***************************************************************************
 *                     InviaComandoThread.java                             *
 *                    --------------------------                           *
 *   date          : Sep 27, 2005                                          *
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
 * Description: Thread che si occupa di gestire l'invio di un comando open
 * 				tramite la classe GestioneSocketComandi
 * 
 * 
 */
public class InviaComandoThread extends Thread{
    
    String open = null;
    
	/**
	 * Costruttore
	 * 
	 * @param comandoOpen Comando da inviare
	 */
	public InviaComandoThread(String comandoOpen){
	   
		open = comandoOpen;
		
    }
   
	/**
	 * Avvia il Thread per l'invio di un comando open, al termine riabilita i pulsanti per 
	 * l'invio di un nuovo comando open
	 */
    public void run(){
    
        ClientFrame.gestSocketComandi.invia(open);
        
        ClientFrame.abilitaInterfaccia(true);
        
    }

}