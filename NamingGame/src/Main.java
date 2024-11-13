import java.util.HashMap;
import java.util.Random;

public class Main {
	public static void main(String[] args) {
		int trials = 1;
		int agentNum = 20;
		int maxSteps = 10;
		for (int i = 0; i < trials; i ++) {
			System.out.print("trial " + (i + 1) + ": ");
			trial(agentNum, maxSteps, true, 4, true);
		}
	}

	/**
	 * Runs one trial of the Naming Game.
	 * @param agentNum number of agents
	 * @param maxSteps maximum number of steps
	 * @param lattice whether agents have more random neighbors or 2
	 * @param connections how many connections each agent should have
	 * @param debug whether console prints out debug messages
	 */
	public static void trial(int agentNum, int maxSteps, boolean lattice, int connections, boolean debug) {
		if (lattice && connections > agentNum) throw new IllegalArgumentException("the amount of connections cannot exceed the number of agents");
		
		Random r = new Random();
		int steps = 0;
		boolean converged = false;
		Agent[] agents = new Agent[agentNum];
		
		//create list of agents
		for(int i = 0; i < agents.length; i ++) {
			agents[i] = new Agent(i + 1);
		}
		
		//if lattice, create connections for each agent equal to the parameter specified
		if(lattice) {
			for(Agent a : agents) {
				while(a.connections() < connections) {
					a.addConnection(agents[r.nextInt(agents.length)]);
				}
			}
		}
		
		//loop until maxsteps are reached or all agents converge
		while(steps < maxSteps) {
			if(converged(agents)) {
				converged = true;
				break;
			}
			
			if(debug) System.out.print("step " + steps + ": ");
			
			//pick two random agents
			int test = r.nextInt(0, agents.length);
			int test2 = 0;
			Agent agent = agents[test];
			Agent agent2;
			if (lattice) {
				// if lattice pick from random connection
				agent2 = agent.getRandomConnection();
				test2 = agent2.getID() + 1;
			} else {
				//otherwise pick random neighbor
				if (test == 0) test2 = 1;
				else if (test == agents.length - 1) test2 = agents.length - 2;
				else {
					if (r.nextBoolean()) test2 = test - 1;
					else test2 = test + 1;
				}
				agent2 = agents[test2];
			}
			
			
			if(agent.inventory() == 0) {
				// if speaker has no words, add a word to both speaker and listener
				if (debug) System.out.println("Agent " + (test + 1) + " has no words, adding a random word");
				agent.addWord(randomWord(r));
				agent2.addWord(randomWord(r));
			} else {
				String str = agent.getRandomWord();
				if (debug) System.out.println("Agent " + (test + 1) + " conveys word " + str);
				
				if(agent2.contains(str)) {
					//agent contains word
					if (debug) System.out.println("Agent " + (test2 + 1) + " has that word");
					agent.removeAllExcept(str);
					agent2.removeAllExcept(str);
				} else {
					//agent does not contain word
					if (debug) System.out.println("Agent " + (test2 + 1) + " does not have that word");
					agent2.addWord(str);
				}
			}
			steps ++;
		}
		if(converged) System.out.println("All agents converged within " + steps + " steps");
		else System.out.println("Agents did not converge within the maximum steps");
		
		if(debug) {
			for(Agent a : agents) {
				System.out.println(a);
			}
			System.out.println();
		}
		HashMap<String, Integer> map = listOfWords(agents);
		for (String name: map.keySet()) {
		    String key = name.toString();
		    String value = map.get(name).toString();
		    System.out.println(key + ": " + value);
		}
	}
	
	/**
	 * Creates a random word with a length between 1 and 10.
	 * @param r
	 * @return random word
	 */
	public static String randomWord(Random r) {
		int length = r.nextInt(1, 11);
		String result = "";
		for(int i = 0; i < length; i ++) {
			result += (char) (64 + r.nextInt(1, 26));
		}
		return result;
	}
	
	/**
	 * Checks if all agents have an inventory of 1 consisting of the same word.
	 * @param agents
	 * @return true if if all agents have an inventory of 1 consisting of the same word; false otherwise
	 */
	public static boolean converged(Agent[] agents) {
		if(agents[0].inventory() == 0) return false;
		String s = agents[0].getTopWord();
		for(Agent agent : agents) {
			if(agent.inventory() != 1 || !agent.getTopWord().equals(s)) return false;
		}
		return true;
	}
	
	/**
	 * Gets a hashmap of all words present among all agents, 
	 * where the key is the word and the value is the amount of times it appears.
	 * @param agents the list of agents used in a trial
	 * @return a HashMap of String/Integer
	 */
	public static HashMap<String, Integer> listOfWords(Agent[] agents){
		HashMap<String, Integer> list = new HashMap<String, Integer>();
		for(Agent a: agents) {
			for(String s: a.getAllWords()) {
				if(!list.containsKey(s)) list.put(s, 1);
				else list.replace(s, list.get(s) + 1);
			}
		}
		return list;
	}
}
