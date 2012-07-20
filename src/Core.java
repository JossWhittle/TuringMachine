import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs when you call it...
 * 
 * @author Joss
 * 
 */
public class Core {

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
		try {
			if (args.length >= 1) {
				loadProgram(new Scanner(new File(args[0])));
			} else {
				loadProgram(new Scanner(System.in));
			}
			runProgram();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Loads a program file into memory
	 * 
	 * @param dir
	 *            The path
	 */
	private void loadProgram(Scanner in) {
		TAPE = new ArrayList<Byte>(255);
		STATES = new ArrayList<State>(255);		
		// int c = 0;
		try {
			while (in.hasNext()) {
				// System.out.println(c++);
				in.next("(?iu)\\((\\d+),([B01]),([B01]),([LRN]),(\\d+|H)\\)");

				int sid = Integer.parseInt(in.match().group(1)) - 1;

				Byte scan = (byte) (Byte.parseByte(in.match().group(2)) + 1);
				Byte print = (byte) (Byte.parseByte(in.match().group(3)) + 1);
				byte direction = NULL;
				if (in.match().group(4).equals("L")) {
					direction = LEFT;
				} else if (in.match().group(4).equals("R")) {
					direction = RIGHT;
				}

				int next = -1;
				try {
					next = Integer.parseInt(in.match().group(5)) - 1;
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Runs the loaded program
	 */
	private void runProgram() {
		try {
			for (int i = 0; i < STATES.size(); i++) {
				System.out.println("[STATE " + i + "]\n" + STATES.get(i).toString());
			}
			System.out.println("\n\n");

			while (m_STATE != HALT) {
				Instruction i = currentState().get(read());
				if (i == null) {
					System.out.println("\n\nUndefined Behaviour: [State "+m_STATE+1+"] [Instruction "+read()+"]");
					System.exit(0);
				} else {
					
				}
			}
			
			System.out.println("\n\nAll done!");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
		return TAPE.get(m_HEAD);
	}
	
	/**
	 * Gets the current state
	 * @return The state
	 */
	private State currentState() {
		return STATES.get(m_STATE);
	}
	
	/**
	 * Sets the current state
	 * @param sid The state id
	 */
	private void setState(int sid) {
		m_STATE = sid;
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
