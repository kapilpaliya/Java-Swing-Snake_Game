@echo off
echo Enter The Name Of The Main Class
set/p "ch=>"
echo Set The Name Of The Jar File
set/p "name=>"

cd classes
echo Main-Class: %ch%>manifest.txt
jar -cfm %name%.jar manifest.txt *
echo Successfully Created

cd..
move classes\%name%.jar %name%.jar
del classes\menifest.txt
pause