package SSHViewer.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


public class RemoteSSHViewer extends JFrame {
	
	private JPanel 		pa_loginPanel;
	private static JTabbedPane results = new JTabbedPane();
	private static JTextField tf_Command = null;
	
	public RemoteSSHViewer () {
		super("RemoteSSHViewer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.setSize(800,800);
		this.results.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    	this.initUI();
	}
	
	private void initUI() {
		Container container = this.getContentPane();
		
		container.setLayout(new BorderLayout());
		
		this.pa_loginPanel = new SSHLoginPanel();
		
		container.add(this.pa_loginPanel, BorderLayout.NORTH);
		
		this.results.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    
    	container.add(results, BorderLayout.CENTER);
    
    	container.setVisible(true);
    	this.setVisible(true);
	}
			
	public static void generateResultsPanel(String hostName, String[] commands, String[] resultMessages) {
		final JPanel panel = new JPanel(new BorderLayout());
		
		
		final JPanel topPanel = new JPanel(new BorderLayout());
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		final SSHResultsPanel resultsPanel = new SSHResultsPanel(hostName, commands, resultMessages);
		topPanel.add(resultsPanel, BorderLayout.CENTER);
		
    	JButton closeButton = new JButton("Remove");
    	final String host = hostName;
    	closeButton.addActionListener(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
        		results.remove(panel);
        		resultsPanel.delete();
      		}
    	});
    	
    	buttonPanel.add(closeButton);
    	
    	topPanel.setVisible(true);
    	buttonPanel.setVisible(true);
    	panel.add(topPanel, BorderLayout.CENTER);
    	panel.add(buttonPanel, BorderLayout.SOUTH);
    	
    	results.add(panel, hostName);
    	results.setSelectedComponent(panel);
    	
    	panel.setVisible(true);
	}
	
	public static void main(String [] args) {
		RemoteSSHViewer rsshv = new RemoteSSHViewer();
		rsshv.show();
	}
}