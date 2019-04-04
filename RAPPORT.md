**Nom/Prénom Etudiant 1 :**

**Nom/Prénom Etudiant 2 :**

# Rapport TP1

## Question 5.1 Brigde Grid
*Donnez les valeurs des paramètres et la justification de ces choix*  
*  gamma = 0.9 (gamma ne doit pas etre inférieure à 0,6)
*  bruit = 0  

Pour que l'agent traverse le pont, il faut qu'il n'y ait aucun bruit. 
En effet, lorsqu'il y a du bruit, l'agent préfère mettre toutes les chances 
de son coté en allant chercher la récompense la plus proche. Ainsi, lorsqu'il 
n'y a pas de bruit, il va chercher la plus grosse récompense car il ne risque 
pas de tomber du pont.  
En ce qui concerne gamma, si la valeur est trop faible, l'atténuationmul du cumul 
des récompenses va ˆetre trop forte, rendant ainsi la récompense proche plus intéressante 
que celle disponible de l'autre cˆoté du pont.

## Question 5.2 Discount Grid

1. Changement de récompense = -2.0, ainsi l'agent reçoit un malus pour chaque déplacement, il va donc avoir tendance à "se jeter" sur la récompense la plus proche, bien qu'elle soit plus faible que le +10.  

2. Changement de récompense (=-1.0). Ainsi, l'agent reçois un malus pour chaque déplacement, ce qui favorise le chemin court, bien qu'il soit plus risqué.  

3. Changement du facteur d'atténuation: $\gamma$ = 0.2. L'agent accorde plus d'importance aux récompenses proches qu'au récompenses futures et va donc préférer l'état absorbant de récompense +1.  

4. Pour que l'agent évite les états absorbants, il suffit de mettre une récompense >10 sur les autres cases, ainsi, l'agent préférera rester loin des états absorbants pour continuer à recevoir de grosses récompenses.  


# Rapport TP2

## Question 1:
*Précisez et justifiez les éléments que vous avez utilisés pour la définition d’un état du MDP pour le jeu du Pacman (partie 2.2)*


## Question 2:
*Précisez et justifiez les fonctions caractéristiques que vous avez choisies pour la classe FeatureFunctionPacman (partie 2.3).*
