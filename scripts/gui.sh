#bin/bash

cd $(dirname $0)/..
java --module-path libs/ --add-modules javafx.controls,javafx.base,javafx.graphics,javafx.media,javafx.fxml -jar libs/lecteur_musique-1.0.jar