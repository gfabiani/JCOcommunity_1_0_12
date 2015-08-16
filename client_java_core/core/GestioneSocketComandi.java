package client_java_core.core;

/***************************************************************************
 * 			              GestioneSocketComandi.java                       *
 * 			              --------------------------                       *
 *   date          : Jul 19, 2004                                          *
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
   
/**
 * Description:
 * Gestione della socket Comandi, apertura connessione, chiusura connessione, invio comando
 * 
 */
public class GestioneSocketComandi{
	
	/*    
	 * stato 0 = non connesso.
	 * stato 1 = inviata richiesta socket comandi, in attesa di risposta.
	 * stato 2 = inviato risultato sulle operazioni della password, attesa per ack o nack. Se la     
	 *           risposta Ã¨ ack si passa allo stato 3.
	 * stato 3 = connesso correttamente.
	 */
	
	GestionePassword gestPassword = null;
	
	static ReadThread readTh = null; //thread per la ricezione dei caratteri inviati dal webserver
	static NewThread timeoutThread = null; //thread per la gestione dei timeout
	int  stato = 0;  //stato socket comandi
	Socket socket = null;	
	static String responseLine = null; //stringa in ricezione dal Webserver
	static final String socketComandi = "*99*0##";
	static final String socketSuperComandi = "*99*9##";
	
	BufferedReader input = null;
	PrintWriter output = null;  
	OpenWebNet openWebNet = null;
	   
	/**
	 * Costruttore
	 *
	 */
	public GestioneSocketComandi(){
		gestPassword = new GestionePassword();
	}

	
	/**
	 * Tentativo di apertura socket comandi verso il webserver
	 * Diversi possibili stati:
	 * stato 0 = non connesso
	 * stato 1 = inviata richiesta socket comandi, in attesa di risposta
	 * stato 2 = inviato risultato sulle operazioni della password, attesa per ack o nack. Se la     
	 *           risposta Ã¨ ack si passa allo stato 3
	 * stato 3 = connesso correttamente
	 * 
	 * @param ip Ip del webserver al quale connettersi
	 * @param port Porta sulla quale aprire la connessione
	 * @param passwordOpen Password open del webserver
	 * @return true Se la connessione va a buon fine, false altrimenti
	 */
	public boolean connect(String ip, int port, long passwordOpen){ 
		try{
			ClientFrame.scriviSulLog("Tentativo connessione a "+ ip +"  Port: "+ port,1,0,0);
			socket = new Socket(ip, port);
			setTimeout(0);			
			input= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			ClientFrame.scriviSulLog("Buffer reader creato",2,0,0);
			output = new PrintWriter(socket.getOutputStream(),true);
			ClientFrame.scriviSulLog("Print Writer creato",2,0,0);
		}catch (IOException e){
			ClientFrame.scriviSulLog("Impossibile connettersi!",0,1,1);
			this.close();
			//e.printStackTrace();	
		}
		
		if(socket != null){
			while(true){
				readTh = null;
				readTh = new ReadThread(socket,input,0);
				readTh.start();
				try{
					readTh.join();
				}catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					ClientFrame.scriviSulLog("----- ERRORE readThread.join() durante la connect:",2,1,0);
					e1.printStackTrace();
				}
				
				if(responseLine != null){
		    		if (stato == 0){ //ho mandato la richiesta di connessione
			        	ClientFrame.scriviSulLog("\n----- STATO 0 ----- ",2,0,0);
			        	ClientFrame.scriviSulLog("Rx: " + responseLine,1,0,0);
			            if (responseLine.equals(OpenWebNet.MSG_OPEN_OK)) {
			            	if(ClientFrame.superSocket.isSelected()){
			            		ClientFrame.scriviSulLog("Tx: "+socketSuperComandi,1,0,0);
			            		output.write(socketSuperComandi); //super comandi
			            	}
			            	else{
			            		ClientFrame.scriviSulLog("Tx: "+socketComandi,1,0,0);
			            		output.write(socketComandi); //comandi
			            	}
			            	output.flush();  
			            	stato = 1; 
			            	setTimeout(0);
			            }else{
			            	//se non mi connetto chiudo la socket
			                ClientFrame.scriviSulLog("Chiudo la socket verso il server " + ip,1,0,0);
			                this.close();
			            	break;
			            }
					}else if (stato == 1) { //ho mandato il tipo di servizio richiesto
						ClientFrame.scriviSulLog("\n----- STATO 1 -----", 2, 0, 0);
						ClientFrame.scriviSulLog("Rx: " + responseLine, 1, 0, 0);

						if (ClientFrame.abilitaPass.isSelected()) {
							//applico algoritmo di conversione
							ClientFrame.scriviSulLog("Controllo sulla password", 1, 0, 0);
							long risultato = gestPassword.applicaAlgoritmo(passwordOpen, responseLine);
							ClientFrame.scriviSulLog("Tx: " + "*#" + risultato + "##", 1, 0, 0);
							output.write("*#" + risultato + "##");
							output.flush();
							stato = 2; //setto stato dopo l'autenticazione
							setTimeout(0);
						} else {
							//non devo fare il controllo della password
							ClientFrame.scriviSulLog("NON effettuo il controllo sulla password - mi aspetto ACK", 1, 0, 0);
							if (responseLine.equals(OpenWebNet.MSG_OPEN_OK)) {
								ClientFrame.scriviSulLog("Ricevuto ack, stato = 3", 1, 0, 0);
								stato = 3;
								break;
							} else {
								ClientFrame.scriviSulLog("Impossibile connettersi!!", 0, 1, 1);
								//se non mi connetto chiudo la socket
								ClientFrame.scriviSulLog("Chiudo la socket verso il server " + ip, 2, 0, 0);
								this.close();
								break;
							}
						}
					} else if (stato == 2) {
						ClientFrame.scriviSulLog("\n----- STATO 2 -----", 2, 0, 0);
						ClientFrame.scriviSulLog("Rx: " + responseLine, 1, 0, 0);
						if (responseLine.equals(OpenWebNet.MSG_OPEN_OK)) {
							ClientFrame.scriviSulLog("Connessione OK", 1, 0, 0);
							stato = 3;
							break;
						} else {
							ClientFrame.scriviSulLog("Impossibile connettersi!!", 0, 1, 1);
							//se non mi connetto chiudo la socket
							ClientFrame.scriviSulLog("Chiudo la socket verso il server " + ip, 2, 0, 0);
							this.close();
							break;
						}
					} else break; //non dovrebbe servire (quando passo per lo stato tre esco dal ciclo con break)
		    	}else{
		    		ClientFrame.scriviSulLog("--- Risposta dal webserver NULL",2,0,0);
		    		this.close();
		    		break;//ramo else di if(responseLine != null)
		    	}
		    }//chiude while(true)
		}else{
			
		}
		
		if (stato == 3) return true;
		else return false;
	}//chiude connect()
	
	
	/**
	 * Chiude la socket comandi ed imposta stato = 0
	 *
	 */
	public void close(){
		if(socket != null){
			try { 
				socket.close();
				socket = null;
				stato = 0;
				ClientFrame.scriviSulLog("-----Socket chiusa correttamente-----",1,0,0);				
			} catch (IOException e) {				
				// TODO Auto-generated catch block
				System.out.println("Errore Socket: <GestioneSocketComandi>");
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Metodo per l'invio di un comando open
	 * 
	 * @param comandoOpen comando da inviare
	 * @return 0 se il comando vine inviato, 1 se non Ã¨ possibile inviare il comando
	 */
	public int invia(String comandoOpen){
		//creo l'oggetto openWebNet con il comandoOpen
		try{
			openWebNet = new OpenWebNet(comandoOpen);
			if(openWebNet.isErrorFrame()){
				ClientFrame.scriviSulLog("ERRATA frame open "+comandoOpen+", la invio comunque!!!",1,0,0);
			}else{
				ClientFrame.scriviSulLog("CREATO oggetto OpenWebNet "+openWebNet.getFrameOpen(),1,0,0);
			}
		}catch(Exception e){
			ClientFrame.scriviSulLog("ERRORE nella creazione dell'oggetto OpenWebNet "+comandoOpen,1,0,0);
			System.out.println("Eccezione in GestioneSocketComandi durante la creazione del'oggetto OpenWebNet");
			e.printStackTrace();
		}

		ClientFrame.scriviSulLog("Tx: "+comandoOpen,0,0,1);
		output.write(comandoOpen);
		output.flush();
		
		do{ 
			setTimeout(0);
			readTh = null;
			readTh = new ReadThread(socket,input,0);
			readTh.start();
			try{
				readTh.join();
			}catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				ClientFrame.scriviSulLog("----- ERRORE readThread.join() durante l'invio comando:",2,1,0);
				e1.printStackTrace();
			}
			
	    	if(responseLine != null){
	        	if (responseLine.equals(OpenWebNet.MSG_OPEN_OK)){
	        		ClientFrame.scriviSulLog("Rx: " + responseLine,0,0,1);
	        		ClientFrame.scriviSulLog("Comando inviato correttamente",0,0,0);
	        		if(!ClientFrame.mantieniSocket.isSelected()) this.close();
	        		return 0;
	        		//break;
	        	}else if(responseLine.equals(OpenWebNet.MSG_OPEN_KO)){
	        		ClientFrame.scriviSulLog("Rx: " + responseLine,0,0,1);
	        		ClientFrame.scriviSulLog("Comando NON inviato correttamente",0,1,0);
	        		if(!ClientFrame.mantieniSocket.isSelected()) this.close();
	        		return 0;
	        		//break;
	        	}else{
	        		//RICHIESTA STATO
	        		ClientFrame.scriviSulLog("Rx: " + responseLine,0,0,1);
	        		if(responseLine == OpenWebNet.MSG_OPEN_OK){
	        			ClientFrame.scriviSulLog("Comando inviato correttamente",0,0,0);
		        		if(!ClientFrame.mantieniSocket.isSelected()) this.close();
		        		return 0;
		        		//break;
	        		}else if(responseLine == OpenWebNet.MSG_OPEN_KO){
	        			ClientFrame.scriviSulLog("Comando NON inviato correttamente",0,1,0);
		        		if(!ClientFrame.mantieniSocket.isSelected()) this.close();
		        		return 0;
		        		//break;
	        		}
	        	}
	    	}else{
	    		ClientFrame.scriviSulLog("Impossibile inviare il comando",0,1,1);
	    		if(!ClientFrame.mantieniSocket.isSelected()) this.close();
	    		return 1;
	    		//break;
	    	}
		}while(true); 
	}//chiude metodo invia(...)
		
	
	/**
	 * Attiva il thread per il timeout sulla risposta inviata dal WebServer.
	 * 
	 * @param tipoSocket: 0 se Ã¨ socket comandi, 1 se Ã¨ socket monitor
	 */
	public void setTimeout(int tipoSocket){
		timeoutThread = null;
		timeoutThread = new NewThread("timeout",tipoSocket);
		timeoutThread.start();
	}	
	
}//chiuse la classe
