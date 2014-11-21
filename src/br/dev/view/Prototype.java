package br.dev.view;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import br.dev.controller.business.Util;
import br.dev.model.listener.ButtonListener;

import javax.swing.JSeparator;

public class Prototype {

	private JFrame frmTimesheet;
	JTextPane console;

	public JLabel textIdle;
	public JLabel textElapsed;
	public JLabel textRemain;
	public JLabel labelHour;
	public JLabel textEndTime;
	public JLabel textStartTime;
	public JLabel textTimeBase;
	public JLabel textPredicted;
	public JLabel textPredPause;

	public JButton buttonInitialTime;
	public JButton buttonFinalTime;

	public JTextPane txtSession;
	public JTextArea lblhid;

	private ButtonListener listener;
	private JMenuItem mntmDailyBackup;

	/**
	 * Create the application.
	 */
	public Prototype() {
		initialize();
	}

	public void addListner(ButtonListener listener){
		this.listener = listener;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		String osInfo = System.getProperty("os.name");

		try {
			if(osInfo.toLowerCase().contains("windows"))
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			else
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		frmTimesheet = new JFrame();
		frmTimesheet.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		frmTimesheet.setTitle("TimeSheet "+Util.getVersionNumber());
		frmTimesheet.setIconImage(Toolkit.getDefaultToolkit().getImage(Prototype.class.getResource("/resources/document-open-recent.png")));
		frmTimesheet.setResizable(false);
		frmTimesheet.setBounds(100, 100, 744, 578);
		frmTimesheet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTimesheet.getContentPane().setLayout(null);
		frmTimesheet.setLocationRelativeTo(null);

		console = new JTextPane();

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Console", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(400, 6, 328, 514);
		frmTimesheet.getContentPane().add(panel);
		panel.setLayout(null);

		JPanel penelPredicted = new JPanel();
		penelPredicted.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Time Predicted", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		penelPredicted.setBounds(18, 45, 371, 121);
		frmTimesheet.getContentPane().add(penelPredicted);
		penelPredicted.setLayout(null);

		textPredicted = new JLabel();
		textPredicted.setHorizontalAlignment(SwingConstants.CENTER);
		textPredicted.setBounds(8, 16, 355, 95);
		penelPredicted.add(textPredicted);
		textPredicted.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
		textPredicted.setEnabled(false);

		console.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		console.setBounds(10, 16, 308, 487);
		panel.add(console);
		console.setEnabled(false);
		console.setEditable(false);

		JPanel panelStartTime = new JPanel();
		panelStartTime.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Check-in", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelStartTime.setBounds(18, 208, 174, 46);
		frmTimesheet.getContentPane().add(panelStartTime);
		panelStartTime.setLayout(null);

		textStartTime = new JLabel();
		textStartTime.setHorizontalAlignment(SwingConstants.CENTER);
		textStartTime.setBounds(6, 16, 166, 23);
		panelStartTime.add(textStartTime);
		textStartTime.setEnabled(false);

		JPanel panelEndTime = new JPanel();
		panelEndTime.setLayout(null);
		panelEndTime.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Check out", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelEndTime.setBounds(213, 208, 177, 46);
		frmTimesheet.getContentPane().add(panelEndTime);

		textEndTime = new JLabel();
		textEndTime.setHorizontalAlignment(SwingConstants.CENTER);
		textEndTime.setEnabled(false);
		textEndTime.setBounds(6, 16, 168, 23);
		panelEndTime.add(textEndTime);

		JPanel panelRemaining = new JPanel();
		panelRemaining.setLayout(null);
		panelRemaining.setBorder(new TitledBorder(null, "Time Remaining", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelRemaining.setBounds(18, 265, 174, 46);
		frmTimesheet.getContentPane().add(panelRemaining);

		textRemain = new JLabel();
		textRemain.setHorizontalAlignment(SwingConstants.CENTER);
		textRemain.setEnabled(false);
		textRemain.setBounds(6, 16, 166, 23);
		panelRemaining.add(textRemain);

		JPanel panelElapsed = new JPanel();
		panelElapsed.setLayout(null);
		panelElapsed.setBorder(new TitledBorder(null, "Time Elapsed", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelElapsed.setBounds(213, 265, 177, 46);
		frmTimesheet.getContentPane().add(panelElapsed);

		textElapsed = new JLabel();
		textElapsed.setHorizontalAlignment(SwingConstants.CENTER);
		textElapsed.setEnabled(false);
		textElapsed.setBounds(6, 16, 167, 23);
		panelElapsed.add(textElapsed);

		JPanel panelIdle = new JPanel();
		panelIdle.setLayout(null);
		panelIdle.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Time Idle", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelIdle.setBounds(18, 322, 174, 46);
		frmTimesheet.getContentPane().add(panelIdle);

		textIdle = new JLabel();
		textIdle.setHorizontalAlignment(SwingConstants.CENTER);
		textIdle.setEnabled(false);
		textIdle.setBounds(6, 16, 166, 23);
		panelIdle.add(textIdle);

		txtSession = new JTextPane();
		txtSession.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		txtSession.setText("Session not initialized.");
		txtSession.setEnabled(false);
		txtSession.setEditable(false);
		txtSession.setBounds(19, 493, 174, 20);
		frmTimesheet.getContentPane().add(txtSession);

		textTimeBase = new JLabel();
		textTimeBase.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		textTimeBase.setEnabled(false);
		textTimeBase.setText("0h 0m 0s");
		textTimeBase.setBounds(113, 178, 76, 20);
		frmTimesheet.getContentPane().add(textTimeBase);

		JLabel lblPredictedStop = new JLabel("Pred Pause");
		lblPredictedStop.setIcon(new ImageIcon(Prototype.class.getResource("/resources/icon-pause-16.png")));
		lblPredictedStop.setFont(new Font("Segoe UI Semilight", Font.BOLD, 11));
		lblPredictedStop.setBounds(215, 178, 90, 20);
		frmTimesheet.getContentPane().add(lblPredictedStop);

		textPredPause = new JLabel();
		textPredPause.setText("0h 0m 0s");
		textPredPause.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		textPredPause.setEnabled(false);
		textPredPause.setBounds(316, 178, 76, 20);
		frmTimesheet.getContentPane().add(textPredPause);

		buttonInitialTime = new JButton("Check-in");
		buttonInitialTime.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		buttonInitialTime.setIcon(new ImageIcon(Prototype.class.getResource("/resources/591258-in-16.png")));

		buttonFinalTime = new JButton("Check out ");
		buttonFinalTime.setIcon(new ImageIcon(Prototype.class.getResource("/resources/591248-out-16.png")));
		buttonFinalTime.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));


		buttonFinalTime.setEnabled(false);
		buttonFinalTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!listener.onCheckout())
					return;

				buttonInitialTime.setEnabled(true);
				buttonFinalTime.setEnabled(false);
			}
		});
		buttonFinalTime.setBounds(284, 11, 106, 23);
		frmTimesheet.getContentPane().add(buttonFinalTime);

		buttonInitialTime.setEnabled(false);
		buttonInitialTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!listener.onCheckin())
					return;

				buttonInitialTime.setEnabled(false);
				buttonFinalTime.setEnabled(true);

				textEndTime.setText("");
			}
		});

		buttonInitialTime.setBounds(171, 11, 106, 23);
		frmTimesheet.getContentPane().add(buttonInitialTime);

		final JButton buttonTimeSheet = new JButton("New TimeSheet");
		buttonTimeSheet.setIcon(new ImageIcon(Prototype.class.getResource("/resources/menu-alt-16.png")));
		buttonTimeSheet.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		buttonTimeSheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!listener.onNewTimeSheet())
					return;

				buttonFinalTime.setEnabled(false);
				console.setText("New TimeSheet Created.");
				buttonInitialTime.setEnabled(true);

				textStartTime.setText("");
				textEndTime.setText("");
				textRemain.setText("");
				textElapsed.setText("");
				textPredicted.setText("");
				textIdle.setText("");
				txtSession.setText("Session not initialized.");
			}
		});
		buttonTimeSheet.setBounds(19, 11, 131, 23);
		frmTimesheet.getContentPane().add(buttonTimeSheet);

		JLabel lblTimeBase = new JLabel("Work Time");
		lblTimeBase.setIcon(new ImageIcon(Prototype.class.getResource("/resources/prisoner-16.png")));
		lblTimeBase.setFont(new Font("Segoe UI Semilight", Font.BOLD, 11));
		lblTimeBase.setBounds(23, 177, 90, 20);
		frmTimesheet.getContentPane().add(lblTimeBase);

		labelHour = new JLabel();
		labelHour.setHorizontalAlignment(SwingConstants.RIGHT);
		labelHour.setText("0h 0m 0s");
		labelHour.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		labelHour.setEnabled(false);
		labelHour.setBounds(330, 493, 60, 20);
		frmTimesheet.getContentPane().add(labelHour);

		JLabel lblNow = new JLabel("Now");
		lblNow.setIcon(new ImageIcon(Prototype.class.getResource("/resources/icon-ios7-clock-outline-16.png")));
		lblNow.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNow.setFont(new Font("Segoe UI Semilight", Font.BOLD, 11));
		lblNow.setBounds(271, 493, 60, 20);
		frmTimesheet.getContentPane().add(lblNow);

		lblhid = new JTextArea("");
		lblhid.setBounds(18, 380, 372, 90);
		frmTimesheet.getContentPane().add(lblhid);
		lblhid.setBackground(SystemColor.control);
		lblhid.setEditable(false);
		lblhid.setLineWrap(true);
		lblhid.setFont(new Font("Segoe UI Semibold", Font.BOLD, 11));

		JMenuBar menuBar = new JMenuBar();
		frmTimesheet.setJMenuBar(menuBar);

		JMenu mnInfo = new JMenu("TimeSheet");
		menuBar.add(mnInfo);


		final JMenuItem mntmCheckOut = new JMenuItem("Check out");
		mntmCheckOut.setIcon(new ImageIcon(Prototype.class.getResource("/resources/591248-out-16.png")));
		final JMenuItem mntmCheckin = new JMenuItem("Check-in");
		mntmCheckin.setIcon(new ImageIcon(Prototype.class.getResource("/resources/591258-in-16.png")));

		JMenuItem mntmNewTimesheet = new JMenuItem("New TimeSheet");
		mntmNewTimesheet.setIcon(new ImageIcon(Prototype.class.getResource("/resources/menu-alt-16.png")));
		mntmNewTimesheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonTimeSheet.doClick();
			}
		});
		mntmNewTimesheet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnInfo.add(mntmNewTimesheet);

		mntmCheckin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonInitialTime.doClick();
			}
		});
		mntmCheckin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnInfo.add(mntmCheckin);

		mntmCheckOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonFinalTime.doClick();
			}
		});
		mntmCheckOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
		mnInfo.add(mntmCheckOut);

		JMenu mnData = new JMenu("Data");
		menuBar.add(mnData);

		mntmDailyBackup = new JMenuItem("Daily Backup");
		mntmDailyBackup.setIcon(new ImageIcon(Prototype.class.getResource("/resources/day-16.png")));
		mntmDailyBackup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.dailyBackup();
			}
		});
		mnData.add(mntmDailyBackup);

		JMenuItem mntmMonthlyBackup = new JMenuItem("Month Backup");
		mntmMonthlyBackup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.monthlyBackup();
			}
		});
		mntmMonthlyBackup.setIcon(new ImageIcon(Prototype.class.getResource("/resources/month-16.png")));
		mnData.add(mntmMonthlyBackup);

		JSeparator separator = new JSeparator();
		mnData.add(separator);

		JMenuItem mntmGenTab = new JMenuItem("Generate Calendar");
		mntmGenTab.setEnabled(false);
		mnData.add(mntmGenTab);

		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);

		JMenuItem mntmVerifyUpdates = new JMenuItem("Check for updates");
		mntmVerifyUpdates.setIcon(new ImageIcon(Prototype.class.getResource("/resources/cloud-16.png")));
		mnAbout.add(mntmVerifyUpdates);
		mntmVerifyUpdates.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK));
		mntmVerifyUpdates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CheckUpdates check = new CheckUpdates();
				check.showDialog();
				return;
			}
		});


		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setIcon(new ImageIcon(Prototype.class.getResource("/resources/info-16.png")));
		mnAbout.add(mntmAbout);
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				console.setText("\n TimeSheet 2014 - Version "+Util.getVersionNumber()+" \n Source: https://github.com/denisaguilar/TimeSheet"+ "\n\n Back to Console (Ctrl+Z)");
			}
		});

		mntmVerifyUpdates.doClick();
		frmTimesheet.setVisible(true);
	}

	public int showMessageChoice(final String message, final String title, final String icon){
		return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(Prototype.class.getResource(icon)));
	}

	public void showMessage(final String message, final String title, final String icon){
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				JOptionPane jOptionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.CLOSED_OPTION, new ImageIcon(Prototype.class.getResource(icon)));
				JDialog dialog = jOptionPane.createDialog(null, title);
				dialog.setLocationRelativeTo(null);
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);
			}
		});

		thread.start();
	}

	public void updateConsole(String info){
		if(info != null)
			console.setText(info);
		else{
			console.setText("Go ahead... make my day.");
		}
	}
}
