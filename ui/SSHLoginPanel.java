package SSHViewer.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class SSHLoginPanel extends JPanel {
	
	private JTextField tf_host = new JTextField(20);
	private JTextField tf_login = new JTextField(20);
	private JPasswordField pf_password = new JPasswordField();
	
	private JButton bu_Connect = new JButton("Connect");
	private JButton bu_Cancel = new JButton("Cancel");
	private JButton bu_Reset = new JButton("Reset");
	
	private static String hostName; 
	private static String user;
	private static String pwd;
	
	// execute the standard commands
	private static final String[] COMMANDS = {"df -h", 
									"tail /tmp/messages"};//,
//									"sqlplus -s hub20/ch1ch3n1tza @count"};
				
				
	
	public SSHLoginPanel() {
		initUI();
	}
	
	private void initUI() {
		
		// imlpementing listeners for buttons
		this.bu_Connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doConnect();
			}
		});
		
		this.bu_Cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
		
		this.bu_Reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doReset();
			}
		});
		
		
		
		
		// doing the ui
		this.setLayout(new GridBagLayout());
		/////////// HOST ////////////////
		this.add(new JLabel("Host Name: "),
      			new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
      			GridBagConstraints.WEST, GridBagConstraints.NONE,
      			new Insets(0, 10, 0, 10), 0, 0));
		
		
		this.add(this.tf_host,
				new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
		      	GridBagConstraints.CENTER,
		      	GridBagConstraints.HORIZONTAL,
		      	new Insets(0, 0, 0, 0), 0, 0));
		
		
		/////////// username /////////////
		this.add(new JLabel("User Name: "),
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
      			GridBagConstraints.WEST, GridBagConstraints.NONE,
      			new Insets(0, 10, 0, 10), 0, 0));
		
		this.add(this.tf_login,
				new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
			    GridBagConstraints.WEST,
			    GridBagConstraints.HORIZONTAL,
			    new Insets(0, 0, 0, 0), 0, 0));
		
		/////////// password //////////////
		this.add(new JLabel("Password"),
				new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
      			GridBagConstraints.WEST, GridBagConstraints.NONE,
      			new Insets(0, 10, 0, 10), 0, 0));
		
		this.add(this.pf_password,
				new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
			    GridBagConstraints.WEST,
			    GridBagConstraints.HORIZONTAL,
			    new Insets(0, 0, 0, 0), 0, 0));
		
		/////////	buttons	////////////////
		this.add(this.bu_Connect,
				new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
      			GridBagConstraints.WEST, GridBagConstraints.NONE,
      			new Insets(0, 10, 0, 10), 0, 0));
		
		this.add(this.bu_Cancel,
				new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
      			GridBagConstraints.WEST, GridBagConstraints.NONE,
      			new Insets(0, 10, 0, 10), 0, 0));
      			
		this.add(this.bu_Reset,
				new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
      			GridBagConstraints.WEST, GridBagConstraints.NONE,
      			new Insets(0, 10, 0, 10), 0, 0));
		
		this.setSize(500,200);
		this.setVisible(true);
		
	}
	
	
	private void doConnect() {
		
		String hostName = this.tf_host.getText().trim();
		String userName = this.tf_login.getText().trim();
		String password = new String(this.pf_password.getPassword());
		
		if (hostName != null || hostName != "" ||
			userName != null || userName != "" ||
			password != null || password != "") {
				
				setHostName(hostName);
				setUserName(userName);
				setPassword(password);
		
				SSHViewer.ssh.RemoteSSH rsshcd = 
						new SSHViewer.ssh.RemoteSSHCoordinator(hostName, userName, password);
				
				String[] outputMessages = rsshcd.executeCommands(COMMANDS);
				
				if (outputMessages != null) {
					RemoteSSHViewer.generateResultsPanel(hostName, COMMANDS, outputMessages);
				}
				
				rsshcd.close();
				
				
				
		} else {
			JOptionPane.showMessageDialog(null, "Error", "Eror in input", JOptionPane.ERROR_MESSAGE);
		}
			
		
	}
	
	private void doReset() {
		this.tf_host.setText("");
		this.tf_login.setText("");
		this.pf_password.setText("");
	}
	
	private void doCancel() {
	}
	
	private String getHost() {
		String theHost = this.tf_host.getText().trim();
		setHostName(theHost);
		return theHost;		
	}
	
	private String getUser() {
		String login = this.tf_login.getText().trim();
		setUserName(login);
		return login;
	}	
	
	private String getPassword() {
		String pwd = new String(this.pf_password.getPassword());
		setPassword(pwd);
		return pwd;
	}
	
	
	private void setHost(String host) {
		hostName = host;
	}
	
	private void setUser(String user) {
		user = user;
	}
	
	private void setPsswd(String pwd) {
		pwd = pwd;
	}
	
	public static void setHostName(String h) {
		hostName = h;
	}
	
	public static void setUserName(String u) {
		user = u;
	}
	
	public static void setPassword(String p) {
		pwd = p;
	}
	
	public static String getHostName() {
		return hostName;
	}
	
	public static String getUserName() {
		return user;
	}
	
	public static String getPwd() {
		return pwd;
	}
	
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.getContentPane().add(new SSHLoginPanel());
		f.show();
		f.pack();
	}
}

