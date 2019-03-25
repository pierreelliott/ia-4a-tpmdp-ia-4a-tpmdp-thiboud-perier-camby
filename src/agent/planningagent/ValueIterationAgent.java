package agent.planningagent;

import java.util.*;

import util.HashMapUtil;

import java.util.stream.Collectors;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;
	
	/**
	 * 
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		
		this.V = new HashMap<Etat,Double>();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
	}
	
	
	
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);

	}
	
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise la nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		//delta est utilise pour detecter la convergence de l'algorithme
		//Dans la classe mere, lorsque l'on planifie jusqu'a convergence, on arrete les iterations        
		//lorsque delta < epsilon 
		//Dans cette classe, il  faut juste mettre a jour delta 
		this.delta=0.0;
		HashMap<Etat, Double> newV = this.V.keySet().stream().collect(Collectors.toMap(etat -> etat, etat -> new Double(this.V.get(etat)), (a, b) -> b, HashMap::new));
		//*** VOTRE CODE
		List<Double> diffOldAndNew = new ArrayList<>();

		double valueEtat, valueEtatSuivant, valueNewEtat;
		List<Double> valuesEtatSuivant;
		Map<Etat, Double> etatsTransition;
		for (Etat etat : this.getMdp().getEtatsAccessibles()) {
			valueEtat = this.V.get(etat);
			if (this.getMdp().estAbsorbant(etat)) {
				continue;
			}

			valuesEtatSuivant = new ArrayList<>();
			for (Action action : this.getMdp().getActionsPossibles(etat)) {
				valueEtatSuivant = 0.0;
				try {
					etatsTransition = this.getMdp().getEtatTransitionProba(etat, action);
					for (Etat etatTransition : etatsTransition.keySet()) {
						valueEtatSuivant += (this.gamma * this.V.get(etatTransition)
									+ this.getMdp().getRecompense(etat, action, etatTransition))
								* etatsTransition.get(etatTransition);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				valuesEtatSuivant.add(valueEtatSuivant);
			}

			valueNewEtat = Collections.max(valuesEtatSuivant);
			newV.put(etat, valueNewEtat);
			diffOldAndNew.add(Math.abs(valueEtat - valueNewEtat));
		}

		this.delta = Collections.max(diffOldAndNew);

		this.V = newV;

		//mise a jour de vmax et vmin utilise pour affichage du gradient de couleur:
		//vmax est la valeur max de V pour tout s
		Set<Etat> keyset = this.V.keySet();
		this.vmax = Collections.max(this.V.values());
		this.vmin = Collections.min(this.V.values());
		//vmin est la valeur min de V pour tout s
		// ...
		
		//******************* laisser cette notification a la fin de la methode	
		this.notifyObs();
	}
	
	
	/**
	 * renvoi l'action executee par l'agent dans l'etat e 
	 * Si aucune actions possibles, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
		//*** VOTRE CODE
		Action action;
		List<Action> actions = this.mdp.getActionsPossibles(e);

		if(actions.isEmpty()) {
			action = Action2D.NONE;
		} else {
			int r = rand.nextInt(actions.size());//random entre 0 inclu et param exlu
			action = actions.get(r);
		}

		return action;
		
	}


	@Override
	public double getValeur(Etat _e) {
                 //Renvoie la valeur de l'Etat _e, c'est juste un getter, ne calcule pas la valeur ici
                 //(la valeur est calculee dans updateV
		//*** VOTRE CODE
		double value = this.V.get(_e);
		
		return value;
	}
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans etat 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		//*** VOTRE CODE
		List<Action> actionsPossibles = this.mdp.getActionsPossibles(_e);

		
		// retourne action de meilleure valeur dans _e selon V, 
		// retourne liste vide si aucune action legale (etat absorbant)
		List<Action> returnactions = new ArrayList<Action>();
	
		return returnactions;
		
	}
	
	@Override
	public void reset() {
		super.reset();
                //reinitialise les valeurs de V 
		//*** VOTRE CODE
		for (Etat etat : this.V.keySet()) {
			this.V.put(etat, 0.);
		}
		
		this.notifyObs();
	}

	

	

	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}
	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}


	
	

	
}