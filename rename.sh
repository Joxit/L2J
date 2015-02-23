#!/bin/sh
if [[ -n $2 ]]
then
	echo "Usage : $0 [Name] [ID]"
	echo "Will rename all files [Name]_x.htm by [ID]-x.htm"
	exit 0
fi
ID=$2
NAME=$1

for file in $NAME* 
do
        new=$ID-`echo $file | grep -o [0-9a-zA-Z]*\.htm.*`
        mv -v $file $new
done
