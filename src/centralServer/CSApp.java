package centralServer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class CSApp {

	// *****GUI components*****
	private JFrame Main = new JFrame();
	private JButton Start = new JButton();
	private JButton Stop = new JButton();
	private JLabel lOnline = new JLabel();
	public static volatile JTextArea onlineClientsIps = new JTextArea();
	private JScrollPane spOnline = new JScrollPane();
	public static volatile JTextArea taConsole = new JTextArea();
	private JScrollPane spConsole = new JScrollPane();
	private JLabel clientsCounter = new JLabel();
	private JButton bClearConsole = new JButton();
	private Thread t1;

	public static void main(String[] args) {
		CSApp cs = new CSApp();
		cs.buildMainWindow();

	}

	// *****Creating user interface for server*****
	public void buildMainWindow() {
		Main.getContentPane().setLayout(null);
		Main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Main.setResizable(false);
		Main.setTitle("Central Server");
		Main.setSize(416, 556);
		Main.setLocation(300, 150);
		Main.setVisible(true);

		Start.setBounds(20, 30, 160, 40);
		Start.setBackground(Color.BLUE);
		Start.setForeground(Color.WHITE);
		Start.setText("Start server");
		Main.getContentPane().add(Start);

		Stop.setText("Stop server");
		Stop.setBounds(20, 130, 160, 40);
		Stop.setBackground(Color.BLUE);
		Stop.setForeground(Color.WHITE);
		Stop.setEnabled(false);
		Main.getContentPane().add(Stop);

		taConsole.setFont(new java.awt.Font("Arial", 0, 14));
		taConsole.setForeground(Color.BLACK);
		taConsole.setBackground(Color.WHITE);
		taConsole.setEditable(false);

		spConsole.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		spConsole.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spConsole.setViewportView(taConsole);
		Main.getContentPane().add(spConsole);
		spConsole.setBounds(10, 240, 380, 250);

		Main.getContentPane().add(bClearConsole);
		bClearConsole.setBounds(10, 491, 380, 20);
		bClearConsole.setText("Clear console");
		bClearConsole.setBackground(Color.BLUE);
		bClearConsole.setForeground(Color.WHITE);

		Main.getContentPane().add(lOnline);
		lOnline.setText("Peers connected: ");
		lOnline.setBounds(210, 10, 150, 20);

		onlineClientsIps.setForeground(new java.awt.Color(0, 0, 255));

		spOnline.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spOnline.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spOnline.setViewportView(onlineClientsIps);
		Main.getContentPane().add(spOnline);
		spOnline.setBounds(210, 30, 180, 180);

		clientsCounter.setBounds(10, 220, 160, 20);
		Main.getContentPane().add(clientsCounter);

		// *****Calling actions*****
		MainWindowAction();
	}

	// *****Adding actions for buttons in GUI*****
	public void MainWindowAction() {
		// *****Starting the server*****
		Start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				actionStart();
			}
		});

		// *****Stopping the server*****
		Stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionStop();
			}
		});

		// *****Clearing console*****
		bClearConsole.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				taConsole.setText("");
			}
		});
	}

	// *****Creating instance of CentralServer and starting it as a thread*****
	public void actionStart() {

		CentralServer cs = new CentralServer();
		t1 = new Thread(cs);
		t1.start();

		Start.setEnabled(false);
		Stop.setEnabled(true);

	}

	// *****Exiting server*****
	public void actionStop() {
		CentralServer.ExitServer(t1);
		Start.setEnabled(true);
		Stop.setEnabled(false);
	}

	// *****Setters and getters*****
	public JButton getStart() {
		return Start;
	}

	public void setStart(JButton start) {
		Start = start;
	}

	public JButton getStop() {
		return Stop;
	}

	public void setStop(JButton stop) {
		Stop = stop;
	}

	public JScrollPane getSpConsole() {
		return spConsole;
	}

	public void setSpConsole(JScrollPane spConsole) {
		this.spConsole = spConsole;
	}

	public Thread getT1() {
		return t1;
	}

	public void setT1(Thread t1) {
		this.t1 = t1;
	}
}
