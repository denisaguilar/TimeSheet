package br.dev.func;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class Util {
	
	private static final String VERSION = "0.4.4";
	
	private static long second = 1000;
	private static long minute = (60 * 1000);
	private static long hour = (60 * 60 * 1000);
	private static long day = (24 * 60 * 60 * 1000);

	public static String printTime(long timeMilli, String format){
		long diffSeconds = getSeconds(timeMilli);
		long diffMinutes = getMinuts(timeMilli);
		long diffHours = getHours(timeMilli);
		long diffDays = getDay(timeMilli);		
	
		return String.format(format, diffHours, diffMinutes, diffSeconds);
	}
	
	public static long convertTime(long timeMilli){
		long diffSeconds = getSeconds(timeMilli);
		long diffMinutes = getMinuts(timeMilli);
		long diffHours = getHours(timeMilli);
		long diffDays = getDay(timeMilli);
		
		return toSeconds(diffSeconds) + toMinutes(diffMinutes) + toHours(diffHours) + toDays(diffDays);
	}
	
	public static byte[] inputStreamToByteArray(InputStream is) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];
		
		try {
			while ((nRead = is.read(data, 0, data.length)) != -1) {
			  baos.write(data, 0, nRead);
			}
			
			return baos.toByteArray();			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			baos.flush();
			baos.close();
		}
		
		return null;
	}
	
	public static String getVersion(){
		return VERSION;
	}
	
	public static long toSeconds(long value){
		return value * 1000;
	}
	
	public static long toMinutes(long value){
		return value * (60 * 1000);
	}
	
	public static long toHours(long value){
		return value * (60 * 60 * 1000);
	}
	
	public static long toDays(long value){
		return value * (24 * 60 * 60 * 1000);
	}
	
	public static long getSeconds(long value){
		return value / second % 60;
	}
	
	public static long getMinuts(long value){
		return value / minute % 60;
	}
	
	public static long getHours(long value){
		return value / hour % 24;
	}
	
	public static long getDay(long value){
		return value / day;
	}
}
