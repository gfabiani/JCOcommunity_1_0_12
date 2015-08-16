package client_java_core.core;

/***************************************************************************
 * 			                 MainFrameClient.java                          *
 * 			              --------------------------                       *
 *   date          : Feb 15, 2005                                          *
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

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Description:
 * Lancia l'interfaccia grafica ClientTCPThread
 * 
 */
public class MainFrameClient {
	ClientFrame frameClient = null;
	boolean packFrame = false;
	  //Construct the application
	/**	
	 * Costruttore
	 */
	public MainFrameClient() {
		frameClient = null;
		frameClient = new ClientFrame();
	    //Validate frame that have preset sizes
	    //Pack frame that have useful preferred size info, e.g. from their layout
	    if (packFrame) {
	    	frameClient.pack();
	    }
	    else {
	    	frameClient.validate();
	    } 
	    //Center the window
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    Dimension frameUserSize = frameClient.getSize();
	    if (frameUserSize.height > screenSize.height) {
	      frameUserSize.height = screenSize.height;
	    }
	    if (frameUserSize.width > screenSize.width) {
	      frameUserSize.width = screenSize.width;
	    }
	    frameClient.setLocation((screenSize.width - frameUserSize.width) / 2, (screenSize.height - frameUserSize.height) / 2);
	    frameClient.setVisible(true);
	    
	    //Mao da vedere cosa togliere
	    frameClient.setResizable(true);
	    frameClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit program when frame is closed
	   	ImageIcon img = new ImageIcon(this.getClass().getResource("immagini/icona.jpeg"));
    	frameClient.setIconImage(img.getImage());
    	//attivo il thread per il garbage collector
    	StatoGUIthread garbageCollector = new StatoGUIthread("garbage collector");
    	garbageCollector.start();
	    
	  }
}
