package client_java_core.core;

/***************************************************************************
 * 			                  ThreadDurata.java                            *
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
 * Gestisce le sleep durante i cicli di sia su comando singolo sia su sequenza open
 * 
 */
public class ThreadDurata extends Thread{
	
	String name = null;
	SequenzaOpen sequenzaOpen = null;
	int tipo; //0 se è lanciato dal pannello per il ciclo di un comando, 1 se si tratta di una sequenza
			  //di open
	int durataTime;
	
	/**
	 * Costruttore
	 * 
	 * @param threadName Nome del Thread
	 * @param tipoInvio Vale 0 se è lanciato dal pannello per il ciclo di un comando, 1 se si tratta di una sequenza di open 
	 * @param durata Tempo di attesa tra l'invio di un comando e il successivo
	 */
	public ThreadDurata(String threadName, int tipoInvio, int durata){
		name = threadName;
		tipo = tipoInvio;
		durataTime = durata;
	}
	
	/**
	 * Avvia il Thread
	 */
	public void run(){		
		if(durataTime != -1){
			do{
				try {
					Thread.sleep(durataTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					break;
					
				}
				ClientFrame.scriviSulLog("Durata max test terminata",1,0,0);
				ClientFrame.inviaComando.setEnabled(true);
				ClientFrame.cicloComando.setEnabled(true);
				ClientFrame.avviaSequenza.setEnabled(true);
				ClientFrame.pauseSequenza.setEnabled(true);
				ClientFrame.stopSequenza.setEnabled(true);
				if(tipo == 0){
					if(ClientFrame.avviaCicloComando != null) AvviaCicloComando.statoCiclo = 0;
					break;
				}else{
					if(ClientFrame.avviaSequenzaOpen != null) ClientFrame.sequenzaOpen.stop();
					break;
				}
			}while(true);
		}
		
		name = null;
		sequenzaOpen = null;
	}

}
