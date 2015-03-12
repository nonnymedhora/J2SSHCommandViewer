package SSHViewer.ssh;


import com.sshtools.j2ssh.SshClient;

public class SSHManagerFactory {
	
	private static int num = 0;	// number of channels
	
	
	public static SSHManager getManager(String host, String user, String pwd) {
		SSHManager manager = SSHManager.getSSHManager(host, user, pwd);
		++num;
		return manager;
	}
	
	public static int getNum() {
		return num;
	}
}	