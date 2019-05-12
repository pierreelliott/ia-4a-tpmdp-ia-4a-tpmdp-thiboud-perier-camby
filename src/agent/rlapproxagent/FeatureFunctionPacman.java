package agent.rlapproxagent;

import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.environnementRL.EnvironnementPacmanMDPClassic;
import environnement.Action;
import environnement.Etat;
/**
 * Vecteur de fonctions caracteristiques pour jeu de pacman: 4 fonctions phi_i(s,a)
 *
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionPacman implements FeatureFunction{
	private double[] vfeatures ;

	private static int NB_FEATURES = 4;

	private static int NBACTIONS = 4;//5 avec NONE possible pour pacman, 4 sinon
	//--> doit etre coherent avec EnvironnementPacmanRL::getActionsPossibles


	public FeatureFunctionPacman() {

	}

	@Override
	public int getFeatureNb() {
		return NB_FEATURES;
	}

	@Override
	public double[] getFeatures(Etat e, Action a) {
		vfeatures = new double[NB_FEATURES];
		StateGamePacman stategamepacman ;
		//EnvironnementPacmanMDPClassic envipacmanmdp = (EnvironnementPacmanMDPClassic) e;

		//calcule pacman resulting position a partir de Etat e
		if (e instanceof StateGamePacman){
			stategamepacman = (StateGamePacman)e;
		}
		else{
			System.out.println("erreur dans FeatureFunctionPacman::getFeatures n'est pas un StateGamePacman");
			return vfeatures;
		}

		StateAgentPacman nextState= stategamepacman.movePacmanSimu(0, new ActionPacman(a.ordinal()));

		int nextX = nextState.getX();
		int nextY = nextState.getY();

		// Biais
		vfeatures[0] = 1;

		// Nombre de fantome à proximité de la prochaine position
		if (stategamepacman.isGhost(nextX + 1, nextY)){
			vfeatures[1] += 1;
		}
		if (stategamepacman.isGhost(nextX - 1, nextY)){
			vfeatures[1] += 1;
		}
		if (stategamepacman.isGhost(nextX, nextY + 1)){
			vfeatures[1] += 1;
		}
		if (stategamepacman.isGhost(nextX, nextY - 1)){
			vfeatures[1] += 1;
		}
		vfeatures[1] /= 4;

		// Prochaine position contient un dot
		vfeatures[2] = stategamepacman.getMaze().isFood(nextX, nextY) ? 1 : 0;

		// Distance du dot le plus proche
		vfeatures[3] = stategamepacman.getClosestDot(nextState) / (double) (stategamepacman.getMaze().getSizeX() + stategamepacman.getMaze().getSizeY());



		return vfeatures;
	}

	public void reset() {
		vfeatures = new double[NB_FEATURES];

	}

}
