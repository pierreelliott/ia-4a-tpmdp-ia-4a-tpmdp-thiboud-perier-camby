**Nom/Prénom Etudiant 1 :**

**Nom/Prénom Etudiant 2 :**

# Rapport TP1

## Question 5.1 Brigde Grid
*Donnez les valeurs des paramètres et la justification de ces choix*  
*  &gamma; = 0.9 (ne doit pas etre inférieur à 0,6)
*  bruit = 0  

Pour que l'agent traverse le pont, il faut qu'il n'y ait aucun bruit. 
En effet, lorsqu'il y a du bruit, l'agent préfère mettre toutes les chances 
de son coté en allant chercher la récompense la plus proche. Ainsi, lorsqu'il 
n'y a pas de bruit, il va chercher la plus grosse récompense car il ne risque 
pas de tomber du pont.  
En ce qui concerne &gamma;, si la valeur est trop faible, l'atténuation du cumul 
des récompenses va être trop forte, rendant ainsi la récompense proche plus intéressante 
que celle disponible de l'autre côté du pont.

## Question 5.2 Discount Grid

1. Changement de récompense = -2.0, ainsi l'agent reçoit un malus pour chaque déplacement, il va donc avoir tendance à "se jeter" sur la récompense la plus proche, bien qu'elle soit plus faible que le +10.  

2. Changement de récompense (=-1.0). Ainsi, l'agent reçois un malus pour chaque déplacement, ce qui favorise le chemin court, bien qu'il soit plus risqué.  

3. Changement du facteur d'atténuation: &gamma; = 0.2. L'agent accorde plus d'importance aux récompenses proches qu'au récompenses futures et va donc préférer l'état absorbant de récompense +1.  

4. Pour que l'agent évite les états absorbants, il suffit de mettre une récompense >10 sur les autres cases, ainsi, l'agent préférera rester loin des états absorbants pour continuer à recevoir de grosses récompenses.  


# Rapport TP2

## Question 1:
*Précisez et justifiez les éléments que vous avez utilisés pour la définition d’un état du MDP pour le jeu du Pacman (partie 2.2)*

Pour définir un état du MDP, il faut inclure le plus d'informations utiles possible, tout en limitant le nombre d'états différents.
Pour cela, nous avons utilisé un champs de vision au delà duquel l'agent ne pourra pas voir (SCOPE). Ainsi, nous réduisons grandement le nombre d'états 
tout en conservant uniquement des infos utiles : ce qui se passe proche du pacman.
Nous avons testé les features suivantes :
 - __La position du pacman (X,Y):__ utile sur les petites maps car l'agent peut apprendre des chemins à prendre pour le mener aux dots, néanmoins, 
 cette information comporte de nombreuses combinaisons possibles et fait donc très vite monter le nombre d'états possibles, ce qui rend l'apprentissage difficile, 
 particulièrement sur les grilles importantes.
 
 - __La distance et direction du fantome le plus proche (s'il est dans le champs de vision):__ indispensable pour pouvoir eviter les fantomes. Le fait de réduire 
 la position du fantome à une distance (*taille_du_scope*+1 possibilitées) et une direction (4 possibilités) permet de résuire le nombre d'états possibles.
 
 - __la direction du dot le plus proche__ (XXXXXXXXXXXXXXXXXXdans le champs de vision (sinon il va essayer de traverser tout alors qu'il peut peut-etre pas))
 
 - __les directions sur lesquels le pacman peut se déplacer (celles où il n'y a pas de mur):__ permettent au pacman de pouvoir différencer un état où il peut atteindre un dot
 devant lui et un état ou il ne peut pas car il y a un mur entre lui et le dot.

Après divers tests, nous avons sélectionné les features et paramètres suivants :

 - SCOPE_SIZE = 4
 - Distance et direction du fantome le plus proche dans le scope
 - Direction du dot le plus proche
 - Les mouvement possibles
 
Voici les résultats obtenus (avec nbmeans=5, nbepisodelearn = 1000, nbepisodegreedy = 300):

| smallGrid | smallGrid2 | mediumGrid |
|---|---|---|
|70%|82%|58%|

En observant les parties, nous avons remarqué que le pacman avait tendance à parfois rester bloqué
entre deux position sans vouloir en sortir, à moins qu'un fantome ne s'approche, une piste d'amélioration
possible serait d'inciter le pacman à ne pas revenir sur ces pas.

Nous avons également remarqué que le pacman avait tendance à éviter les fantomes à tout prix, et faire
de gros détours pour atteindre les dots, ce qui menait à des courbes d'apprentissages négatives, bien que 
le taux de vitoires soit élévé.

__NB:__ Nous avons présenté ici un paramètrage équilibré (qui est bon sur les 3 grilles)
nous avons pu concevoir des pacmans "spéialistes" avec par exemple un taux de réussite de 100% sur smallGrid, 
mais 0% sur mediumGrid (en utilisant les coordonnées X,Y du pacman).

## Question 2:
*Précisez et justifiez les fonctions caractéristiques que vous avez choisies pour la classe FeatureFunctionPacman (partie 2.3).*
