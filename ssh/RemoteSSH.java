package SSHViewer.ssh; 

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.session.SessionChannelClient;


public interface RemoteSSH extends Runnable {
	
	// this interface will serve as the bridge between
	// the gui and the data flow and interaction with the
	// remote SSH client -- this si done in such a way so as 
	// to decouple the application to the extent that
	// the gui has no knowledge of the SSH activities
	// and appears as good as a local call to RemoteSSH
	
	// the class implementing the interfacegenerates an instance 
	// of SSHManager via its factory SSHManager Factory -- 
	// it can then issuse commands 
	// to this SSHManager -- which will interact with the SSH
	// and relay messages from the SSHManager to the GUI
	
	
	public String[] executeCommands(String[] commands);
	public String executeCommand(String command);
	public String executeContinuousProcessCommand(String command);
	public String[] getReturnMessages();
	public String returnServerMessages(SSHManager manager);
	public String getHost();
	public String getUser();
	public String getPwd();
	public void close();
	
	
	public void addHost(String host, String[] data);
	public void deleteHost(String host);
	public String[] getUserData(String host);
	
}