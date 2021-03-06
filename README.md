# lecteur_musique_v2

Logiciel permettant d'écouter des musiques et d'avoir un contrôle facile sur l'ordre de lecture de celles-ci. L'utilisateur peut choisir un dossier dans lequel sont placés ces musiques, elles se retrouverons dans la liste d'attente du logiciel (à droite). Il pourra déplacer les prochaines musiques qu'il souhaite entendre dans la liste d'attente prioritaire (au centre), pour cela il fera un double-clic sur la musique (dans la liste d'attente ou dans la liste de recherche).

- Demo
<div align="center">
<img src="https://github.com/ValentinLe/lecteur_musique_v2/blob/master/screenshots/demo.gif" alt="demo" width="716" height="402">
</div>

- Fenêtre des paramètres
<div align="center">
<img src="https://github.com/ValentinLe/lecteur_musique_v2/blob/master/screenshots/parametres.PNG" alt="parametres" width="500">
</div>

## Contrôles
- Drag and drop entre la liste d'attente prioritaire et secondaire, on peut déplacer une musique au sein d'une liste d'attente ou bien la faire changer de liste à la position souhaitée.
- Double clic sur une musique pour la faire changer de liste (double clic sur une musique de la liste d'attente pour la mettre à la suite de la liste d'attente prioritaire et inversement).
- Ctrl + Haut/Bas pour changer la position dans la liste de la musique séléctionnée.
- Ctrl + Gauche/Droite pour déplacer la musique de liste d'attente.
- Echap pour effacer le texte de la recherche.

## Dépendences
- <a href="https://github.com/mpatric/mp3agic">mp3agic</a> afin de récupérer les metadata des fichiers MP3

## Informations
- Permet de lire uniquement les fichiers MP3. 
- Projet fait avec l'environnement Maven.
