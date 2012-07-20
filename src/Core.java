import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Runs when you call it...
 * 
 * @author Joss
 * 
 */
public class Core {

	// CMD ARGS
	private boolean QUIET = false;
	private int TIMEOUT = 0;

	// Constants
	private static final Byte NULL = 0, ZERO = 1, ONE = 2, LEFT = -1, RIGHT = 1, HALT = -1;

	// Members
	private int m_HEAD = 0, m_STATE = 0;
	private ArrayList<Byte> TAPE = new ArrayList<Byte>(255);
	private ArrayList<State> STATES = new ArrayList<State>(255);

	/**
	 * Constructor
	 * 
	 * @param args
	 *            System Params
	 */
	public Core(String[] args) {

		// Command Line Args
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option file = parser.addStringOption('f', "file");
		CmdLineParser.Option quiet = parser.addBooleanOption('q', "quiet");
		CmdLineParser.Option timeout = parser.addIntegerOption('t', "timeout");
		CmdLineParser.Option help = parser.addBooleanOption('h', "help");

		// Parse Arguments
		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException e) {
			System.err.println(e.getMessage());
			printUsage();
			System.exit(2);
		}

		// Retrieve Values
		QUIET = (Boolean) parser.getOptionValue(quiet, Boolean.FALSE);

		TIMEOUT = (Integer) parser.getOptionValue(timeout, new Integer(0));

		String FILE = (String) parser.getOptionValue(file);

		// If -h || --help show help and exit
		if ((Boolean) parser.getOptionValue(help, Boolean.FALSE)) {
			printUsage();
			System.exit(2);
		}

		// Run
		try {
			if (FILE != null) {
				// Load from file and run
				if (loadProgram(new Scanner(new File(FILE)))) {
					runProgram();
				}
			} else {
				// Load from std input and run
				if (loadProgram(new Scanner(System.in))) {
					runProgram();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Loads a program file into memory
	 * 
	 * @param dir
	 *            The path return Was the load successful?
	 */
	private boolean loadProgram(Scanner in) {

		try {
			// Try to read the first line of the file as the initial state of
			// the TAPE
			String line = in.nextLine();

			// Does the line match a string of B's 0's and 1's?
			Pattern pattern = Pattern.compile("(?iux)^([01B]+)$");
			Matcher matcher = pattern.matcher(line);
			boolean tapeMatchFound = matcher.find();

			if (tapeMatchFound) {
				String[] t = in.match().group(0).trim().split("");
				for (String h : t) {
					TAPE.add(strByte(h));
				}
			}

			while (in.hasNext()) {
				if (tapeMatchFound) {
					line = in.nextLine();
				} else {
					tapeMatchFound = true;
				}

				// Does this line match the pattern of (1,B,0,R,2) ?
				pattern = Pattern.compile("(?iux)^\\((\\d+),([01B]),([01B]),([LRN]),(\\d+|H)\\)$");
				matcher = pattern.matcher(line);
				boolean matchFound = matcher.find();

				if (matchFound) {
					int sid = Integer.parseInt(matcher.group(1)) - 1;

					Byte scan = strByte(matcher.group(2));
					Byte print = strByte(matcher.group(3));
					Byte direction = (matcher.group(4).equals("L") ? LEFT : (matcher.group(4).equals("R")) ? RIGHT : NULL);

					int next = -1;
					try {
						next = Integer.parseInt(matcher.group(5)) - 1;
					} catch (Exception ex) {
					}

					while (STATES.size() <= sid) {
						STATES.add(new State());
					}
					if (STATES.get(sid) == null) {
						STATES.add(sid, new State());
					}

					STATES.get(sid).push(scan, new Instruction(print, direction, next));
				}
			}
			return true;
		} catch (Exception ex) {
			System.err.println("Error loading program...");
			System.exit(2);
		}
		return false;
	}

	/**
	 * Runs the loaded program
	 */
	private void runProgram() {
		if (TAPE.size() == 0) {
			TAPE.add(NULL);
		}
		try {
			String t = "[TAPE]\n\t";
			for (Byte b : TAPE) {
				t += "" + b;
			}
			System.out.println(t + "\n");

			for (int i = 0; i < STATES.size(); i++) {
				System.out.println("[STATE " + i + "]\n" + STATES.get(i).toString());
			}
			System.out.println();

			while (m_STATE != HALT) {
				Instruction i = currentState().get(read());
				if (i == null) {
					System.err.println("Undefined Behaviour: [State " + (m_STATE) + "] [Instruction " + read() + "]\n");
					printTape();
					System.exit(2);
				} else {
					write(i.getPrint());
					moveHead(i.getDir());
					setState(i.getNext());

					if (!QUIET || m_STATE == HALT) {
						printTape();
					}

					if (!QUIET && TIMEOUT > 0) {
						Thread.sleep(TIMEOUT);
					}
				}
			}

			System.out.println("\nAll done!");

		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	private void printTape() {
		String t = "";
		for (Byte b : TAPE) {
			t += "" + (b == 0 ? "_" : (b - 1));
		}
		System.out.println(t);
	}

	/**
	 * Moves the tape head accordingly
	 * 
	 * @param dir
	 *            The direction to move
	 */
	private void moveHead(byte dir) {
		m_HEAD += dir;
		if (m_HEAD < 0) {
			TAPE.add(0, NULL);
			m_HEAD = 0;
		} else if (m_HEAD >= TAPE.size()) {
			TAPE.add(NULL);
		}
	}

	/**
	 * Writes to the tape
	 * 
	 * @param b
	 *            The value to write
	 */
	private void write(Byte b) {
		TAPE.set(m_HEAD, b);
	}

	/**
	 * Reads from the tape
	 * 
	 * @return The value
	 */
	private Byte read() {
		return (byte) (TAPE.get(m_HEAD));
	}

	/**
	 * Gets the current state
	 * 
	 * @return The state
	 */
	private State currentState() {
		return STATES.get(m_STATE);
	}

	/**
	 * Sets the current state
	 * 
	 * @param sid
	 *            The state id
	 */
	private void setState(int sid) {
		m_STATE = sid;
	}

	/**
	 * Takes a string and converts it to either NULL, ZERO, or ONE
	 * 
	 * @param h
	 *            The string
	 * @return The byte
	 */
	private Byte strByte(String h) {
		return ((h.equals("0")) ? ZERO : ((h.equals("1")) ? ONE : NULL));
	}

	/**
	 * Prints the help message
	 */
	private void printUsage() {
		System.err.println("Usage: Turing Machine [{-f,--file} path]\n"
				+ "                      [{-t,--timeout} duration]\n" + "                      [{-q,--quiet}]\n"
				+ "                      [{-h,--help}]");
	}

	/**
	 * Entry Point
	 * 
	 * @param args
	 *            System Params
	 */
	public static void main(String[] args) {
		new Core(args);
	}
}
