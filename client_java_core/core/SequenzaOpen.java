package client_java_core.core;

/***************************************************************************
 * 			                  SequenzaOpen.java                            *
 * 			              --------------------------                       *
 *   date          : Sep 29, 2004                                          *
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Description:
 * Gestisce il caricamento e la lettura di un file contenente la sequenza open da inviare
 * 
 */
public class SequenzaOpen {
	
	//variabili
	FileDialog fileDialog = null;
	File file = null;
	String directoryFile = null;
	static File tempFile = null; //contiene la lista di comandi da inviare dopo la precompilazione del file
	FileWriter writerTempFile = null;
	BufferedReader readerTempFile = null; 

	static int statoSequenzaOpen = 0; //0 se non è abilitato l'avvio della sequenza di comandi, 1 altrimenti
	
	/**
	 * Costruttore: crea file dialog per selezionare il file con la sequenza di comandi da inviare
	 *
	 */
	public SequenzaOpen(){
		fileDialog = new FileDialog(new Frame(), "Seleziona file con sequenza comandi",FileDialog.LOAD);
		fileDialog.setVisible(true);
		if(fileDialog.getFile() != null){	
			file = new File(fileDialog.getDirectory(), fileDialog.getFile());
			directoryFile = fileDialog.getDirectory();
			ClientFrame.sequenzaTextField.setText(directoryFile + fileDialog.getFile());
			fileDialog = null;
			//creo il file temporaneo
			try {
				tempFile = File.createTempFile("tmp","txt");
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} 
			
			try {
				writerTempFile = new FileWriter(tempFile);
				readerTempFile = new BufferedReader(new FileReader(tempFile));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				precompilazioneFile(file);
				statoSequenzaOpen = 1;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}else{
			ClientFrame.sequenzaOpen = null;
		}
	}
	
	/**
	 * Legge il comando dal file, automaticamente ad una successiva
	 * chiamata di questa funzione viene richiamato il comando successivo
	 *   
	 * @return Comando letto dal file
	 */
	public String leggi(){
		
		String comando = null;

		try {
			comando = readerTempFile.readLine();
			//System.out.println("comando: "+comando);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(comando == null){
			if(ClientFrame.cicloSequenza.isSelected()){ //resetto la lettura del file dal primo comando
				try {
					readerTempFile.close();
					readerTempFile = new BufferedReader(new FileReader(tempFile));
					//leggo il primo comando
					comando = readerTempFile.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				//riattivo i pulsanti 
				ClientFrame.inviaComando.setEnabled(true);
				ClientFrame.cicloComando.setEnabled(true);
				ClientFrame.stopComando.setEnabled(true);
				ClientFrame.pauseSequenza.setEnabled(true);
				ClientFrame.stopSequenza.setEnabled(true);
				ClientFrame.avviaSequenza.setEnabled(true);
				this.stop();
			}
		}
		return comando;
	}

	/**
	 * Interrompe la sequenza per l'invio dei comandi open, non viene riazzerato
	 * il puntatore all'elenco dei comandi, un successo riavvio della sequenza invierebbe 
	 * il comando successivo a quello in cui si era fermata la sequenza
	 *
	 */
	public void pause(){
		statoSequenzaOpen = 0;		
	}
	
	/**
	 * Interrompe la sequenza per l'invio dei comandi open e riazzerra il puntatore 
	 * all'elenco dei comandi, un successo riavvio della sequenza invierebbe il 
	 * primo comando della lista
	 *
	 */
	public void stop(){
		statoSequenzaOpen = 0;
		try {
			readerTempFile.close();
			readerTempFile = new BufferedReader(new FileReader(tempFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Elimina i commenti ed espande tutte le graffe del file in ingresso
	 * 
	 * @param file1 File File in ingresso da precompilare
	 * @throws IOException
	 */
	public void precompilazioneFile(File file1) throws IOException{
		BufferedReader bufferReader = null;
		ArrayList list = new ArrayList(); //contiene tutti i comandi corretti ricavati da un comando con []
		ArrayList list1 = new ArrayList();
		ArrayList listPrima = new ArrayList();
		ArrayList listDopo = new ArrayList();
		boolean riinizia = true;
		String riga;
		
		bufferReader = new BufferedReader(new FileReader(file1));
		
		while(true){
			riinizia = true;
			riga = bufferReader.readLine();
			if(riga == null) break; //ho raggiunto la fine del file
			if(!riga.startsWith("//")){
				if(!contieneQuadre(riga)){ //non contiene quadre --> scrivi il comando in tempFile
					writerTempFile.write(riga+"\n");
				}else{ //contiene quadre --> espandi e scrivi i comandi in tempFile
					list = espandi(riga);
					while(riinizia){
						list1.clear();
						listPrima.clear();
						listDopo.clear();
						int i = 0;
						for (i = 0; i < list.size(); i++) {
							String temp = (String) list.get(i);
							for (int j = 0; j < temp.length(); j++) {
								if(temp.charAt(j) == '['){
									list1 = espandi(temp);						
									//shiftare tutto e riiniziare il while
									for (int k = 0; k < i; k++) {
										listPrima.add(list.get(k));							
									}
									for (int k = i+1; k < list.size(); k++) {
										listDopo.add(list.get(k));						
									}
									list.clear();
									for (int k = 0; k < listPrima.size(); k++) {
										list.add(listPrima.get(k));
									}
									for (int k = 0; k < list1.size(); k++) {
										list.add(list1.get(k));
									}
									for (int k = 0; k < listDopo.size(); k++) {
										list.add(listDopo.get(k));
									}
									riinizia = true;
									break;
								}else{
									riinizia = false;
								}
							}
							if(riinizia == true) break;
						}
						if(riinizia == false) break;
					} // chiude while(riinizia)				
//					scrivo i comandi presenti in list in tempFile
					for (int i = 0; i < list.size(); i++) {
						writerTempFile.write(list.get(i)+"\n");
					}	
				
				} 	
			}else{ //la riga è un commento
				
			}
		}
		
		writerTempFile.close();

		//stampa tutti i comando da inviare
//		while(true){
//			String ciao = readerTempFile.readLine();
//			System.out.println("Comando: "+ciao);
//			if(ciao ==  null) break;
//		}
	}

	/**
	 * Verifica se all'interno di una stringa ci sono delle parentesi quadre aperte [
	 * 
	 * @param temp Stringa da analizzare
	 * @return True se la stringa contiene almeno una quadra, False altrimenti
	 */
	public boolean contieneQuadre(String temp) {
		for (int j = 0; j < temp.length(); j++) {
			if(temp.charAt(j) == '['){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Espande le quadre trovate in una stringa con tutti i comandi
	 * 
	 * @param stringa Stringa da analizzare
	 * @return Nuova lista di comandi
	 */
	public ArrayList espandi(String stringa){
		ArrayList lista = new ArrayList();
		int numInizio = -1;
		int numFine = -1;
		int indiceInizio = -1;
		int indiceFine = -1;
		Integer numeroInt = null;
		
		for (int i = 0; i < stringa.length(); i++) {
			if(stringa.charAt(i) == '['){			
				indiceInizio = i;
				// [numInizio-numFine] extract the first number and save it in numInizio
				if(stringa.charAt(i+2) == '-'){ //the first number is composed by a single number
					String numero = stringa.substring(i+1,i+2);
					numeroInt = new Integer(numero);
					numInizio = numeroInt.intValue();
				}else{ //the first number is composed by two numbers
					String numero = stringa.substring(i+1,i+3);
					numeroInt = new Integer(numero);
					numInizio = numeroInt.intValue();
				} 
			}else if(stringa.charAt(i) == ']'){
				indiceFine = i;
				//extract the second number and save it in numFine
				if(stringa.charAt(i-2) == '-'){ //the second number is composed by a single number
					String numero = stringa.substring(i-1,i);
					numeroInt = new Integer(numero);
					numFine = numeroInt.intValue();
				}else{ //the second number is composed by two numbers
					String numero = stringa.substring(i-2,i);
					numeroInt = new Integer(numero);
					numFine = numeroInt.intValue();
				}
				
				//ho trovato sia numInizio che numFine, espando le [...]
				String prima = stringa.substring(0,indiceInizio);
				String dopo = stringa.substring(indiceFine+1,stringa.length());
				for (int j = numInizio; j <= numFine; j++) {
					lista.add(prima+j+dopo);						
				}
				break;
			}
		}//chiude il for
		
		return lista;
	}
	
}
