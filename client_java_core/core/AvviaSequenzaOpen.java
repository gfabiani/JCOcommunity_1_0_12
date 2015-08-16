package client_java_core.core;

/***************************************************************************
 * 			                AvviaSequenzaOpen.java                         *
 * 			              --------------------------                       *
 *   date          : Oct 5, 2004                                           *
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
 * Thread per inviare una sequenza di comandi open letti da file esterno
 * 
 */
public class AvviaSequenzaOpen extends Thread{

	String name = null;
	String pass = null;
	int passwordOpen = 0;
	Integer passInt = null;
	int sleepTime;
	String comando = null;
	
	/**   
	 * Costruttore
	 * 
	 * @param threadName Nome del Thread
	 * @param sleep Tempo di attesa tra l'invio di due comandi consecutivi
	 */
	public AvviaSequenzaOpen(String threadName, int sleep){
		name = threadName;
		sleepTime = sleep;
	}
	
	/**
	 * Avvio del Thread
	 */
	public void run(){
		comando = null;
		while(SequenzaOpen.statoSequenzaOpen != 0){ //condizione di stop o pausa
			comando = ClientFrame.sequenzaOpen.leggi();
			if(comando != null){
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(ClientFrame.gestSocketComandi.stato == 0){ //non sono ancora connesso
					if(ClientFrame.testCampiInseriti()){
						if(ClientFrame.abilitaPass.isSelected()){
							passInt = null;
							pass = ClientFrame.password.getText();
							passInt = new Integer(pass);
							passwordOpen = passInt.intValue(); //password in formato int
						}
						if(ClientFrame.gestSocketComandi.connect(ClientFrame.ip.getText(), 20000, passwordOpen)){
							ClientFrame.gestSocketComandi.invia(comando);												
						}else{
							//Connessione KO
						}
					}
				}else if(ClientFrame.gestSocketComandi.stato == 3){ //sono già connesso
					ClientFrame.gestSocketComandi.invia(comando);							
				}else{
					ClientFrame.scriviSulLog("%%%%%%%%%%%%% SITUAZIONE NON GESTITA %%%%%%%%%%%%%%%",2,0,0);
				}
			}else{
				ClientFrame.scriviSulLog("File terminato",1,0,0);
				break;
			}		
		}//chiude while
		
		//termino il thread durata
		if(ClientFrame.threadDurata != null) ClientFrame.threadDurata.interrupt();
	}

}
