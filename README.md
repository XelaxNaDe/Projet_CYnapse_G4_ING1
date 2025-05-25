# CYnapse

Un outil de génération et de résolution de labyrinthes en Java.

CYnapse est une application Java qui permet de générer, modifier et résoudre des labyrinthes à l'aide d'algorithmes classiques comme DFS et A*.  
Ce projet a été développé dans le cadre du module Projet ING1 à CY Tech.


## Fonctionnalités

- Génération de labyrinthe avec différents algorithmes
- Résolution automatique avec visualisation du chemin
- Modification manuelle du labyrinthe (ajout de murs, entrée, sortie)
- Sauvgarde et restauration d'un labyrinthe


## Technologies

- Java 22
- JavaFX 24.0.1
- Maven
- MVC (Modèle-Vue-Contrôleur)


## Structure

- `src/main/java/projatlab/`: Code source principal
- `controller/` : Contrôleurs de l’interface
- `algorithms/` : Algorithmes de génération et de résolution
- `view/` : Interface graphique
- `model/`: Modèle d'un labyrinthe et de ces cellules


## Contributeurs

- Arthur JIN
- Yusuf RUMELI
- Lohan BRIARD
- Sarusan NITHIYARAJAN
- Omar LARABI


##  Installation

### Prérequis

- [Java JDK 22](https://jdk.java.net/22/)
- [Maven](https://maven.apache.org/)


### Cloner le projet

```bash
git clone https://github.com/XelaxNaDe/Projet_CYnapse_G4_ING1.git
cd Projet_CYnapse_G4_ING1/CYnapse/cynapse

### Lancer l'application

mvn clean javafx:run




