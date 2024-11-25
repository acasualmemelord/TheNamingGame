import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class Agent {
	private int id;
	private String startingWord;
	private ArrayList<String> words;
	private ArrayList<Agent> connections;
	
	public Agent (int id) {
		this.id = id;
		this.words = new ArrayList<String>();
		this.connections = new ArrayList<Agent>();
	}
	
	/**
	 * Gets a random word from the wordlist.
	 * @return a random word from the wordlist 
	 */
	public String getRandomWord() {
		Random r = new Random();
		return words.get(r.nextInt(0, words.size()));
	}
	
	/**
	 * Adds a word to inventory. If this is the first word, it is set as the starting word.
	 * @param word the word to add
	 */
	public void addWord(String word) {
		if(words.size() == 0) startingWord = word;
		words.add(word);
	}
	
	/**
	 * Adds a connection to an agent. Returns false if the agent is already connected.
	 * @param a an agent
	 * @return whether adding was successful
	 */
	public boolean addConnection(Agent a) {
		if(connections.contains(a) || a.getID() == id) return false;
		connections.add(a);
		a.getConnections().add(this);
		return true;
	}
	
	/**
	 * Gets a random connection.
	 * @return
	 */
	public Agent getRandomConnection() {
		if (connections.size() == 0) throw new IndexOutOfBoundsException("No connection found!");
		Random r = new Random();
		return connections.get(r.nextInt(connections.size()));
	}
	
	/**
	 * Removes all words except the word specified.
	 * @param word the word to keep
	 */
	public void removeAllExcept(String word) {
		for (Iterator<String> iterator = words.iterator(); iterator.hasNext(); ) {
		    String str = iterator.next();
		    if (!str.equals(word)) {
		        iterator.remove();
		    }
		}
	}
	
	//fairly self explanatory methods
	public int getID() {
		return id;
	}
	
	public String getStartingWord() {
		return startingWord;
	}
	
	public String getTopWord() {
		return words.get(0);
	}
	
	public ArrayList<String> getAllWords() {
		return words;
	}
	
	public int inventory() {
		return words.size();
	}
	
	public boolean contains(String word) {
		return words.contains(word);
	}
	
	public int connections() {
		return connections.size();
	}
	
	public ArrayList<Agent> getConnections(){
		return connections;
	}
	
	public void printConnections() {
		System.out.printf("Agent %d: %d connections\n", id, connections.size());
	}
	
	public String toString() {
		return String.format("Agent %d (starting word %s): %s", id, startingWord, Arrays.toString(words.toArray()));
	}
}
