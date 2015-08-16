package client_java_core.core;

/***************************************************************************
 * 			                   NewThread.java                              *
 * 			              --------------------------                       *
 *   date          : Jul 13, 2004                                          *
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
 * Gestisce i timeout durante la procedura di connessione al WebServer 
 * e l'invio dei comandi open
 * 
 */
public class NewThread extends Thread{
	
	String name;
	int time = 0; //rappresenta lo sleep del thread (15 sec o 30 sec)
	int statoEntrata = 0;
	int tipoSocket; // 0 se la socket è di tipo comandi, 1 se è di tipo monitor
	
	/**
	 * Costruttore
	 * 
	 * @param threadName Nome del Thread
	 * @param numSocket Tipo di socket che richiama il costruttore, 0 se è socket comandi, 1 se è monitor
	 */
	public NewThread (String threadName, int numSocket){
		name = threadName;
		tipoSocket = numSocket;
		if(tipoSocket == 0) statoEntrata = ClientFrame.gestSocketComandi.stato;
		else statoEntrata = GestioneSocketMonitor.statoMonitor;
		ClientFrame.scriviSulLog("Thread per il timeout attivato",2,0,0);
	}
	
	/**
	 * Avvia il Thread per gestire il timeout
	 */
	public void run(){
		do{
			time = 30000;
			
			try{
				Thread.sleep(time);
			}catch (InterruptedException e){
				ClientFrame.scriviSulLog("Thread timeout interrotto!",2,0,0);
				break;
				//e.printStackTrace();
			}
	
			ClientFrame.scriviSulLog("Thread timeout SCADUTO!",2,0,0);
			//chiudo il thread per la ricezione dei caratteri
			if(tipoSocket == 0){
				if (GestioneSocketComandi.readTh != null) GestioneSocketComandi.readTh.interrupt();
			}else{
				if (GestioneSocketMonitor.readThMon != null) GestioneSocketMonitor.readThMon.interrupt();
			}
			break;
		}while(true);
	}
}
