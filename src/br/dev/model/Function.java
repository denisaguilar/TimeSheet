package br.dev.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Function {
	
	private long timePerDay;
	private long predPause;	
	
	private TimeSheet timeSheet;
	private TimePack tempTimePack;
	private long temp;
		
	
	public long getPredPause() {
		return predPause;
	}

	public void setPredPause(long predPause) {
		this.predPause = predPause;
	}

	public long getTimePerDay() {
		return timePerDay;
	}

	public void setTimePerDay(long timePerDay) {
		this.timePerDay = timePerDay;
		timeSheet.setTimeRemain(getTimePerDay());
	}

	public TimeSheet getTimeSheet() {
		return timeSheet;
	}

	public TimePack getTempTimePack() {
		return tempTimePack;
	}

	public Function() {
		timeSheet = TimeSheet.getInstance();
		tempTimePack = new TimePack();		
	}
	
	public void setInitialTime(Date date){
		tempTimePack = new TimePack();
		long predicted;
		
		//adicionado tratamento para customTime
		if(date == null){
			tempTimePack.setStart(new Date().getTime());
			predicted = tempTimePack.getStart() + timeSheet.getTimeRemain();
		}else{
			tempTimePack.setStart(date.getTime());
			
			predicted = tempTimePack.getStart() + timeSheet.getTimeRemain();
			
			//Caso seja a primeira execução em CustomTime o timeElapsed é definido por (TempoAtual - TempoInicial)
			if(timeSheet.getTimePacks().isEmpty())
				timeSheet.setTimeElapsed(new Date().getTime() - tempTimePack.getStart());
			else
				// Caso já existam sessions armazenadas, o tempo inicial deve ser medido por ((TempoAtual - TempoInicial) + TotalTimeElapsed)
				timeSheet.setTimeElapsed((new Date().getTime() - tempTimePack.getStart()) + getTotalTimeElapsed());
				
			//Atualizando o timRemaminig
			timeSheet.setTimeRemain(timePerDay - timeSheet.getTimeElapsed());
		}	
		
		timeSheet.setTimePredicted(predicted);
	}
	
	private long getTotalTimeElapsed(){
		long time = 0;
		
		for (TimePack tp : timeSheet.getTimePacks()) {
			time += tp.getInterval();
		}
		
		return time;
		
	}
	
	public void setFinalTime(Date date) throws Exception{
		
		if(date.getTime() < getTempTimePack().getStart()){
			throw new Exception();						
		}
		
		if(date ==  null)
			tempTimePack.setEnd(new Date().getTime());
		else
			tempTimePack.setEnd(date.getTime());
				
		timeSheet.getTimePacks().add(getTempTimePack());
	}	
	
	
	public long updateTimeElapsed(boolean update, int updateFrequence){		
		long diff = 0;
		if(update)
			diff = getTimeSheet().getTimeElapsed() + Util.toSeconds(updateFrequence);
		else{
			diff = getTotalTimeElapsed();
		}
				
		timeSheet.setTimeElapsed(diff);
		return diff;
	}
	
	public long updateTimeRemain(boolean update, int updateFrequence){
		long diff;
		if(update)
			diff = getTimeSheet().getTimeRemain() - Util.toSeconds(updateFrequence);
		else{ 
			diff = timePerDay - timeSheet.getTimeElapsed();			
		}
		
		timeSheet.setTimeRemain(diff);
		return diff;		
	}
			
	public long updateTimeIdle(boolean update, boolean now, int updateFrequence){
		List<TimePack> tps = getTimeSheet().getTimePacks();
		
		long idleTime = 0;
		
		if(tps.size() > 0){			
			if(update){	
				temp += Util.toSeconds(updateFrequence);
				idleTime = temp;
			}else{
				idleTime = timeSheet.getIdleTime();
				TimePack tp = tps.get(tps.size() -1);
				if(now){
					idleTime += (new Date().getTime() - tp.getEnd());
				}else{
					long totalIdle = 0;
					for (int i = 0; i < tps.size(); i++) {
						if(tps.size() - i == 1)
							totalIdle += (getTempTimePack().getStart() - tps.get(i).getEnd());
						else						
							totalIdle += tps.get(i + 1).getStart() - tps.get(i).getEnd();
					}
					
					idleTime = totalIdle;
				}
				
				temp = idleTime;
				timeSheet.setIdleTime(idleTime);				
			}
		}			
		return idleTime;		
	}
	
	public Date toDate(long value){
		return new Date(value);
	}
		
	public String formatDate(String format, long time){
		String formatDate = "dd/MM/yyyy ~> hh:mm:ss a";		
		SimpleDateFormat sdf = new SimpleDateFormat(format == null ? formatDate : format);
		return sdf.format(new Date(time));
	}

	public String generateInfo() {
		String format = "dd/MM/yyyy hh:mm:ss a";
		StringBuffer sb = new StringBuffer();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
		sb.append("\n..::TimeSheet Console ::..");
		
		if(getTimeSheet().getTimePacks().size() > 0){

			List<TimePack> tps = getTimeSheet().getTimePacks();

			for (int i = tps.size()-1; i >= 0; i--) {
				TimePack tp = tps.get(i);

				sb.append("\n");
				sb.append("Session "+(i+1));
				sb.append("   ------------------------------------------- \n");
				sb.append("\tStart :"+ formatDate(format, tp.getStart()));
				sb.append("\n");
				sb.append("\tEnd  :"+ formatDate(format, tp.getEnd()));
				sb.append("\n");
				sb.append("\tInterval :"+ Util.printTime(tp.getInterval(), "%sh %sm %ss"));

			}	
		}
		
		return sb.toString();
	}
	
}
