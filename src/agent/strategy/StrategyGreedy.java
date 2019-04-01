package agent.strategy;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;
/**
 * Strategie qui renvoit un choix aleatoire avec proba epsilon, un choix glouton (suit la politique de l'agent) sinon
 * @author lmatignon
 *
 */
public class StrategyGreedy extends StrategyExploration{
	/**
	 * parametre pour probabilite d'exploration
	 */
	protected double epsilon;
	private Random rand = new Random();
	
	
	
	public StrategyGreedy(RLAgent agent,double epsilon) {
		super(agent);
		this.epsilon = epsilon;
	}

	@Override
	public Action getAction(Etat _e) {//renvoi null si _e absorbant
		double d = rand.nextDouble();
		List<Action> actionsLegales = this.agent.getActionsLegales(_e);
		if (actionsLegales.isEmpty()){
			return null;
		}

		int randomIndexAction;
		List<Action> actionsPolitique = this.agent.getPolitique(_e);

		if(d <= this.epsilon) {
			// Si on est dans les 'epsilon %' de chances, on renvoie une action aléatoire
			randomIndexAction = this.rand.nextInt(actionsLegales.size());
			return actionsLegales.get(randomIndexAction);
		} else {
			// Sinon, on applique une politique greedy
			// Et on prend donc une action aléatoire de la politique (qui fait partie des meilleures actions possibles)
			if(actionsPolitique.size() == 1) {
				return actionsPolitique.get(0);
			} else {
				randomIndexAction = this.rand.nextInt(actionsPolitique.size());
				return actionsPolitique.get(randomIndexAction);
			}
		}
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
		System.out.println("epsilon:"+epsilon);
	}

/*	@Override
	public void setAction(Action _a) {
		// TODO Auto-generated method stub
		
	}*/

}
