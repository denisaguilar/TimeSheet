package br.dev.func;

import java.util.ArrayList;
import java.util.List;

public class TimeSheet {
	
	private long timePredicted;
	private long timeRemain;
	private long timeElapsed;
	private long idleTime;
	private List<TimePack> timePacks = new ArrayList<>();
			
	public long getIdleTime() {
		return idleTime;
	}
	public void setIdleTime(long idleTime) {
		this.idleTime = idleTime;
	}
	public long getTimePredicted() {
		return timePredicted;
	}
	public void setTimePredicted(long time) {
		this.timePredicted = time;
	}
	public long getTimeRemain() {
		return timeRemain;
	}
	public void setTimeRemain(long time) {
		this.timeRemain = time;
	}
	public long getTimeElapsed() {
		return timeElapsed;
	}
	public void setTimeElapsed(long time) {
		this.timeElapsed = time;
	}
	public List<TimePack> getTimePacks() {
		return timePacks;
	}
	public void setTimePacks(List<TimePack> timePacks) {
		this.timePacks = timePacks;
	}
	
	public static TimeSheet getInstance(){
		TimeSheet ts = new TimeSheet();	
		return ts; 	
	}
	
	
}
