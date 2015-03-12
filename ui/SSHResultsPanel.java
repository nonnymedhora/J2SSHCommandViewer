// vrsn1 -- here the commands issued by the SSHresultsPanel
// are applicable only to the last host which was used
// see vrsn2 below

////package SSHViewer.ui;
////
////import SSHViewer.ssh.RemoteSSHCoordinator;
////
////import java.awt.*;
////import java.awt.event.*;
////import javax.swing.*;
////import javax.swing.border.*;
////import javax.swing.event.*;
////
////
////public class SSHResultsPanel extends JPanel {
////	
////	private String[] results;
////	private static JTextArea[] areas = null;
////	private static String[] commands;
////	private static String host;
////
////	public SSHResultsPanel (String hostName, String[] theCommands, String[] resultmessages) {
////	
////		host = hostName;
////		
////		commands = new String[theCommands.length];
////		for(int i = 0; i < theCommands.length; ++i) {
////			commands[i] = theCommands[i];
////		}
////		setCommands(commands);
////		
////		
////		results = new String[resultmessages.length];
////		for (int i = 0; i < resultmessages.length; ++i) {
////			results[i] = resultmessages[i];
////		}
////		initUI();
////	}
////	
////	private void initUI() {
////		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
////		
////		if (results != null) {
////			this.areas = new JTextArea[results.length];
////			
////			for(int i = 0; i < results.length; ++i) {
////				JPanel resultViewPanel = new JPanel ();
////				resultViewPanel.setLayout(new BoxLayout(resultViewPanel, BoxLayout.Y_AXIS));
////				this.areas[i] = new JTextArea(results[i]);
////				JScrollPane scp = new JScrollPane(this.areas[i]);
////				Border bd = BorderFactory.createRaisedBevelBorder();
////				TitledBorder td = BorderFactory.createTitledBorder(bd, commands[i]);
////				scp.setViewportBorder(td);
////				resultViewPanel.add(scp);
////				
////				this.add(resultViewPanel);
////			}
////		}
////		
////		this.setVisible(true);
////	}
////	
////	
////	private String[] getTheCommands() {
////		String[] theCommands = new String[commands.length];
////		for (int i =  0; i < commands.length; ++i) {
////			theCommands[i] = commands[i];
////		}
////		return theCommands;
////	}
////	
////	private void setCommands(String[] theCommands) {
////		for (int i = 0; i < theCommands.length; ++i) {
////			commands[i] = theCommands[i];
////		}
////	}
////	
////	
////	public static String[] getCommands() {
////		return commands;
////	}
////	
////	
////	public static JTextArea[] getTextAreas() {
////		return areas;
////	}
////}

//vrsn 2
// here there is one panel
// containing 2 panels
// top panel containing the results of basic commands sent on startup
// and the second of the same command buttons
// the remove button will still be controlled via the RemoteSSHViewer
// to remove the panel from view and will control the cache via the
// delete method.
package SSHViewer.ui;

import SSHViewer.ssh.RemoteSSHCoordinator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;


public class SSHResultsPanel extends JPanel {
	
	private String[] results;
	private static JTextArea[] areas = null;
	private static String[] commands;
	private static String host;

	public SSHResultsPanel (String hostName, String[] theCommands, String[] resultmessages) {
	
		host = hostName;
		
		commands = new String[theCommands.length];
		for(int i = 0; i < theCommands.length; ++i) {
			commands[i] = theCommands[i];
		}
		setCommands(commands);
		
		
		results = new String[resultmessages.length];
		for (int i = 0; i < resultmessages.length; ++i) {
			results[i] = resultmessages[i];
		}
		initUI();
	}
	
	private void initUI() {
		
		this.setLayout(new BorderLayout());
		
		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new GridLayout(1, results.length));//FlowLayout());// BoxLayout(this, BoxLayout.X_AXIS));
		
		JPanel commandPanel = new JPanel();
		commandPanel.setLayout(new FlowLayout());//BoxLayout(this, BoxLayout.X_AXIS));
		
		if (results != null) {
			this.areas = new JTextArea[results.length];
			JButton[] bu_Commands = new JButton[commands.length];
			
			for(int i = 0; i < results.length; ++i) {
				JPanel resultViewPanel = new JPanel ();
				resultViewPanel.setLayout(new BorderLayout());//(new BoxLayout(resultViewPanel, BoxLayout.Y_AXIS));
				
//				System.out.println(results[i]);
				
				this.areas[i] = new JTextArea(results[i]);
				JScrollPane scp = new JScrollPane(this.areas[i]);
				Border bd = BorderFactory.createRaisedBevelBorder();
				TitledBorder td = BorderFactory.createTitledBorder(bd, commands[i]);
				scp.setViewportBorder(td);
				resultViewPanel.add(scp);
				resultViewPanel.setVisible(true);
				resultsPanel.add(resultViewPanel);
				
				
				bu_Commands[i] = new JButton(commands[i]);
				bu_Commands[i].setActionCommand(commands[i]);
				bu_Commands[i].addActionListener(commandListener);
			
				commandPanel.add(bu_Commands[i]);
			
			}
			
			commandPanel.add(new JLabel("Execute Remote Shell Command"));
    		final JTextField tf_Command = new JTextField(20);
    		commandPanel.add(tf_Command);
    	
	    	final JButton bu_Command = new JButton("Execute");
	    	bu_Command.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			executeCustomCommand(host, tf_Command.getText().trim());
	    		}
	    	});
    	
    		commandPanel.add(bu_Command);
		}
		resultsPanel.setVisible(true);
		commandPanel.setVisible(true);
		
		this.add(resultsPanel, BorderLayout.CENTER);
		this.add(commandPanel, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	
	private String[] getTheCommands() {
		String[] theCommands = new String[commands.length];
		for (int i =  0; i < commands.length; ++i) {
			theCommands[i] = commands[i];
		}
		return theCommands;
	}
	
	private void setCommands(String[] theCommands) {
		for (int i = 0; i < theCommands.length; ++i) {
			commands[i] = theCommands[i];
		}
	}
	
	
	public static String[] getCommands() {
		return commands;
	}
	
	
	public static JTextArea[] getTextAreas() {
		return areas;
	}
	
	
	private static ActionListener commandListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JButton) {
				String command = e.getActionCommand();
				
				SSHViewer.ssh.RemoteSSH rsshcd = 
						new SSHViewer.ssh.RemoteSSHCoordinator(host);

				String outputMessage = rsshcd.executeCommand(command);
				
				String[] commands = SSHResultsPanel.getCommands();
				
				int j = 0;
				
				for (int i = 0; i < commands.length; ++i) {
					if (command.equalsIgnoreCase(commands[i])) {
						j = i;
					}
				}
				
				JTextArea[] areas = SSHResultsPanel.getTextAreas();
				
				areas[j].setText(outputMessage);
		
			}
		}
	};
	
	// either df-h, tail, etc -- the std commands
	// issued when the login button is clicked
	public static String executeStandardCommand(String host, String command) {
		SSHViewer.ssh.RemoteSSH rsshcd = 
						new SSHViewer.ssh.RemoteSSHCoordinator(host);
		String[] userData = (String[]) rsshcd.getUserData(host);
		
		if (userData != null) {
			String outputMessage = rsshcd.executeCommand(command);
			return outputMessage;
		}
		
		return null;
	}
	
	public static void executeCustomCommand(String hostName, String command) {
		SSHViewer.ssh.RemoteSSH rsshcd = new SSHViewer.ssh.RemoteSSHCoordinator(host);
		String[] userData = (String[]) rsshcd.getUserData(hostName);
		
		if (userData != null) {
			String outputMessage = rsshcd.executeCommand(command);
			JFrame fr = new JFrame(host + ": " + command);
			fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			JTextArea area = new JTextArea(outputMessage);				
			JScrollPane sp = new JScrollPane(area);
			fr.getContentPane().add(sp);
			
			fr.setLocation(200,200);
			fr.setSize(300,300);
			fr.show();
		}
	}
	
	public void delete() {	
		SSHViewer.ssh.RemoteSSH rsshcd = new SSHViewer.ssh.RemoteSSHCoordinator(host, false);
		rsshcd.deleteHost(host);
		
	}	
		
}