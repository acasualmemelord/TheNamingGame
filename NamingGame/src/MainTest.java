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
	
	@Test
    void trialConvergenceTest() {
        Main.arr = new ArrayList<>(); // Reset static variables
        Main.maps = new ArrayList<>();
        
        int agentNum = 5;
        int maxSteps = 1000;
        boolean lattice = true;
        int connections = 2;
        boolean debug = false;

        Main.trial(agentNum, maxSteps, lattice, connections, debug);

        // Verify that all agents converged
        assertFalse(Main.arr.isEmpty(), "Steps array should not be empty");
        assertTrue(Main.arr.get(0) < maxSteps, "Agents should converge within maxSteps");
        assertEquals(1, Main.map.size(), "Final map should have only one word indicating convergence");
    }
	
	@Test
    void trialNonConvergenceTest() {
        Main.arr = new ArrayList<>();
        Main.maps = new ArrayList<>();

        int agentNum = 5;
        int maxSteps = 10; // Too few steps for convergence
        boolean lattice = false;
        int connections = 0; // Not relevant when lattice is false
        boolean debug = false;

        Main.trial(agentNum, maxSteps, lattice, connections, debug);

        // Verify that agents did not converge
        assertTrue(Main.arr.isEmpty(), "Steps array should be empty when agents do not converge");
        assertNotEquals(1, Main.maps.get(Main.maps.size() - 1).keySet().size(), "Final map should have more than one word");
    }
	
	@Test
    void trialTwoAgentsTest() {
        Main.arr = new ArrayList<>();
        Main.maps = new ArrayList<>();

        int agentNum = 2;
        int maxSteps = 500;
        boolean lattice = true;
        int connections = 1; // Only one connection possible
        boolean debug = true;

        Main.trial(agentNum, maxSteps, lattice, connections, debug);

        // Verify that agents converge
        assertFalse(Main.arr.isEmpty(), "Steps array should not be empty");
        assertTrue(Main.arr.get(0) < maxSteps, "Agents should converge within maxSteps");
    }
	
	@Test
    void trialInvalidInputTest() {
        // Test invalid number of agents
        assertThrows(IllegalArgumentException.class, () -> {
            Main.trial(1, 1000, true, 1, false);
        });

        // Test invalid connections
        assertThrows(IllegalArgumentException.class, () -> {
            Main.trial(5, 1000, true, 0, false);
        });

        // Test connections greater than agents
        assertThrows(IllegalArgumentException.class, () -> {
            Main.trial(5, 1000, true, 6, false);
        });
    }
}
