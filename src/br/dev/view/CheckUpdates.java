package br.dev.view;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import br.dev.model.business.Util;


public class CheckUpdates {
	
//	static{
//		new Thread(new Runnable() {			
//			@Override
//			public void run() {
//				if(!isUpToDate()){
//					btnOk.setEnabled(true);	
//					btnOk.setVisible(true);			
//				}else{
//					dialog.dispose();
//				}
//			}
//		}).start();
//	}

	private static JDialog dialog;
	private static boolean isDone;
	private static JLabel lblMsg = new JLabel("Check for updates..");
	private static JButton btnOk;
		
	private static String newVersion;
	private JSeparator separator;

	/**
	 * Create the application.
	 */

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	public boolean showDialog() {
		
		new Thread(new Runnable() {			
			@Override
			public void run() {
				if(!isUpToDate()){
					btnOk.setEnabled(true);	
					btnOk.setVisible(true);			
				}else{
					dialog.dispose();
				}
			}
		}).start();
		
		dialog = new JDialog(null, JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setTitle("Check for updates");
		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(CheckUpdates.class.getResource("/resources/519929-27_Cloud-16.png")));
		dialog.setResizable(false);
		dialog.setBounds(100, 100, 311, 162);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.getContentPane().setLayout(null);
		dialog.setLocationRelativeTo(null);
		lblMsg.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
				
		lblMsg.setHorizontalAlignment(SwingConstants.CENTER);
		lblMsg.setBounds(10, 59, 285, 14);
		
		btnOk = new JButton("Update");
		btnOk.setIcon(new ImageIcon(CheckUpdates.class.getResource("/resources/519624-123_CloudDownload-16.png")));
		dialog.getRootPane().setDefaultButton(btnOk);
		btnOk.setBounds(206, 99, 89, 23);
		btnOk.setVisible(false);
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblMsg.setText("Download new version...");
				
				try {
					URL url = new URL(Util.getRelease());
					InputStream is = url.openStream();
					
					File file = new File(System.getProperty("user.dir")+"\\TMS-"+newVersion+".jar");
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
		
		dialog.getContentPane().add(btnOk);
		
		
		dialog.getContentPane().add(lblMsg);
		
		separator = new JSeparator();
		separator.setBounds(15, 84, 275, 2);
		dialog.getContentPane().add(separator);
		
		JLabel labelVersion = new JLabel("TimeSheet Version "+Util.getVersionNumber());
		labelVersion.setHorizontalAlignment(SwingConstants.CENTER);
		labelVersion.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		labelVersion.setBounds(10, 34, 285, 14);
		dialog.getContentPane().add(labelVersion);
		dialog.setVisible(true);
			
		return isDone;				
	}
	
	private static boolean isUpToDate(){
		byte[] responseBytes = null;
		
			URL url;
			try {
				url = new URL(Util.getVersion());
				InputStream is = url.openStream();
				responseBytes = Util.inputStreamToByteArray(is);
			} catch (IOException e1) {
				lblMsg.setText("No internet connection avaliable!");
				return true;			
			}finally{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			String downloadedString = new String(responseBytes);

			StringTokenizer curVersionTokens = new StringTokenizer(downloadedString, ".");
			StringTokenizer thisVersionTokens = new StringTokenizer(Util.getVersionNumber(), ".");
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
			return true;
		}
		
		return true;
	}
}
