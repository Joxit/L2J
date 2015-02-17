#!/bin/sh

ID=31149
NAME=alchemical_mixing_jar_

for file in $NAME* 
do
        new=$ID-`echo $file | grep -o [0-9a-zA-Z]*\.htm.*`
        mv -v $file $new
done
