package tchat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

public class Historique {
	String data;
	InetAddress addrhist;
	File monfichier;
	
	public void Historique(InetAddress addrhist) throws IOException
	{
		this.addrhist=addrhist;
		monfichier = new File("/home/sbesnard/Bureau/tchat/"+addrhist.getHostAddress());
		if(monfichier.exists())
		{
			BufferedReader br = new BufferedReader(new FileReader(monfichier));
			String line;
			while ((line = br.readLine()) != null) {
			   data = data + line + "\n";
			}
			br.close();
			
		}
		else
		{
			monfichier.createNewFile();
		}
	}
	
	public String get_data()
	{
		return data;
	}
	
	public void set_data(String moredata) throws IOException
	{
		FileWriter writer = new FileWriter(monfichier);
		PrintWriter printWriter = new PrintWriter(writer);
		printWriter.print(moredata);
		writer.flush();
		writer.close();
	}
	
	
}
