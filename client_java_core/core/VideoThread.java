package client_java_core.core;

/***************************************************************************
 *                            VideoThread.java                             *
 *                    --------------------------                           *
 *   date          : Sep 20, 2005                                          *
 *   copyright     : (C) 2005 by Bticino S.p.A. Erba (CO) - Italy          *
 *                   Embedded Software Development Laboratory              *
 *   license       : GPL                                                   *
 *   email         :                                                       *
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

import java.awt.Image;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.ImageIcon;

import client_java_core.core.MyAuthenticator;
import client_java_core.core.SSLSetup;

/**
 * Description:
 * Gestisce tramite un thread la ricezione delle immagini dal webserver
 * 
 */
public class VideoThread extends Thread{
	
    	String ip;
    	SSLSocketFactory sslFact = null;
    	SSLSocket socketSSL = null;
        PrintWriter outputStream = null;
        DataInputStream data;
        
        VideoThread videoThread = null;     
        
        Image img;
        ImageIcon imageIcon = new ImageIcon();
        
        byte[] bytes0 = new byte[100000];  
        byte[] bytes1 = new byte[100000]; 
        byte[] bytes2; // contiene l'immagine jpeg
        
        /**
    	 * Costruttore
    	 * 
    	 * @param ipAddress Indirizzo ip del webserver dal quale catturare le immagini
    	 */
		public VideoThread(String ipAddress){
		    ip = ipAddress; 
        }
       
		/**
		 * Avvia il Thread per il refresh dell'immagine della videocamera
		 */
        public void run(){
            
            while(PanelVideocontrollo.tlcAttiva == 1){
	            try {
	                sleep(PanelVideocontrollo.sliderTimeout.getValue());
	            } catch (InterruptedException e) {
	                // TODO Auto-generated catch block
	                //e.printStackTrace();
	            }
	            
			   
	            try {

			        URL myUrl = new URL("https://".concat((String)PanelVideocontrollo.indirizzoIP.getText()).concat("/telecamera.php"));
			        SSLSetup.initializeForSSL();
			        SSLSetup.overrideTrustManager();
			        SSLSetup.setDebug();
			        Authenticator.setDefault (new MyAuthenticator ());

			        HttpsURLConnection urlc = (HttpsURLConnection) myUrl.openConnection();
			        urlc.setRequestMethod("GET");
//				    urlc.setRequestProperty("Host","xxxxxxx");
//				    urlc.setRequestProperty("Accept-encoding","zip");
//					urlc.setRequestProperty("Content-Type","application/jpg");
			        urlc.setDoOutput(true);
			        urlc.setUseCaches(false);
			       
			        urlc.connect();
	            
		            DataInputStream br = new DataInputStream(urlc.getInputStream());   
	                
	                bytes0 = new byte[100000];
	                bytes1 = new byte[100000];
		                	
	                int length = 0;
	                int temp = -1;
	                while(true){                   
		                 
		                try{
		                
		                    temp = br.read();
		                    
		                    if(temp == -1) break;
			                 
			                    bytes1[length] = (byte)temp;
			                    length++;
		                
		                } catch (IOException e1) {
			                // TODO Auto-generated catch block
			                System.out.println("Eccezione VideoThread in data.read()");
			                //e1.printStackTrace();
			            }
	                    
	                }
		                
	                bytes2 = new byte[length];   
	                
	                for(int z = 0; z < length; z++){
	                    bytes2[z] = (byte)bytes1[z];
	                }
	                
	                img = Toolkit.getDefaultToolkit().createImage(bytes2);
	                
	                imageIcon.setImage(img);
	                
	                if (PanelVideocontrollo.tlcAttiva == 1){		            
	                    PanelVideocontrollo.labelImage.setIcon(imageIcon);
		                PanelVideocontrollo.labelImage.repaint();		                
	                }
	                
	            }catch (IOException e3) {
                    // TODO Auto-generated catch block
                    e3.printStackTrace();
	            }

            }
            
        }

}