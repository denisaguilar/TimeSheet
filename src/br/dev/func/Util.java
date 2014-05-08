package br.dev.func;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class Util {
	
	private static final String VERSION = "0.4.6.1";
	
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
	
	public static String[] getText(){
		String text = "Pulp Fiction (1994);Vincent: Want some bacon?;Jules: No man, I don't eat pork.;Vincent: Are you Jewish?;Jules: Nah, I ain't Jewish, I just don't dig on swine, that's all.;Vincent: Why not?;Jules: Pigs are filthy animals. I don't eat filthy animals.;Vincent: Bacon tastes gooood. Pork chops taste gooood.;Jules: Hey, sewer rat may taste like pumpkin pie, but I'd never know 'cause I wouldn't eat the filthy motherfucker. Pigs sleep and root in shit. That's a filthy animal. I ain't eat nothin' that ain't got sense enough to disregard its own feces.;Vincent: How about a dog? Dogs eats its own feces.;Jules: I don't eat dog either.;Vincent: Yeah, but do you consider a dog to be a filthy animal?;Jules: I wouldn't go so far as to call a dog filthy but they're definitely dirty. But, a dog's got personality. Personality goes a long way.;Vincent: Ah, so by that rationale, if a pig had a better personality, he would cease to be a filthy animal. Is that true?;Jules: Well we'd have to be talkin' about one charming motherfuckin' pig. I mean he'd have to be ten times more charmin' than that Arnold on Green Acres, you know what I'm sayin'?;";
		return text.split(";");
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
