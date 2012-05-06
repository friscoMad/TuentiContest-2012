package tuenti.contest.challenge19;

/*File LZ77v01.java 
Copyright 2006, R.G.Baldwin
LZ77 is the name given to a lossless data compression
algorithm published in papers by Abraham Lempel and Jacob
Ziv in 1977. This algorithm forms the basis for many LZ
variations including LZW, LZSS and others. LZ77 is known
as a dictionary encoding algorithm.
This program implements a very close approximation to the
LZ77 compression algorithm.  While it may not match that
algorithm exactly, the match is close enough to satisfy
the primary purpose of the program, which is to explain
how the LZ77 algorithm works.
This program is provided for educational purposes only. If
you use the program for any purpose, you use it at your
own risk.  The author of the program accepts no
responsibility for any damages that may result from your
use of the program.
The program was specifically designed to serve its primary
purpose of education.  No thought was given to speed,
efficiency, memory utilization, or any other factor that
would be important in a program written for production
data compression purposes.  In some cases, the program was
purposely made less efficient by using two or more
statements to accomplish a task that could be accomplished
by a single more complex statement.
Several sample messages are hard-coded into the program.
You can switch between those messages by enabling and
disabling specific messages using comments and then
recompiling the program.  You can also insert your own
message and recompile the program to use your message if
you choose to do so.
The program first encodes a message into a collection of
Tuple objects stored in an ArrayList object using a very
close approximation to the LZ77 lossless data compression
algorithm.
Then the program decodes the message by extracting the
information from the Tuple objects and using that
information to reconstruct the original message.
The program does not actually convert the encoded
(compressed) message into a bit stream, although it
wouldn't be difficult to do so.  Since the purpose of the
effort was to illustrate the behavior of the LZ77
algorithm, and not to write a production data compression
program, converting the compressed data into a bit stream
was considered to be superfluous to the purpose.  However,
the program does provide a theoretical analysis of the
amount of compression that should be achieved if the
results were converted into a bit stream.
This program was tested against the demonstration Java
program named LZ that can be downloaded from
http://www.mathcs.sjsu.edu/faculty/khuri/animation.html
Neither the source code from that program nor the source
code from any other existing Java program was consulted
during the development of the code for this program.
Although numerous technical descriptions of the LZ77
algorithm were consulted, all of the source code in this
program is the original work of this author.
The animated interactive nature of the demonstration
program mentioned above was extremely helpful in helping
this author to understand the LZ77 algorithm.
The output from this program matches the output from the
above mentioned demonstration program for 60 characters of
input data and values of 10 for searchWindowLen and
lookAheadWindowLen. (The demonstration program is limited
to an input data length of 60 characters.)
Quite a lot of output material is displayed on the screen
when this program is running.  This material is designed to
be used in a tutorial lesson to explain the behavior of
the LZ lossless data compression algorithm.
The program was tested using J2SE 5.0 and WinXP.  J2SE 5.0
or later is required due to the use of generics.
 **********************************************************/
import java.util.*;

class LZ77v01 {
	// Use comments to enable and disable and to select
	// between the following demonstration messages.
	/*
	 * String rawData = "BAGHDAD, Iraq Violence increased " + "across Iraq after
	 * a lull following the Dec. 15 " + "parliamentary elections, with at least
	 * two dozen " + "people including a U.S. soldier killed Monday in " +
	 * "shootings and bombings mostly targeting the Shiite-" + "dominated
	 * security services. The Defense Ministry " + "director of operations,
	 * Brig. Gen. Abdul Aziz " + "Mohammed-Jassim, blamed increased violence in
	 * the " + "past two days on insurgents trying to deepen the " + "political
	 * turmoil following the elections. The " + "violence came as three Iraqi
	 * opposition groups " + "threatened another wave of protests and civil " +
	 * "disobedience if allegations of fraud are not " + "properly
	 * investigated.";
	 */
	String rawData = "Miss Kalissippi from Mississippi is a "
			+ "cowgirl who yells yippi when she rides her horse in "
			+ "the horse show in Mississippi.";
	// A collection of Tuple objects
	ArrayList<Tuple> encodedData = new ArrayList<Tuple>();

	// A reference to a single Tuple object
	Tuple thisTuple;

	// A substring that represents the search window.
	String searchSubstring;

	// Working variables
	int matchLen;
	int matchLoc;
	int charCnt;
	int searchWindowStart;
	int lookAheadWindowEnd;

	// Modify the following values to change the length of
	// the search window and/or the lookAhead window.
	// Optimum values are one less than even powers of 2.
	int searchWindowLen = 7;
	int lookAheadWindowLen = 3;

	// -----------------------------------------------------//

	public static void main(String[] args) {
		LZ77v01 instance = new LZ77v01();
		instance.doIt(instance.rawData);
	}// end main

	// -----------------------------------------------------//

	void doIt(String rawData) {

		// Display a scale on the screen.
		System.out.println("123456789012345678901234567890"
				+ "12345678901234567890123456789");
		// Display the raw data, 59 characters to the line.
		display59(rawData);
		System.out.println();// blank line

		// Process the message, one char at a time.
		charCnt = 0;
		while (charCnt < rawData.length()) {
			// Establish the beginning of the search window.
			searchWindowStart = (charCnt - searchWindowLen >= 0) ? charCnt
					- searchWindowLen : 0;
			// Establish the end of the lookAhead window.
			lookAheadWindowEnd = (charCnt + lookAheadWindowLen < rawData
					.length()) ? charCnt + lookAheadWindowLen : rawData
					.length();

			// Display the contents of the search window.
			System.out.print(rawData.substring(searchWindowStart, charCnt)
					+ " - ");
			// Display the contents of the lookAheadWindow on the
			// same line as the search window, separated by a
			// dash.
			System.out.print(rawData.substring(charCnt, lookAheadWindowEnd)
					+ " - ");

			// Get a substring from the search window to search
			// for a match.
			if (charCnt == 0) {
				// Begin with an empty search window.
				searchSubstring = "";
			} else {
				searchSubstring = rawData.substring(searchWindowStart, charCnt);
			}// end else
			// Search the search window for a match to the next
			// character in the lookAhead window.
			matchLen = 1;
			String searchTarget = rawData
					.substring(charCnt, charCnt + matchLen);
			if (searchSubstring.indexOf(searchTarget) != -1) {
				// A match was found for the one-character string.
				// See if the match extends beyond one character.
				// Limit the length of the possible match to
				// lookAheadWindowLen
				matchLen++;// Increment length of searchTarget.
				while (matchLen <= lookAheadWindowLen) {
					// Keep testing and extending the length of the
					// string being tested for a match until the
					// test fails.
					searchTarget = rawData.substring(charCnt, charCnt
							+ matchLen);
					matchLoc = searchSubstring.indexOf(searchTarget);
					// Be careful to avoid searching beyond the end
					// of the raw data.
					if ((matchLoc != -1)
							&& ((charCnt + matchLen) < rawData.length())) {
						matchLen++;
					} else {
						// The matching test failed. Break out of the
						// loop.
						break;
					}// end else
				}// end while
				// Reduce matchLen to the longest length that
				// matched.
				matchLen--;

				// We went one step too far increasing matchLen. Go
				// back and get the location in the search window
				// where the last match occurred.
				matchLoc = searchSubstring.indexOf(rawData.substring(charCnt,
						charCnt + matchLen));

				// Increase the character counter to cause the
				// outer loop to skip the matching characters.
				charCnt += matchLen;

				// Encapsulate the following information in a Tuple
				// object.
				// 1. Offset distance back to the location of the
				// match in the search window.
				// 2. Length of the match.
				// 3. First non-matching character in the lookAhead
				// window
				// Calculate the offset
				int offset = (charCnt < (searchWindowLen + matchLen)) ? charCnt
						- matchLoc - matchLen : searchWindowLen - matchLoc;
				// Get and save the next non-matching character in
				// the lookAhead window.
				String nextChar = rawData.substring(charCnt, charCnt + 1);
				// Instantiate and populate the Tuple object.
				thisTuple = new Tuple(offset, matchLen, nextChar);
				// Save the Tuple object in a Collection object of
				// type ArrayList.
				encodedData.add(thisTuple);
			} else {
				// A match was not found for the next character.
				// Encapsulate the following information in a Tuple
				// object.
				// 1. 0
				// 2. 0
				// 3. The non-matching character.
				String nextChar = rawData.substring(charCnt, charCnt + 1);
				thisTuple = new Tuple(0, 0, nextChar);
				encodedData.add(thisTuple);
			}// end else

			// Increment the character counter that controls the
			// outer loop.
			charCnt++;

			// Display the contents of the Tuple on the same line
			// as the search window and the lookAhead window,
			// separated by a dash.
			System.out.print(thisTuple + " - ");

			// Display the text represented by the Tuple on the
			// same line as above, separated by a dash.
			if (thisTuple.stringLen > 0) {
				// The Tuple contains a pointer to a character or
				// string in the search window. Expand and
				// display it.
				int start = charCnt - 1 - thisTuple.stringLen
						- thisTuple.offset;
				int end = charCnt - 1 - thisTuple.offset;
				System.out.println(rawData.substring(start, end)
						+ thisTuple.nextChar);
			} else {
				// The tuple contains a character for which no
				// match was found. Display it.
				System.out.println(thisTuple.nextChar);
			}// end else

		}// end while loop

		/***********************************************************************
		 * The original message has been encoded into a collection of Tuple
		 * objects where each object contains the following information: 1.
		 * offset points backwards to the beginning of a match relative to the
		 * current location. Has a value of 0 if there is no match for the next
		 * character. 2. stringLen specifies the length of the match or 0 if
		 * there is no match for the next character. 3. nextChar is the first
		 * non-matching character following a match, or the next character if
		 * there is no match.
		 * 
		 * It would not be particularly difficult at this point to iterate on
		 * the collection and to produce a binary stream containing these three
		 * values. For example, depending on the values specified for
		 * searchWindowLen and lookAheadWindowLen, the first two values listed
		 * above could be encoded into the number of bits required to contain
		 * the largest possible offset value and the largest possible stringLen
		 * value. The character from the Tuple object could be encoded into
		 * eight bits. Then, the set of bits required to describe the Tuple
		 * could be inserted into a stream of bits.
		 * 
		 * This demonstration program does not produce such a compressed binary
		 * file.
		 **********************************************************************/

		// Now decode the encoded data from the Tuple objects.
		// Reconstruct and display the original message.
		// Reconstruct the message into the following
		// StringBuffer object.
		StringBuffer reconData = new StringBuffer();

		// Get an iterator on the collection of Tuple objects.
		Iterator<Tuple> iterator = encodedData.iterator();

		// Iterate on the collection
		while (iterator.hasNext()) {
			Tuple nextTuple = iterator.next();
			if (nextTuple.stringLen == 0) {
				// There was no match for this character. Just
				// append it to the StringBuffer.
				reconData.append(nextTuple.nextChar);
			} else {
				// This Tuple contains information about a match.
				// Use that information to reconstruct and append
				// the matching data.
				for (int cnt = 0; cnt < nextTuple.stringLen; cnt++) {
					// Iterate once for each character in the string
					// that must be reconstructed.
					// Obtain the matching characters from the
					// previously constructed part of the output
					// based on an offset value and a stringLen value
					// that are stored in the Tuple.
					char workingChar = reconData.charAt(reconData.length()
							- nextTuple.offset);
					// Append the character to the StringBuffer. Note
					// that this increases the length of the
					// StringBuffer object. Thus, the next iteration
					// of the for loop will get the next character
					// that is already in the StringBuffer object.
					reconData.append(workingChar);
				}// end for loop
				// Now append the non-matching character that is
				// stored in the Tuple.
				reconData.append(nextTuple.nextChar);
			}// end else
		}// end while

		// The original message has been reconstructed.
		// Display a scale on the screen.
		System.out.println("n123456789012345678901234567890"
				+ "12345678901234567890123456789");

		// Now display the reconstructed message 59 characters
		// to the line.
		display59(new String(reconData));

		// Analyze the compression
		System.out.println("nAnalyze the compression.");
		System.out.println("Assume:");
		System.out.println(" 8 bits required per raw data character.");

		// Calculate the number of bits required to contain the
		// largest values that can occur in the values for
		// offset and stringLen in the Tuple. Assume that the
		// character in the Tuple is contained in 8 bits.
		int offsetBitsRequired = getBitsRequired(searchWindowLen);
		int stringLenBitsRequired = getBitsRequired(lookAheadWindowLen);
		int tupleBitsRequired = 8 + offsetBitsRequired + stringLenBitsRequired;

		System.out.println(" Maximum offset value of " + searchWindowLen
				+ " char requires " + offsetBitsRequired + " bits.");
		System.out.println(" Maximum stringLen value of " + lookAheadWindowLen
				+ " char requires " + stringLenBitsRequired + " bits.");
		System.out.println(" Character in Tuple requires 8 bits.");

		System.out.println(" " + tupleBitsRequired
				+ " bits required per Tuple.");
		int msgLength = rawData.length() * 8;
		System.out.println("Raw data length = " + msgLength + " bits.");
		System.out.println("Number Tuples: " + encodedData.size());
		int tupleLength = encodedData.size() * tupleBitsRequired;
		System.out.println("Total Tuple length = " + tupleLength + " bits.");
		System.out.println("Compression factor = " + (double) msgLength
				/ tupleLength);

	}// end doIt

	// -----------------------------------------------------//

	// Method to display a String 59 characters to the line.
	void display59(String data) {
		for (int cnt = 0; cnt < data.length(); cnt += 59) {
			if ((cnt + 59) < data.length()) {
				// Display 59 characters.
				System.out.println(data.substring(cnt, cnt + 59));
			} else {
				// Display the final line, which may be short.
				System.out.println(data.substring(cnt));
			}// end else
		}// end for loop
	}// end display59

	// -----------------------------------------------------//

	// Method to return the number of bits required to
	// contain a given integer value. (There must be a
	// better way to do this, such as finding the log to the
	// base 2.)
	int getBitsRequired(int value) {
		if (value < 2) {
			return 1;
		} else if (value < 4) {
			return 2;
		} else if (value < 8) {
			return 3;
		} else if (value < 16) {
			return 4;
		} else if (value < 32) {
			return 5;
		} else if (value < 64) {
			return 6;
		} else if (value < 128) {
			return 7;
		} else if (value < 256) {
			return 8;
		} else {
			System.out
					.println("Bit conversion too large, terminating program.");
			System.exit(1);
			return 0;// Make compiler happy.
		}// end else
	}// end getBitsRequired

	// -----------------------------------------------------//

	// The purpose of this inner class is to provide a
	// wrapper for the three critical data values that
	// are produced during implementation of the LZ77
	// compression algorithm.
	class Tuple {
		// offset points to the beginning of a match relative
		// to the current location. Has a value of 0 if there
		// is no match.
		int offset;
		// stringLen specifies the length of the match or 0 if
		// there is no match
		int stringLen;
		// nextChar is the first non-matching character
		// following the match, or the only character if there
		// is no match.
		String nextChar;

		// ---------------------------------------------------//

		// Constructor
		Tuple(int offset, int stringLen, String nextChar) {
			this.offset = offset;
			this.stringLen = stringLen;
			this.nextChar = nextChar;
		}// end constructor

		// ---------------------------------------------------//

		// Overridden toString method
		public String toString() {
			return offset + "," + stringLen + "," + nextChar;
		}// end toString
	}// end class Tuple
	// -----------------------------------------------------//
}// end class LZ77v01
