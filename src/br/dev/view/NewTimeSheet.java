package br.dev.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import br.dev.model.business.Util;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import java.awt.Font;

public class NewTimeSheet{

	private long timeDay;
	private long predPause;
	private int updateSeconds = 1;
	private int clockUpdateSecods = 5;
	
	JDialog dialog;
	boolean isDone;
	private JTextField textUpdate;
	private JTextField textClockUpdate;
	private JTextField textTimeHour;
	private JTextField textTimeMinute;
	private JTextField textTimeSeconds;
	private JTextField textTimeIdleH;
	private JTextField textTimeIdleM;
	private JTextField textTimeIdleS;
	
	public long getPredPause(){
		return predPause;
	}

	public long getTimePerDay() {
		return timeDay;
	}
	
	public int getUpdateSeconds(){
		return updateSeconds;
	}
	
	
	public int getClockUpdateSeconds(){
		return clockUpdateSecods;
	}
	
	public void setUpdateSeconds(int value){
		updateSeconds = value;
	}
	
	
	public void setClockUpdateSeconds(int value){
		clockUpdateSecods = value;
	}	
	
	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */

	public boolean showDialog() {	
				
		dialog = new JDialog(null, JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setTitle("New TimeSheet");
		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(NewTimeSheet.class.getResource("/resources/icon-ios7-gear-16.png")));
		dialog.setResizable(false);
		dialog.addWindowListener(new WindowAdapter() {			
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
			}			
		});
				
		dialog.setBounds(100, 100, 342, 212);
		dialog.getContentPane().setLayout(null);
		dialog.setLocationRelativeTo(null);
		
		JLabel lblH = new JLabel("H");
		lblH.setBounds(213, 42, 29, 14);
		dialog.getContentPane().add(lblH);
		
		JLabel lblM = new JLabel("M");
		lblM.setBounds(258, 42, 29, 14);
		dialog.getContentPane().add(lblM);
					
		JLabel lblS = new JLabel("S");
		lblS.setBounds(301, 42, 29, 14);
		dialog.getContentPane().add(lblS);
		
		JLabel lblNewLabel = new JLabel("TimeBase (HH:MM:SS)");
		lblNewLabel.setIcon(new ImageIcon(NewTimeSheet.class.getResource("/resources/prisoner-16.png")));
		lblNewLabel.setBounds(10, 38, 161, 14);
		dialog.getContentPane().add(lblNewLabel);		
		
		JLabel lblUpdateinterval = new JLabel("UpdateInterval (Seconds)");
		lblUpdateinterval.setIcon(new ImageIcon(NewTimeSheet.class.getResource("/resources/update-16.png")));
		lblUpdateinterval.setBounds(11, 84, 161, 14);
		dialog.getContentPane().add(lblUpdateinterval);
		
		JLabel lblNewLabel_1 = new JLabel("Clock Update Interval");
		lblNewLabel_1.setIcon(new ImageIcon(NewTimeSheet.class.getResource("/resources/icon-ios7-clock-outline-16.png")));
		lblNewLabel_1.setBounds(11, 108, 161, 14);
		dialog.getContentPane().add(lblNewLabel_1);
		
		textTimeHour = new JTextField();
		textTimeHour.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						textTimeHour.selectAll();
					}
				});
			}
		});
		textTimeHour.setHorizontalAlignment(SwingConstants.CENTER);
		textTimeHour.setBounds(181, 38, 29, 20);
		textTimeHour.setText("08");
		dialog.getContentPane().add(textTimeHour);
		textTimeHour.setColumns(2);
		
		textTimeMinute = new JTextField();
		textTimeMinute.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						textTimeMinute.selectAll();
					}
				});
			}
		});
		textTimeMinute.setHorizontalAlignment(SwingConstants.CENTER);
		textTimeMinute.setColumns(2);
		textTimeMinute.setBounds(223, 38, 29, 20);
		textTimeMinute.setText("00");
		dialog.getContentPane().add(textTimeMinute);
		
		textTimeSeconds = new JTextField();
		textTimeSeconds.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						textTimeSeconds.selectAll();
					}
				});
			}
		});
		textTimeSeconds.setHorizontalAlignment(SwingConstants.CENTER);
		textTimeSeconds.setColumns(2);
		textTimeSeconds.setBounds(269, 38, 29, 20);
		textTimeSeconds.setText("00");
		dialog.getContentPane().add(textTimeSeconds);
		
		textUpdate = new JTextField();
		textUpdate.setHorizontalAlignment(SwingConstants.RIGHT);
		textUpdate.setBounds(182, 84, 126, 20);
		textUpdate.setText(String.valueOf(getUpdateSeconds()));
		dialog.getContentPane().add(textUpdate);
		textUpdate.setColumns(10);
		
		textClockUpdate = new JTextField();
		textClockUpdate.setHorizontalAlignment(SwingConstants.RIGHT);
		textClockUpdate.setBounds(182, 108, 126, 20);
		textClockUpdate.setText(String.valueOf(getClockUpdateSeconds()));
		dialog.getContentPane().add(textClockUpdate);
		textClockUpdate.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(17, 137, 297, 8);
		dialog.getContentPane().add(separator);
		
		JLabel lblConfigureApplication = new JLabel("Configure Application");
		lblConfigureApplication.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblConfigureApplication.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfigureApplication.setBounds(10, 11, 297, 14);
		dialog.getContentPane().add(lblConfigureApplication);
		
		JButton btnSave = new JButton("Save");
		btnSave.setIcon(new ImageIcon(NewTimeSheet.class.getResource("/resources/checkmark-24-16.png")));
		dialog.getRootPane().setDefaultButton(btnSave);
		btnSave.addActionListener(new ActionListener() {
					
			public void actionPerformed(ActionEvent arg0) {
				SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
				Date timeBase = null;
				Date timeI = null;
				
				try {
					timeBase = sdf.parse(String.format("%s:%s:%s", textTimeHour.getText(), textTimeMinute.getText(), textTimeSeconds.getText()));
					timeI = sdf.parse(String.format("%s:%s:%S", textTimeIdleH.getText(), textTimeIdleM.getText(), textTimeIdleS.getText()));
					
					Date dateZero = sdf.parse("00:00:00");					
					timeDay = Util.convertTime(timeBase.getTime() - dateZero.getTime());
					predPause = Util.convertTime(timeI.getTime() - dateZero.getTime());
					
					if(!textUpdate.getText().isEmpty())
						setUpdateSeconds(Integer.parseInt(textUpdate.getText()));
					
					if(!textClockUpdate.getText().isEmpty())
						setClockUpdateSeconds(Integer.parseInt(textClockUpdate.getText()));					
					
					dialog.setVisible(false);
				} catch (ParseException e) {
					JOptionPane.showMessageDialog(dialog, "Wrong format of TimeBase", "Error", JOptionPane.ERROR_MESSAGE);					
				}
				
				isDone = true;				
				
			}
		});
		btnSave.setBounds(125, 149, 89, 23);
		dialog.getContentPane().add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setIcon(new ImageIcon(NewTimeSheet.class.getResource("/resources/cancel-16.png")));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialog.setVisible(false);
			}
		});
		btnCancel.setBounds(224, 149, 89, 23);
		dialog.getContentPane().add(btnCancel);			
		
		JLabel lblIdletime = new JLabel("IdleTime (HH:MM:SS)");
		lblIdletime.setIcon(new ImageIcon(NewTimeSheet.class.getResource("/resources/icon-pause-16.png")));
		lblIdletime.setBounds(10, 60, 161, 14);
		dialog.getContentPane().add(lblIdletime);
		
		textTimeIdleH = new JTextField();
		textTimeIdleH.setText("01");
		textTimeIdleH.setHorizontalAlignment(SwingConstants.CENTER);
		textTimeIdleH.setColumns(2);
		textTimeIdleH.setBounds(181, 60, 29, 20);
		textTimeIdleH.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						textTimeIdleH.selectAll();
					}
				});
			}
		});
		
		dialog.getContentPane().add(textTimeIdleH);
		
		JLabel label_1 = new JLabel("H");
		label_1.setBounds(213, 64, 29, 14);
		dialog.getContentPane().add(label_1);
		
		textTimeIdleM = new JTextField();
		textTimeIdleM.setText("00");
		textTimeIdleM.setHorizontalAlignment(SwingConstants.CENTER);
		textTimeIdleM.setColumns(2);
		textTimeIdleM.setBounds(223, 60, 29, 20);
		textTimeIdleM.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						textTimeIdleM.selectAll();
					}
				});
			}
		});
		
		dialog.getContentPane().add(textTimeIdleM);
		
		JLabel label_2 = new JLabel("M");
		label_2.setBounds(258, 64, 29, 14);
		dialog.getContentPane().add(label_2);
		
		textTimeIdleS = new JTextField();
		textTimeIdleS.setText("00");
		textTimeIdleS.setHorizontalAlignment(SwingConstants.CENTER);
		textTimeIdleS.setColumns(2);
		textTimeIdleS.setBounds(269, 60, 29, 20);
		textTimeIdleS.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						textTimeIdleS.selectAll();
					}
				});
			}
		});
		
		dialog.getContentPane().add(textTimeIdleS);
		
		JLabel label_3 = new JLabel("S");
		label_3.setBounds(301, 64, 29, 14);
		dialog.getContentPane().add(label_3);
		
		dialog.setVisible(true);
		
		return isDone;
	}
}
