package SSHViewer.ssh; 

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.session.SessionChannelClient;


public class RemoteSSHCoordinator implements RemoteSSH {
	
	// this class will serve as the bridge between
	// the gui and the data flow and interaction with the
	// remote SSH client -- this si done in such a way so as 
	// to decouple the application to the extent that
	// the gui has no knowledge of the SSH activities
	// and appears as good as a local call to ot.
	
	// the class generates an instance of SSHManager via 
	// its factory SSHManager Factory -- it can then issuse commands 
	// to this SSHManager -- which will interact with the SSH
	// and relay messages from the SSHManager to the GUI
	
	// it maintains a HashMap of HostNames and User Data

	// note this means that only one user can access each host
	// this simpleies SSHResultsPanel to display the generic
	// commands again for each host reference( -- unlike as in vrsn1)

	
	private static SSHManager manager = null;
	private static SshClient client = null;
	
	private static String host;
	private static String user;
	private static String pwd;
	
	private static String[] messages = null;
	
	
	// sets up interaction with a new client
	public RemoteSSHCoordinator(String hName, String uName, String pwd) {
		host = hName;
		user = uName;
		pwd = pwd;
		
		manager = new SSHManagerFactory().getManager(hName, uName, pwd);
		client = manager.makeConnection(host);
		manager.checkConnectionStatus(client);
		
		if (manager.authenticateClient(client)) {
			// Client has been authenticated
			// add info to the HostCache
			addHost(host, new String[] {user, pwd});
		} else {
			System.out.println("Not Authenticated");
		}
	}
	
	// sets up interaction for an existing client
	public RemoteSSHCoordinator(String hostName) {
		HostCache cache = (HostCache)HostCache.getHostCache();
		host = hostName; 
		user = ((String[])cache.getUserData(hostName))[0];
		pwd = ((String[])cache.getUserData(hostName))[1];
		
		manager = new SSHManagerFactory().getManager(host, user, pwd);
		client = manager.makeConnection(host);
		manager.checkConnectionStatus(client);
		
		if (!manager.authenticateClient(client)) {
			// client already existed -- no need to manipulate
			// hashmap and reference maps -- if authentication
			// fails -- sayso
			System.out.println("Not Authenticated");
		}
	}
	
	// returns an instance of the RemoteSSHCoordinator for the
	// host without performing a connection --- will be
	// used solely for interacting with the hashmaps;
	public RemoteSSHCoordinator(String hostName, boolean connect) {
	}
	
	public String[] executeCommands(String[] commands) {
		manager.checkConnectionStatus(client);
		try {
			messages = manager.executeCommandsCaptureOutput(commands);
						
		} catch (Exception e) {
			System.out.println("Exception in RemoteSSHCoordinator[executeCommands-String[]]");
			e.printStackTrace();
		} finally {
		}
		return messages;
	}
	
	public String executeCommand(String command) {
		manager.checkConnectionStatus(client);
		try {
			String response = manager.executeCommandCaptureOutput(command);
			return response;
		} catch (Exception e) {
			System.out.println("Exception in RemoteSSHCoordinator[executeCommand-String]");
			e.printStackTrace();
		} finally {
		}
		return null;
	}
	
	
	// use this for commands which produce continuous output
	// or for commands which need to run on a continuous thread.
	// will implement later if needed
	public String executeContinuousProcessCommand(String command) {
//		manager.checkConnectionStatus(client);
//		try {
//			String response = manager
//		} catch(Exception e) {
//		} finally {
//		}
		return null;
	}
	
	public void run() {
	}
	
	
	public String[] getReturnMessages() {
		return messages;
	}
	
	public String returnServerMessages(SSHManager manager) {
		String srvrmsg = null;
		return srvrmsg;
	}
	
	
	public String getHost() {
		return host;
	}
	
	public String getUser(){
		return user;
	}
	
	public String getPwd() {
		return pwd;
	}
	
	
	public void close() {
		if (manager != null) {
			manager.exit();
			manager = null;
		}
	}
	
	
	public String[] getUserData(String host) {
		String userdata[] = ((HostCache)HostCache.getHostCache()).getUserData(host);
		if (userdata != null) {
			return userdata;
		}
		return null;
	}
	
	public void addHost(String host, String[] data) {
		HostCache.getHostCache().addHost(host, data);
	}
	
	
	public void deleteHost(String host) {
		HostCache cache = (HostCache)HostCache.getHostCache();
		int numref = (int)cache.getHostReferenceNum(host);
		if (numref == 1) {
			cache.deleteHostRefInfo(host);
			System.out.println("Removing Host: " + host + " from HostCache");
			cache.remove((String)host);
		} else {
			cache.reduceHostRefInfo(host);
		}
		
	}
	
	// two hashmaps -- one for the data
		// one for the number of references
	protected  static class HostCache extends java.util.HashMap {
		
		private static HostCache cacheInstance = new HostCache();
		
		private HostCache() {
			super();
		}
		
		public static HostCache getHostCache() {
				return cacheInstance;
		}
		
		
		private static java.util.HashMap refNumCache = new java.util.HashMap();
		
		public static String[] getUserInfo(String hostName) {
			
			return userData;
		}
		
		
		public String[] getUserData(String hostName) {
			String[] userdata = new String[2];
			userdata[0] = ((String[])this.get(hostName))[0];
			userdata[1] = ((String[])this.get(hostName))[1];
			userData(hostName, userdata);
			return userdata;
		}
		
		private static String[] userData = new String[2];
		
		private static void userData(String hostName, String[] userdata) {
			userData[0] = userdata[0];
			userData[1] = userdata[1];
		}
		
		
		public static void deleteHostRefInfo(String hostName) {	
			System.out.println("Deleting all references for host: + host");			
			refNumCache.remove(hostName);
		}
		
		
		public static int getHostReferenceNum(String host) {
			return ((Integer)refNumCache.get(host)).intValue();
		}
		
		
		public boolean containsHost(String hostName) {
			return this.get(hostName) != null ? true : false;
		}
		
		public void addHost(String hostName, String[] data) {
			// if host does not exist in cache
			// add it and set its ref count to 1
			if (this.get(hostName) == null) {
				this.put(hostName, data);
				System.out.println("Host: " + hostName + " added to HostCache");			
				this.refNumCache.put(hostName, new Integer("1") );
				System.out.println("HostRefNum set to 1: " + hostName);			
			} else {
				// increase count of hostName
				int currentRefNum = ((Integer)refNumCache.get(hostName)).intValue();
				this.refNumCache.put(hostName, new Integer(++currentRefNum));
				System.out.println("HostRefNum set to " + currentRefNum +": " + hostName);	
			}
		}
		
		public void reduceHostRefInfo(String host) {
			// decrease count of hostName
			int currentRefNum = ((Integer)refNumCache.get(host)).intValue();
			this.refNumCache.put(host, new Integer(--currentRefNum));
			System.out.println("HostRefNum reduced to " + currentRefNum + ": " + host);	
		}
		
	}
	
	
}