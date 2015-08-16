package client_java_core.core;

/***************************************************************************
 * 			                AvviaCicloComando.java                         *
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
 * Thread per inviare a ciclo un comando open   
 * 
 */
public class AvviaCicloComando extends Thread{
	
	String name = null;
	String comando = null;
	int passwordOpen = 0;
	Integer passInt = null;
	String pass = null;
	int sleepTime;
	static int statoCiclo = 0; //ciclo non attivo, se viene attivato diventa uno
	
	/**
	 * Costruttore
	 * 
	 * @param threadName Nome del Thread
	 * @param comandoOpen Comando open da inviare in sequenza
	 * @param sleep Tempo di attesa tra l'invio di due comandi consecutivi
	 */
	public AvviaCicloComando(String threadName, String comandoOpen, int sleep){
		name = threadName;
		comando = comandoOpen;
		sleepTime = sleep;
	}
	
	/**
	 * Avvio del Thread
	 */
	public void run(){
		while(statoCiclo != 0){ //fino a che non viene premuto il pulsante stop resta nel ciclo
			if(comando != null){
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("Thread interrotto: <AvviaCicloComando>");
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
				ClientFrame.scriviSulLog("Comando null",2,0,0);
			}				 
		}
		
		//termino il thread durata
		if(ClientFrame.threadDurata != null) ClientFrame.threadDurata.interrupt();
	}

}

