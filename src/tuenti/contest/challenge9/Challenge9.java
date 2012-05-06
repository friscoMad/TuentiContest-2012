package tuenti.contest.challenge9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.Logger;

import tuenti.contest.challenge9.SearchMatcher.Match;

public class Challenge9 {
	private static Logger log = Logger.getLogger(Challenge9.class);

	private String getFileName(int fileNo) {
		String formatted = String.format("%04d", fileNo);
		return "D:\\eclipse\\workspace\\tuenti2\\extra\\documents\\"+formatted;
	}

	//Used JEdit implemetantion of Boyer-Moore for searching
	//Modified to work only including 2 files
	public static void main(String[] args) {
		Challenge9 main = new Challenge9();
		try {
			main.process();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void process() throws IOException {
		Scanner in = new Scanner(System.in);
		int words = in.nextInt();
		in.nextLine();
		for (int i = 0; i < words; i++) {
			String word = in.next("\\w+");
			int times = in.nextInt();
			if (in.hasNextLine()) {
				in.nextLine();
			}
			log.debug("Word: " + word + " Times: " + times);
			String position = findFile(word, times);
			log.debug(position);
			System.out.println(position);
		}
		
	}

	//Find the file containing the word we want
	private String findFile(String word, int times) throws IOException {
		BoyerMooreSearchMatcher searchMatcher = new BoyerMooreSearchMatcher(word,true,true); 
		for (int i = 1; i < 801; i++) {
			int fileTimes = times;
			String fileContents = getFile(i);
			Match match;
			while ((match =searchMatcher.nextMatch(fileContents, false, true, false, false)) != null) {
				fileContents = fileContents.substring(match.start+1);
				times--;
				if (times == 0) {
					log.debug("found in file: " +i);
					File file = new File(getFileName(i));		
					return i+"-"+findLineAndPos(file, word, fileTimes, searchMatcher);
				}
			}
		}
		return "";
	}
	
	private String getFile(int i) throws IOException {
		String file = readFileAsString(getFileName(i));
		return file;
	}
	
	//Read file to string, if files were bigger we must change this to another implementation
    private static String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    //Search line with the searchd word
    //Currently this part is the slowest, but I didn't found a easier way to conver from character position to
    //line number
	private String findLineAndPos(File file, String word, int times, BoyerMooreSearchMatcher searchMatcher) throws FileNotFoundException {
		Scanner scan = new Scanner(file);
		long lineCount = 1;		
		while (scan.hasNextLine()) {
			String originalLine = scan.nextLine();
			String line = originalLine;
			Match match; 
			int lineTimes = times;
			while ((match =searchMatcher.nextMatch(line, false, true, false, false)) != null) {
				line = line.substring(match.start+1);
				times--;
				if (times == 0) {
					log.debug("found in line: " +lineCount);
					return lineCount + "-" + findWordNumber(originalLine, word, lineTimes);
				}
			}			
			lineCount++;
		}
		return null;
	}

	//Find the word number
	private long findWordNumber(String line, String word, int lineTimes) {
		Scanner scan = new Scanner (line);
		int wordNo = 1;
		word = word.toLowerCase();
		while(scan.hasNext("(\\w|\\d)+")) {
			String word2 = scan.next("\\w+");
			if (word2.toLowerCase().equals(word)) {
				lineTimes--;
				if (lineTimes == 0) {
					return wordNo;
				}
			}
			wordNo++;
		}
		return 0;
	}

}
