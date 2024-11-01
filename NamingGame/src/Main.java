import java.util.Random;

public class Main {
	public static void main(String[] args) {
		Random r = new Random();
		int steps = 0;
		int agentNum = 20;
		Agent[] agents = new Agent[agentNum];
		for(int i = 0; i < agents.length; i ++) {
			agents[i] = new Agent(i + 1);
		}
		while(steps < 10000) {
			System.out.print("step " + steps + ": ");
			if(converged(agents)) {
				System.out.println("all agents have converged");
				break;
			}
			
			int test = r.nextInt(0, agents.length);
			int test2;
			if (test == 0) test2 = 1;
			else if (test == agents.length - 1) test2 = agents.length - 2;
			else {
				if (r.nextBoolean()) test2 = test - 1;
				else test2 = test + 1;
			}
			Agent agent = agents[test];
			Agent agent2 = agents[test2];
			
			if(agent.inventory() == 0) {
				System.out.println("Agent " + (test + 1) + " has no words, adding a random word");
				agent.addWord(randomWord(r));
				agent2.addWord(randomWord(r));
			} else {
				String str = agent.getRandomWord();
				System.out.println("Agent " + (test + 1) + " conveys word " + str);
				if(agent2.contains(str)) {
					System.out.println("Agent " + (test2 + 1) + " has that word");
					agent.removeAllExcept(str);
					agent2.removeAllExcept(str);
				} else {
					System.out.println("Agent " + (test2 + 1) + " does not have that word");
					agent2.addWord(str);
				}
			}
			
			System.out.println();
			steps ++;
		}
		
		for(Agent agent : agents) {
			System.out.println(agent);
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
