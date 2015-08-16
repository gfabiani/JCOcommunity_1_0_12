package client_java_core.core;

/***************************************************************************
 * 			                  ClientFrame.java                             *
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import client_java_core.core.PanelVideocontrollo;

      
/**          
 * Description:   
 * Costruisce l'interfaccia grafica dell'applicazione e gestisce tutti i listener dei pulsanti
 * 
 */
public class ClientFrame extends JFrame{
      	
	static Font fontLabel = new java.awt.Font("Arial", 0, 13);
	Font fontMenu = new java.awt.Font("Lucida bright", 0, 13);
	static Color colorButton = new Color(224,223,227);
	
	static GestioneSocketComandi gestSocketComandi = null;
	static GestioneSocketMonitor gestSocketMonitor = null;
	
	InviaComandoThread inviaComandoThread = null;
	
	static boolean pluginPassword = false; //true se presente il plugin per il controllo della password, false altrimenti 
	static boolean pluginSCS = false;//true se presente il plugin per la gestione dell'scs, false altrimenti
	
	static int lineeApplicativo = 0;
	static int lineeUtente = 0;
	static int lineeDebug = 0;
	//caricamento immagini
	ImageIcon imageAvvia = new ImageIcon(this.getClass().getResource("immagini/play.gif"));
	ImageIcon imageCiclo = new ImageIcon(this.getClass().getResource("immagini/ciclo.gif"));
	ImageIcon imageApri = new ImageIcon(this.getClass().getResource("immagini/apri.gif"));
	ImageIcon imageStop = new ImageIcon(this.getClass().getResource("immagini/stop.gif"));
	ImageIcon imagePause = new ImageIcon(this.getClass().getResource("immagini/pause.gif"));
	ImageIcon imagePulisci = new ImageIcon(this.getClass().getResource("immagini/taglia.gif"));
	ImageIcon bannerAlto = new ImageIcon(this.getClass().getResource("immagini/banner_alto.jpg"));
	JLabel labelBanner = new JLabel(bannerAlto);
	
	//gestione file di log
	static GestisciFileLog gestFileLog = null;
	JLabel labelLog = new JLabel("File di Log : ");
	JButton fileButton = new JButton("File ");
	static JTextField fileTextField = new JTextField(25);
	static JCheckBox appendi = new JCheckBox("Appendi");
	JButton attiva = new JButton("Attiva ");
	JButton interrompi = new JButton("Interrompi ");
	static String fileSelezionato = null;

	//gestione invio comando open
	JLabel labelComandoOpen = new JLabel("Open: ");
	static JTextField comandoOpen = new JTextField(15);
	JLabel labelSleepComando = new JLabel("Ritardo : ");
	JComboBox sleepComando = new JComboBox();
	JLabel labelDurataCiclo = new JLabel("Durata  : ");
	JComboBox durataCiclo = new JComboBox();
	static JButton inviaComando = new JButton();
	static JButton stopComando = new JButton();
	static JButton cicloComando = new JButton();
	int sleepTimeComando = 0; //inizializzo a 0
	int durataTimeComando = -1; //inizializzo a infinito
	static AvviaCicloComando avviaCicloComando = null;
	//gestione superSocketComandi
	static JCheckBox superSocket = new JCheckBox("   Super Socket");
	
	//gestione file con sequenza comandi
	static SequenzaOpen sequenzaOpen = null;
	JButton seleziona = new JButton();
	static JTextField sequenzaTextField = new JTextField(23);
	JLabel labelSequenza = new JLabel("Sequenza Open : ");
	JLabel labelSleep = new JLabel("Ritardo : ");
	JComboBox sleepSequenza = new JComboBox();
	JLabel labelDurataCicloSequenza = new JLabel("Durata  : ");
	JComboBox durataCicloSequenza = new JComboBox();
	static JButton avviaSequenza = new JButton();
	static JButton stopSequenza = new JButton();
	static JButton pauseSequenza = new JButton();
	static JCheckBox cicloSequenza = new JCheckBox("   Ciclo");
	//static int cicloSeqOpen = 0; //vale 1 se continua dall'inizio una volta arrivato in fondo al file con la sequenza open,0 altrimenti
	int sleepTimeSequenza = 0; //inizializzo a 0
	int durataTimeSequenza = -1; //inizializzo a infinito
	static AvviaSequenzaOpen avviaSequenzaOpen = null;
	static ThreadDurata threadDurata = null;	
	
	//colore di sfondo
	Color grigio = new Color(191,191,191);
	Color sfondo = new Color(193,233,244);
	Color pulsanteColor = new Color(70,204,244);
	Color menuColor = new Color(34,79,244);
	
	//variabili interfaccia 
	static JCheckBox abilitaPass = new JCheckBox(null, null, true);
	
	JLabel labelIP = new JLabel("IP :   ");
	JLabel labelPassword = new JLabel("   Password open :   ");
	static Integer passInt = null;
	static JTextField ip = new JTextField(11);
	static JTextField password = new JTextField(7);
	static int passwordOpen = 0;
	static JTextArea areaLogApplicativo;
	static JTextArea areaLogUtente; 
	static JTextArea areaLogDebug;
	static JCheckBox monitor = new JCheckBox("   Attiva Monitor");
	static JCheckBox mantieniSocket = new JCheckBox("   Mantieni socket aperta");  //se cliccata mantiene la connessione aperta dopo ogni comando, quando viene rilasciata chiudo la socket
	static JTextField info;
	//variabili per controllare quali log sono attivi
	static boolean logAppl = true; 
	static boolean logUtente = false;
	static boolean logDebug = false;
	
	
	JFrame f; //frame per la gestione di "About JClientOpen"
	
	static JScrollPane scrollerApplicativo;
	JScrollPane scrollerUtente;
	JScrollPane scrollerDebug;
	static JScrollBar scrollBarApplicativo = new JScrollBar();
	static JScrollBar scrollBarUtente = new JScrollBar();
	static JScrollBar scrollBarDebug = new JScrollBar();
	   
//	creo il menu in alto al frame
	JMenuBar menuBar = new JMenuBar();
	JMenu menu1 = new JMenu("File");
	static JMenuItem close = new JMenuItem("Chiudi");
	JMenu menu2 = new JMenu("Finestra");
	JCheckBoxMenuItem windowComando = new JCheckBoxMenuItem("Comando");
	JCheckBoxMenuItem windowSequenza = new JCheckBoxMenuItem("Sequenza");
	JMenu menu3 = new JMenu("Aiuto");
	JMenuItem about = new JMenuItem("Versione JClientOpen");
	JMenu menu4 = new JMenu("Seriale");
	JMenuItem parametriSeriale = new JMenuItem("Imposta Parametri");
	
	BorderLayout borderLayout = new BorderLayout();
	
	static JTabbedPane pannelloLog = new JTabbedPane(); //pannello a schede per visualizzare i log
	 
	JTabbedPane tabbedPane = new JTabbedPane();
	JPanel panelLeft;   // contiene i comandi di invio, ...
	JPanel panelRight;  // NON UTILIZZATO
	JPanel panelDown;   // contiene l'area dedicata al livello di verbosity
	JPanel panelNorth = new JPanel();  // contiene le aree per impostare ip, password, ...
	JPanel panelComando; //pannello che si occupa di gestire l'invio di un comando open
	JPanel panelSequenza; //pannello che si occupa di gestire il file con la sequenza di comandi da inviare
	JPanel panelNorthMain; //contiene i pannelli panelNorth, panelComando e panelSequenza
	JPanel panelCenter; // contiene l'area dedicata alla visualizzazione dei log
	JPanel mainPanel;   // contiene tutti i pannelli sopra definiti
	JPanel contentPane; //contiene tutta l'interfaccia grafica(banner e contenuti)	
	
	GridBagConstraints c = new GridBagConstraints();
	
	ImageIcon icon;
	JLabel labelAbout;
	
	//pannello pulisci stampe
	JPanel panelClear = new JPanel();
	JButton clearLog = new JButton();
	
//	pannello per il controllo stampe
	JPanel panelAbilita = new JPanel();
	JCheckBox abilitaLogAppl = new JCheckBox("  Applicativo");
	JCheckBox abilitaLogUtente = new JCheckBox("  Utente");
	JCheckBox abilitaLogDebug = new JCheckBox("  Debug");
	
	//pannello opzioni di visualizzazione (SCS --- open)
	JPanel panelVisualizzazione = new JPanel();
	//ButtonGroup groupVisualizzazione = new ButtonGroup();
	static JCheckBox visualizzaMon = new JCheckBox("  Mon");
	static JCheckBox visualizzaOpen = new JCheckBox("  Open");
	static JCheckBox visualizzaSCS = new JCheckBox("  SCS");
	static JCheckBox visualizzaCommenti = new JCheckBox("  Commenti");
	
	//variabili metodo scriviSulLog()
	static MyDate miaData = null;
	static String df = null;
	static String flag = null;   
     	
	/**  
	 * Costruttore     
	 *           
	 */       
	public ClientFrame(){		
		try{
			miaData = new MyDate();
			gestSocketComandi = new GestioneSocketComandi();
			gestSocketMonitor = new GestioneSocketMonitor();
			//Ricerca plugin disponibili
			//System.out.println("Ricerca plugin disponibili");
			try{    
				File  f = new File(this.getClass().getResource("external_jar/Android_Beta_Autentication.jar").getPath());
				System.out.println("Plugin password open trovato");
				pluginPassword = true;  
			}catch(NullPointerException e){
				pluginPassword = false;
			}        
			try{    
				File f = new File(this.getClass().getResource("../plugin/pluginSCS.txt").getPath());
				System.out.println("Plugin SCS trovato");
				pluginSCS = true;   
			}catch(NullPointerException e){
				pluginSCS = false;
			}
			
			addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent e) {   
	            	if (gestFileLog != null) gestFileLog.interrompi();         
	            	//salvo la cofigurazione prima di chiudere
	            	File file = new File("./conf.txt");
	            	try {  
						FileWriter fileWriter = new FileWriter(file);
						fileWriter.write(ip.getText()+"\n");
						fileWriter.write(password.getText()+"\n");
						fileWriter.write(mantieniSocket.isSelected()+"\n");
						fileWriter.write(monitor.isSelected()+"\n");
						fileWriter.write(comandoOpen.getText());
						fileWriter.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
	            	System.exit(0);
	            }
			});
			jbinit();
		}
		catch (Exception e){
			e.printStackTrace();
		}			
	}//chiude il costruttore

	/**
	 * Inizializzazione dei componenti
	 * 
	 * @throws Exception
	 */
	public void jbinit() throws Exception{
		//pannello generale che contiene tutti gli altri
		contentPane = (JPanel)this.getContentPane();
		contentPane.setLayout(new BorderLayout());
				
		GridBagLayout bagLayout = new GridBagLayout();
		panelLeft = new JPanel(new GridLayout (0,1,0,5));
		panelRight = new JPanel(new FlowLayout());
		panelDown = new JPanel(new FlowLayout());
		//panelSequenza = new JPanel(bagLayout);
		panelNorthMain = new JPanel(new BorderLayout());
		mainPanel = new JPanel(new BorderLayout()); 
				
		menuBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		//panelNorthMain.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		//panelSequenza.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		//panelLeft.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		panelDown.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		//inizializzazione componenti	
		abilitaLogAppl.setSelected(true);
		logAppl = true;
		
		areaLogApplicativo = new JTextArea();
		areaLogApplicativo.setLineWrap(false);
		areaLogApplicativo.setEditable(false);
	    scrollerApplicativo = new JScrollPane(areaLogApplicativo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
    			   JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);		
	    scrollerApplicativo.setVerticalScrollBar(scrollBarApplicativo);
	    
		areaLogUtente = new JTextArea();
		areaLogUtente.setLineWrap(false);
		areaLogUtente.setEditable(false);
		scrollerUtente = new JScrollPane(areaLogUtente, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				   JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollerUtente.setVerticalScrollBar(scrollBarUtente);
				
		areaLogDebug = new JTextArea();
		areaLogDebug.setLineWrap(false);
		areaLogDebug.setEditable(false);
		scrollerDebug = new JScrollPane(areaLogDebug, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				   JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollerDebug.setVerticalScrollBar(scrollBarDebug);

		sequenzaTextField.setEditable(false);
		fileTextField.setEditable(false);
		
		sleepComando.setPreferredSize(new Dimension(120,24));
		sleepComando.addItem("0");
		sleepComando.addItem("0.05 sec");
		sleepComando.addItem("0.1 sec");
		sleepComando.addItem("0.5 sec");
		sleepComando.addItem("1 sec");
		sleepComando.addItem("2 sec");
		sleepComando.addItem("3 sec");
		sleepComando.addItem("4 sec");
		sleepComando.addItem("5 sec");
		sleepComando.addItem("10 sec");
		
		durataCiclo.setPreferredSize(new Dimension(120,24));
		durataCiclo.addItem("Infinita");
		durataCiclo.addItem("1 min");
		durataCiclo.addItem("2 min");
		durataCiclo.addItem("3 min");
		durataCiclo.addItem("5 min");
		durataCiclo.addItem("15 min");
		durataCiclo.addItem("30 min");
		durataCiclo.addItem("1 ora");
		durataCiclo.addItem("1 ora e 30 min");
		durataCiclo.addItem("2 ore");

		sleepSequenza.setPreferredSize(new Dimension(120,24));
		sleepSequenza.addItem("0");
		sleepSequenza.addItem("0.05 sec");
		sleepSequenza.addItem("0.1 sec");
		sleepSequenza.addItem("0.5 sec");
		sleepSequenza.addItem("1 sec");
		sleepSequenza.addItem("2 sec");
		sleepSequenza.addItem("3 sec");
		sleepSequenza.addItem("4 sec");
		sleepSequenza.addItem("5 sec");
		sleepSequenza.addItem("10 sec");
		
		durataCicloSequenza.setPreferredSize(new Dimension(120,24));
		durataCicloSequenza.addItem("Infinita");
		durataCicloSequenza.addItem("1 min");
		durataCicloSequenza.addItem("2 min");
		durataCicloSequenza.addItem("3 min");
		durataCicloSequenza.addItem("5 min");
		durataCicloSequenza.addItem("15 min");
		durataCicloSequenza.addItem("30 min");
		durataCicloSequenza.addItem("1 ora");
		durataCicloSequenza.addItem("1 ora e 30 min");
		durataCicloSequenza.addItem("2 ore");
				
//		gestione menÃ¹
		menu1.setMnemonic(KeyEvent.VK_F);
		menu1.add(close);
		menu2.setMnemonic(KeyEvent.VK_F);
		menu2.add(windowComando);
		menu2.add(windowSequenza);
		windowComando.setSelected(true);
		windowSequenza.setSelected(false);
		menu3.setMnemonic(KeyEvent.VK_A);
		menu3.add(about);
		menu4.setMnemonic(KeyEvent.VK_S);
		menu4.add(parametriSeriale);
		menuBar.add(menu1);		
		menuBar.add(menu2);
		if(pluginSCS) menuBar.add(menu4);
		menuBar.add(menu3); 
		this.setJMenuBar(menuBar);
		menu1.setFont(fontLabel);
		menu2.setFont(fontLabel);
		menu3.setFont(fontLabel);
		menu4.setFont(fontLabel);
		windowComando.setFont(fontLabel);
		windowSequenza.setFont(fontLabel);
		close.setFont(fontLabel);
		parametriSeriale.setFont(fontLabel);
		about.setFont(fontLabel);
		
		
//		Pannello "panelLeft" le opzioni di visualizzazione
		//pannello clear
		panelClear.setBorder(BorderFactory.createTitledBorder("Svuota"));
		panelClear.setLayout(new GridBagLayout());
		panelClear.add(clearLog, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0,0,0,0), 0, 0));
		clearLog.setFont(fontLabel);
		clearLog.setText("Pulisci ");
		clearLog.setToolTipText("Pulisci");
		clearLog.setBackground(colorButton);
		clearLog.setIcon(imagePulisci);
		clearLog.setBorder(BorderFactory.createRaisedBevelBorder());
		
		//Pannello visualizzazione
		panelVisualizzazione.setBorder(BorderFactory.createTitledBorder("Visualizza"));
		panelVisualizzazione.setLayout(new GridLayout(0,1,0,2));
		panelVisualizzazione.add(visualizzaCommenti);
		panelVisualizzazione.add(visualizzaMon);
		panelVisualizzazione.add(visualizzaOpen);
		panelVisualizzazione.setFont(fontLabel);
		if(pluginSCS) panelVisualizzazione.add(visualizzaSCS); //se c'Ã¨ il plugin visualizzo l'opzione di visualizzazione scs
		
		visualizzaCommenti.setSelected(true);
		visualizzaOpen.setSelected(true);
		visualizzaMon.setSelected(true);
		if(pluginSCS) visualizzaSCS.setSelected(true);
		
		visualizzaCommenti.setFont(fontLabel);
		visualizzaOpen.setFont(fontLabel);
		visualizzaMon.setFont(fontLabel);
		visualizzaSCS.setFont(fontLabel);
		
		
		//pannello per abilitare il livello di stampe visibile
		panelAbilita.setBorder(BorderFactory.createTitledBorder("Abilita Log"));
		panelAbilita.setLayout(new GridLayout(0,1,0,2));
		panelAbilita.add(abilitaLogAppl);
		panelAbilita.add(abilitaLogUtente);
		panelAbilita.add(abilitaLogDebug);
		abilitaLogAppl.setFont(fontLabel);
		abilitaLogUtente.setFont(fontLabel);
		abilitaLogDebug.setFont(fontLabel);
		
		panelLeft.add(panelClear);
		panelLeft.add(panelAbilita);
		panelLeft.add(panelVisualizzazione);
		
		//Pannello "panelDown" per l'attivazione del file di log
		panelDown.add(labelLog);
		panelDown.add(fileButton);
		panelDown.add(fileTextField);
		panelDown.add(appendi);
		panelDown.add(attiva);	
		panelDown.add(interrompi);
		fileButton.setFont(fontLabel);
		fileTextField.setFont(fontLabel);
		appendi.setFont(fontLabel);
		attiva.setFont(fontLabel);
		interrompi.setFont(fontLabel);
		
		fileButton.setIcon(imageApri);
		fileButton.setToolTipText("Seleziona file di log");
		fileButton.setBackground(colorButton);
		fileButton.setBorder(BorderFactory.createRaisedBevelBorder());
		attiva.setIcon(imageAvvia);
		attiva.setToolTipText("Attiva");
		attiva.setBackground(colorButton);
		attiva.setBorder(BorderFactory.createRaisedBevelBorder());
		interrompi.setIcon(imageStop);
		interrompi.setToolTipText("Interrompi");
		interrompi.setBackground(colorButton);
		interrompi.setBorder(BorderFactory.createRaisedBevelBorder());
		
		// Pannello "panelNorth" contiene ip, passwordOpen ...		 
		panelNorth.setLayout(new GridBagLayout());
		panelNorth.setBackground(grigio);
		JPanel panelIp = new JPanel(bagLayout);
		JPanel panelPassword = new JPanel(bagLayout);
		panelIp.add(labelIP);
		panelIp.add(ip);
		panelIp.setBackground(grigio);
		panelPassword.add(abilitaPass);
		panelPassword.add(labelPassword);
		panelPassword.add(password);
		panelPassword.setBackground(grigio);
		mantieniSocket.setBackground(grigio);       
		monitor.setBackground(grigio);   
		abilitaPass.setBackground(grigio);
		labelIP.setFont(fontLabel);
		ip.setFont(fontLabel);
		abilitaPass.setFont(fontLabel);   
		labelPassword.setFont(fontLabel);
		password.setFont(fontLabel);
		mantieniSocket.setFont(fontLabel);
		monitor.setFont(fontLabel);
		mantieniSocket.setFocusable(false);
		monitor.setFocusable(false);
		/*panelNorth.add(labelIP,        new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));*/
		panelNorth.add(panelIp,        new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,0,0,0), 0, 0));
		if(pluginPassword){ //se c'Ã¨ il plugin per la password	
			panelNorth.add(panelPassword,        new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
		            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,0,0,0), 0, 0));
		}else{ //altrimenti setto il flag per non effettuare il controllo della password		
			abilitaPass.setSelected(false);
		}
		panelNorth.add(mantieniSocket,        new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,0,0,0), 0, 0));
		panelNorth.add(monitor,        new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,0,0,0), 0, 0));
        		
		
//      Pannello "panelComando" componenti per invio comando singoli
        GridLayout gridLayout= new GridLayout(2,4);
		panelComando = new JPanel(gridLayout);
		panelComando.setBorder(BorderFactory.createTitledBorder(""));
        JPanel panel1 = new JPanel(bagLayout);
        JPanel panel2 = new JPanel(bagLayout);
        JPanel panel3 = new JPanel(bagLayout);
        JPanel panel4 = new JPanel(bagLayout);
        JPanel panel5 = new JPanel(bagLayout);
        JPanel panel6 = new JPanel(bagLayout);
        panelComando.add(panel1);
        panelComando.add(panel2);
        panelComando.add(panel3);
        panelComando.add(panel4);
        panelComando.add(panel5);		
        panelComando.add(panel6);
		panel1.add(labelComandoOpen, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));
		panel1.add(comandoOpen, new GridBagConstraints(1, 0, 2, 2, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 0, 0));
		/*panel2.add(labelSuperSocket, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));*/
		if(pluginSCS){
		    panel2.add(superSocket, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
		}
		panel3.add(labelSleepComando, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));
		panel3.add(sleepComando, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 0, 0));
		panel6.add(labelDurataCiclo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
		panel6.add(durataCiclo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 5, 0), 0, 0));
		panel4.add(inviaComando, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 2, 2));
		panel4.add(cicloComando, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 10, 0), 2, 2));
		panel4.add(stopComando, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 10, 0), 2, 2));
        
		labelComandoOpen.setFont(fontLabel);
		comandoOpen.setFont(fontLabel);
		superSocket.setFont(fontLabel);
		labelSleepComando.setFont(fontLabel);
		sleepComando.setFont(fontLabel);
		labelDurataCiclo.setFont(fontLabel);
		durataCiclo.setFont(fontLabel);
		
		inviaComando.setIcon(imageAvvia);
		inviaComando.setFocusable(false);
		inviaComando.setToolTipText("Invia Comando");
	    inviaComando.setBorder(BorderFactory.createRaisedBevelBorder());
	    cicloComando.setIcon(imageCiclo);
	    cicloComando.setFocusable(false);
	    cicloComando.setToolTipText("Avvia Ciclo");
	    cicloComando.setBorder(BorderFactory.createRaisedBevelBorder());
	    stopComando.setIcon(imageStop);
	    stopComando.setFocusable(false);
	    stopComando.setToolTipText("Stop Ciclo");
	    stopComando.setBorder(BorderFactory.createRaisedBevelBorder());
	    superSocket.setFocusable(false);
        
//      posizionamento componeti panelSequenza
	    GridLayout gridLayout1= new GridLayout(2,4);
		panelSequenza = new JPanel(gridLayout1);  
		panelSequenza.setBorder(BorderFactory.createTitledBorder(""));
		panelSequenza.setVisible(false);
        JPanel panel1s = new JPanel(bagLayout);
        JPanel panel2s = new JPanel(bagLayout);        
        JPanel panel3s = new JPanel(bagLayout);
        JPanel panel4s = new JPanel(bagLayout);
        JPanel panel5s = new JPanel(bagLayout);
        JPanel panel6s = new JPanel(bagLayout);
		panelSequenza.add(panel1s);
		panelSequenza.add(panel2s);
		panelSequenza.add(panel3s);
		panelSequenza.add(panel4s);
		panelSequenza.add(panel5s);		
		panelSequenza.add(panel6s);
	    panel1s.add(labelSequenza, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
	    panel1s.add(seleziona, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 2, 2));
	    panel2s.add(sequenzaTextField, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
	    panel3s.add(labelSleep, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
	    panel3s.add(sleepSequenza, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 0, 0));
	    panel6s.add(labelDurataCicloSequenza, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
	    panel6s.add(durataCicloSequenza, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 0, 0));
	    panel4s.add(avviaSequenza, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 10, 0), 2, 2));
	    panel4s.add(pauseSequenza, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 10, 0), 2, 2));
	    panel4s.add(stopSequenza, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 10, 0), 2, 2));
	    panel5s.add(cicloSequenza, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
	            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 10, 0), 0, 0));
	    
	    labelSequenza.setFont(fontLabel);
	    sequenzaTextField.setFont(fontLabel);
	    labelSleep.setFont(fontLabel);
	    sleepSequenza.setFont(fontLabel);
	    labelDurataCicloSequenza.setFont(fontLabel);
	    durataCicloSequenza.setFont(fontLabel);
	    cicloSequenza.setFont(fontLabel);
	    seleziona.setFont(fontLabel);
	    
	    seleziona.setIcon(imageApri);
	    seleziona.setFocusable(false);
	    seleziona.setToolTipText("Seleziona");
	    seleziona.setBorder(BorderFactory.createRaisedBevelBorder());
	    seleziona.setBackground(colorButton);
	    seleziona.setText("File ");
	    avviaSequenza.setIcon(imageAvvia);
	    avviaSequenza.setFocusable(false);
	    avviaSequenza.setToolTipText("Avvia sequenza");
	    avviaSequenza.setBorder(BorderFactory.createRaisedBevelBorder());
	    pauseSequenza.setIcon(imagePause);
	    pauseSequenza.setFocusable(false);
	    pauseSequenza.setToolTipText("Pausa");
	    pauseSequenza.setBorder(BorderFactory.createRaisedBevelBorder());
	    stopSequenza.setIcon(imageStop);
	    stopSequenza.setFocusable(false);
	    stopSequenza.setToolTipText("Stop");
	    stopSequenza.setBorder(BorderFactory.createRaisedBevelBorder());
	    
	    tabbedPane.setFocusable(false);

        
        pannelloLog.addTab("Log applicativo", scrollerApplicativo);
        pannelloLog.addTab("Log utente", scrollerUtente);
        pannelloLog.addTab("Debug", scrollerDebug);
        
        panelNorthMain.add(panelNorth, BorderLayout.NORTH);
        panelNorthMain.add(panelComando, BorderLayout.CENTER);
        panelNorthMain.add(panelSequenza, BorderLayout.SOUTH);
               
		mainPanel.add(panelLeft, BorderLayout.WEST);
		mainPanel.add(pannelloLog, BorderLayout.CENTER);
		mainPanel.add(panelNorthMain, BorderLayout.NORTH);
		mainPanel.add(panelDown, BorderLayout.SOUTH);

		tabbedPane.addTab("Controllo OPEN", mainPanel);
		tabbedPane.addTab("Controllo Video", new PanelVideocontrollo());
				
		//aggiungo tutto al contentPane
		contentPane.add(labelBanner, BorderLayout.NORTH);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		
		this.addComponentListener(new ComponentListener(){

			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				Dimension d = getSize();

				Image i = bannerAlto.getImage().getScaledInstance((int)d.getWidth(),81, Image.SCALE_DEFAULT);
				ImageIcon icon2 = new ImageIcon(i);
				//mainPanel.add(new JLabel(icon), BorderLayout.NORTH);
				labelBanner.setIcon(icon2);
			}

			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
			}
			
		});
		
		abilitaPass.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(abilitaPass.isSelected()){
					//password open abilitata
					password.setEnabled(true);
					labelPassword.setEnabled(true);
					
				}else{
					//password open disabilitata
					password.setEnabled(false);
					labelPassword.setEnabled(false);
					
				}				
	         }
	      });
	
		inviaComando.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {		

				String pass = null;
				passInt = null;
				
				if(comandoOpen.getText().length() != 0){	
					if(gestSocketComandi.stato == 0){ //non sono ancora connesso
						if(testCampiInseriti()){
							if(abilitaPass.isSelected()){
								pass = password.getText();
								passInt = new Integer(pass);
								passwordOpen = passInt.intValue(); //password in formato int
							}
						
							if(gestSocketComandi.connect(ip.getText(), 20000, passwordOpen)){
														    
							    abilitaInterfaccia(false);
							    
							    inviaComandoThread = null;
							    inviaComandoThread = new InviaComandoThread(comandoOpen.getText());
							    inviaComandoThread.start();
							 									
							}else{
								//Connessione KO
							}
							pass = null;
						}
					}else if(gestSocketComandi.stato == 3){ //sono giÃ  connesso
						
					    abilitaInterfaccia(false);
					    
					    inviaComandoThread = null;
					    inviaComandoThread = new InviaComandoThread(comandoOpen.getText());
					    inviaComandoThread.start();
					    							
					}else{
						scriviSulLog("%%%%%%%%%%%%% SITUAZIONE NON GESTITA %%%%%%%%%%%%%%%",2,0,0);
					}
				}else{
					scriviSulLog("comando open non valido",2,0,0);
				}
				
	         }
			
	      });
		
		
		clearLog.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				areaLogApplicativo.setText(null);
				areaLogDebug.setText(null);
				areaLogUtente.setText(null);
				System.gc();
	         }
	      });
		
		abilitaLogAppl.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logAppl = !logAppl;
	         }
	      });
		
		abilitaLogUtente.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logUtente = !logUtente;
	         }
	      });
		
		abilitaLogDebug.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logDebug = !logDebug;
	         }
	      });
		
		
		/************************************************/
		/*Se viene premuto la socket comandi non viene 	*/
		/*chiusa al termine dell'invio di un comando,   */
		/*altrimenti sÃ¬.							    */
		/************************************************/
		mantieniSocket.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	        }
		});
		
		monitor.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				attivaMonitor();
			}
	      });
		
		fileButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestFileLog = new GestisciFileLog();
	        }
		});
		
		attiva.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gestFileLog != null){
					if (gestFileLog.attiva()) {
						fileButton.setEnabled(false);
						fileTextField.setEnabled(false);
						appendi.setEnabled(false);
						attiva.setEnabled(false);
					}
				}
			}
		});
		
		interrompi.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gestFileLog != null) {
					if (gestFileLog.interrompi()) {
						fileButton.setEnabled(true);
						fileTextField.setEnabled(true);
						appendi.setEnabled(true);
						attiva.setEnabled(true);				
					}
				}
	        }
		});
		
		cicloComando.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AvviaCicloComando.statoCiclo = 1;
				avviaCicloComando = null;
				System.gc();
				avviaCicloComando = new AvviaCicloComando("thread per il ciclo di un comando",comandoOpen.getText(),sleepTimeComando);
				avviaCicloComando.start();
				threadDurata = null;
				System.gc();
				threadDurata = new ThreadDurata("Thread per la durata del ciclo", 0, durataTimeComando);
				threadDurata.start();
				inviaComando.setEnabled(false);
				cicloComando.setEnabled(false);
				stopComando.setEnabled(true);
				pauseSequenza.setEnabled(false);
				stopSequenza.setEnabled(false);
				avviaSequenza.setEnabled(false);				
	        }
		});
		
		stopComando.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(avviaCicloComando != null) AvviaCicloComando.statoCiclo = 0;
				inviaComando.setEnabled(true);
				cicloComando.setEnabled(true);
				stopComando.setEnabled(true);
				pauseSequenza.setEnabled(true);
				stopSequenza.setEnabled(true);
				avviaSequenza.setEnabled(true);
	        }
		});
		

		seleziona.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sequenzaOpen = new SequenzaOpen();				
	        }
		});
				
		avviaSequenza.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sequenzaOpen != null){
					SequenzaOpen.statoSequenzaOpen = 1;	
					avviaSequenzaOpen = null;   
					System.gc();
					avviaSequenzaOpen = new AvviaSequenzaOpen("Avvio Thread sequenza open", sleepTimeSequenza);
					avviaSequenzaOpen.start();
					threadDurata = null;
					System.gc();
					threadDurata = new ThreadDurata("Thread per la durata del ciclo", 1, durataTimeSequenza);
					threadDurata.start();
					inviaComando.setEnabled(false);
					cicloComando.setEnabled(false);
					stopComando.setEnabled(false);
					pauseSequenza.setEnabled(true);
					stopSequenza.setEnabled(true);
					avviaSequenza.setEnabled(false);
				}
	        }
		});
				
		pauseSequenza.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sequenzaOpen != null){
					sequenzaOpen.pause();
					inviaComando.setEnabled(true);
					cicloComando.setEnabled(true);
					stopComando.setEnabled(true);
					pauseSequenza.setEnabled(true);
					stopSequenza.setEnabled(true);
					avviaSequenza.setEnabled(true);
				}
	        }
		});
				
		stopSequenza.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sequenzaOpen != null){
					sequenzaOpen.stop();
					inviaComando.setEnabled(true);
					cicloComando.setEnabled(true);
					stopComando.setEnabled(true);
					pauseSequenza.setEnabled(true);
					stopSequenza.setEnabled(true);
					avviaSequenza.setEnabled(true);
				}
	        }
		});
		
		cicloSequenza.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		sleepComando.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sleepComando.getSelectedIndex() == 0) sleepTimeComando = 0;//0 sec
				else if(sleepComando.getSelectedIndex() == 1) sleepTimeComando = 50;//0,05 sec
				else if(sleepComando.getSelectedIndex() == 2) sleepTimeComando = 100;//0,1 sec
				else if(sleepComando.getSelectedIndex() == 3) sleepTimeComando = 500;//0,5 sec
				else if(sleepComando.getSelectedIndex() == 4) sleepTimeComando = 1000;//1 sec
				else if(sleepComando.getSelectedIndex() == 5) sleepTimeComando = 2000;//2 sec
				else if(sleepComando.getSelectedIndex() == 6) sleepTimeComando = 3000;//3 sec
				else if(sleepComando.getSelectedIndex() == 7) sleepTimeComando = 4000;//4 sec
				else if(sleepComando.getSelectedIndex() == 8) sleepTimeComando = 5000;//5 sec
				else if(sleepComando.getSelectedIndex() == 9) sleepTimeComando = 10000;//10 sec
	        }
		});
		
		sleepSequenza.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sleepSequenza.getSelectedIndex() == 0) sleepTimeSequenza = 0;//0 sec
				else if(sleepSequenza.getSelectedIndex() == 1) sleepTimeSequenza = 50;//0,05 sec
				else if(sleepSequenza.getSelectedIndex() == 2) sleepTimeSequenza = 100;//0,1 sec
				else if(sleepSequenza.getSelectedIndex() == 3) sleepTimeSequenza = 500;//0,5 sec
				else if(sleepSequenza.getSelectedIndex() == 4) sleepTimeSequenza = 1000;//1 sec
				else if(sleepSequenza.getSelectedIndex() == 5) sleepTimeSequenza = 2000;//2 sec
				else if(sleepSequenza.getSelectedIndex() == 6) sleepTimeSequenza = 3000;//3 sec
				else if(sleepSequenza.getSelectedIndex() == 7) sleepTimeSequenza = 4000;//4 sec
				else if(sleepSequenza.getSelectedIndex() == 8) sleepTimeSequenza = 5000;//5 sec
				else if(sleepSequenza.getSelectedIndex() == 9) sleepTimeSequenza = 10000;//10 sec
	        }
		});
		
		durataCiclo.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(durataCiclo.getSelectedIndex() == 0) durataTimeComando = -1;//infinita
				else if(durataCiclo.getSelectedIndex() == 1) durataTimeComando = 60000; //1 min
				else if(durataCiclo.getSelectedIndex() == 2) durataTimeComando = 120000;//2 min
				else if(durataCiclo.getSelectedIndex() == 3) durataTimeComando = 180000;//3 min
				else if(durataCiclo.getSelectedIndex() == 4) durataTimeComando = 300000;//5 min
				else if(durataCiclo.getSelectedIndex() == 5) durataTimeComando = 900000;//15 min
				else if(durataCiclo.getSelectedIndex() == 6) durataTimeComando = 1800000;//30 min
				else if(durataCiclo.getSelectedIndex() == 7) durataTimeComando = 3600000;//1 ora
				else if(durataCiclo.getSelectedIndex() == 8) durataTimeComando = 5400000;//1 ora e 30 min
				else if(durataCiclo.getSelectedIndex() == 9) durataTimeComando = 7200000;//2 ore
	        }
		});
		
		durataCicloSequenza.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(durataCicloSequenza.getSelectedIndex() == 0) durataTimeSequenza = -1;//infinita
				else if(durataCicloSequenza.getSelectedIndex() == 1) durataTimeSequenza = 60000;//1 min
				else if(durataCicloSequenza.getSelectedIndex() == 2) durataTimeSequenza = 120000;//2 min
				else if(durataCicloSequenza.getSelectedIndex() == 3) durataTimeSequenza = 180000;//3 min
				else if(durataCicloSequenza.getSelectedIndex() == 4) durataTimeSequenza = 300000;//5 min
				else if(durataCicloSequenza.getSelectedIndex() == 5) durataTimeSequenza = 900000;//15 min
				else if(durataCicloSequenza.getSelectedIndex() == 6) durataTimeSequenza = 1800000;//30 min
				else if(durataCicloSequenza.getSelectedIndex() == 7) durataTimeSequenza = 3600000;//1 ora
				else if(durataCicloSequenza.getSelectedIndex() == 8) durataTimeSequenza = 5400000;//1 ora e 30 min
				else if(durataCicloSequenza.getSelectedIndex() == 9) durataTimeSequenza = 7200000;//2 ore
	        }
		});
				
		close.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
	        }
		});
		
		about.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrameAboutBox dlg = new FrameAboutBox();
			    Dimension dlgSize = dlg.getPreferredSize();
			    Dimension frmSize = getSize();
			    Point loc = getLocation();
			    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
			    dlg.setModal(true);
			    dlg.pack();
			    dlg.show();
	        }
		});
		
		windowComando.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(windowComando.isSelected()) panelComando.setVisible(true);
				else panelComando.setVisible(false);
			}
		});
		
		windowSequenza.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(windowSequenza.isSelected()) panelSequenza.setVisible(true);
				else panelSequenza.setVisible(false);
	        }
		});
	
		tabbedPane.addMouseListener(new MouseListener(){

            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                if(tabbedPane.getSelectedIndex() == 0){
				    
				}else if(tabbedPane.getSelectedIndex() == 1){
				    PanelVideocontrollo.indirizzoIP.setText(ip.getText());
				    PanelVideocontrollo.password.setText(password.getText());
				}
				
				
				
            }

            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }
		    
		});
		
		this.setSize(new Dimension(980,760));   
		this.setTitle("ClientTCP OPEN");   
		
		//se presente carico la configurazione di default
		BufferedReader bufferReader = null;
		File file = new File("./conf.txt");
		try{
			bufferReader = new BufferedReader(new FileReader(file));
			int i = 0;
			while(i < 5){
				String temp = bufferReader.readLine();
				if(i == 0) ip.setText(temp);
				if(i == 1) password.setText(temp);
				if(i == 2){
					if(temp.equals("true")) mantieniSocket.setSelected(true);
					else mantieniSocket.setSelected(false);
				}
				if(i == 3){
					/*if(temp.equals("true")) monitor.setSelected(true);
					else monitor.setSelected(false);
					attivaMonitor();*/
				}
				if(i == 4) comandoOpen.setText(temp);				
				i++;    
			}
		}
		catch (Exception e){
			scriviSulLog("--- Impossibile caricare l'ultima configurazione utilizzata ---\n",0,0,0);
			//e.printStackTrace();
		}			
		
		scriviSulLog("/*** PROGRAMMA AVVIATO CORRETTAMENTE ***/\n",0,0,0);
	}//chiude jbinit()
	 
	/**
	 * Attiva la monitor, se l'attivazione avviene con successo viene fleggata
	 * la checkbox corrispondente, altrimenti no.
	 * 
	 */
	static public void attivaMonitor(){
		String pass = null;
		passInt = null;
		if(monitor.isSelected()){
			if(testCampiInseriti()){
				if(abilitaPass.isSelected()){
					pass = password.getText();
					passInt = new Integer(pass);
					passwordOpen = passInt.intValue(); //password in formato int
				}
				if(gestSocketMonitor.connect(ip.getText(), 20000, passwordOpen)){
					//connessione OK thread monitorizza giÃ  attivato
				}else{
					monitor.setSelected(false);
				}
				pass = null;
			}else{ 
				//se ip e password non sono stati digitati deseleziono la casella
				monitor.setSelected(false);
			}
		}else if (!monitor.isSelected()){
			if (gestSocketMonitor != null)	gestSocketMonitor.close();
		}else {
			scriviSulLog("----SITUAZIONE NON GESTITA----",2,0,0);
		}
	}
		
	/**
	 * Procedura che si occupa di verificare che l'indirizzo ip e la password open siano
	 * stati inseriti al momento della connessione.
	 *
	 * @return true se ip e password sono stati inseriti, 0 altrimenti
	 */
	static public boolean testCampiInseriti(){
		if(ip.getText().length() == 0){
			scriviSulLog("ip non valido",2,0,0);
			return false; //dati non corretti
		}else if (abilitaPass.isSelected()){ //devo controllare la password
			if(password.getText().length() == 0){
				scriviSulLog("password open non valida",2,0,0);
				return false; //dati non corretti
			}else return true; //se non devo fare il controllo sulla password restituisco true
		}else return true; //dati corretti
	} 
	 
	
	/**
	 * Gestisce le informazioni di log, stampandole a video
	 * e scrivendole sul file di log.
	 * 
	 * @param testo Scritta da loggare
	 * @param verbosityLevel Definisce il grado di verbosity del testo da stampare, 
	 * 		  0 livello applicativo, utente e debug
	 * 		  1 livello utente e debug	
	 * 		  2 livello debug
	 * @param color Vale 0 se il testo deve essere stampato in nero, 1 se deve essere rosso
	 * @param log Vale 1 se il testo deve essere scritto sul file di log, 0 altrimenti
	 */
	public static void scriviSulLog(String testo, int verbosityLevel, int color, int log){
		//Color c;
		df = miaData.returnDate();
		/*if (color == 1) c = Color.RED;
		else c = Color.BLACK;*/
		//df = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss").format(new Date());
		
		if(log == 1 && gestFileLog != null){
			flag = null;
			if(mantieniSocket.isSelected() && monitor.isSelected()) flag = "VV";
			else if(mantieniSocket.isSelected() && !monitor.isSelected()) flag = "VX";
			else if(!mantieniSocket.isSelected() && monitor.isSelected()) flag = "XV";
			else if(!mantieniSocket.isSelected() && !monitor.isSelected()) flag = "XX";
			
			gestFileLog.scrivi(df,ip.getText(),password.getText(),flag,testo);
		}
		
		boolean ok = false;
		if(testo.startsWith("SCS:")){
			ok = visualizzaSCS.isSelected();
		}else if(testo.startsWith("Tx:") || testo.startsWith("Rx:")){
			ok = visualizzaOpen.isSelected();
		}else if(testo.startsWith("Mon:")){
			ok = visualizzaMon.isSelected();
		}else if(visualizzaCommenti.isSelected()){
			ok = true;
		}

		switch(verbosityLevel){
			case 0:	
			    if(PanelVideocontrollo.attivaLog.isSelected()){
			        if(testo.startsWith("Tx:") || testo.startsWith("Rx:") || testo.startsWith("Mon:") || testo.startsWith("SCS:"))
			            PanelVideocontrollo.logArea.append(testo+"\n");
			    }
				if(logAppl && ok) areaLogApplicativo.append(df+"    "+testo+"\n");
				if(logUtente) areaLogUtente.append(df+"    "+testo+"\n");	
				if(logDebug) areaLogDebug.append(df+"    "+testo+"\n");
				break;
			case 1:
				if(logUtente) areaLogUtente.append(df+"    "+testo+"\n");	
				if(logDebug) areaLogDebug.append(df+"    "+testo+"\n");	
				break;
			case 2:
				if(logDebug) areaLogDebug.append(df+"    "+testo+"\n");
				break;
			default:
				break;		
		}
  
		//posizione la scrollbar in fondo
		areaLogApplicativo.setCaretPosition(areaLogApplicativo.getDocument().getLength());
		areaLogUtente.setCaretPosition(areaLogUtente.getDocument().getLength());
		areaLogDebug.setCaretPosition(areaLogDebug.getDocument().getLength());
		PanelVideocontrollo.logArea.setCaretPosition(PanelVideocontrollo.logArea.getDocument().getLength());
	}
	
	
	static public int abilitaInterfaccia(boolean stato){
	    
	    inviaComando.setEnabled(stato);
	    cicloComando.setEnabled(stato);
	    stopComando.setEnabled(stato);	    
	    
	    return (0);
	}
	
}//chiude classe
