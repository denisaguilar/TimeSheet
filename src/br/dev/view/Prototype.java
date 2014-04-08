package br.dev.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import br.dev.func.Function;
import br.dev.func.Util;

import com.sun.jmx.snmp.tasks.Task;
import java.awt.Toolkit;

public class Prototype {

	private JFrame frame;
	
	private static int updateSeconds = 1;
	private static int clockUpdateInterval = 5;
	
	private Thread updateThread = null;
	
	private Function func;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Prototype window = new Prototype();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Prototype() {		
		initialize();
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
		
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Prototype.class.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		frame.setResizable(false);
		frame.setBounds(100, 100, 744, 578);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Console", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(373, 11, 344, 496);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Now", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(55, 82, 254, 76);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
				
		final Label textHour = new Label();
		textHour.setAlignment(Label.CENTER);
		textHour.setBounds(6, 16, 242, 53);
		panel_1.add(textHour);
		textHour.setFont(new Font("Tahoma", Font.BOLD, 25));
		textHour.setEnabled(false);
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				SimpleDateFormat format = new SimpleDateFormat("HH:mm");
				while(true){
					try {					
						textHour.setText(format.format(new Date()));	
						Thread.sleep(clockUpdateInterval * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		
		thread.start();
		
		final JTextPane textPane = new JTextPane();
		textPane.setBounds(6, 16, 332, 467);
		panel.add(textPane);
		textPane.setEnabled(false);
		textPane.setEditable(false);	
		
		JPanel panelStartTime = new JPanel();
		panelStartTime.setBorder(new TitledBorder(null, "Start Time", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelStartTime.setBounds(19, 208, 344, 46);
		frame.getContentPane().add(panelStartTime);
		panelStartTime.setLayout(null);
		
		final JTextPane textStartTime = new JTextPane();
		textStartTime.setBounds(6, 16, 332, 23);
		panelStartTime.add(textStartTime);
		textStartTime.setEnabled(false);
		textStartTime.setEditable(false);
		
		JPanel panelEndTime = new JPanel();
		panelEndTime.setLayout(null);
		panelEndTime.setBorder(new TitledBorder(null, "End Time", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelEndTime.setBounds(19, 264, 344, 46);
		frame.getContentPane().add(panelEndTime);
		
		final JTextPane textEndTime = new JTextPane();
		textEndTime.setEnabled(false);
		textEndTime.setEditable(false);
		textEndTime.setBounds(6, 16, 332, 23);
		panelEndTime.add(textEndTime);
		
		final JPanel panelRemaining = new JPanel();
		panelRemaining.setLayout(null);
		panelRemaining.setBorder(new TitledBorder(null, "Time Remaining", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelRemaining.setBounds(19, 321, 344, 46);
		frame.getContentPane().add(panelRemaining);
		
		final JTextPane textRemain = new JTextPane();
		textRemain.setEnabled(false);
		textRemain.setEditable(false);
		textRemain.setBounds(6, 16, 332, 23);
		panelRemaining.add(textRemain);
		
		JPanel panelElapsed = new JPanel();
		panelElapsed.setLayout(null);
		panelElapsed.setBorder(new TitledBorder(null, "Time Elapsed", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelElapsed.setBounds(19, 378, 344, 46);
		frame.getContentPane().add(panelElapsed);
		
		final JTextPane textElapsed = new JTextPane();
		textElapsed.setEnabled(false);
		textElapsed.setEditable(false);
		textElapsed.setBounds(6, 16, 332, 23);
		panelElapsed.add(textElapsed);
		
		JPanel panelPredicted = new JPanel();
		panelPredicted.setLayout(null);
		panelPredicted.setBorder(new TitledBorder(null, "Time Predicted", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPredicted.setBounds(19, 435, 344, 46);
		frame.getContentPane().add(panelPredicted);
		
		final JTextPane textPredicted = new JTextPane();
		textPredicted.setEnabled(false);
		textPredicted.setEditable(false);
		textPredicted.setBounds(6, 16, 332, 23);
		panelPredicted.add(textPredicted);
		
		final JTextPane txtSession = new JTextPane();
		txtSession.setText("Session not initialized.");
		txtSession.setEnabled(false);
		txtSession.setEditable(false);
		txtSession.setBounds(19, 487, 344, 20);
		frame.getContentPane().add(txtSession);
		
		final JLabel textTimeBase = new JLabel();
		textTimeBase.setEnabled(false);
		textTimeBase.setText("0h 0m 0s");
		textTimeBase.setBounds(90, 178, 76, 20);
		frame.getContentPane().add(textTimeBase);
		
		
		
		final Task task = new Task() {	
			boolean isDone;
						
			@Override
			public void run() {
				isDone = false;
				while(!isDone){
					try {
						Thread.sleep(updateSeconds * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			
//					long timeElapsed = func.getTimeSheet().getTimeElapsed();
//					long timeRemain = func.getTimeSheet().getTimeRemain();
//					
//					textPane.setText("> : "+ (timeRemain < 0));
//					
//					if(timeRemain <= 0){
//						
//					}					
						
					//alterar para temp values
					func.updateTimeElapsed(true, updateSeconds);
					textElapsed.setText(Util.printTime(func.getTimeSheet().getTimeElapsed(), "%sh %sm %ss"));
				
					func.updateTimeRemain(true, updateSeconds);
					textRemain.setText(Util.printTime(func.getTimeSheet().getTimeRemain(), "%sh %sm %ss"));		
									
				}				
			}
			
			@Override
			public void cancel() {
				isDone = true;				
			}
		};
		
		
		
		final JButton buttonInitialTime = new JButton("Check-in");
		final JButton buttonFinalTime = new JButton("Checkout");
		
		final JButton btnNow =  new JButton("Now");;
		final JButton btnCustom = new JButton("Custom");;
		
		
		buttonFinalTime.setEnabled(false);
		buttonFinalTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnNow.setVisible(true);
				btnCustom.setVisible(true);									
			}
		});
		buttonFinalTime.setBounds(232, 11, 89, 23);
		frame.getContentPane().add(buttonFinalTime);
		
		
		//Custom
		btnCustom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CustomTime custon = new CustomTime();
								
				if(!custon.showDialog()){
					return;
				}
				
				Date customTime = custon.getCustomDate();			
				
				if(buttonFinalTime.isEnabled()){
					
					//Regra de negocio
					if(customTime.getTime() < func.getTempTimePack().getStart()){
						JOptionPane.showMessageDialog(null, "O tempo final não pode ser inferiror ao inicial", "Erro de tempo", JOptionPane.WARNING_MESSAGE);
						return;						
					}					
					
					func.setFinalTime(customTime);					
					
					// TODO Migrar thread para temp
					try {
						task.cancel();
						updateThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					long time = func.getTempTimePack().getEnd();
					textEndTime.setText(func.toDate(time).toString());
									
					buttonInitialTime.setEnabled(true);
					buttonFinalTime.setEnabled(false);
					
					//TODO alterar para core
					func.updateTimeElapsed(false, 0);
					textElapsed.setText(Util.printTime(func.getTimeSheet().getTimeElapsed(), "%sh %sm %ss"));
					
					//TODO alterar para core
					func.updateTimeRemain(false, 0);
					textRemain.setText(Util.printTime(func.getTimeSheet().getTimeRemain(), "%sh %sm %ss"));
					
					updateConsole(textPane);
					
					buttonInitialTime.setEnabled(true);
					buttonFinalTime.setEnabled(false);	
					
				}else if(buttonInitialTime.isEnabled()){
					func.setInitialTime(customTime);
										
					// se o valor é custon, o time elapsed deve ser calculado antes.
//					func.getTimeSheet().setTimeElapsed(new Date().getTime() - func.getTempTimePack().getStart());
					// alem disso o valor do time remaining tambem deve ser atualizado.
//					func.getTimeSheet().setTimeRemain(func.getTimePerDay() - func.getTimeSheet().getTimeElapsed());
					
					updateThread = new Thread(task);					
					updateThread.start();
					
					long time = func.getTempTimePack().getStart();					
					textStartTime.setText(func.toDate(time).toString());
					
					long predictedTime = func.getTimeSheet().getTimePredicted();
					textPredicted.setText(func.toDate(predictedTime).toString());					
							
					buttonInitialTime.setEnabled(false);
					buttonFinalTime.setEnabled(true);	
					
					textEndTime.setText("");
				}
								
				
				btnNow.setVisible(false);
				btnCustom.setVisible(false);
				
			}
		});
		
		btnCustom.setVisible(false);
		btnCustom.setBounds(185, 48, 89, 23);
		frame.getContentPane().add(btnCustom);
		
		
		//Now
		btnNow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				if(buttonFinalTime.isEnabled()){
					func.setFinalTime(null);
					
					try {
						task.cancel();
						updateThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					long time = func.getTempTimePack().getEnd();
					textEndTime.setText(func.toDate(time).toString());
									
					buttonInitialTime.setEnabled(true);
					buttonFinalTime.setEnabled(false);
					
					func.updateTimeElapsed(false, 0);
					textElapsed.setText(Util.printTime(func.getTimeSheet().getTimeElapsed(), "%sh %sm %ss"));
					
					func.updateTimeRemain(false, 0);
					textRemain.setText(Util.printTime(func.getTimeSheet().getTimeRemain(), "%sh %sm %ss"));
					
					updateConsole(textPane);
					
					buttonInitialTime.setEnabled(true);
					buttonFinalTime.setEnabled(false);	
				}else if(buttonInitialTime.isEnabled()){
					func.setInitialTime(null);
				
					updateThread = new Thread(task);
					updateThread.start();
					
					long time = func.getTempTimePack().getStart();
					
					textStartTime.setText(func.toDate(time).toString());
					
					long predictedTime = func.getTimeSheet().getTimePredicted();
					textPredicted.setText(func.toDate(predictedTime).toString());			
								
					buttonInitialTime.setEnabled(false);
					buttonFinalTime.setEnabled(true);
					
					textEndTime.setText("");
				}				
				
				btnNow.setVisible(false);
				btnCustom.setVisible(false);
			}
		});
		btnNow.setVisible(false);
		btnNow.setBounds(86, 48, 89, 23);
		frame.getContentPane().add(btnNow);
		
		buttonInitialTime.setEnabled(false);
		buttonInitialTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNow.setVisible(true);
				btnCustom.setVisible(true);
				
				txtSession.setText("Session sequence: "+ (func.getTimeSheet().getTimePacks().size()+1));	
								
			}
		});
		buttonInitialTime.setBounds(132, 11, 89, 23);
		frame.getContentPane().add(buttonInitialTime);
		
		JButton buttonTimeSheet = new JButton("New TimeSheet");
		buttonTimeSheet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				NewTimeSheet setTime = new NewTimeSheet();
				
				if(!setTime.showDialog(clockUpdateInterval, updateSeconds))
					return;
				
				updateSeconds = setTime.getUpdateSeconds();
				clockUpdateInterval = setTime.getClockUpdateSeconds();
								
				if(updateThread != null)
					try {
						task.cancel();
						updateThread.join();
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				
				func = new Function();				
				buttonFinalTime.setEnabled(false);
				
				func.setTimePerDay(setTime.getTimePerDay());
				textTimeBase.setText(Util.printTime(func.getTimePerDay(), "%sh %sm %ss"));
				
				btnNow.setVisible(false);
				btnCustom.setVisible(false);
				
				textPane.setText("New TimeSheet Created.");
				buttonInitialTime.setEnabled(true);				
				
				textStartTime.setText("");
				textEndTime.setText("");
				textRemain.setText("");
				textElapsed.setText("");
				textPredicted.setText("");
				txtSession.setText("Session not initialized.");
			}
		});
		buttonTimeSheet.setBounds(10, 11, 112, 23);
		frame.getContentPane().add(buttonTimeSheet);
		
		
		
		
		JLabel lblTimeBase = new JLabel("Work Time");
		lblTimeBase.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTimeBase.setBounds(22, 178, 76, 20);
		frame.getContentPane().add(lblTimeBase);
		
				
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnInfo = new JMenu("Info");
		menuBar.add(mnInfo);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPane.setText("\n\n TimeSheet 2014 - Version 0.3.4 \n\n Source: https://github.com/denisaguilar/TimeSheet/releases");
			}
		});
		mnInfo.add(mntmAbout);
		
	}
	
	private void updateConsole(JTextPane textPane){
		textPane.setText(func.generateInfo());
	}
}
