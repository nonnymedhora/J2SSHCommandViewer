package SSHViewer.ssh;

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.transport.TransportProtocolState;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.common.hosts.DialogHostKeyVerification;
	
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.connection.ChannelInputStream;
import com.sshtools.j2ssh.connection.ChannelOutputStream;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

// this class performs the actual interaction
// with the ssh 


public class SSHManager {
	
	private static String host = null;
	private static String user = null;
	private static String password = null;
	
	private static SshClient ssh = null;
	
	private static BufferedReader reader = null;
	
	private static final String[] commands = {
				"df -h",
				"tail /tmp/messages",
				"sqlplus -s hub20/ch1ch3n1tza @count" };
	
	public static final int COMMANDS = 3;
	
	private static String responseMessages[] = null;
	
	private SSHManager(String hName, String uName, String pwd)  {
		
		host = hName;
		user = uName;
		password = pwd;
		
		doDuty();
	}
	
	
	
	public static SSHManager getSSHManager(String hName, String uName, String pwd) {
		return new SSHManager(hName, uName, pwd);
	}
	
	
	private void doDuty() {
		System.out.println("Host is " + host);
		System.out.println("User is " + user);
		System.out.println("Password is " + password);
	}
	
	public static SshClient makeConnection(String host) {
		ssh = null;
		try {
			ssh = new SshClient();
			ssh.connect(host);
		} catch (Exception e) {
			System.out.println("Exception in SSHManager[makeConnection]");
			e.printStackTrace();
			return ssh = null;
		} finally {
			
		}
		return ssh;
	}
	
	
//	public static String getSSHServerResponseMessages(SshClient ssh) {
//		if (ssh != null) {
//			String response = "";
//			try {
//				SessionChannelClient sClient = ssh.getActiveSession();
//				ChannelOutputStream outS = sClient.getOutputStream();
//				
//				Bu
//				
//				
//			} catch (Exception e) {
//			} finally {
//			}
//			
//			return response;
//		}
//	}
	
	
	
	
	public static void checkConnectionStatus(SshClient ssh) {
		try {
			TransportProtocolState state = ssh.getConnectionState();

            switch(state.getValue()) {
            	case TransportProtocolState.DISCONNECTED :
                	System.out.println("Transport protocol has disconnected!");
                    break;
                case TransportProtocolState.CONNECTED :
                	System.out.println("Transport protocol is connected!");
                    break;
                case TransportProtocolState.NEGOTIATING_PROTOCOL :
                	System.out.println("Transport protocol being neogitated!");
                    break;
                case TransportProtocolState.PERFORMING_KEYEXCHANGE :
                	System.out.println("Transport protocol is performing key excahmge!");
                    break;
                default:
                	System.out.println("Transport protocol NOT INITIALIZED!");
                    break;
            }
        } catch(Exception e) {
        	System.out.println("Exception in SSHExample[checkConnectionState()]");
            e.printStackTrace();
        }
	}
	
	
	public  static boolean authenticateClient(SshClient sshClient) {
		boolean authenticated = false;
		try {
			PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
			
			// set user profile properties
			pwd.setUsername(user);
 			pwd.setPassword(password);
 			
 			// Authenticate the user
 			int result = sshClient.authenticate(pwd);
 			
 			if(result==AuthenticationProtocolState.COMPLETE) {
 				return authenticated = true;
 			} else if (result==AuthenticationProtocolState.FAILED) {
			    System.out.println("The authentication failed");
			    return authenticated = false;
			} else if(result==AuthenticationProtocolState.PARTIAL) {
	     		System.out.println("The authentication succeeded but another"
	                        	+ "authentication is required");
				return authenticated = false;
			} else if(result==AuthenticationProtocolState.COMPLETE) {
     			System.out.println("The authentication is complete");  
				return authenticated = false;
			}
		} catch (Exception e) {
		}
		return authenticated;
	}
	
	
	public void exit() {
		if (ssh != null) {
			ssh.disconnect();
			checkConnectionStatus(ssh);
			ssh = null;
		}
	}
	

	public static String[] executeCommandsCaptureOutput(String[] theCommands) {																											
		StringBuffer buff = null;
		String[] responses = null;
		String s;
		try {
			responses = new String[theCommands.length];
			
			SessionChannelClient[] sshClients = new SessionChannelClient[theCommands.length];
			for (int i = 0; i < theCommands.length; ++i) {
				buff = new StringBuffer();
			
				sshClients[i] = ssh.openSessionChannel();
				ChannelInputStream channelInputStream = sshClients[i].getInputStream();
				
				reader = new BufferedReader(new InputStreamReader(channelInputStream));
				
				System.out.println("Attempting command: " + theCommands[i]);
				
				sshClients[i].executeCommand(theCommands[i]);
				while((s = reader.readLine()) != null) {
					buff.append(s + "\n");
				}
				channelInputStream.close();
				reader.close();
				
				channelInputStream = null;
				reader = null;
				
				sshClients[i].close();
				sshClients[i] = null;
				s = "";
				
				responses[i] = buff.toString();
				buff = null;
				
			}
			
			if (responses != null) {
				setReturnMessages(responses);
			}
			
			ssh.disconnect();
			checkConnectionStatus(ssh);
			ssh = null;
			
		} catch (Exception e) {
			System.out.println("Exception in SSHManager[executeCommandsCaptureOutput]");
			e.printStackTrace();
		} finally {
			if (ssh != null) {
				ssh.disconnect();
				ssh = null;
			}
		}

		if (responses != null) {
			return responses;
		}
		
		return null;
		
	}
	
	
	
	public static String executeCommandCaptureOutput(String theCommand) {
			StringBuffer buff = new StringBuffer();
			String response = null;
			String s;
			
			try {
				SessionChannelClient sshClient = ssh.openSessionChannel();
				
				ChannelInputStream chIns = sshClient.getInputStream();
				
				reader = new BufferedReader(new InputStreamReader(chIns));
				
				System.out.println("Attempting command: " + theCommand);
				
				sshClient.executeCommand(theCommand);
				
				while((s = reader.readLine()) != null) {
					buff.append(s + "\n");
				}
				
				chIns.close();
				reader.close();
				sshClient.close();
				
				chIns = null;
				reader = null;
				sshClient = null;
				
				response = buff.toString();
				buff = null;
				
				if (response != null) {
					setReturnMessages(new String[] {response});
				}
				
				ssh.disconnect();
				checkConnectionStatus(ssh);
				ssh = null;	
				
				
		} catch (Exception e) {
			System.out.println("Exception in SSHManager[executeCommandCaptureOutput]");
			e.printStackTrace();
		} finally {
			if (ssh != null) {
				ssh.disconnect();
				ssh = null;
			}
		}
//		System.out.println("Host: " + host + "\nCommand: " + theCommand + "\n" + response);
		return response!=null ? response : null;
	}
	
	
	public static void setReturnMessages(String[] messages) {
		responseMessages = new String[messages.length];
		for (int i = 0; i < messages.length; ++i) {
			responseMessages[i] = messages[i];
		}
	}
	
	public static String[] getReturnMessages() {
		return responseMessages;
	}
	
	
	
	
		
}