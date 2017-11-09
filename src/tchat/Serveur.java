package tchat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Timer;

public class Serveur implements Runnable, LinkActionListener {

	
	private IHM interfacee;
	final static int MAX_DATA_SIZE = 128;  // Taille maximale des données envoyées
	private InetAddress addr;
	private final InetAddress addrmulti = InetAddress.getByName("224.10.5.1");
	private int port;
	public int ttl;                       // paramètre : time to live
	private MulticastSocket socket;        // Socket multicast
	private boolean controle = false;
	
	private boolean dernierarrivant = false;

	private String ancienpseudo;
	private String actuelpseudo="nickname";
	private long timing ;
	private String messtext;
	HashMap<String, String> hmap = new HashMap<String, String>();
	//DATE
	private final SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	
		
	public Serveur(IHM interfacee,InetAddress addr,int port, int ttl) throws IOException
	{
	
		this.interfacee=interfacee;
		this.ttl=ttl;
		this.addr=addr;
		this.port=port;
		
		interfacee.addServ2ActionListener(this);

		hmap.put(interfacee.getNick(), InetAddress.getLocalHost().getHostAddress());
		
		socket = new MulticastSocket(port);
		socket.setTimeToLive(ttl);
		socket.joinGroup(addr);
		
		interfacee.Set_list_Nick("All");
		timing = System.currentTimeMillis();
		new Thread(this).start();
		
		sendpacket(2);
		
	}
	
	public void gestion_control(String addr,String data)
	{
		String[] tab = data.split(" ");
    	
    	try {
			if(tab[0].equals("aaaaaaaaa") && (addr.equals(InetAddress.getLocalHost().getHostAddress())))
				{System.out.println("reception trame A");
				if(dernierarrivant)
				{
					sendpacket(5);
				} 
				 hmap.put(tab[1], addr);
				 interfacee.Set_list_Nick(tab[1]);
				}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			if(tab[0].equals("uuuuuuuuu") && (addr.equals(InetAddress.getLocalHost().getHostAddress())))
			{
				System.out.println("reception trame U");
				String ancienpseudo = new String();
				ancienpseudo = tab[2];
				String ipancienpseudo = hmap.get(ancienpseudo);
				hmap.remove(ancienpseudo);
				hmap.put(tab[1], ipancienpseudo);
				
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(tab[0].equals("qqqqqqqqq") /*&& (addr.equals(InetAddress.getLocalHost().getHostAddress()))*/)
    		{
    		 System.out.println("reception trame Q");
    		 hmap.remove(tab[1]);
    		 interfacee.Remove_List_Nick(tab[1]);
    		 
    		}
    	
    	if(tab[0].equals("ddddddddd") /*&& (addr.equals(InetAddress.getLocalHost().getHostAddress()))*/)
		{
    		System.out.println("reception trame S");
    		dernierarrivant=true;
		 
		}
    	
    	if(tab[0].equals("iiiiiiiii") /*&& (addr.equals(InetAddress.getLocalHost().getHostAddress()))*/)
			{
				dernierarrivant=true;
				System.out.println("reception trame I");
				for( int i=1;i<(tab.length)-1;i=i+2)
					{
						hmap.put(tab[i],tab[i+1]);
						interfacee.Set_list_Nick(tab[i]);
					}
			}
    	/*
    	for(Entry<String, String> entry : hmap.entrySet()) {
    	    String cle = entry.getKey();
    	    String valeur = entry.getValue();
    	    System.out.println(cle);
    	    System.out.println(valeur);
    	    System.out.println("ttt");
    	}*/
    	
    	controle=false;
    }
	
	
	public String creation_packet(int code)
	{
		String mess = null;
		
		switch(code)
		{
		case 1: System.out.println("paquet lancement data");
				String texte_date = sdf.format(new Date());	
				mess = interfacee.getNick()+" "+"["+texte_date+"]" + " dit : "+ "\n"  + messtext;
				
				break;
				
		case 2: System.out.println("paquet lancement A");
				mess="aaaaaaaaa " +interfacee.getNick(); 
				
				break;
				
		case 3: System.out.println("paquet lancement U");
				mess="uuuuuuuuu " +interfacee.getNick()+" " + ancienpseudo;
				break;
		
		case 4: System.out.println("paquet lancement Q");
				mess="qqqqqqqqq " +interfacee.getNick();
				break;
				
		case 5: System.out.println("paquet lancement I");
				String temporaire="";
				for(Entry<String, String> entry : hmap.entrySet()) 
					{
					String cle = entry.getKey();
					String valeur = entry.getValue();
					temporaire=temporaire +cle+" "+valeur;
					}
  	 
				//addr=InetAddress.getByName(mess); RECUP ADDR UNICAST VERS dernier (trame A)
				mess="iiiiiiiii "+temporaire;
				dernierarrivant=false;
				break;
		
		case 6: System.out.println("paquet lancement S");
				//addr=InetAddress.getByName(mess); RECUP ADDR UNICAST VERS premier de la hmap 
				mess="ddddddddd ";
				break;
		
		
		}
		return mess;
	}
	
	public void sendpacket(int code)	
	{
		String mess;
		mess = creation_packet(code);
		//mess= "iiiiiiiii "+"trust"+" 192.168.2.5"+" theo"+ " 198.56.2.3";
		System.out.println(mess);
		byte [] buffer= mess.getBytes() ;
	    DatagramPacket datag = new DatagramPacket(buffer,0,buffer.length, addr,port);
	    try {
			socket.send(datag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	private boolean verif_controle(String text)
	{
		String[] tab = text.split(" ");
		if(tab[0].length()>8)
		{
		return true;
		}
		else{return false;}
	}
	
	public void actionPerformed4(String user) {
		
		hmap.remove(actuelpseudo);
		sendpacket(4);
		if(dernierarrivant)
		{
			sendpacket(6);
		}
	}

	
	public void actionPerformed3(String pseudo) {
		
		try {
			String tempo = InetAddress.getLocalHost().getHostAddress();
			ancienpseudo=actuelpseudo;
			actuelpseudo=pseudo;
			System.out.println("QUOI"); 
			System.out.println("QUOI   "+ancienpseudo);
			hmap.remove(ancienpseudo);
			hmap.put(actuelpseudo, tempo);
			sendpacket(3);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*for(Entry<String, String> entry : hmap.entrySet()) {
    	    String cle = entry.getKey();
    	    String valeur = entry.getValue();
    	    System.out.println(cle);
    	    System.out.println(valeur);
    	    System.out.println("ttt");}*/
	}
	
	public void actionPerformed2(String user) { //choix de com 1 - 1
		System.out.println(user);
		
		if(user.equals("All"))
		{
			System.out.println("multi on");
			addr=addrmulti;
		}
		
		else
		{
			String ipdistante = hmap.get(user);
			//System.out.println(ipdistante);
			try {
				InetAddress addrtemporaire=InetAddress.getByName(ipdistante);
				addr=addrtemporaire;
				//System.out.println(addr.toString());
				interfacee.affiche("connexion to : "+user);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	public void actionPerformed(String mess)   //sending text
	{ 
	    	
	    messtext=mess;
	    sendpacket(1);
 
	}
	public void run()
	{
		//Check si on est seul
		if(timing - System.currentTimeMillis() > 1000 ){dernierarrivant=true;}
		
		//RECEPTION
		byte [] buffer = new byte[MAX_DATA_SIZE];
	    DatagramPacket datag = new DatagramPacket(buffer,buffer.length);
	    while(true) {
	    	
	        datag.setLength(MAX_DATA_SIZE);
	        
	        try {
				socket.receive(datag);
				} catch (IOException e) {
				e.printStackTrace();
				}
	        
	        String data =new String(buffer,datag.getOffset(), datag.getLength());
	        controle=verif_controle(data);
	        
	        
	        if(controle)
	        {
	        	System.out.println("trame de controle");
	        	InetAddress addrext = datag.getAddress();
	        	String addr = addrext.toString();
	        	
	        	gestion_control(addr,data);
	        
	        }
	        
	        else
	        {
	        	System.out.println("trame de data");
	        	interfacee.affiche(data + " ");
	        }
	        
	      }
	    
	}
	
		public static void main(String[] args) throws Exception 
		{

			IHM interfacee = new IHM();
		    Serveur serv = new Serveur(interfacee,InetAddress.getByName("224.10.5.1"),555, 10);
		    
		}

		
		

		
		
	}

