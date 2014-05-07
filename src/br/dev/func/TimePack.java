package br.dev.func;


public class TimePack {
	
	private long start;
	private long end;	
	private long interval;
		
	public void setInterval(long interval) {
		this.interval = interval;
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
		this.interval = end - start;
	}
	
	public long getInterval() {
		return interval;
	}	
}
