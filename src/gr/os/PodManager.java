package gr.os;

import gr.model.GnuPodList;
import gr.ui.components.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class PodManager {

	private static boolean  checkIfPodMounted(String ipodMountPoint)  {
		boolean present = false;
		StringBuilder builderCmd = new StringBuilder();
		builderCmd.append("ls ").append(ipodMountPoint);
		
		if (ipodMountPoint.isEmpty()) {
			return present;
		}
		
		Process p;
		try {
			p = Runtime.getRuntime().exec(builderCmd.toString());
			BufferedReader bri = new BufferedReader(new InputStreamReader( p.getInputStream()));

			String line;
			while ((line = bri.readLine()) != null) {
				present = true;
			}
			bri.close();
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return present;
	}
	
	
	public static List<String> getPodFiles(String ipodMountPoint) throws PodException {
		List<String> allFiles = new ArrayList<String>();
		if  ( checkIfPodMounted(ipodMountPoint) ) {
			StringBuilder builderCmd = new StringBuilder();
			builderCmd.append("gnupod_search -m ").append(ipodMountPoint);

			Process p;
			try {
				String line;
				p = Runtime.getRuntime().exec(builderCmd.toString());
				BufferedReader bri = new BufferedReader(new InputStreamReader( p.getInputStream()));
				BufferedReader bre = new BufferedReader(new InputStreamReader( p.getErrorStream()));

				while ((line = bri.readLine()) != null) {
					allFiles.add(line);
				}
				bri.close();

				String lineErr = "";
				while ((line = bre.readLine()) != null) {
					lineErr+=line;
				}
				bre.close();
				if (lineErr.length() > 0) {
					throw new PodException(lineErr );
				}
				
				p.waitFor();
			} catch (IOException e) {
				throw new PodException( e.getMessage()  );
			} catch (InterruptedException e) {
				throw new PodException( e.getMessage()  );
			}

			allFiles.remove(0);
			allFiles.remove(0);
			allFiles.remove(0);
		}  else {
			throw new  PodException("iPod not mounted..");
		}
		return allFiles;
	}


	public static List<String> getInfoData(List<String> allFiles, GnuPodList category) {
		List<String> info = new ArrayList<String>();

		for (String entry : allFiles) {
			String[] tokens = entry.split(GnuPodList.SEPARATOR);
			String infoEntry = tokens[category.getPosition()];
			if (!info.contains(infoEntry)) {
				info.add(infoEntry);
			}
		}
		Collections.sort(info);
		return info;
	}

	
	public static String deletePodFiles(String ipodMountPoint, List<String> gnuSongs, ProgressBar bar) throws PodException {
		if (gnuSongs.size() == 0) return "Nothing to delete!";
		
		StringBuilder builderCmd = new StringBuilder();
		builderCmd.append( "gnupod_search -m ").append(ipodMountPoint.replace("'", "\\'")).append(" --id=\"^%s$\" -d") ;
		return createFile(ipodMountPoint, builderCmd.toString(), gnuSongs, false, bar);
	}

	public static String  addFilesToPod(String ipodMountPoint, List<String> gnuSongs, ProgressBar bar) throws PodException {
		if (gnuSongs.size() == 0) return "Nothing to add!";
		
		StringBuilder builderCmd = new StringBuilder();
		builderCmd.append( "gnupod_addsong -m ").append(ipodMountPoint.replace("'", "\\'")).append( "  %s%s").append(" --artwork *.jpg") ;
		return createFile(ipodMountPoint, builderCmd.toString(), gnuSongs, true, bar);
	}

	private static String encodeSong(String song) {
		song = song.replace(" ", "\\ ");
		song = song.replace("(", "\\(");
		song = song.replace(")", "\\)");
		return song;
	}
	
	private static String createFile(String ipodMountPoint, String command, List<String> gnuSongs, boolean fix, ProgressBar bar) throws PodException {
		StringBuilder builderMktunes = new StringBuilder();
		builderMktunes.append("mktunes  -m ").append(ipodMountPoint.replace("'", "\\'"));
		
		final String directory = System.getProperty("user.home");
		final String filename = "gnupod.sh";
		
		StringBuilder builder = new StringBuilder();
		File file = new File(directory, filename);
		try {
			file.createNewFile();
			file.setWritable(true);
			
			if ( !file.canWrite() || !file.canRead()  ) {
				throw new  PodException("Tmp file now available");
			}
			
			FileUtils.writeStringToFile(file, "#!/bin/sh \n", false);
			for (String sondPath : gnuSongs) {
				if (fix) {
					sondPath = encodeSong(sondPath);
				}
				sondPath = sondPath.trim();
				
				String execCommand = String.format(command, sondPath,  (sondPath.endsWith("mp3")? "" : "/*") );
				FileUtils.writeStringToFile(file, execCommand + "\n", true);
			}
			FileUtils.writeStringToFile(file, builderMktunes.toString() + "\n", true);
			Process p = Runtime.getRuntime().exec("/bin/sh " + file.getAbsolutePath()  );
			
			
			String line;
			BufferedReader bri = new BufferedReader(new InputStreamReader( p.getInputStream()));
			while ((line = bri.readLine()) != null) {
				//builder.append(line).append("\n");
				bar.appendToBar(line);
			}
			bri.close();
			
			BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			while ((line = bre.readLine()) != null) {
				//builder.append(line).append("\n");
				bar.appendToBar(line);
			}
			bre.close();
			p.waitFor();
						
		} catch (IOException e) {
			throw new  PodException(e.getMessage());
		} catch (InterruptedException e) {
			throw new  PodException(e.getMessage());
		} finally {
			if (!file.delete()) {
				//throw new  PodException("Tmp file couldn't be deleted");
			}
		}

		return builder.toString();
	}
	
	public static void initializePod(String ipodMountPoint)  throws PodException{
		StringBuilder builderCmd = new StringBuilder();
		builderCmd.append("gnupod_INIT  -m  ").append(ipodMountPoint);
		
		StringBuilder errorBuiler = new StringBuilder();
		Process p;
		try {
			p = Runtime.getRuntime().exec(builderCmd.toString());
			BufferedReader bri = new BufferedReader(new InputStreamReader( p.getInputStream()));

			String line;
			while ((line = bri.readLine()) != null) {
				errorBuiler.append(line).append("\n");
			}
			bri.close();
			p.waitFor();
		} catch (Exception e) {
			throw new PodException(e.getMessage());
		}
		if (errorBuiler.length() > 0) {
			throw new PodException(errorBuiler.toString());
		}
		
	}
}
