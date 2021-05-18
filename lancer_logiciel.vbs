Set WshShell = CreateObject("WScript.Shell")
scriptDir = Replace(WScript.ScriptFullName, WScript.ScriptName, "")
WshShell.CurrentDirectory = scriptDir
WshShell.Run "javaw --module-path libs/ --add-modules javafx.controls,javafx.base,javafx.graphics,javafx.media,javafx.fxml -jar libs/lecteur_musique-1.0.jar"
