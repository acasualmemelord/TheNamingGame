import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class Agent {
	private int id;
	private String startingWord;
	private ArrayList<String> words;
	
	public Agent (int id) {
		this.id = id;
		this.words = new ArrayList<String>();
	}
	
	public String getStartingWord() {
		return startingWord;
	}
	
	public String getTopWord() {
		return words.get(0);
	}
	
	public String getRandomWord() {
		Random r = new Random();
		return words.get(r.nextInt(0, words.size()));
	}
	
	public int inventory() {
		return words.size();
	}
	
	public boolean contains(String word) {
		return words.contains(word);
	}
	
	public void addWord(String word) {
		if(words.size() == 0) startingWord = word;
		words.add(word);
	}
	
	public void removeAllExcept(String word) {
		for (Iterator<String> iterator = words.iterator(); iterator.hasNext(); ) {
		    String str = iterator.next();
		    if (!str.equals(word)) {
		        iterator.remove();
		    }
		}
	}
	
	public String toString() {
		return String.format("Agent %d (starting word %s): %s", id, startingWord, Arrays.toString(words.toArray()));
	}
}
