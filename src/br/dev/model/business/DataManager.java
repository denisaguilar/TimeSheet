package br.dev.model.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import br.dev.model.time.TimePack;
import br.dev.model.time.TimeSheet;

public class DataManager {
	
	private long timePerDay;
	private long predPause;	
	
	private TimeSheet ts;
	private TimePack tp;
		
	protected void setTimePerDay(long timePerDay) {
		this.timePerDay = timePerDay;
	}

	protected void setPredPause(long predPause) {
		this.predPause = predPause;
	}

	public long getTimePerDay() {
		return timePerDay;
	}

	public long getPredPause() {
		return predPause;
	}

	protected void setTs(TimeSheet ts) {
		this.ts = ts;
	}

	protected void setTp(TimePack tp) {
		this.tp = tp;
	}
	
	public TimeSheet getTs() {
		return ts;
	}

	public TimePack getTp() {
		return tp;
	}
	
	public TimePack getLastTP(){
		return ts.getTimePacks().get(ts.getTimePacks().size() - 1);
	}
	
	public DataManager() {
		super();
		tp = new TimePack();
		ts = new TimeSheet();
	}

	public DataManager(Function function) {
		super();
		setTs(function.getTimeSheet());
		setTp(function.getTempTimePack());	
		setTimePerDay(function.getTimePerDay());
		setPredPause(function.getPredPause());
	}

	@SuppressWarnings("unchecked")
	public void writeSessionPack(){
		removeLines();		
		
		JSONObject obj = new JSONObject();
		
		obj.put("s", ts.getTimePacks().size());
		obj.put("i", tp.getStart());
		obj.put("o", tp.getEnd());
		
		obj.put("in", tp.getInterval());
		
		obj.put("it", ts.getIdleTime());
		obj.put("te",ts.getTimeElapsed());
		obj.put("tr",ts.getTimeRemain());
		obj.put("tp", ts.getTimePredicted());
		
		writeFileInfo(obj.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	public void writeTimeSheetInfo(){
		JSONObject obj = new JSONObject();
		obj.put("wt", timePerDay);
		obj.put("pp", predPause);
		cleanFile();
		writeFileInfo(obj.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	public void writeCheckinInfo(){
		JSONObject obj = new JSONObject();
		obj.put("seq", ts.getTimePacks().size()+1);
		obj.put("i", tp.getStart());
		writeFileInfo(obj.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	public void writeCheckoutInfo(){
		JSONObject obj = new JSONObject();
		obj.put("seq", ts.getTimePacks().size());
		obj.put("o", tp.getEnd());
		writeFileInfo(obj.toJSONString());
	}
	
	public void dailyBackup(){
		File file = null;
		
		try {
			file = directoryManager();
			SaveFileChooser sfc = new SaveFileChooser(null, null, "TMS Data");
			sfc.setFileName(file.getName());
			
			if(sfc.showDialog()){			
				OutputStream os = new FileOutputStream(sfc.getSelectedFile());						
				Files.copy(file.toPath(), os);				
				os.flush();
				os.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void removeLines(){
		BufferedReader br = null;
		PrintWriter pw = null;
		File file = null;
		File tempFile = null;
		
		try {
			file = directoryManager();
			tempFile = new File(file.toPath()+".temp");
			
			br = new BufferedReader(new FileReader(file));
			pw = new PrintWriter(new FileWriter(tempFile));		
			
			String line = null;
			while(br.ready() && (line = br.readLine()) != null){
				JSONObject obj = (JSONObject) JSONValue.parse(line);				
				if(obj.get("seq") == null){
					pw.println(line);					
				}
			}				
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{	
			try {
				br.close();
				pw.flush();
				pw.close();		
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		file.delete();
		tempFile.renameTo(file);		
	}
	
	private void cleanFile(){
		try {
			directoryManager().delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeFileInfo(String text){
		File file;
		PrintWriter pw = null;
		
		try {
			file = directoryManager();
			pw = new PrintWriter(new FileWriter(file, true), true);
						
			pw.write(text);
			pw.println();	
		} catch (IOException e) {
			e.printStackTrace();
		}finally{	
			pw.flush();
			pw.close();
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
		BufferedReader br = null;
		try {
			File file = directoryManager();
			
			br = new BufferedReader(new FileReader(file));
			String line;
			while((line = br.readLine()) != null){
				JSONObject obj = (JSONObject) JSONValue.parse(line);
				
				if(obj != null){
					if(obj.get("pp") != null && obj.get("wt") != null){
						setTimePerDay((long)obj.get("wt"));
						setPredPause((long)obj.get("pp"));				
					}else if(obj.get("seq") != null){					
						tp.setStart((long)obj.get("i"));
					}else if(obj.get("s") != null){
						TimePack tp = new TimePack();
						tp.setStart((long)obj.get("i"));
						tp.setEnd((long)obj.get("o"));
						tp.updateInterval();
										
						ts.getTimePacks().add(tp);
						ts.setIdleTime((long)obj.get("it"));
						ts.setTimeElapsed((long)obj.get("te"));
						ts.setTimeRemain((long)obj.get("tr"));
						ts.setTimePredicted((long)obj.get("tp"));
					}
				}
			}	
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private File getYearFile(File basePath, Calendar cal){
		int year = cal.get(Calendar.YEAR);		
		return new File(basePath,String.valueOf(year));
	}
	
	private File getMonthFile(File basePath, Calendar cal){
		String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		return new File(basePath,month);
	}
	
	private File getDayFile(File basePath, Calendar cal){
		return new File(basePath, cal.get(Calendar.DAY_OF_MONTH)+"_"+cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
	}
	
	private File directoryManager() throws IOException{
		File basePath = new File(System.getProperty("java.io.tmpdir"), ".tms");
		
		Calendar cal = Calendar.getInstance();
					
		File yearPath = getYearFile(basePath, cal);
		File monthPath = getMonthFile(yearPath, cal);		
		File dayPath = getDayFile(monthPath, cal);
		
		if(!dayPath.exists()){
			dayPath.getParentFile().mkdirs();
			Files.createFile(dayPath.toPath());	
		}
		
		return dayPath;
	}
}
