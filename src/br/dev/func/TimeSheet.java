package br.dev.func;

import java.util.ArrayList;
import java.util.List;

public class TimeSheet {
	
	private long timePredicted;
	private long timeRemain;
	private long timeElapsed;
	private List<TimePack> timePacks = new ArrayList<>();
	
	
	
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
	
	public static TimeSheet getInstance(long timeRemain){
		TimeSheet ts = new TimeSheet();	
		ts.timeRemain = timeRemain;
		return ts; 	
	}
	
	
}
