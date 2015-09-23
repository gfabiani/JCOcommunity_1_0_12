package client_java_core.core;

/***************************************************************************
 * 			              GestioneSocketMonitor.java                       *
 * 			              --------------------------                       *
 *   date          : Sep 6, 2004                                          *
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bticino.GestionePassword;
import com.bticino.openwebnet.OpenWebNetUtils;

/**
 * Description:
 * Gestione della socket Monitor, apertura monitor, chiusura monitor
 * 
 */
public class GestioneSocketMonitor{

	GestionePassword gestPassword = null;
	
	static final String socketMonitor = "*99*1##";
	static ReadThread readThMon = null; //thread per la ricezione dei caratteri inviati dal webserver
	static NewThread timeoutThreadMon = null; //thread per la gestione dei timeout
	static int  statoMonitor = 0;  //stato socket monitor
	static Socket socketMon = null;	
	static String responseLineMon = null; //stringa in ricezione dal Webserver
	BufferedReader inputMon = null;
	PrintWriter outputMon = null;
	Monitorizza monThread = null;
	
	
	/**
	 * Costruttore
	 *
	 */
	public GestioneSocketMonitor(){ 
		gestPassword = new GestionePassword();
	}
	
	
	/**
	 * Tentativo di apertura socket monitor verso il webserver
	 * 
	 * @param ip Ip del webserver al quale connettersi
	 * @param port porta sulla quale aprire la connessione
	 * @param passwordOpen password open del webserver
	 * @return true se la connessione va a buon fine, false altrimenti
	 */
	public boolean connect(String ip, int port, int passwordOpen){ //tipo rappresenta socket comandi o monitor
		try {
			ClientFrame.scriviSulLog("Mon: Tentativo connessione a "+ ip +"  Port: "+ port,1,0,0);
			socketMon = new Socket(ip, port);
			setTimeout(1);		  	
			inputMon= new BufferedReader(new InputStreamReader(socketMon.getInputStream()));
			ClientFrame.scriviSulLog("Mon: Buffer reader creato",2,0,0);
			outputMon = new PrintWriter(socketMon.getOutputStream(),true);
			ClientFrame.scriviSulLog("Mon: Print Writer creato",2,0,0);
		}catch (IOException e){
			ClientFrame.scriviSulLog("Mon: Impossibile connettersi con host "+ip+"\n",1,1,0);
			this.close();
			//e.printStackTrace();	
		}
		
		if(socketMon != null){
		    while(true){	    	    
		    	readThMon = null;
				readThMon = new ReadThread(socketMon,inputMon,1);
				readThMon.start();
				try{
					readThMon.join();
				}catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					ClientFrame.scriviSulLog("Mon: ----- ERRORE readThread.join() durante la connect:",2,1,0);
					e1.printStackTrace();
				}
				
				if(responseLineMon != null){
		    		if (statoMonitor == 0 ){
			        	ClientFrame.scriviSulLog("\nMon: ----- STATO 0 ----- ",2,0,0);
			        	ClientFrame.scriviSulLog("Mon: Rx: " + responseLineMon,1,0,0);
			            if (responseLineMon.equals(OpenWebNet.MSG_OPEN_OK)) {
			            	ClientFrame.scriviSulLog("Mon: Tx: "+socketMonitor,1,0,0);
			            	outputMon.write(socketMonitor); //comandi
			            	outputMon.flush();
			            	statoMonitor = 1; //setto stato autenticazione
			            	setTimeout(1);
			            }else{
			            	//se non mi connetto chiudo la socket
			                ClientFrame.scriviSulLog("Mon: Chiudo la socket verso il server ",1,0,0);
			                this.close();
			            	break; 
			            }
					} else if (statoMonitor == 1){ 
						ClientFrame.scriviSulLog("\nMon: ----- STATO 1 -----",2,0,0);
						ClientFrame.scriviSulLog("Mon: Rx: " + responseLineMon,1,0,0);						 
						if(ClientFrame.abilitaPass.isSelected()){
					    	//applico algoritmo di conversione
							ClientFrame.scriviSulLog("Controllo sulla password", 1, 0, 0);
							//long risultato = gestPassword.applicaAlgoritmo(passwordOpen, responseLine);
							Long seed = Long.valueOf(responseLineMon.substring(2, responseLineMon.length() - 2));
							ClientFrame.scriviSulLog("Tx: " + "seed=" + seed, 1, 0, 0);
							Long risultato = OpenWebNetUtils.passwordFromSeed(seed, passwordOpen);
							ClientFrame.scriviSulLog("Tx: " + "*#" + risultato + "##", 1, 0, 0);
							outputMon.write("*#" + risultato + "##");
					    	outputMon.flush();
					    	statoMonitor = 2; //setto stato dopo l'autenticazione 
				        	setTimeout(1);
						}else{
			        		//non devo fare il controllo della password
							ClientFrame.scriviSulLog("Mon: NON effettuo il controllo sulla password - mi aspetto ACK",2,0,0);
			        		if(responseLineMon.equals(OpenWebNet.MSG_OPEN_OK)){
			        			ClientFrame.scriviSulLog("Mon: Ricevuto ack, statoMonitor = 3",2,0,0);
			        			statoMonitor = 3;
			        			ClientFrame.scriviSulLog("Mon: Monitor attivata con successo",1,0,0);
			        			break;
			        		}
			        		else{
			        			ClientFrame.scriviSulLog("Mon: Impossibile connettersi!!",0,1,1);
				               	//se non mi connetto chiudo la socket
				               	ClientFrame.scriviSulLog("Mon: Chiudo la socket verso il server " + ip,2,0,0);
				               	this.close();
				               	break;
			        		}
			        	}
				    } else if(statoMonitor == 2){
				    	ClientFrame.scriviSulLog("\nMon: ----- STATO 2 -----",2,0,0);
				    	ClientFrame.scriviSulLog("Mon: Rx: " + responseLineMon,1,0,0);
				    	if (responseLineMon.equals(OpenWebNet.MSG_OPEN_OK)) {
			               	ClientFrame.scriviSulLog("Mon: Monitor attivata con successo",1,0,0);
			               	statoMonitor = 3;
			               	break;
			            }else{
			            	ClientFrame.scriviSulLog("Mon: Impossibile attivare la monitor",1,1,0);
			               	//se non mi connetto chiudo la socket
			               	ClientFrame.scriviSulLog("Mon: Chiudo la socket monitor\n",2,0,0);
			               	this.close();
			               	break;			               	
			            }
				    } else break; //non dovrebbe servire (quando passo per lo stato tre esco dal ciclo con break)
		    	}else{
		    		ClientFrame.scriviSulLog("Mon: Risposta dal webserver NULL",2,0,0);
		    		this.close();
		    		break;//ramo else della funzione riceviStringa()
		    	}
		    }//chiude while(true)
		}else{
			//System.out.println("$$$$$$$");
		}
		
		if(statoMonitor == 3){
			monThread = null;
			monThread = new Monitorizza(socketMon, inputMon);
			monThread.start();		
		}   
		
		if (statoMonitor == 3) return true;
		else return false;
		
	}//chiude connect()
	
	
	/**
	 * Chiude la socket monitor ed imposta statoMonitor = 0
	 *
	 */
	public void close(){
		if(socketMon != null){
			try { 
				socketMon.close();
				socketMon = null;
				statoMonitor = 0;
				ClientFrame.scriviSulLog("MON: Socket monitor chiusa correttamente-----\n",1,0,0);				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("MON: Errore chiusura Socket: <GestioneSocketMonitor>");
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Attiva il thread per il timeout sulla risposta inviata dal WebServer.
	 * 
	 * @param tipoSocket: 0 se è socket comandi, 1 se è socket monitor
	 */
	public void setTimeout(int tipoSocket){
		timeoutThreadMon = null;
		timeoutThreadMon = new NewThread("timeout",tipoSocket);
		timeoutThreadMon.start();
	}
	
}




