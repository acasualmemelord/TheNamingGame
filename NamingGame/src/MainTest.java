import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

class MainTest {
	
	boolean includes(char[] arr, char ch) {
		for(char c : arr) {
			if(c == ch) return true;
		}
		return false;
	}
	
	@Test
	void convergedTest() {
		ArrayList<Agent> arr = new ArrayList<Agent>();
		for(int i = 0; i < 10; i ++) {
			Agent a = new Agent(i + 1);
			a.addWord("foo");
			arr.add(a);
		}
		assertEquals(Main.converged(arr), true, "converged was false when it should have been true");
		
		ArrayList<Agent> arr2 = new ArrayList<Agent>();
		for(int i = 0; i < 10; i ++) {
			Agent a = new Agent(i + 1);
			a.addWord(i + "");
			arr2.add(a);
		}
		assertEquals(Main.converged(arr2), false, "converged was true when it should have be false");
	}
	
	@Test
	void listOfWordsTest() {
		//test 1: one agent, two words
		ArrayList<Agent> arr = new ArrayList<Agent>();
		Agent a = new Agent(1);
		a.addWord("foo");
		a.addWord("bar");
		arr.add(a);
		
		LinkedHashMap<String, Integer> map = Main.listOfWords(arr);
		assertEquals(2, map.size(), "wrong map size");
        assertEquals(1, map.get("foo"), "wrong count for foo");
        assertEquals(1, map.get("bar"), "wrong count for bar");
        
        //test 2: two agents, overlapping words
        arr = new ArrayList<Agent>();
		a = new Agent(1);
		a.addWord("foo");
		a.addWord("bar");
		arr.add(a);
		
		a = new Agent(2);
		a.addWord("foo");
		a.addWord("bad");
		arr.add(a);
		
		map = Main.listOfWords(arr);
		assertEquals(3, map.size(), "wrong map size");
        assertEquals(2, map.get("foo"), "wrong count for foo");
        assertEquals(1, map.get("bar"), "wrong count for bar");
        assertEquals(1, map.get("bad"), "wrong count for bad");
        
        //test 3: empty agents
        arr = new ArrayList<Agent>();
        arr.add(new Agent(1));
        arr.add(new Agent(2));
        
        map = Main.listOfWords(arr);
        assertTrue(map.isEmpty(), "map not empty");
	}
	
	@Test
	void shortenMapTest() {
		Map<String, Integer> map = new LinkedHashMap<>();
		for(int i = 0; i < 30; i ++) {
			map.put("foo", i);
		}
		Main.shortenMap(map);
		if(map.size() > 20) fail("shortenMap didn't shorten 30 to 20");
		
		map = new LinkedHashMap<>();
		for(int i = 0; i < 10; i ++) {
			map.put("foo", i);
		}
		Map<String, Integer> map2 = map;
		Main.shortenMap(map2);
		if(!map.equals(map2)) fail("shortenMap changed map when size < 20");
	}
	
	@Test
	void randomWordTest() {
		String word = Main.randomWord(new Random());
		if (word.length() < 1 || word.length() > 10) fail("incorrect length");
		char[] consonants = new char[] {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z'};
		char[] vowels = new char[] {'a', 'e', 'i', 'o', 'u', 'y'};
		String nextChar = "vowel";
		for (char c : word.toCharArray()) {
			if(nextChar.equals("vowel")) {
				if (includes(consonants, c)) fail("found two consonants");
				else nextChar = "consonant";
			}
			else {
				if (includes(vowels, c)) fail("found two vowels");
				else nextChar = "vowel";
			}
		}
	}
	
	
}
