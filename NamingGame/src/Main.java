import java.util.Random;

public class Main {
	public static void main(String[] args) {
		int trials = 1;
		int agentNum = 100;
		int maxSteps = 30000;
		for (int i = 0; i < trials; i ++) {
			System.out.print("trial " + (i + 1) + ": ");
			trial(agentNum, maxSteps, true, true);
		}
	}

	public static void trial(int agentNum, int maxSteps, boolean lattice, boolean debug) {
		Random r = new Random();
		int steps = 0;
		boolean converged = false;
		Agent[] agents = new Agent[agentNum];
		
		//create list of agents
		for(int i = 0; i < agents.length; i ++) {
			agents[i] = new Agent(i + 1);
		}
		
		//if lattice, create 4 connections for each agent
		if(lattice) {
			for(Agent a : agents) {
				while(a.connections() < 4) {
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
				if (debug) System.out.println("Agent " + (test + 1) + " has no words, adding a random word");
				agent.addWord(randomWord(r));
				agent2.addWord(randomWord(r));
			} else {
				String str = agent.getRandomWord();
				if (debug) System.out.println("Agent " + (test + 1) + " conveys word " + str);
				if(agent2.contains(str)) {
					if (debug) System.out.println("Agent " + (test2 + 1) + " has that word");
					agent.removeAllExcept(str);
					agent2.removeAllExcept(str);
				} else {
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
		}
	}
	
	public static String randomWord(Random r) {
		int length = r.nextInt(1, 11);
		String result = "";
		for(int i = 0; i < length; i ++) {
			result += (char) (64 + r.nextInt(1, 26));
		}
		return result;
	}
	
	public static boolean converged(Agent[] agents) {
		if(agents[0].inventory() == 0) return false;
		String s = agents[0].getTopWord();
		for(Agent agent : agents) {
			if(agent.inventory() != 1 || !agent.getTopWord().equals(s)) return false;
		}
		return true;
	}
}
