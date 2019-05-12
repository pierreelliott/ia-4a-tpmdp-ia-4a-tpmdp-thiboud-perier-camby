package agent.rlapproxagent;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Agent qui apprend avec QLearning en utilisant approximation de la Q-valeur :
 * approximation lineaire de fonctions caracteristiques
 *
 * @author laetitiamatignon
 *
 */
public class QLApproxAgent extends QLearningAgent{

	private FeatureFunction featureFunction;
	private double[] weights;

	public QLApproxAgent(double alpha, double gamma, Environnement _env,FeatureFunction _featurefunction) {
		super(alpha, gamma, _env);

		this.featureFunction = _featurefunction;
		// init weights at 0
		this.weights = new double[featureFunction.getFeatureNb()];
		Arrays.fill(weights, 0.);

	}


	@Override
	public double getQValeur(Etat e, Action a) {
		double[] features = featureFunction.getFeatures(e,a);

		double res = 0.;

		if (!(features.length==weights.length)){
			System.err.println("ERROR : feature vector and weight vector dimensions don't match: " + features.length + " and " + weights.length );
		}
		else {
			for (int i=0; i<weights.length; i++){
				res+= weights[i]*features[i];
			}
		}
		return res;

	}




	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL){
			System.out.println("QL: mise a jour poids pour etat \n"+e+" action "+a+" etat' \n"+esuivant+ " r "+reward);
		}
       //inutile de verifier si e etat absorbant car dans runEpisode et threadepisode
		//arrete episode lq etat courant absorbant

		for (int k = 0; k < weights.length; k++){
			weights[k] += getAlpha() * (reward + getGamma() * getMaxQValeur(esuivant) - getQValeur(e,a)) * featureFunction.getFeatures(e,a)[k];

		}


	}


	private double getMaxQValeur(Etat e) {
		double max = Double.MIN_VALUE;

		for( Action a : env.getActionsPossibles(e)) {
			double QValue = this.getQValeur(e, a);
			if( QValue > max ) {
				max = QValue ;
			}
		}
		return max;
	}


	@Override
	public void reset() {
		super.reset();
		this.qvaleurs.clear();

		Arrays.fill(weights,0);

		this.episodeNb =0;
		this.notifyObs();
	}


}
