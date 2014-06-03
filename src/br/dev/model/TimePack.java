package br.dev.model;


public class TimePack {
	
	private long start;
	private long end;	
	private long interval;
		
	public void updateInterval() {
		this.interval = end - start;;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;		
	}
	
	public long getInterval() {
		return interval;
	}	
}
