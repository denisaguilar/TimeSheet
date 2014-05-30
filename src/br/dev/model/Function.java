package br.dev.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
	
	@SuppressWarnings("unchecked")
	public void writeSessionPack(){
		removeLine(String.valueOf(getTimeSheet().getTimePacks().size()));		
		
		JSONObject obj = new JSONObject();
		
		obj.put("s", getTimeSheet().getTimePacks().size());
		obj.put("i", tempTimePack.getStart());
		obj.put("o", tempTimePack.getEnd());
		
		obj.put("in", tempTimePack.getInterval());
		
		obj.put("it", getTimeSheet().getIdleTime());
		obj.put("te",getTimeSheet().getTimeElapsed());
		obj.put("tr",getTimeSheet().getTimeRemain());
		
		writeFileInfo(obj.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	public void writeTimeSheetInfo(){
		JSONObject obj = new JSONObject();
		obj.put("wt", timePerDay);
		obj.put("pp", predPause);
		writeFileInfo(obj.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	public void writeCheckinInfo(){
		JSONObject obj = new JSONObject();
		obj.put("seq", getTimeSheet().getTimePacks().size()+1);
		obj.put("i", tempTimePack.getStart());
		writeFileInfo(obj.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	public void writeCheckoutInfo(){
		JSONObject obj = new JSONObject();
		obj.put("seq", getTimeSheet().getTimePacks().size());
		obj.put("o", tempTimePack.getEnd());
		writeFileInfo(obj.toJSONString());
	}
	
	private void removeLine(String sequenceNumber){
		try {
			File file = directoryManager();
			File tempFile = new File(file.toPath()+".temp");
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));		
			
			String line = null;
			while((line = br.readLine()) != null){
				JSONObject obj = (JSONObject) JSONValue.parse(line);				
				if(obj.get("seq") == null){
					pw.println(line);					
				}
			}
			
			pw.flush();
			pw.close();
			br.close();
			
			file.delete();
			tempFile.renameTo(file);			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void writeFileInfo(String text){
		File file;
		try {
			file = directoryManager();
			
			FileWriter fileWriter =  new FileWriter(file, true);
			PrintWriter pw = new PrintWriter(fileWriter, true);

			pw.write(text);
			pw.println();	
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public boolean hasDataInFile(){
		File file = null;
		try {
			file = directoryManager();			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (file == null || file.length() > 0);	
	}
	
	public void readFileInfo(){
		try {
			File file = directoryManager();
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while((line = br.readLine()) != null){
				JSONObject obj = (JSONObject) JSONValue.parse(line);
				
				if(obj != null){
					if(obj.get("pp") != null && obj.get("wt") != null){
						setTimePerDay((long)obj.get("wt"));
						setPredPause((long)obj.get("pp"));					
					}
					
					if(obj.get("s") != null){
						tempTimePack.setStart((long)obj.get("i"));
						tempTimePack.setEnd(0);
						
						TimePack tp = new TimePack();
						tp.setStart((long)obj.get("i"));
						tp.setEnd((long)obj.get("o"));
						
						timeSheet.getTimePacks().add(tp);
						timeSheet.setIdleTime((long)obj.get("it"));
						timeSheet.setTimeElapsed((long)obj.get("te"));
						timeSheet.setTimeRemain((long)obj.get("tr"));		
					}
					
					if(obj.get("seq") != null){					
						tempTimePack.setStart((long)obj.get("i"));
						tempTimePack.setEnd(0);
					}
				}
				
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private File directoryManager() throws IOException{
		File basePath = new File(Util.path);
		
		Calendar cal = Calendar.getInstance();
		
		int year = cal.get(Calendar.YEAR);		
		File yearPath = new File(basePath,String.valueOf(year));
		
		String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		File monthPath = new File(yearPath,month);
		
		File dayPath = new File(monthPath, cal.get(Calendar.DAY_OF_MONTH)+"_"+cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
		
		if(!dayPath.exists()){
			dayPath.getParentFile().mkdirs();
			Files.createFile(dayPath.toPath());			
		}
		
		return dayPath;
	}

	public String generateInfo() {
		String format = "dd/MM/yyyy hh:mm:ss a";
		StringBuffer sb = new StringBuffer();
		
		//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
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
