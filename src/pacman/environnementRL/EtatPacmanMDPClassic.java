package pacman.environnementRL;

import java.util.Objects;

import pacman.elements.MazePacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */


public class EtatPacmanMDPClassic implements Etat , Cloneable{

	private int pacmanX;
	private int pacmanY;
	private int closestGhostDistance; // en nombre de cases
	private int closestGhostDirection;
	private int closestDotDistance;
	private int closestDotDirection;
	private int possibleDirections;
	private int dimensions; // le nombre d'états possibles

	// Type de configuration que l'on utilise (1, 2 ou 3)
	private static final int CONFIG = 3;

	// L'agent peut voir à une distance (en cases) de :
	public final int SCOPE_SIZE = 4;


	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){

		if (_stategamepacman.getNumberOfPacmans()!=1){
			System.err.println("Mauvais nombre de pacman : " + _stategamepacman.getNumberOfPacmans());
		}

		// Position du pacman

		StateAgentPacman pacState = _stategamepacman.getPacmanState(0);
		pacmanX = pacState.getX();
		pacmanY = pacState.getY();

		// Distances et directions du fantome le plus proche

		closestGhostDistance = SCOPE_SIZE + 1;
		closestGhostDirection = 0;

		for (int i=0; i <_stategamepacman.getNumberOfGhosts(); i++){
			StateAgentPacman ghostState = _stategamepacman.getGhostState(i);
			int distance = calculDistance(pacState, ghostState);

			if (distance < closestGhostDistance) {
				closestGhostDistance = distance;
				closestGhostDirection = calculDirection(pacmanX, pacmanY, ghostState.getX(), ghostState.getY());
			}
		}


		// Dot le plus proche dans le scope

		closestDotDistance = _stategamepacman.getMaze().getSizeX() + _stategamepacman.getMaze().getSizeX(); //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX test avec distance max

		for (int x = 0; x < _stategamepacman.getMaze().getSizeX(); x++) {
			for (int y = 0; y < _stategamepacman.getMaze().getSizeY(); y++) {
				if(_stategamepacman.getMaze().isFood(x, y) && calculDistance(pacState, x, y) < closestDotDistance){
					closestDotDistance = calculDistance(pacState, x, y);
					closestDotDirection = calculDirection(pacmanX, pacmanY, x, y);
				}
			}
		}


		// Directions de déplacement possibles (le hashage d'un tableau de boolean étant plus gourmant en temps de calcul, nous représentons ces 4 "booléens" comme un entier)
		if (CONFIG == 1) {
			possibleDirections = 0;
			if (pacmanX - 1 > 0 && !_stategamepacman.getMaze().isWall(pacmanX - 1, pacmanY)) {
				possibleDirections += 1;
			}
			if (pacmanY - 1 > 0 && !_stategamepacman.getMaze().isWall(pacmanX, pacmanY - 1)) {
				possibleDirections += 10;
			}
			if (pacmanY + 1 < _stategamepacman.getMaze().getSizeY() && !_stategamepacman.getMaze().isWall(pacmanX, pacmanY + 1)) {
				possibleDirections += 100;
			}
			if (pacmanX + 1 < _stategamepacman.getMaze().getSizeX() && !_stategamepacman.getMaze().isWall(pacmanX + 1, pacmanY)) {
				possibleDirections += 1000;
			}
		}

		// dimensions
		if (CONFIG == 1){
			dimensions = (SCOPE_SIZE+1)*4*4*_stategamepacman.getMaze().getSizeX()*_stategamepacman.getMaze().getSizeY();
		}
		else if (CONFIG == 2){
			dimensions = (SCOPE_SIZE+1)*4*4*16;
		}
		else {
			dimensions = (SCOPE_SIZE+1)*4*4;
		}

	}


	private int calculDistance(StateAgentPacman agent1, StateAgentPacman agent2){
		return Math.abs(agent1.getX() - agent2.getX()) + Math.abs(agent1.getY() - agent2.getY());
	}

	private int calculDistance(StateAgentPacman agent1, int x, int y){
		return Math.abs(agent1.getX() - x) + Math.abs(agent1.getY() - y);
	}
	@Override
	public String toString() {

		return "";
	}

	public int calculDirection(int x1, int y1, int x2, int y2) {
		int diffX = x1 - x2;
		int diffY = y1 - y2;
		int direction;

		if (diffX + diffY <= 0 && diffX < diffY) {
			direction = MazePacman.NORTH;
		} else if (diffX + diffY > 0 && diffX <= diffY) {
			direction = MazePacman.EAST;
		} else if (diffX + diffY >= 0 && diffX > diffY) {
			direction = MazePacman.SOUTH;
		} else {
			direction = MazePacman.WEST;
		}

		return direction;
	}

	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}



		// on renvoie le clone
		return clone;
	}

	@Override
	public int hashCode(){
		if (CONFIG == 1){
			return Objects.hash(pacmanX, pacmanY, closestGhostDirection, closestGhostDistance, closestDotDirection);//, possibleDirections);
		}
		else if (CONFIG == 2){
			return Objects.hash(closestGhostDirection, closestGhostDistance, closestDotDirection, possibleDirections);
		}
		else {
			return Objects.hash(closestGhostDirection, closestGhostDistance, closestDotDirection);
		}

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		return this.hashCode() == obj.hashCode();
	}

	public int getDimensions(){
		return dimensions;
	}
}
