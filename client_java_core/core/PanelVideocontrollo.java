package client_java_core.core;

/***************************************************************************
 *                            PanelVideocontrollo.java                     *
 *                    --------------------------                           *
 *   date          : Sep 13, 2005                                          *
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import client_java_core.core.MyAuthenticator;
import client_java_core.core.SSLSetup;

/**
 * Description: crea il pannello per la gestione delle immagini provenienti dalle telecamere
 * 
 * 
 */

public class PanelVideocontrollo extends JPanel{
    static GestioneSocketComandi socketCom = new GestioneSocketComandi();
    
    //Caricamento immagini
    ImageIcon imagePiu = new ImageIcon(this.getClass().getResource("immagini/piu.gif"));
	ImageIcon imageMeno = new ImageIcon(this.getClass().getResource("immagini/meno.gif"));
	ImageIcon imageSu = new ImageIcon(this.getClass().getResource("immagini/su.gif"));
	ImageIcon imageGiu = new ImageIcon(this.getClass().getResource("immagini/giu.gif"));
	ImageIcon imageSinistra = new ImageIcon(this.getClass().getResource("immagini/sinistra.gif"));
	ImageIcon imageDestra = new ImageIcon(this.getClass().getResource("immagini/destra.gif"));
	ImageIcon imageAttivaTLC = new ImageIcon(this.getClass().getResource("immagini/play.gif"));
	ImageIcon imageStopTLC = new ImageIcon(this.getClass().getResource("immagini/stop.gif"));
	ImageIcon noImage = new ImageIcon(this.getClass().getResource("immagini/noImage.gif"));
	ImageIcon imagePulisci = new ImageIcon(this.getClass().getResource("immagini/taglia.gif"));

	SSLSocketFactory sslFact = null;
    SSLSocket socketSSL = null;
    PrintWriter outputStream = null;
    DataInputStream br;
    
    static VideoThread videoThread = null; 
    
    AttivaTLCThread attivaTLCThread = null;
    
    static int tlcAttiva = 0; //vale 1 se la telecamera è attiva, 0 altrimenti
    
    Image img;
    static JLabel labelTLC = new JLabel();
    static JLabel labelImage = new JLabel();
    ImageIcon imageIcon = new ImageIcon();
    byte[] bytes0 = new byte[100000];  
    byte[] bytes1 = new byte[100000]; 
    byte[] bytes2; // contiene l'immagine jpeg
  
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanelLeft = new JPanel();
    JPanel jPanelCenter = new JPanel();
    JPanel jPanelNorth = new JPanel();
    JPanel jPanelSouth = new JPanel();
    JLabel labelLuminosita = new JLabel();
    JButton lumMeno = new JButton();
    JButton lumPiu = new JButton();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    GridLayout gridLayout1 = new GridLayout();
    GridLayout gridLayout2 = new GridLayout();
    JLabel labelContrasto = new JLabel();
    JButton conMeno = new JButton();
    JButton conPiu = new JButton();
    JLabel labelColore = new JLabel();
    JButton colMeno = new JButton();
    JButton colPiu = new JButton();
    JLabel labelZoom = new JLabel();
    JButton zoomMeno = new JButton();
    JButton zoomPiu = new JButton();
    JLabel labelQualita = new JLabel();
    JButton qualMeno = new JButton();
    JButton qualPiu = new JButton();
    
    JLabel labelTimeout = new JLabel();
    static JSlider sliderTimeout = new JSlider();
    static JCheckBox attivaLog = new JCheckBox("Attiva Log");
    JButton pulisciLog = new JButton("Pulisci ");
    static JTextArea logArea = new JTextArea();
    static JScrollBar scrollLog = new JScrollBar();
    JScrollPane scrollPane = null;
    
    //comandi per spostarsi nell'immagine zoomata
    JButton su = new JButton();
    JButton giu = new JButton();
    JButton sinistra = new JButton();
    JButton destra = new JButton();
    
    JPanel jPanelEast = new JPanel();
    JButton attivaTLC = new JButton();
    JButton disattivaTLC = new JButton();
    JLabel labelIndirizzoIP = new JLabel();
    static JTextField indirizzoIP = new JTextField(11);
    static JLabel labelPassword = new JLabel();
    static JTextField password = new JTextField(7);
    JLabel labelIndirizzoTLC = new JLabel();    
    static JComboBox indTLC = new JComboBox();

    public PanelVideocontrollo() {
      try {
        jbInit();
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }

    void jbInit() throws Exception {
      this.setLayout(borderLayout1);
      jPanelCenter.setBackground(Color.gray);
      labelLuminosita.setText("Luminosità :   ");
      labelLuminosita.setFont(ClientFrame.fontLabel);
      lumMeno.setText("");
      lumMeno.setIcon(imageMeno);
      lumMeno.setBorder(BorderFactory.createRaisedBevelBorder());
      lumMeno.setFocusable(false);
      lumMeno.setToolTipText("diminuisci luminosità");
      lumPiu.setText("");
      lumPiu.setIcon(imagePiu);
      lumPiu.setBorder(BorderFactory.createRaisedBevelBorder());
      lumPiu.setFocusable(false);
      lumPiu.setToolTipText("aumenta luminosità");
      labelContrasto.setText("Contrasto   :   ");
      labelContrasto.setFont(ClientFrame.fontLabel);
      conMeno.setText("");
      conMeno.setIcon(imageMeno);
      conMeno.setBorder(BorderFactory.createRaisedBevelBorder());
      conMeno.setFocusable(false);
      conMeno.setToolTipText("diminuisci contrasto");
      conPiu.setText("");
      conPiu.setIcon(imagePiu);
      conPiu.setBorder(BorderFactory.createRaisedBevelBorder());
      conPiu.setFocusable(false);
      conPiu.setToolTipText("aumenta contrasto");
      labelColore.setText("Colore        :   ");
      labelColore.setFont(ClientFrame.fontLabel);
      colMeno.setText("");
      colMeno.setIcon(imageMeno);
      colMeno.setBorder(BorderFactory.createRaisedBevelBorder());
      colMeno.setFocusable(false);
      colMeno.setToolTipText("diminuisci colore");
      colPiu.setText("");
      colPiu.setIcon(imagePiu);
      colPiu.setBorder(BorderFactory.createRaisedBevelBorder());
      colPiu.setFocusable(false);
      colPiu.setToolTipText("aumenta colore");
      labelZoom.setText("Zoom         :   ");
      labelZoom.setFont(ClientFrame.fontLabel);
      zoomMeno.setText("");
      zoomMeno.setIcon(imageMeno);
      zoomMeno.setBorder(BorderFactory.createRaisedBevelBorder());
      zoomMeno.setFocusable(false);
      zoomMeno.setToolTipText("diminuisci zoom");
      zoomPiu.setText("");
      zoomPiu.setIcon(imagePiu);
      zoomPiu.setBorder(BorderFactory.createRaisedBevelBorder());
      zoomPiu.setFocusable(false);
      zoomPiu.setToolTipText("aumenta zoom");
      labelQualita.setText("Qualità       :   ");
      labelQualita.setFont(ClientFrame.fontLabel);
      qualMeno.setText("");
      qualMeno.setIcon(imageMeno);
      qualMeno.setBorder(BorderFactory.createRaisedBevelBorder());
      qualMeno.setFocusable(false);
      qualMeno.setToolTipText("diminuisci qualità");
      qualPiu.setText("");
      qualPiu.setIcon(imagePiu);
      qualPiu.setBorder(BorderFactory.createRaisedBevelBorder());
      qualPiu.setFocusable(false);
      qualPiu.setToolTipText("aumenta qualità");
      attivaTLC.setText("Attiva ");
      attivaTLC.setFont(ClientFrame.fontLabel);
      attivaTLC.setFocusable(false);
	  attivaTLC.setIcon(imageAttivaTLC);
	  attivaTLC.setToolTipText("Attiva Telecamera");
	  attivaTLC.setBackground(ClientFrame.colorButton);
	  attivaTLC.setBorder(BorderFactory.createRaisedBevelBorder());
      disattivaTLC.setDebugGraphicsOptions(0);
      disattivaTLC.setText("Disattiva ");
      disattivaTLC.setFont(ClientFrame.fontLabel);
      disattivaTLC.setFocusable(false);
	  disattivaTLC.setIcon(imageStopTLC);
	  disattivaTLC.setToolTipText("Disattiva Telecamera");
	  disattivaTLC.setBackground(ClientFrame.colorButton);
	  disattivaTLC.setBorder(BorderFactory.createRaisedBevelBorder());
      labelIndirizzoIP.setText("IP:   ");
      labelIndirizzoIP.setFont(ClientFrame.fontLabel);
      indirizzoIP.setFont(ClientFrame.fontLabel);
      labelPassword.setText("Password:   ");
      labelPassword.setFont(ClientFrame.fontLabel);
      password.setFont(ClientFrame.fontLabel);
      labelIndirizzoTLC.setText("Indirizzo telecamera:   ");
      labelIndirizzoTLC.setFont(ClientFrame.fontLabel);
      indirizzoIP.setPreferredSize(new Dimension(125,20)); 
      indirizzoIP.setEditable(false);
      
      //non visualizzo il text field
      labelPassword.setVisible(false);
      password.setVisible(false);
      
      password.setEditable(false);
      indTLC.setFont(ClientFrame.fontLabel);
      indTLC.setFocusable(false);
      
      for(int i = 0; i < 10; i++){
          indTLC.addItem("0".concat(new Integer(i).toString()));
      }
      
      for(int i = 10; i < 100; i++){
          indTLC.addItem(new Integer(i).toString());
      }
      
      
      labelImage.setPreferredSize(new Dimension(320, 240));
      labelImage.setIcon(noImage);
      labelImage.setDoubleBuffered(true);
      
      sliderTimeout.setMinimum(0);
      sliderTimeout.setMaximum(5000);
	  sliderTimeout.setMajorTickSpacing(500);	
	  sliderTimeout.setPaintTicks(true);
	  sliderTimeout.setForeground(Color.BLACK);
	  sliderTimeout.setFocusable(false);
	  sliderTimeout.setFont(ClientFrame.fontLabel);
	  sliderTimeout.setPreferredSize(new Dimension(200,30));
	  sliderTimeout.setValue(0);
      
	  labelTimeout.setFont(ClientFrame.fontLabel);
	  labelTimeout.setText("Ritardo ciclo " + sliderTimeout.getValue() + " ms : ");
	  
	  
	  su.setIcon(imageSu);
	  su.setFocusable(false);
	  su.setToolTipText("Su");
	  su.setBorder(BorderFactory.createRaisedBevelBorder());
	  giu.setIcon(imageGiu);
	  giu.setFocusable(false);
	  giu.setToolTipText("Giù");
	  giu.setBorder(BorderFactory.createRaisedBevelBorder());
	  sinistra.setIcon(imageSinistra);
	  sinistra.setFocusable(false);
	  sinistra.setToolTipText("Sinistra");
	  sinistra.setBorder(BorderFactory.createRaisedBevelBorder());
	  destra.setIcon(imageDestra);
	  destra.setFocusable(false);
	  destra.setToolTipText("Destra");
	  destra.setBorder(BorderFactory.createRaisedBevelBorder());
	  
	  
	  jPanelNorth.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	  GridLayout grid = new GridLayout();
	  jPanelNorth.setLayout(grid);
	  grid.setColumns(1);
	  JPanel panelInd = new JPanel();
	  JPanel panelPassword = new JPanel();
	  JPanel panelTelecamera = new JPanel();
	  JPanel panelAD = new JPanel(); //pannello Attiva/Disattiva telecamera
	  panelInd.add(labelIndirizzoIP);
	  panelInd.add(indirizzoIP);
	  panelPassword.add(labelPassword);
	  panelPassword.add(password);
	  panelTelecamera.add(labelIndirizzoTLC);
	  panelTelecamera.add(indTLC);
      panelAD.add(attivaTLC);
      panelAD.add(disattivaTLC);
	  
	  jPanelNorth.add(panelInd, null);
	  jPanelNorth.add(panelPassword, null);
	  jPanelNorth.add(panelTelecamera, null);
	  jPanelNorth.add(panelAD, null);
       
      JPanel panelLum = new JPanel();
      JPanel panelCon = new JPanel();
      JPanel panelCol = new JPanel();
      JPanel panelZoom = new JPanel();
      JPanel panelQual = new JPanel();
      panelLum.add(labelLuminosita, null);
      panelLum.add(lumMeno, null);
      panelLum.add(lumPiu, null);
      panelCon.add(labelContrasto, null);
      panelCon.add(conMeno, null);
      panelCon.add(conPiu, null);
      panelCol.add(labelColore, null);
      panelCol.add(colMeno, null);
      panelCol.add(colPiu, null);
      panelZoom.add(labelZoom, null);
      panelZoom.add(zoomMeno, null);
      panelZoom.add(zoomPiu, null);
      panelQual.add(labelQualita, null);
      panelQual.add(qualMeno, null);
      panelQual.add(qualPiu, null);
      
      JPanel panelComandiZoom = new JPanel(new GridBagLayout());
      panelComandiZoom.add(sinistra,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      panelComandiZoom.add(destra,   new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      panelComandiZoom.add(new JLabel("      "),   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      panelComandiZoom.add(su,  new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      panelComandiZoom.add(giu,   new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
      
      jPanelLeft.setLayout(gridBagLayout1);
      jPanelLeft.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
      jPanelLeft.add(panelLum,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 15, 0));
      jPanelLeft.add(panelCon,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
      jPanelLeft.add(panelCol,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
      jPanelLeft.add(panelQual,   new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
      jPanelLeft.add(panelZoom,   new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
      jPanelLeft.add(panelComandiZoom,   new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 180, 0), 0, 0));
            
      JPanel panelImage = new JPanel(new GridBagLayout());
      panelImage.add(labelImage,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(35, 0, 0, 0), 0, 0));
      panelImage.add(labelTimeout,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(40, 0, 0, 0), 0, 0));
      panelImage.add(sliderTimeout,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 120, 0), 0, 0));
      
      
      attivaLog.setFont(ClientFrame.fontLabel);
      attivaLog.setFocusable(false);
      attivaLog.setToolTipText("Abilita stampe");
      pulisciLog.setFont(ClientFrame.fontLabel);
      pulisciLog.setFocusable(false);
      pulisciLog.setToolTipText("Cancella log");
      pulisciLog.setBackground(ClientFrame.colorButton);
      pulisciLog.setIcon(imagePulisci);
      pulisciLog.setBorder(BorderFactory.createRaisedBevelBorder());
      logArea.setFont(ClientFrame.fontLabel);
      logArea.setBackground(Color.white);   
      logArea.setLineWrap(false);
      logArea.setEditable(false);
      logArea.setBorder(BorderFactory.createRaisedBevelBorder());
      
      scrollPane = new JScrollPane(logArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
      scrollPane.setVerticalScrollBar(scrollLog);
      
      JPanel panelLog = new JPanel(new GridBagLayout());
      panelLog.add(attivaLog,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 30, 0, 30), 0, 0));
      panelLog.add(pulisciLog,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 30, 0, 60), 0, 0));
      panelLog.add(scrollPane,   new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
              ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(20, 0, 20, 30), 0, 0));
      	    
      
      //labelTLC.setFont(new java.awt.Font("Serif", 1, 18));
      //labelTLC.setText("Telecamera disattivata");
      jPanelCenter.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
      jPanelCenter.setLayout(new BorderLayout());   
      jPanelCenter.add(labelTLC, BorderLayout.NORTH);
      jPanelCenter.add(panelImage, BorderLayout.CENTER);
      jPanelCenter.add(panelLog, BorderLayout.EAST);
      
      jPanelSouth.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                       
      this.add(jPanelNorth, BorderLayout.NORTH);
      //this.add(jPanelSouth, BorderLayout.SOUTH);
      this.add(jPanelCenter, BorderLayout.CENTER);
      //this.add(jPanelEast, BorderLayout.EAST);
      this.add(jPanelLeft, BorderLayout.WEST);
      
      
      //Listener
      
      attivaTLC.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) throws NullPointerException{		
			    
			    if(ClientFrame.monitor.isSelected()){
			        
			    }else{
			        JOptionPane.showMessageDialog(null, "Attivando la telecamera verrà automaticamente attivata la funzione di monitor!!!");
			        ClientFrame.monitor.setSelected(true);
			        ClientFrame.attivaMonitor();
					
			    }
			    
			    attivaTLC = null;
			    
			    if(indirizzoIP.getText().length() == 0){
			        
			        ClientFrame.scriviSulLog("indirizzo ip per attivazione telecamera non valido",2,0,0);
			        
			    }else{
			    		    
			        Integer passInt = null;
				    
				    String pass = null;
				    int passwordOpen = 0;
						
						if(socketCom.stato == 0){ //non sono ancora connesso
							
							if(ClientFrame.abilitaPass.isSelected()){
								pass = password.getText();
								passInt = new Integer(pass);
								passwordOpen = passInt.intValue(); //password in formato int
							}
							if(socketCom.connect(indirizzoIP.getText(), 20000, passwordOpen)){
								
							    //abilitaInterfaccia(false);
							    attivaTLCThread = null;
							    attivaTLCThread = new AttivaTLCThread("*7*9**##");
							    attivaTLCThread.start();
//								    inviaComandoThread = null;
//								    inviaComandoThread = new InviaComandoThread((String)comandoOpen.getText());
//								    inviaComandoThread.start();
							    
							}else{
							    pass = null;
							    return;
							    //Connessione KO
							}
							pass = null;
							
						}else if(socketCom.stato == 3){ //sono già connesso
												    
						    attivaTLCThread = null;
						    attivaTLCThread = new AttivaTLCThread("*7*9**##");
						    attivaTLCThread.start();
						    
						}else{
							ClientFrame.scriviSulLog("%%%%%%%%%%%%% SITUAZIONE NON GESTITA %%%%%%%%%%%%%%%",2,0,0);
						}
						
						try {
	                        attivaTLCThread.join();
	                    } catch (InterruptedException e2) {
	                        // TODO Auto-generated catch block
	                        e2.printStackTrace();
	                    }
						
	                    
						if(socketCom.stato == 0){ //non sono ancora connesso
							
								if(ClientFrame.abilitaPass.isSelected()){
									pass = password.getText();
									passInt = new Integer(pass);
									passwordOpen = passInt.intValue(); //password in formato int
								}
								if(socketCom.connect(indirizzoIP.getText(), 20000, passwordOpen)){
									
								    //abilitaInterfaccia(false);
								    String temp1 = "*7*0*40".concat((String)indTLC.getSelectedItem()).concat("##");
								    attivaTLCThread = null;
								    attivaTLCThread = new AttivaTLCThread(temp1);
								    attivaTLCThread.start();
								    
								}else{
									//Connessione KO
								    pass = null;
								    return;
								}
								pass = null;
							
						}else if(socketCom.stato == 3){ //sono già connesso
							
						    String temp1 = "*7*0*40".concat((String)indTLC.getSelectedItem()).concat("##");
						    attivaTLCThread = null;
						    attivaTLCThread = new AttivaTLCThread(temp1);
						    attivaTLCThread.start();
						    
						}else{
							ClientFrame.scriviSulLog("%%%%%%%%%%%%% SITUAZIONE NON GESTITA %%%%%%%%%%%%%%%",2,0,0);
						}
				    	
				    try {
                        attivaTLCThread.join();
                    } catch (InterruptedException e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }
				    

				    try{
				        URL myUrl = new URL("https://".concat((String)indirizzoIP.getText()).concat("/telecamera.php"));
				        SSLSetup.initializeForSSL();
				        SSLSetup.overrideTrustManager();
				        SSLSetup.setDebug();
				        Authenticator.setDefault (new MyAuthenticator ());

				        HttpsURLConnection urlc = (HttpsURLConnection) myUrl.openConnection();
				        urlc.setRequestMethod("GET");
//				         urlc.setRequestProperty("Host","xxxxxxx");
//				         urlc.setRequestProperty("Accept-encoding","zip");
				        //urlc.setRequestProperty("Content-Type","application/jpg");
				        urlc.setDoOutput(true);
				        urlc.setUseCaches(false);
				        urlc.connect();

		                bytes0 = new byte[100000];
		                bytes1 = new byte[100000];
		                       
		                br = new DataInputStream(urlc.getInputStream());
		                
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
		                       System.out.println("Eccezione AttivaTLC 2222");
			                   e1.printStackTrace();
			                    
			                }
		                   
		                }
		                     
		                bytes2 = new byte[length];   
		                
		                for(int z = 0; z < length; z++){
		                    bytes2[z] = (byte)bytes1[z];
		                }
		                
		                img = Toolkit.getDefaultToolkit().createImage(bytes2);
		                
		                imageIcon.setImage(img);
		                labelImage.setIcon(imageIcon);
		                labelImage.setBorder(null);
		                	                
                        //socketSSL.close();

				    
				    } catch (IOException e3) {
                            // TODO Auto-generated catch block
                            e3.printStackTrace();
                    }
		                
	                videoThread = new VideoThread(indirizzoIP.getText());
	                videoThread.start();
	                tlcAttiva = 1;
				    
		            
			    }
			}
      });      
     
      
      disattivaTLC.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              
              if(tlcAttiva == 1){
                  
      			socketCom.connect(indirizzoIP.getText(), 20000, 0);
      			socketCom.invia("*7*9**##");
      			tlcAttiva = 0;
      			  
      			if(videoThread != null) videoThread.interrupt();
      			
      			labelImage.setIcon(noImage);
      			labelImage.repaint();
                 
              } 
                    
          }
  	  });
      
      
      lumMeno.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {

              if(tlcAttiva == 1){
	              socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*151##");
              }
	              
          }
  	  });
      
      
      lumPiu.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          
              if(tlcAttiva == 1){
            	  socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*150##");
              }
                            
          }
  	  });
      
      
      conMeno.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          
        	  if(tlcAttiva == 1){
        		  socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*161##");
              }
              
          }
  	  });
      
      
      conPiu.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          
              if(tlcAttiva == 1){
            	  socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*160##");
              }
              
          }
  	  });
      
      
      colMeno.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              
              if(tlcAttiva == 1){
            	  socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*171##");
              }
              
          }
  	  });
      
      
      colPiu.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          
              if(tlcAttiva == 1){                  
            	  socketCom.connect(indirizzoIP.getText(), 20000, 0);
                  socketCom.invia("*7*170##");
              }
                                          
          }
  	  });
      
      
      zoomMeno.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              
              if(tlcAttiva == 1){
            	  socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*121##");
              }
              
          }
  	  });
      
      
      zoomPiu.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          
        	  if(tlcAttiva == 1){
        		  socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*120##");
              }
                            
          }
  	  });
      
      
      qualMeno.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              
              if(tlcAttiva == 1){
            	  socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*181##");
              }
                            
          }
  	  });
      
      
      qualPiu.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              
              if(tlcAttiva == 1){
            	  socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*180##");
              }
                            
          }
  	  });
      
      
      su.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              
              if(tlcAttiva == 1){
            	  socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*141##");
              }
                            
          }
  	  });
      
      
      giu.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          
              if(tlcAttiva == 1){
            	  socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*140##");
              }
              
          }
  	  });
      
      
      sinistra.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          
              if(tlcAttiva == 1){
            	  socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*131##");
              }
              
          }
  	  });
      
      
      destra.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          
              if(tlcAttiva == 1){                  
            	  socketCom.connect(indirizzoIP.getText(), 20000, 0);
	              socketCom.invia("*7*130##");
              }
                            
          }
  	  });
      
      
      sliderTimeout.addMouseListener(new MouseListener() {
		    public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				int levTimeout = sliderTimeout.getValue();
				labelTimeout.setText("Ritardo ciclo " + levTimeout + " ms : ");		
			}

			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}	    
	    } );
      
      pulisciLog.addActionListener( new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          
              logArea.setText(null);
              
          }
  	  });
      
  } 

}
