package br.dev.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import com.sun.jmx.snmp.tasks.Task;

import br.dev.model.Function;
import br.dev.model.Util;
import br.dev.model.listener.ButtonListener;
import br.dev.view.CustomTime;
import br.dev.view.NewTimeSheet;
import br.dev.view.Prototype;

public class TMS implements ButtonListener{
	private Prototype prot;
	private Function func;
		
	private static int wordPerSecond = 13;
	private static int updateSeconds = 1;
	private static int clockUpdateInterval = 5;
	
	private Task taskIdleTime;
	private Task taskUpdateTime;
	
	private Thread updateThreadSimpleTime = null;
	private Thread updateThreadIdleTime = null;
	
	public static void main(String[] args) {
		new TMS();
	}
	
	public TMS() {
		prot = new Prototype();
		prot.addListner(this);
		Util.path = "c:\\temp\\tms";
			
		//TimeIdle
		taskIdleTime = new Task(){
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
					
					long value = func.updateTimeIdle(true, true, updateSeconds);
					prot.textIdle.setText(Util.printTime(value, "%sh %sm %ss"));	
					
					if(value > func.getPredPause() && value < func.getPredPause() + 10){
						prot.showMessage("Hey, Mandatory brake time is over, Back to Work!!", "Time Over!", "/resources/prisoner-32.png");			
					}
				}
			}

			@Override
			public void cancel() {
				isDone = true;				
			}
			
		};
		
		//TimeElapsed, TimeRemainin
		taskUpdateTime = new Task() {	
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
			
					long timeElapsed = func.updateTimeElapsed(true, updateSeconds);
					prot.textElapsed.setText(Util.printTime(timeElapsed, "%sh %sm %ss"));
				
					long timeRemain = func.updateTimeRemain(true, updateSeconds);
					prot.textRemain.setText(Util.printTime(timeRemain, "%sh %sm %ss"));	
					
					if(timeRemain < 0 && timeRemain > -1000){
						prot.showMessage("Hey, work time is Over, Enjoy your Freedom!!", "Time Over!", "/resources/running64.png");					
					}
				}				
			}
			
			@Override
			public void cancel() {
				isDone = true;				
			}
		};
		
		//Clock
		new Thread(new Runnable() {			
			@Override
			public void run() {
				SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
				while(true){
					try {					
						prot.labelHour.setText(format.format(new Date()));	
						Thread.sleep(clockUpdateInterval * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
		
		
		//Text Panel
		new Thread(new Runnable() {			
			@Override
			public void run() {
				do{
					try {
						Thread.sleep(1000 * 5);
						String[] text = Util.getText();
						for (int i = 0; i < text.length; i++) {
							String[] t = text[i].split("#"); 
							prot.lblhid.setText(t[0]);
							
							if(t.length == 1){
								Thread.sleep(1000 * text[i].split("").length / wordPerSecond);
							}else{
								Thread.sleep(1000 * Integer.parseInt(t[1]));
							}
						}					
						
						prot.lblhid.setText("");
						
					} catch (InterruptedException e) {
						prot.lblhid.setText("ERROR!");
					}				
				
				}while(true);
			}
		}).start();	
		
		func = new Function();
		if(func.hasDataInFile()){
			if(prot.showMessageChoice("Carregar pelo hist", "Restaurar", "/resources/prisoner-32.png") == 1);
				func.readFileInfo();
				updateFields();
		}
	}
	
	@Override
	public boolean onCheckout() {
		return onCheckout(0);
	}
	
	@Override
	public boolean onCheckout(long preTime) {
		CustomTime custon = new CustomTime();
		
		if(!custon.showDialog()){
			return false;
		}
		
		Date customTime = custon.getCustomDate();			
				
		try {
			func.setFinalTime(customTime);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "O tempo final não pode ser inferior ao inicial", "Erro de tempo", JOptionPane.WARNING_MESSAGE);
			return false;
		}		
		
		long idleTime = func.updateTimeIdle(false, true, 0);
		prot.textIdle.setText(Util.printTime(idleTime, "%sh %sm %ss"));
							
		try {
			taskUpdateTime.cancel();
			updateThreadSimpleTime.join();
			
			updateThreadIdleTime = new Thread(taskIdleTime);
			updateThreadIdleTime.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		long time = func.getTempTimePack().getEnd();
		prot.textEndTime.setText(func.formatDate(null, time));
							
		long elapsedTime = func.updateTimeElapsed(false, 0);
		prot.textElapsed.setText(Util.printTime(elapsedTime, "%sh %sm %ss"));
		
		long remainTime = func.updateTimeRemain(false, 0);
		prot.textRemain.setText(Util.printTime(remainTime, "%sh %sm %ss"));
							
		prot.updateConsole(func.generateInfo());	
		
		func.writeCheckoutInfo();
		func.writeSessionPack();
		
		return true;
	}

	@Override
	public boolean onCheckin() {
		return onCheckin(0);
	};
	
	@Override
	public boolean onCheckin(long preTime) {
		Date customTime;
		if(preTime == 0){
			CustomTime custon = new CustomTime();		
			if(!custon.showDialog())
				return false;
			
			customTime = custon.getCustomDate();
		}else{		
			customTime = new Date(preTime);	
			
			prot.buttonInitialTime.setEnabled(false);
			prot.buttonFinalTime.setEnabled(true);	
		}
		
		func.setInitialTime(customTime);
						
		try {
			if(updateThreadIdleTime != null){
				taskIdleTime.cancel();
				updateThreadSimpleTime.join();
			}
			
			updateThreadSimpleTime = new Thread(taskUpdateTime);					
			updateThreadSimpleTime.start();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}	
		
		long idleTime = func.updateTimeIdle(false, false,0);
		prot.textIdle.setText(Util.printTime(idleTime, "%sh %sm %ss"));
		
		long time = func.getTempTimePack().getStart();					
		prot.textStartTime.setText(func.formatDate(null, time));
		
		long predictedTime = func.getTimeSheet().getTimePredicted();
		prot.textPredicted.setText(func.formatDate(null, predictedTime));
	
		prot.textEndTime.setText("");		
		prot.txtSession.setText("Session sequence: "+ (func.getTimeSheet().getTimePacks().size()+1));	
		
		if(preTime == 0)
			func.writeCheckinInfo();
		
		return true;
	}

	@Override
	public boolean onNewTimeSheet() {
		return onNewTimeSheet(false);
	}
	
	@Override
	public boolean onNewTimeSheet(boolean preLoaded) {

		if(!preLoaded){		
			NewTimeSheet timesheet = new NewTimeSheet();		
			if(!timesheet.showDialog(clockUpdateInterval, updateSeconds))
				return false;
			updateSeconds = timesheet.getUpdateSeconds();
			clockUpdateInterval = timesheet.getClockUpdateSeconds();
		
			func.setTimePerDay(timesheet.getTimePerDay());
			func.setPredPause(timesheet.getTimeIdle());
			
			func.writeTimeSheetInfo();
		}	
	
		if(updateThreadSimpleTime != null)
			try {
				taskUpdateTime.cancel();
				updateThreadSimpleTime.join();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}

		prot.textTimeBase.setText(Util.printTime(func.getTimePerDay(), "%sh %sm %ss"));
		prot.textPredPause.setText(Util.printTime(func.getPredPause(), "%sh %sm %ss"));		
			
		return true;
	}
	
	public void updateFields(){
		onNewTimeSheet(true);
		
		if(func.getTempTimePack().getStart() != 0)
			onCheckin(func.getTempTimePack().getStart());
		
		prot.updateConsole(func.generateInfo());
		
			
	}	
}
