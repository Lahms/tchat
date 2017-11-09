package tchat;

import java.io.IOException;

public interface LinkActionListener {
	public void actionPerformed(String mess) ; //sending text
	public void actionPerformed2(String user); //select nick
	public void actionPerformed3(String user); //change nick
	public void actionPerformed4(String user); //leave
}
