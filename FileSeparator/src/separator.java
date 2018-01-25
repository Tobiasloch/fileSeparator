import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

public class separator implements Runnable {
	
	private File inputFile;
	private File outputSource;
	
	private boolean foundType;
	private boolean foundSeparator;
	
	private ArrayList<Pattern> type;
	private ArrayList<Pattern> separators;
	private int separateAfterLines;
	
	String console;
	
	private boolean overrideOutputFiles = false;
	
	private ProgressMonitor monitor;
	
	private int returnValue;
	private boolean stop;
	private boolean printAllLines;
	private boolean running;
	
	public mainWindow mainFrame;
	
	separator(File inputFile, File outputSource, ArrayList<Pattern> type, ArrayList<Pattern> separators) {
		this(inputFile, outputSource);

		this.type = type;
		this.separators = separators;
	}
	
	separator(File inputFile, File outputSource) {
		this();
		
		this.inputFile = inputFile;
		this.outputSource = outputSource;
	}
	
	separator() {
		separators = new ArrayList<Pattern>();
		separators.add(Pattern.compile("00:00:00"));
		console = new String();
		console = "";
		separateAfterLines = 0;
		
		type = new ArrayList<Pattern>();
		type.add(Pattern.compile("\\d\\d:\\d\\d:\\d\\d"));
		}
	
	public int separateFile(boolean printAllLines)  { // 0: no exceptions; 1: file ist empty  2: file does not exist 3: create outputFile error 4: canceled 5: type fehlerhaft 6: Progress nicht bei 100 Prozent
		returnValue = 0;
		stop = false;
		running = true;
		this.printAllLines = printAllLines;
		
		foundSeparator = false;
		foundType = false;
		
		// setting up thread
		Thread separateThread = new Thread(this);
		separateThread.start();
		
		return 0;
	}
	
	public void stop (int returnValue)  {
		this.returnValue = returnValue;
		stop = true;
	}
	
	@Override
	public void run() {
		monitor = new ProgressMonitor(mainFrame, "Datei " + inputFile.getName() + " wird bearbeitet...", "", 0, 100);
		
		if (inputFile == null) {
			printConsole("Fehler (1): die uebergebene Datei ist leer.");
			stop(1);
		}
		if (outputSource == null) outputSource = inputFile.getParentFile();
		if (!inputFile.exists() || !outputSource.exists()) {
			printConsole("Fehler (2): die Datei existiert nicht.");
			stop(2);
		}
		
		// creates new file
		int fileCounter = 0;
		if (!createFile(getActiveFile(fileCounter))) {
			printConsole("Fehler (3): Die Dateien konnten nicht erstellt werden.");
			stop(3);
		}
		
		// initiating progressbar
		// pogress counter
		long progress = 0;
		mainFrame.updateConsole();
		
		// read file
		FileReader fr;
		try {
			fr = new FileReader(inputFile);
			BufferedReader br = new BufferedReader(fr);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getActiveFile(fileCounter))));
			
			// initiating line counter
			int lineCounter = 0;
			
			// get first line
			for (String line = br.readLine(); line != null && !stop; line = br.readLine()) {
				// inkrement lineCounter
				lineCounter++;
				
				// if monitor is canceled
				if (monitor.isCanceled()) {
					br.close();
					bw.close();
					printConsole("Fehler (4): Der Vorgang wurde abgebrochen.");
					stop(4);
					break;
				}
				
				if (!printAllLines) {
					for (int i = 0; i < type.size(); i++) { // searches for the type pattern in the active line
						Matcher m = type.get(i).matcher(line);
						
						if (m.find()) { // found the type and prints line
							bw.write(line);
							bw.newLine();
							foundType = true;
							
							break;
						}
					}
				} else {
					bw.write(line);
					bw.newLine();
				}
				
				if (separateAfterLines == 0) {
					for (int i = 0; i < separators.size(); i++) { // searches for the separators in the active line
						Matcher m = separators.get(i).matcher(line);
						
						if (m.find()) {
							fileCounter++;
							foundSeparator = true;
							
							if (!createFile(getActiveFile(fileCounter))) {
								br.close();
								bw.close();
								stop(3);
							}
							printConsole("Datei " + getActiveFile(fileCounter) + " wurde erstellt.");
							mainFrame.updateConsole();
							
							bw.close();
							bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getActiveFile(fileCounter))));
							break;
						}
					}
				} else if (lineCounter % (separateAfterLines) == separateAfterLines-1) {
					fileCounter++;
					foundSeparator = true;
					
					if (!createFile(getActiveFile(fileCounter))) {
						br.close();
						bw.close();
						stop(3);
						break;
					}
					printConsole("Datei " + getActiveFile(fileCounter) + " wurde erstellt.");
					mainFrame.updateConsole();
					
					bw.close();
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getActiveFile(fileCounter))));
				}
				
				// calculating progress
				progress+= line.getBytes().length+2; // +2, da der Zeilenumbruch mit einberechnet werden muss
				monitor.setProgress(getProgressinPercent(progress, inputFile.length()));
				monitor.setNote("Fortschritt: " + progress + "/" + inputFile.length() + " Bytes");
			}
			br.close();
			bw.close();
			
			if (progress < inputFile.length()) {
				printConsole("Fehler (6): Es kann sein, dass nicht die komplette Datei gelesen wurde.");
				stop(6);
			}
			
		} catch (IOException e) {
			printConsole("Fehler (2): Die Input Datei existiert nicht");
			stop(2);
		}
		
		if (returnValue == 0) JOptionPane.showMessageDialog(null, "Datei wurde erfolgreich ausgegeben!");
		else JOptionPane.showMessageDialog(null, "Der Vorgang wurde abebrochen.", "Fehler!", JOptionPane.ERROR_MESSAGE);
		printConsole("Das Programm wurde beendet. Fehlercode(" + returnValue + ")");
		monitor.close();
		
		if (!foundSeparator) printConsole("Es wurde keine neue Datei erstellt, bzw. kein Dateitrennausdruck wurde gefunden.");
		if (!foundType) printConsole("Es wurde keine Zeile gedruckt.");
		
		running = false;
		mainFrame.updateConsole();
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void printConsole(String s) {
		console+=s+"\n";
	}
	
	public void clearConsole() {
		console = "";
	}
	
	public String getConsoleText() {
		return console;
	}
	
	public void setSeparateFileAfterLines(int value) {
		separateAfterLines = value;
	}
	
	private int getProgressinPercent(long start, long end)  {
		return (int)(((float)start/(float)end)*100);
	}
	
	@SuppressWarnings("resource")
	boolean isFileEmpty(File f) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			try {
				if (br.readLine()==null) return true;
			} catch (IOException e) {
				e.printStackTrace();
				return true;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return true;
		}
		
		return false;
	}
	
	File getActiveFile(int fileCounter) {
		int pointSeparator = inputFile.getName().indexOf('.');
		
		String type = inputFile.getName().substring(pointSeparator);
		String name = inputFile.getName().substring(0, pointSeparator);
		return new File(outputSource.getPath() + '\\' + name + " - " + fileCounter + type);
	}
	
	boolean createFile(File file) {
		if (file.exists() && !overrideOutputFiles) {
			int reply = JOptionPane.showConfirmDialog(null, "Die outputDatei existiert bereits, sollen die outputs überschrieben werden?", "outputFile Error", JOptionPane.YES_NO_OPTION);
			printConsole("Fehler: Die output Dateien existieren bereits.");
			
			if (reply == JOptionPane.NO_OPTION)	return false;
			overrideOutputFiles = true;
			printConsole("Bereits vorhandene Dateien werden ueberschrieben.");
		}
		
		if (!file.getParentFile().exists()) {
			int reply = JOptionPane.showConfirmDialog(null, "Der Ordner in dem die output Dateien liegen existiert nicht. Soll er erstellt werden?", "outputFile Error", JOptionPane.YES_NO_OPTION);
			printConsole("Fehler: Der Ordner in dem die output Dateien gespeichert werden sollen, exisitert nicht!");
			
			if (reply == JOptionPane.NO_OPTION)	return false;
			file.getParentFile().mkdirs();
		}
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Die Dateien konnten nicht erstellt werden.", "outputFile Error", JOptionPane.ERROR_MESSAGE);
			printConsole("Fehler: Die Dateien konnten nicht erstellt werden.");
			return false;
		}
		
		return true;
	}
	
	boolean compareCharsInType(char string, char type, boolean typeIsConstant) {
		if (typeIsConstant) {
			if (string == type) return true;
			else return false;
		}
		
		if (Character.isDigit(type) && Character.isDigit(string)) return true;
		if (Character.isAlphabetic(type) && Character.isAlphabetic(string)) return true;
			
		return false;
	}
	
	

	public File getInputFile() {
		return inputFile;
	}

	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}

	public File getOutputSource() {
		return outputSource;
	}

	public void setOutputSource(File outputSource) {
		this.outputSource = outputSource;
	}

	public ArrayList<Pattern> getSeparators() {
		return separators;
	}

	public void setSeparators(ArrayList<Pattern> separators) {
		this.separators = separators;
	}

	public ArrayList<Pattern> getType() {
		return type;
	}

	public void setType(ArrayList<Pattern> type) {
		this.type = type;
	}
	
}
