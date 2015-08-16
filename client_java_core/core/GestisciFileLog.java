package client_java_core.core;

/***************************************************************************
 * 			                 GestisciFileLog.java                          *
 * 			              --------------------------                       *
 *   date          : Sep 21, 2004                                          *
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

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Description:
 * Gestisce la creazione del file di Log tra cui, la sua attivazione, disattivazione 
 * e scrittura
 * 
 */
public class GestisciFileLog {
	
	//varibili
	FileDialog fileDialog;
	FileWriter fileWriter = null;
	File file = null;
	String df = null;
	
	/**
	 * Costruttore: crea file dialog per selezionare il file
	 *
	 */
	public GestisciFileLog(){
		fileDialog = new FileDialog(new Frame(), "Seleziona file di log",FileDialog.LOAD);
		fileDialog.setVisible(true);
		if(fileDialog.getFile() != null){	
			file = new File(fileDialog.getDirectory(), fileDialog.getFile());
			ClientFrame.fileTextField.setText(fileDialog.getDirectory() + fileDialog.getFile());
		}
		fileDialog = null;
	}
	
	/**
	 * Attiva la memorizzazione di stringhe sul file di log
	 * 
	 * @return True se il file di Log è stato attivato correttamnte, False altrimenti
	 */
	public boolean attiva(){
		if(file != null){
			try {
				if(ClientFrame.appendi.isSelected()) fileWriter = new FileWriter(file,true);
				else fileWriter = new FileWriter(file);
				
				df = ClientFrame.miaData.returnDate();
				fileWriter.write("\nFILE DI LOG ATTIVATO IL "+df+"\n\n");
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ClientFrame.scriviSulLog("Impossibile creare fileWriter",1,1,0);
				e.printStackTrace();
				return false;
			}
		}else{
			ClientFrame.scriviSulLog("Nessun file è stato selezionato come log",1,1,0);
			return false;
		}
		
	}
	
	
	/**
	 * 
	 * @param dataOra Data e ora
	 * @param ip Ip del WebServer
	 * @param pass Password open del WebServer
	 * @param flag Indica se i campi "mantieniSocket e attivaMonitor" nell'interfaccia grafica sono fleggati
	 * @param testo Rappresenta la stringa da scrivere
	 */
	public boolean scrivi(String dataOra, String ip, String pass, String flag, String testo){
		if (fileWriter != null) {
			try {
				fileWriter.write(dataOra+"  "+ip+"  "+pass+"  "+flag+" --> "+testo+"\n");
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	/**
	 * Termina la memorizzazione sul file di log
	 *
	 */
	public boolean interrompi(){
		if (fileWriter != null) {
			try {
				df = ClientFrame.miaData.returnDate();
				fileWriter.write("\nFILE DI LOG INTERROTTO DALL'UTENTE IL "+df+"\n");
				fileWriter.close();
				fileWriter = null;
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}
}
