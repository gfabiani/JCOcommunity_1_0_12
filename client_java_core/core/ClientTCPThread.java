package client_java_core.core;

/***************************************************************************
 * 			                ClientTCPThread.java                           *
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
 * 
 * 
 */   
public class ClientTCPThread { //ClientTCPThread
	
	/**
	 * Effettua il controllo sulla versione della Java Runtime Environment utilizzata,
	 * se va a buon fine lancia la classe MainFrameClient.java
	 */
	public static void main(String[] args) {
		/*
		 * TEST JAVA VIRTUAL MACHINE UTILIZZATA
		 */
		 String version = System.getProperty("java.specification.version");
		 //System.out.println("Versione java virtual machine: "+version);
		 if(version.startsWith("1.4") || version.startsWith("1.5")){
		 	//java virtual machine corretta
		 }else{
		 	//System.err.println("Impossibile avviare il programma! Aggiornare l'attuale java virtual machine "+version+" alla versione 1.4.* o 1.5.*");
		 	System.err.println("ATTENZIONE! L'attuale java virtual machine "+version+" potrebbe non consentire un corretto funzionamento del programma," +
		 			"si consiglia di aggiornarla alla versione 1.4.* o 1.5.*");
		 	//System.exit(1);
		 }
		   
		/*    
		 *Test classe OpenWebNet
		 * 
		 */
//		OpenWebNet openWebNet = new OpenWebNet("*99*1##");
//		System.out.println("frame: "+openWebNet.getFrameOpen());
//		System.out.println("lungezza frame: "+openWebNet.getLengthFrameOpen());
//		System.out.println("tipo frame: "+openWebNet.getTipoFrame());
//		System.out.println("chi: "+openWebNet.getChi());
//		System.out.println("cosa: "+openWebNet.getCosa());
//		System.out.println("dove: "+openWebNet.getDove());
//		System.out.println("quando: "+openWebNet.getQuando());
//		System.out.println("grandezza: "+openWebNet.getGrandezza());
//		System.out.println("livello:"+openWebNet.getLivello());
//		System.out.println("interfaccia: "+openWebNet.getInterfaccia());
//		System.out.println("**********");
//		openWebNet = new OpenWebNet("*#1*0##");
//		System.out.println("frame: "+openWebNet.getFrameOpen());
//		System.out.println("lungezza frame: "+openWebNet.getLengthFrameOpen());
//		System.out.println("tipo frame: "+openWebNet.getTipoFrame());
//		System.out.println("chi: "+openWebNet.getChi());
//		System.out.println("cosa: "+openWebNet.getCosa());
//		System.out.println("dove: "+openWebNet.getDove());
//		System.out.println("quando: "+openWebNet.getQuando());
//		System.out.println("grandezza: "+openWebNet.getGrandezza());
//		System.out.println("livello:"+openWebNet.getLivello());
//		System.out.println("interfaccia: "+openWebNet.getInterfaccia());
//		System.out.println("**********");
//		openWebNet = new OpenWebNet("*1*0*11##");
//		System.out.println("frame: "+openWebNet.getFrameOpen());
//		System.out.println("lungezza frame: "+openWebNet.getLengthFrameOpen());
//		System.out.println("tipo frame: "+openWebNet.getTipoFrame());
//		System.out.println("chi: "+openWebNet.getChi());
//		System.out.println("cosa: "+openWebNet.getCosa());
//		System.out.println("dove: "+openWebNet.getDove());
//		System.out.println("quando: "+openWebNet.getQuando());
//		System.out.println("grandezza: "+openWebNet.getGrandezza());
//		System.out.println("livello:"+openWebNet.getLivello());
//		System.out.println("interfaccia: "+openWebNet.getInterfaccia());
//		System.out.println("**********");
//		openWebNet = new OpenWebNet("*#12##");
//		System.out.println("frame: "+openWebNet.getFrameOpen());
//		System.out.println("lungezza frame: "+openWebNet.getLengthFrameOpen());
//		System.out.println("tipo frame: "+openWebNet.getTipoFrame());
//		System.out.println("chi: "+openWebNet.getChi());
//		System.out.println("cosa: "+openWebNet.getCosa());
//		System.out.println("dove: "+openWebNet.getDove());
//		System.out.println("quando: "+openWebNet.getQuando());
//		System.out.println("grandezza: "+openWebNet.getGrandezza());
//		System.out.println("livello:"+openWebNet.getLivello());
//		System.out.println("interfaccia: "+openWebNet.getInterfaccia());
//		System.out.println("**********");
//		openWebNet = new OpenWebNet("*111#");
//		System.out.println("frame: "+openWebNet.getFrameOpen());
//		System.out.println("lungezza frame: "+openWebNet.getLengthFrameOpen());
//		System.out.println("tipo frame: "+openWebNet.getTipoFrame());
//		System.out.println("chi: "+openWebNet.getChi());
//		System.out.println("cosa: "+openWebNet.getCosa());
//		System.out.println("dove: "+openWebNet.getDove());
//		System.out.println("quando: "+openWebNet.getQuando());
//		System.out.println("grandezza: "+openWebNet.getGrandezza());
//		System.out.println("livello:"+openWebNet.getLivello());
//		System.out.println("interfaccia: "+openWebNet.getInterfaccia());
//		System.out.println("**********");
//		openWebNet = new OpenWebNet("*#1004*#0*#30*01*06*2005##");
//		System.out.println("frame: "+openWebNet.getFrameOpen());
//		System.out.println("lungezza frame: "+openWebNet.getLengthFrameOpen());
//		System.out.println("tipo frame: "+openWebNet.getTipoFrame());
//		System.out.println("chi: "+openWebNet.getChi());
//		System.out.println("cosa: "+openWebNet.getCosa());
//		System.out.println("dove: "+openWebNet.getDove());
//		System.out.println("quando: "+openWebNet.getQuando());
//		System.out.println("grandezza: "+openWebNet.getGrandezza());
//		System.out.println("livello:"+openWebNet.getLivello());
//		System.out.println("interfaccia: "+openWebNet.getInterfaccia());
//		for (int i = 0; i < 15; i++) {
//			String valore = openWebNet.getValori(i);
//			System.out.println("valore "+i+": "+valore);
//		}
//		System.out.println("**********");
//		openWebNet = new OpenWebNet("*1*0*0#4#69##");
//		System.out.println("frame: "+openWebNet.getFrameOpen());
//		System.out.println("lungezza frame: "+openWebNet.getLengthFrameOpen());
//		System.out.println("tipo frame: "+openWebNet.getTipoFrame());
//		System.out.println("chi: "+openWebNet.getChi());
//		System.out.println("cosa: "+openWebNet.getCosa());
//		System.out.println("dove: "+openWebNet.getDove());
//		System.out.println("quando: "+openWebNet.getQuando());
//		System.out.println("grandezza: "+openWebNet.getGrandezza());
//		System.out.println("livello:"+openWebNet.getLivello());
//		System.out.println("interfaccia: "+openWebNet.getInterfaccia());
//		System.out.println("**********");
//		openWebNet = new OpenWebNet();
//		System.out.println("frame: "+openWebNet.getFrameOpen());
//		System.out.println("lungezza frame: "+openWebNet.getLengthFrameOpen());
//		System.out.println("tipo frame: "+openWebNet.getTipoFrame());
//		System.out.println("chi: "+openWebNet.getChi());
//		System.out.println("cosa: "+openWebNet.getCosa());
//		System.out.println("dove: "+openWebNet.getDove());
//		System.out.println("quando: "+openWebNet.getQuando());
//		System.out.println("grandezza: "+openWebNet.getGrandezza());
//		System.out.println("livello:"+openWebNet.getLivello());
//		System.out.println("interfaccia: "+openWebNet.getInterfaccia());
//		System.out.println("**********");   
//		System.out.println("FINE");
//		
   
		 
		//ATTIVO LA FINESTRA GRAFICA
		//new ClientGUI(); //obsoleta - sostituita con MainFrameClient() 

		new MainFrameClient();
	} //chiude main
	
}//chiude la classe