package tuenti.contest.challenge8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Challenge8 {

	private static Logger log = Logger.getLogger(Challenge8.class);

	private String getFileName(int fileNo) {
		return "ch8_"+fileNo+".txt";
	}

	public static void main(String[] args) {
		Challenge8 main = new Challenge8();
		try {
			main.process();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void process() throws IOException {
		Scanner in = new Scanner(System.in);
		String queue = in.nextLine();
		log.debug("Starting queue: " + queue.toString());
		int i = 0;
		FileWriter fstream = new FileWriter(getFileName(i));
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(queue);
		out.close();
		while (in.hasNextLine()) {
			in.nextLine();
			processLine(in.nextLine(), i++);
		}
		System.out.println(md5(getFileName(i)));
	}

	//The only optimization I can see here is going to multithreading but as my comp is just a dual core
	//I will not see a lot of performance increase with that.
	private void processLine(String nextLine,
			int file) throws IOException {
		log.debug("Transforms: " + nextLine);
		String[] transforms = nextLine.split(",");
		HashMap<Character, char[]> transformMap = new HashMap<Character, char[]>();
		for (String transform : transforms) {
			String[] parts = transform.split("=>");
			transformMap.put(parts[0].charAt(0), parts[1].toCharArray());
		}
		
		//After a lot of tests with several functions to read and write files 
		//and search and replace in strings I found that the fastest one was just the simplest
		//Avoid transformations and just read an write
		//I use a big sized buffer to speed up the reads and writes
		FileWriter fstream = new FileWriter(getFileName(file+1));
		BufferedWriter out = new BufferedWriter(fstream, 1024 *128);
		FileReader fread = new FileReader(getFileName(file));
		BufferedReader in = new BufferedReader(fread, 1024 *128);
		char[] cBuf = new char[1024 *128];
		int read; 
		while((read =in.read(cBuf)) != -1) {
			for (int i = 0; i < read; i++) {
				char[] change = transformMap.get(cBuf[i]);
				if (change == null) {
					out.write(cBuf[i]);
				} else {
					out.write(change);
				}
			}
		}
		out.flush();
		in.close();
		out.close();
	}

	//Probably this part could be optimized but is not as critical as the translation part
	//It is just a MD5 function using java libraries
	private String md5(String file) throws FileNotFoundException, IOException {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return "";			
		}
		FileInputStream fread = new FileInputStream(file);
		byte[] cBuf = new byte[1024*1024];
		int read;
		while((read = fread.read(cBuf)) != -1) {
			messageDigest.update(cBuf, 0, read);
		}
		fread.close();
		
		byte[] resultByte = messageDigest.digest();
		BigInteger bigInt = new BigInteger(1, resultByte);
		String hashtext = bigInt.toString(16);
		while (hashtext.length() < 32) {
			hashtext = "0" + hashtext;
		}
		return hashtext;
	}
}