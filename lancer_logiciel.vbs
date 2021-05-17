Set WshShell = CreateObject("WScript.Shell")
scriptDir = Replace(WScript.ScriptFullName, WScript.ScriptName, "")
WshShell.CurrentDirectory = scriptDir
dim a
a = "libs/lecteur_musique-1.0.jar"
WshShell.Run "javaw -jar " &  chr(34) & scriptDir & a & chr(34)
