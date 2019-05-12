package agent.rlagent;

import java.util.*;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Renvoi 0 pour valeurs initiales de Q
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {
	/**
	 *  format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
	 */
	protected HashMap<Etat,HashMap<Action,Double>> qvaleurs;
	
	//AU CHOIX: vous pouvez utiliser une Map avec des Pair pour clés si vous préférez
	//protected HashMap<Pair<Etat,Action>,Double> qvaleurs;

	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param _env
	 */
	public QLearningAgent(double alpha, double gamma,
			Environnement _env) {
		super(alpha, gamma,_env);
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();
		
		
	
	}


	
	
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  (plusieurs actions sont renvoyees si valeurs identiques)
	 *  renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)

	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		// retourne action de meilleures valeurs dans e selon Q : utiliser getQValeur()
		// retourne liste vide si aucune action legale (etat terminal)
		List<Action> returnactions = new ArrayList<Action>();
		List<Action> actionsLegales = this.getActionsLegales(e);
		if (actionsLegales.size() == 0){//etat  absorbant; impossible de le verifier via environnement
			System.out.println("aucune action legale");
			return new ArrayList<Action>();
			
		}

		double max = Double.NEGATIVE_INFINITY, qvaleur;
		for (Action action : actionsLegales) {
			qvaleur = getQValeur(e, action);
			max = (qvaleur > max) ? qvaleur : max;
		}
		
		for (Action action : actionsLegales) {
			if(getQValeur(e, action) == max) {
				returnactions.add(action);
			}
		}

		return returnactions;
		
		
	}
	
	@Override
	public double getValeur(Etat e) {
		double max = Double.NEGATIVE_INFINITY;
		double qValeur;

		if(qvaleurs.get(e) == null) {
			return 0.;
		}
		for (Action a : qvaleurs.get(e).keySet()){
			qValeur = this.getQValeur(e, a);
			if (qValeur > max){
				max = qValeur;
			}
		}
		return max;
		
	}

	@Override
	public double getQValeur(Etat e, Action a) {
		HashMap<Action, Double> mapEtat = this.qvaleurs.computeIfAbsent(e, k -> new HashMap<>());

		return mapEtat.computeIfAbsent(a, k -> 0.);
	}
	
	
	
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		//*** VOTRE CODE

		HashMap<Action, Double> mapEtat = this.qvaleurs.computeIfAbsent(e, k -> new HashMap<>());

		mapEtat.put(a, d);
		
		// mise a jour vmax et vmin pour affichage du gradient de couleur:
				//vmax est la valeur de max pour tout s de V
				//vmin est la valeur de min pour tout s de V
				// ...
		this.vmax = this.qvaleurs.keySet().stream()
				.mapToDouble(etat -> Collections.max(this.qvaleurs.get(etat).values()))
				.max().orElse(0.);
		this.vmin = this.qvaleurs.keySet().stream()
				.mapToDouble(etat -> Collections.min(this.qvaleurs.get(etat).values()))
				.min().orElse(0.);
		
		this.notifyObs();
		
	}
	
	
	/**
	 * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL)
			System.out.println("QL mise a jour etat "+e+" action "+a+" etat' "+esuivant+ " r "+reward);

		//*** VOTRE CODE
		// (1−αk)Q(s,a)+αk[r+γ max_b( Q(s',b) )]
		
		double oldValue = this.getQValeur(e, a);
		double max_b = this.getValeur(esuivant);
		double newValue = (1 - this.alpha) * oldValue
				+ this.alpha * (reward + this.gamma * max_b);

		this.setQValeur(e, a, newValue);
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	@Override
	public void reset() {
		super.reset();
		//*** VOTRE CODE
		this.qvaleurs = new HashMap<>();
		
		this.episodeNb =0;
		this.notifyObs();
	}









	


}
