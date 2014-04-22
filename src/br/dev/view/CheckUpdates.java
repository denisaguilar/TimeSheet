package br.dev.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import br.dev.func.Util;


public class CheckUpdates {
	
	static{
		new Thread(new Runnable() {			
			@Override
			public void run() {
				if(!isUpToDate()){
					btnOk.setVisible(true);			
				}else{
					frame.dispose();
				}
			}
		}).start();
	}

	private static JDialog frame;
	private static boolean isDone;
	private static JLabel lblMsg = new JLabel("Check for updates..");
	private static JButton btnOk;
	
	private static final String VERSION = "http://dl.dropboxusercontent.com/s/rxppaoxo9tbmow2/version.txt?dl=1&token_hash=AAFCcRFJ3_-XFPrNDyhLuxcuuQ8yMvTYBl927lF1sA3aVA";
	private static final String RELEASE = "http://dl.dropboxusercontent.com/s/a4eps87lokjh6x1/time_sheet.jar?dl=1&token_hash=AAH7rFbsOIGm94GB2xtSbCO07GE-S6QWTIUH9RsE3MWZfg";
	
	private static String newVersion;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CheckUpdates window = new CheckUpdates();
					window.showDialog();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	public boolean showDialog() {
		frame = new JDialog(null, JDialog.ModalityType.APPLICATION_MODAL);
		frame.setResizable(false);
		frame.setBounds(100, 100, 311, 162);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
				
		lblMsg.setHorizontalAlignment(SwingConstants.CENTER);
		lblMsg.setBounds(10, 44, 285, 14);
		
		btnOk = new JButton("Update");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblMsg.setText("Download new version...");
				
				try {
					URL url = new URL(RELEASE);
					InputStream is = url.openStream();
					
					File file = new File(System.getProperty("user.dir")+"\\TimeSheet-"+newVersion+".jar");
					OutputStream os = new FileOutputStream(file);
					os.write(Util.inputStreamToByteArray(is));
					
					os.flush();
					os.close();
					
					Runtime runTime = Runtime.getRuntime();
					runTime.exec("java -jar "+file.getAbsoluteFile());
										
					System.exit(0);				
				} catch (IOException e1) {
					e1.printStackTrace();
				}			
			}
		});
		btnOk.setBounds(206, 99, 89, 23);
		btnOk.setVisible(false);
		frame.getContentPane().add(btnOk);
		
		
		frame.getContentPane().add(lblMsg);
		frame.setVisible(true);
			
		return isDone;				
	}
	
	private static boolean isUpToDate(){
		byte[] responseBytes = null;
		try {
			URL url = new URL(VERSION);
			InputStream is = url.openStream();
			responseBytes = Util.inputStreamToByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String downloadedString = new String(responseBytes);

		StringTokenizer curVersionTokens = new StringTokenizer(downloadedString, ".");
		StringTokenizer thisVersionTokens = new StringTokenizer(Util.getVersion(), ".");
		StringTokenizer control =  curVersionTokens.countTokens() > thisVersionTokens.countTokens() ? curVersionTokens : thisVersionTokens;
		int vCur, vThis;

		while (control.hasMoreElements()) {
			if (curVersionTokens.hasMoreElements()) vCur = Integer.valueOf(curVersionTokens.nextElement().toString()).intValue();
			else vCur = 0;

			if (thisVersionTokens.hasMoreElements()) vThis = Integer.valueOf(thisVersionTokens.nextElement().toString()).intValue();
			else vThis = 0;

			if (vCur > vThis) {
				lblMsg.setText("New version "+downloadedString+" avaliable !");
				newVersion = downloadedString;
				return false;
			}
			if (vCur < vThis) {
				lblMsg.setText("No updates avaliable!");
				return true;
			}
		}
		
		lblMsg.setText("No updates avaliable!");
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
