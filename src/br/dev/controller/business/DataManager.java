package br.dev.controller.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

import br.dev.model.time.TimePack;
import br.dev.model.time.TimeSheet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

	public TimePack getLastTP() {
		return ts.getTimePacks().get(ts.getTimePacks().size() - 1);
	}

	public DataManager() {
		super();
		tp = new TimePack();
		ts = new TimeSheet();
	}

	public DataManager(OperationCore function) {
		super();
		setTs(function.getTimeSheet());
		setTp(function.getTempTimePack());
		setTimePerDay(function.getTimePerDay());
		setPredPause(function.getPredPause());
	}

	public void writeSessionPack() {
		removeLines();

		JsonObject obj = new JsonObject();

		obj.addProperty("s", ts.getTimePacks().size());
		obj.addProperty("i", tp.getStart());
		obj.addProperty("o", tp.getEnd());

		obj.addProperty("in", tp.getInterval());

		obj.addProperty("it", ts.getIdleTime());
		obj.addProperty("te", ts.getTimeElapsed());
		obj.addProperty("tr", ts.getTimeRemain());
		obj.addProperty("tp", ts.getTimePredicted());

		writeFileInfo(new Gson().toJson(obj));
	}

	public void writeTimeSheetInfo() {
		JsonObject obj = new JsonObject();

		obj.addProperty("wt", timePerDay);
		obj.addProperty("pp", predPause);

		cleanFile();

		writeFileInfo(new Gson().toJson(obj));
	}

	public void writeCheckinInfo() {
		JsonObject obj = new JsonObject();

		obj.addProperty("seq", ts.getTimePacks().size() + 1);
		obj.addProperty("i", tp.getStart());

		writeFileInfo(new Gson().toJson(obj));
	}

	public void writeCheckoutInfo() {
		JsonObject obj = new JsonObject();

		obj.addProperty("seq", ts.getTimePacks().size());
		obj.addProperty("o", tp.getEnd());

		writeFileInfo(new Gson().toJson(obj));
	}

	public void dailyBackup() {
		File fileScr = null;

		try {
			fileScr = directoryManager();
			SaveFileChooser sfc = new SaveFileChooser(null, null, "TMS Data");
			sfc.setFileName(fileScr.getName());

			if (sfc.showDialog()) {
				FileUtils.copyFile(fileScr, sfc.getSelectedFile());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void monthlyBackup() {

		try {
			Calendar cal = Calendar.getInstance();

			File fileScr = getMonthFile(cal);

			if (!fileScr.isDirectory())
				throw new Exception("Invalid directory path");

			SaveFileChooser sfc = new SaveFileChooser(null, null, fileScr.getName());

			if (sfc.showDialog()) {
				File fileDst = new File(sfc.getSelectedFile(), fileScr.getName());
				FileUtils.copyDirectory(fileScr, fileDst);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void removeLines() {
		BufferedReader br = null;
		PrintWriter pw = null;
		File file = null;
		File tempFile = null;

		try {
			file = directoryManager();
			tempFile = new File(file.toPath() + ".temp");

			br = new BufferedReader(new FileReader(file));
			pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;
			while (br.ready() && (line = br.readLine()) != null) {
				Gson gson = new Gson();
				JsonObject obj = gson.fromJson(line, JsonObject.class);

				if (obj.get("seq") == null) {
					pw.println(line);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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

	private void cleanFile() {
		try {
			directoryManager().delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFileInfo(String text) {
		File file;
		PrintWriter pw = null;

		try {
			file = directoryManager();
			pw = new PrintWriter(new FileWriter(file, true), true);

			pw.write(text);
			pw.println();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
	}

	public boolean hasDataInFile() {
		File file = null;
		try {
			file = directoryManager();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (file == null || file.length() > 0);
	}

	public void readFileInfo() {
		BufferedReader br = null;
		try {
			File file = directoryManager();

			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				Gson gson = new Gson();
				JsonObject obj = gson.fromJson(line, JsonObject.class);

				if (obj != null) {
					if (obj.get("pp") != null && obj.get("wt") != null) {
						setTimePerDay(obj.get("wt").getAsLong());
						setPredPause((obj.get("pp").getAsLong()));
					} else if (obj.get("seq") != null) {
						tp.setStart(obj.get("i").getAsLong());
					} else if (obj.get("s") != null) {
						TimePack tp = new TimePack();
						tp.setStart(obj.get("i").getAsLong());
						tp.setEnd(obj.get("o").getAsLong());
						tp.updateInterval();

						ts.getTimePacks().add(tp);
						ts.setIdleTime(obj.get("it").getAsLong());
						ts.setTimeElapsed(obj.get("te").getAsLong());
						ts.setTimeRemain((obj.get("tr").getAsLong()));
						ts.setTimePredicted(obj.get("tp").getAsLong());
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private File getYearFile(Calendar cal) {
		File basePath = getBasePath();

		int year = cal.get(Calendar.YEAR);
		return new File(basePath, String.valueOf(year));
	}

	private File getMonthFile(Calendar cal) {
		File yearPath = getYearFile(cal);

		String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		return new File(yearPath, month);
	}

	private File getDayFile(Calendar cal) {
		File monthFile = getMonthFile(cal);
		return new File(monthFile, cal.get(Calendar.DAY_OF_MONTH) + "_"
				+ cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
	}

	private File directoryManager() throws IOException {
		Calendar cal = Calendar.getInstance();

		File dayPath = getDayFile(cal);

		if (!dayPath.exists()) {
			dayPath.getParentFile().mkdirs();
			Files.createFile(dayPath.toPath());
		}

		return dayPath;
	}

	public static File getBasePath() {
		return new File(System.getProperty("java.io.tmpdir"), ".tms");
	}
}
