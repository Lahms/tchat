package tchat;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;


public class Session extends JFrame {

	private int numsession;
	private InetAddress addrdist;
	
	private final static String newline = "\n";
	private final SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
	private String nickname = "nickname";
	private int delay = 15000; //milliseconds
	private String texte_date;
	private JPanel panel1;
	Vector<LinkActionListener> recepteurs = new Vector<LinkActionListener>(); // liste des recepteurs d'�v�nements
	
	
	private JTextArea display;
	private JTextField write;

	private JComboBox<String> combo = new JComboBox<String>();
	//PARTI CANAUX
	private JPanel zonecanaux;
	private JButton bouton1 ;
	
	//PARTI AFFICHAGE
	private JPanel zoneecriture;
	private JLabel labelnick;
	
	Historique hist;
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public int get_numession()
	{
		return numsession;
	}
	
	public InetAddress get_addr()
	{
		return addrdist;
	}
	
	public void addServ2ActionListener(LinkActionListener e)
	{
	    recepteurs.addElement(e);
	
	}
	
	
	public void Affiche(String text)
	{
		display.append(text + newline);
	}
	
	public void leave_actionPerformed(ActionEvent e)
	{
		for (Enumeration<LinkActionListener> enumere = recepteurs.elements(); enumere.hasMoreElements();)
		    ((LinkActionListener)enumere.nextElement()).actionPerformed6(numsession);
		    
	}
	
	public void writing_actionPerformed(ActionEvent e, String str)
	{
		for (Enumeration<LinkActionListener> enumere = recepteurs.elements(); enumere.hasMoreElements();)
		    ((LinkActionListener)enumere.nextElement()).actionPerformed5(str,numsession);
			System.out.println("TEEXXXTE2 : "+str);
			this.Affiche(str);
			try {
				hist.set_data(str);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    write.setText("");
		    
	}
	

	public Session(int code, String IPdist)
	{
		this.numsession=code;
		try {
			this.addrdist=InetAddress.getByName(IPdist);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		hist = new Historique();
		
		texte_date = sdf.format(new Date());
		this.setSize(550,300);
		panel1 = new JPanel();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		zoneecriture = new JPanel();
		display = new JTextArea();
		display.setEditable(false); 
		write = new JTextField();
		zonecanaux = new JPanel();
		bouton1 = new JButton(" STOP ");
		this.setTitle("Session n");
		this.getContentPane().add(panel1);
		display.setLineWrap(true);
		display.setWrapStyleWord(true);
		
		labelnick = new JLabel("      " +   nickname + " " + texte_date);
		
		JScrollPane scroll = new JScrollPane(display);
		
				


		

		panel1.setLayout(new BorderLayout());	
		panel1.add(zoneecriture);
		panel1.add(zonecanaux,BorderLayout.WEST);
		
		zoneecriture.setLayout(new BorderLayout());

		zoneecriture.add(scroll);
		zoneecriture.add(write, BorderLayout.SOUTH);
		zoneecriture.add(labelnick, BorderLayout.NORTH);
		
		zonecanaux.add(bouton1);
		
		this.Affiche(hist.get_data());
		
		// LISTENER ECRITURE TEXTE
				write.addActionListener(new ActionListener()
				{	
					private String text;
					public void actionPerformed(ActionEvent e)
					{
						 text = write.getText();
						 writing_actionPerformed(e, text);
						 System.out.println("TEEXXXTE :"+ text);
						
					}
				}
				);
				
		//LISTENER CROIX ROUGE	
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
						
						//System.exit(0);
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
		
		this.setVisible(true);
	}
	
}
