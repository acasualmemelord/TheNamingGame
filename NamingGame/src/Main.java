import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {
	public static HashMap<String, Integer> map = new HashMap<>();
	public static ArrayList<Integer> arr = new ArrayList<>();
	public static void main(String[] args) {
		int trials = 1000;
		int agentNum = 500;
		int maxSteps = 1000000;
		for (int i = 0; i < trials; i ++) {
			System.out.print("trial " + (i + 1) + ": ");
			trial(agentNum, maxSteps, true, 2, false);
		}
		int sum = 0;
		for (int i : arr) {
			sum += i;
		}
		System.out.println("average: " + (sum / arr.size()));
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
		if (lattice && connections < 1) throw new IllegalArgumentException("agents must have at least one connnection");
		if (lattice && connections > agentNum) throw new IllegalArgumentException("the amount of connections cannot exceed the number of agents");
		
		Random r = new Random();
		int steps = 0;
		boolean converged = false;
		
		//create list of agents
		ArrayList<Agent> agents = new ArrayList<Agent>();
		ArrayList<Agent> list = new ArrayList<Agent>();
		for(int i = 0; i < agentNum; i ++) {
			Agent a = new Agent(i + 1);
			agents.add(a);
			//if lattice, create connections for each agent equal to the parameter specified
			if (i != 0 && lattice) {
				while(a.connections() < connections && a.connections() < i) {
					Agent b = null;
					if (list.isEmpty()) b = agents.get(r.nextInt(agents.size()));
					else b = list.get(r.nextInt(list.size()));
					if (a.addConnection(b)) {
						list.add(a);
						list.add(b);
					}
				}
			}
		}
		if (debug) {
			for (Agent a : agents) {
				a.printConnections();
			}
		}
		
		map = listOfWords(agents);
		//loop until maxsteps are reached or all agents converge
		while(steps < maxSteps) {
			if(converged(agents)) {
				converged = true;
				break;
			}
			
			if(steps > 0 && steps % 500 == 0) {
				map = listOfWords(agents);
				if (debug) System.out.println("step " + steps + ": " + mapToString(map));
			}
			
			//pick two random agents
			int test = r.nextInt(0, agents.size());
			int test2 = 0;
			Agent agent = agents.get(test);
			Agent agent2;
			if (lattice) {
				// if lattice pick from random connection
				agent2 = agent.getRandomConnection();
				test2 = agent2.getID() + 1;
			} else {
				//otherwise pick random neighbor
				if (test == 0) test2 = 1;
				else if (test == agents.size() - 1) test2 = agents.size() - 2;
				else {
					if (r.nextBoolean()) test2 = test - 1;
					else test2 = test + 1;
				}
				agent2 = agents.get(test2);
			}
			
			
			if(agent.inventory() == 0) {
				// if speaker has no words, add a word to speaker
				agent.addWord(randomWord(r));
			} else {
				String str = agent.getRandomWord();
				
				if(agent2.contains(str)) {
					//agent contains word
					agent.removeAllExcept(str);
					agent2.removeAllExcept(str);
				} else {
					//agent does not contain word
					agent2.addWord(str);
				}
			}
			steps ++;
		}
		if(converged) {
			System.out.println("All agents converged within " + steps + " steps");
			arr.add(steps);
		}
		else System.out.println("Agents did not converge within the maximum steps");
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
	public static boolean converged(ArrayList<Agent> agents) {
		if(agents.get(0).inventory() == 0) return false;
		String s = agents.get(0).getTopWord();
		for(Agent agent : agents) {
			if(agent.inventory() != 1 || !agent.getTopWord().equals(s)) return false;
		}
		return true;
	}
	
	/**
	 * Gets a HashMap of all words present among all agents, 
	 * where the key is the word and the value is the amount of times it appears.
	 * @param agents the list of agents used in a trial
	 * @return a HashMap of String/Integer
	 */
	public static HashMap<String, Integer> listOfWords(ArrayList<Agent> agents){
		HashMap<String, Integer> list = new HashMap<String, Integer>();
		for(Agent a: agents) {
			for(String s: a.getAllWords()) {
				if(!list.containsKey(s)) list.put(s, 1);
				else list.replace(s, list.get(s) + 1);
			}
		}
		return list;
	}
	
	/**
	 * Formats the HashMap into a readable String. For debug only.
	 * @param map the HashMap of String/Integer
	 * @return a String representation of the map
	 */
	public static String mapToString(HashMap<String, Integer> map) {
		String result = "";
		for (String s : map.keySet()) {
			result += String.format("%s: %d, ", s, map.get(s));
		}
		return result.substring(0, result.length() - 2);
	}
}
