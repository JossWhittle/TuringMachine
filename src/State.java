/**
 * Holds instructions for the three TAPE values at this state
 * 
 * @author Joss
 * 
 */
public class State {

	// Constants
	private static final int OPS = 3;

	// Members
	private Instruction[] m_arr;

	/**
	 * Constructor
	 */
	public State() {
		m_arr = new Instruction[OPS];
	}

	/**
	 * Pushes an instruction into the state
	 * 
	 * @param i
	 *            The instruction
	 * @return Was the operation a success? Will there be cake?
	 */
	public boolean push(int n, Instruction i) {
		try {
			if (m_arr[n] == null) {
				m_arr[n] = i;
				return true;
			}
		} catch (Exception ex) {
		}
		return false;
	}

	/**
	 * Gets the instruction matching the tape
	 * 
	 * @param match
	 *            The tape value
	 * @return The instruction
	 */
	public Instruction get(Byte match) {
		return m_arr[match];
	}
	
	/**
	 * Prints the class contents
	 */
	public String toString() {
		String r = "";
		for (Instruction i : m_arr) {
			try {
				r += i.toString()+"\n";
			} catch(Exception ex) {}
		}
		return r;
	}
}
