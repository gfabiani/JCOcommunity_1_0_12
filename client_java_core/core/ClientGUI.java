package client_java_core.core;

/***************************************************************************
 * 			                  ClientGUI.java                               *
 * 			              --------------------------                       *
 *   date          : Jul 15, 2004                                          *
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

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Description:
 * Lancia l'interfaccia grafica ClientTCPThread
 * 
 */
public class ClientGUI {
	/**
	 * Costruttore
	 *
	 * @deprecated Sostituita con la classe MainFrameClient.java
	 */
    public ClientGUI() {        
    	JFrame clientFrame = new ClientFrame();
    	clientFrame.setResizable(true);
    	clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit program when frame is closed
    	clientFrame.setVisible(true);
    	ImageIcon img = new ImageIcon(this.getClass().getResource("immagini/icona.jpeg"));
    	clientFrame.setIconImage(img.getImage());
    	//attivo il thread per il garbage collector
    	StatoGUIthread garbageCollector = new StatoGUIthread("garbage collector");
    	garbageCollector.start();
    }//end constructor
}
