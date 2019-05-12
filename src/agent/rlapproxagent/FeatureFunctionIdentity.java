package agent.rlapproxagent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import environnement.Action;
import environnement.Action2D;
import environnement.Etat;
import javafx.util.Pair;
/**
 * Vecteur de fonctions caracteristiques phi_i(s,a): autant de fonctions caracteristiques que de paire (s,a),
 * <li> pour chaque paire (s,a), un seul phi_i qui vaut 1  (vecteur avec un seul 1 et des 0 sinon).
 * <li> pas de biais ici 
 * 
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionIdentity implements FeatureFunction {

	private HashMap<Pair<Etat,Action>, Integer> index;
	private int count;
	private int featuresNb;

	
	public FeatureFunctionIdentity(int _nbEtat, int _nbAction){
		index = new HashMap<Pair<Etat, Action>, Integer>();
		count = 0;
		featuresNb = _nbEtat*_nbAction;
	}
	
	@Override
	public int getFeatureNb() {
		return featuresNb;
	}

	@Override
	public double[] getFeatures(Etat e,Action a){
		Pair pair = new Pair<Etat,Action>(e,a);

		// Etat déjà rencontré
		if(index.containsKey(pair)){
			return makeArray(index.get(pair));
		}
		// Nouvel état, on l'ajoute à la HashMap
		else {
			index.put(pair, count);
			count++;
			return makeArray(index.get(pair));
		}
	}

	/*
	Returns a double[] of size featureNb filled with 0 and a 1 at index
	 */
	public double[] makeArray(int index){
		double[] res = new double[featuresNb];
		Arrays.fill(res, 0);
		res[index] = 1.0;
		return res;
	}
	

}
