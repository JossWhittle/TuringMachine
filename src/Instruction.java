/**
 * Holds instruction info
 * 
 * @author Joss
 * 
 */
public class Instruction {

	// Constants

	// Members
	private Byte m_print;
	private Byte m_dir;
	private int m_next;

	/**
	 * Constructor
	 */
	public Instruction(Byte print, byte dir, int next) {
		m_print = print;
		m_dir = dir;
		m_next = next;
	}

	/**
	 * Get print
	 * 
	 * @return Print
	 */
	public Byte getPrint() {
		return m_print;
	}

	/**
	 * Gets the direction
	 * 
	 * @return The dir
	 */
	public byte getDir() {
		return m_dir;
	}

	/**
	 * Gets the next instruction
	 * 
	 * @return The id
	 */
	public int getNext() {
		return m_next;
	}
	
	/**
	 * Prints the contents
	 * return The contents of the class
	 */
	public String toString() {
		return "("+(m_print == 0 ? "B" : (m_print-1))+","+(m_dir < 0 ? "L" : (m_dir > 0 ? "R" : "N"))+","+((m_next == -1) ? "H" : m_next)+")";
	}
}
