package tchat;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;



public class IHM extends JFrame {
	
	//FINALS
	private final static String newline = "\n";
	private final SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	//GENERAL VAR
	private String nickname = "nickname";
	private String choosenone="";
	private int delay = 15000; //milliseconds
	private String texte_date;
	boolean modifie = false ;
	int bugjcombo = 0 ;
	//GENERAL
	private JPanel panel1;
	Vector<LinkActionListener> recepteurs = new Vector<LinkActionListener>(); // liste des recepteurs d'événements
	
	//PARTI CANAUX
	private JPanel zonecanaux;
	private JButton bouton1 ;
	
	private JPanel panellistderoulante ;
	private JLabel listderoulante;
	private JLabel labelpseudo;
	private JPanel zoneecriturenick;
	
	private JComboBox<String> combo = new JComboBox<String>();
	
	//PARTI AFFICHAGE
	private JPanel zoneecriture;
	private JLabel labelnick;
    private JScrollPane scroll;
    private JTextArea display;
	private JTextField write;
	private JTextField writenick;
	
	

	//LIAISON LISTENER => INTERFACE => SERVEUR /////////////////////////////////////////////////////////////////////////////
	
	public void addServ2ActionListener(LinkActionListener e)
	{
	    recepteurs.addElement(e);
	
	}
	
	public void affiche(String messageinc)
	{
		display.append( messageinc + newline);
	}
	
	public void leave_actionPerformed(ActionEvent e)
	{
		for (Enumeration<LinkActionListener> enumere = recepteurs.elements(); enumere.hasMoreElements();)
		    ((LinkActionListener)enumere.nextElement()).actionPerformed4(nickname);
		    
	}
	
	public void changeuser_actionPerformed(ActionEvent e)
	{
		for (Enumeration<LinkActionListener> enumere = recepteurs.elements(); enumere.hasMoreElements();)
		    ((LinkActionListener)enumere.nextElement()).actionPerformed3(nickname);
		    
	}
	
	public void senduser_actionPerformed(ActionEvent e)
	{
		for (Enumeration<LinkActionListener> enumere = recepteurs.elements(); enumere.hasMoreElements();)
		    ((LinkActionListener)enumere.nextElement()).actionPerformed2(choosenone);
		    
	}
	
	public void writing_actionPerformed(ActionEvent e, String str)
	{
		for (Enumeration<LinkActionListener> enumere = recepteurs.elements(); enumere.hasMoreElements();)
		    ((LinkActionListener)enumere.nextElement()).actionPerformed(str);
		    write.setText("");
	}
	
	//GETTER SETTER ///////////////////////////////////////////////////////////////////////////////////////////
	public void Set_list_Nick(String str){combo.addItem(str);}
	
	public void Remove_List_Nick(String str){combo.removeItem(str);}
	
	public String Get_choosen(){return choosenone;}
	
	public String getNick()
	{
		return nickname;
	}
	
	public boolean Summon_box()
	{
		if(verifieAction("Voulez vous accepter la session ?"))
		{
			return true;
		}
		else{return false;}
	}
	
	private boolean verifieAction(String message) {
	   
	      if (JOptionPane.showConfirmDialog(null, message,"Accepter vous la session ?",JOptionPane.YES_NO_OPTION)==1){return false;}
	      else{return true;}
	    
	    
	  }
	
	// CONSTUCT  /////////////////////////////////////////////////////////////////////////////////////////// 
	public IHM() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	    //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// FENETRE ///////////////////////
		texte_date = sdf.format(new Date());
		this.setSize(550,300);
		panel1 = new JPanel();
		zoneecriturenick = new JPanel();
		zonecanaux = new JPanel();
		zoneecriture = new JPanel();
		display = new JTextArea();
		display.setEditable(false); 
		write = new JTextField();
		writenick = new JTextField();
		labelpseudo = new JLabel("Pseudo :");
		listderoulante = new JLabel("Liste pseudo :");
		panellistderoulante = new JPanel();
		
		this.setTitle("système de ClavardAge diStribué Interactif MultutilIsateur temps Réel");
		
		display.setLineWrap(true);
		display.setWrapStyleWord(true);
		
		labelnick = new JLabel("      " +   nickname + " " + texte_date);
		
		JScrollPane scroll = new JScrollPane(display);
		
				
		this.getContentPane().add(panel1);
		bouton1 = new JButton(" STOP ");
		//bouton2 = new JButton("canal 2 ");
		
		panellistderoulante.setBorder(new EmptyBorder(5, 10, 15, 10));
		panellistderoulante.setLayout(new BorderLayout());
		panellistderoulante.add(listderoulante);
		panellistderoulante.add(combo, BorderLayout.SOUTH); 
		
		
		panel1.setLayout(new BorderLayout());
		panel1.add(zonecanaux, BorderLayout.WEST);
		zonecanaux.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		panel1.add(zoneecriture);
		zonecanaux.setLayout(new GridLayout(3,0));
		zoneecriturenick.setLayout(new BorderLayout());
		zoneecriture.setLayout(new BorderLayout());
		
		zonecanaux.add(bouton1);
		//zonecanaux.add(bouton2);
		zonecanaux.add(panellistderoulante);
		zonecanaux.add(zoneecriturenick);
		
		zoneecriturenick.add(writenick);
		zoneecriturenick.add(labelpseudo, BorderLayout.NORTH);
		
		zoneecriture.add(scroll);
		zoneecriture.add(write, BorderLayout.SOUTH);
		zoneecriture.add(labelnick, BorderLayout.NORTH);
		

		
		
		// LISTENERS //////////////////////////////////////////////////////////////////
		// LISTENER CROIX ROUGE
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		      JOptionPane.showMessageDialog(null,"Utilise le bouton STOP rohhh");
		      }
		    });
		
		// LISTENER QUITTE
		bouton1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("it works");
				leave_actionPerformed(e);
				System.exit(0);
			}
		}
				);
		
		// LISTENER CONNECTION PEER
		combo.addActionListener(new ActionListener()
		{		
			public void actionPerformed(ActionEvent e)
			{
				if(bugjcombo>0)
				{
					choosenone=(String)combo.getSelectedItem();
					senduser_actionPerformed(e);
					System.out.println("wtf");
					
				}
				bugjcombo=bugjcombo+1;
			}
		}
		);
		
		// LISTENER ECRITURE TEXTE
		write.addActionListener(new ActionListener()
		{	
			private String text;
			public void actionPerformed(ActionEvent e)
			{
				 text = write.getText();
				 writing_actionPerformed(e, text);
				
			}
		}
		);
		
		// LISTENER CHANGE PSEUDO
		writenick.addActionListener(new ActionListener()
		{	
			private String text;
			public void actionPerformed(ActionEvent e)
			{
				boolean ok=true;
				text = writenick.getText();
				if(text.length()<9)
				{
					for(int i = 0 ; i < combo.getItemCount() ; i++)
					{
			            if (combo.getItemAt(i).equals(text)) 
			            {
			            	display.append("Pseudo déja pris" + newline);
			            	ok=false;
			            }
			           
			        }
					
					if(ok)
					{	
						nickname = text;
						labelnick.setText("      " + nickname + " "+ texte_date);
						changeuser_actionPerformed(e);
						
					}
				}
				else{display.append("Votre pseudo peut contenir max 8 caracteres" + newline);}
			}
		}
		);
		
		// LISTENER HORLOGE
		 ActionListener taskPerformer = new ActionListener() {
		 public void actionPerformed(ActionEvent evt) {
			  
			  texte_date = sdf.format(new Date());
			  
			  labelnick.setText("      " + nickname + " "+ texte_date);
			  
		      }
		  };
		  
		//launch horloge
		new Timer(delay, taskPerformer).start();
		//visible
		this.setVisible(true);
		
	}

	
	

}
