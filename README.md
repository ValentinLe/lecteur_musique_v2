# lecteur_musique_v2

Logiciel permettant d'écouter des musiques et d'avoir un contrôle facile sur l'ordre de lecture de celles-ci. L'utilisateur peut choisir un dossier dans lequel sont placés ces musiques, elles se retrouverons dans la liste d'attente du logiciel (à droite). Il pourra déplacer les prochaines musiques qu'il souhaite entendre dans la liste d'attente prioritaire (au centre), pour cela il fera un double-clic sur la musique (dans la liste d'attente ou dans la liste de recherche).

- Demo
<div align="center">
<img src="https://github.com/ValentinLe/lecteur_musique_v2/blob/master/screenshots/demo.gif" alt="demo" width="716" height="374">
</div>

- Fenêtre des paramètres
<div align="center">
<img src="https://github.com/ValentinLe/lecteur_musique_v2/blob/master/screenshots/parametres.PNG" alt="parametres">
</div>

## Contrôles
- Double clic sur une musique pour la faire changer de liste (double clic sur une musique de la liste d'attente pour la mettre à la suite de la liste d'attente prioritaire et inversement).
- flèche de gauche pour mettre le focus sur la liste d'attente prioritaire et flèche de droite pour la liste d'attente normale.
- Ctrl + Haut/Bas pour changer la position dans la liste de la musique séléctionnée.
- Ctrl + Gauche/Droite pour déplacer la musique de liste d'attente.
- Delete pour effacer le texte de la recherche.
- Entrer pour changer la musique sélectionnée de liste.

## Dépendences

- <a href="https://github.com/mpatric/mp3agic">mp3agic</a> afin de récupérer les metadata des fichiers MP3
