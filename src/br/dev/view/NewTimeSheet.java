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

import br.dev.func.Util;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class NewTimeSheet{

	private long timeDay;
	private int updateSeconds;
	private int clockUpdateSecods;
	
	JDialog dialog;
	boolean isDone;
	private JTextField textTimeHour;
	private JTextField textUpdate;
	private JTextField textClockUpdate;
	private JTextField textTimeMinute;
	private JTextField textTimeSeconds;
	

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
	
	public boolean showDialog(int clockUpdate, int updateSeconds) {	
				
		dialog = new JDialog(null, JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setResizable(false);
		dialog.addWindowListener(new WindowAdapter() {			
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
			}			
		});
		
		this.updateSeconds = updateSeconds;
		this.clockUpdateSecods = clockUpdate;
		
		dialog.setBounds(100, 100, 342, 213);
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
		lblNewLabel.setBounds(10, 38, 130, 14);
		dialog.getContentPane().add(lblNewLabel);		
		
		JLabel lblUpdateinterval = new JLabel("UpdateInterval (Seconds)");
		lblUpdateinterval.setBounds(10, 63, 130, 14);
		dialog.getContentPane().add(lblUpdateinterval);
		
		JLabel lblNewLabel_1 = new JLabel("Clock Update Interval (Seconds)");
		lblNewLabel_1.setBounds(10, 87, 161, 14);
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
		textUpdate.setBounds(181, 63, 126, 20);
		textUpdate.setText(String.valueOf(getUpdateSeconds()));
		dialog.getContentPane().add(textUpdate);
		textUpdate.setColumns(10);
		
		textClockUpdate = new JTextField();
		textClockUpdate.setHorizontalAlignment(SwingConstants.RIGHT);
		textClockUpdate.setBounds(181, 87, 126, 20);
		textClockUpdate.setText(String.valueOf(getClockUpdateSeconds()));
		dialog.getContentPane().add(textClockUpdate);
		textClockUpdate.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 118, 297, 8);
		dialog.getContentPane().add(separator);
		
		JLabel lblConfigureApplication = new JLabel("Configure Application");
		lblConfigureApplication.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfigureApplication.setBounds(10, 11, 297, 14);
		dialog.getContentPane().add(lblConfigureApplication);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
					
			public void actionPerformed(ActionEvent arg0) {
				SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
				Date timeBase = null;
				
				try {
					timeBase = sdf.parse(String.format("%s:%s:%s", textTimeHour.getText(), textTimeMinute.getText(), textTimeSeconds.getText()));
					
					Date dateZero = sdf.parse("00:00:00");					
					timeDay = Util.convertTime(timeBase.getTime() - dateZero.getTime());
					
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
		btnSave.setBounds(119, 137, 89, 23);
		dialog.getContentPane().add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialog.setVisible(false);
			}
		});
		btnCancel.setBounds(218, 137, 89, 23);
		dialog.getContentPane().add(btnCancel);			
		
		dialog.setVisible(true);
		
		return isDone;
	}
}
