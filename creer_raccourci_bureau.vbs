Set WshShell = CreateObject("Wscript.shell")
strDesktop = WshShell.SpecialFolders("Desktop")
scriptDir = Replace(WScript.ScriptFullName, WScript.ScriptName, "")
Set oMyShortcut = WshShell.CreateShortcut(strDesktop + "\Slatify.lnk")
oMyShortcut.IconLocation = scriptDir + "\resources\images\logo.ico"
oMyShortcut.TargetPath = scriptDir + "\lancer_logiciel.vbs"
oMyShortCut.Hotkey = "ALT+CTRL+F"
oMyShortCut.Save
