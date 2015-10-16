#!/bin/sh
DCMA_HOME=`pwd`

export MAVEN_OPTS="-Djava.library.path=$DCMA_HOME/native -Ddcma.home=$DCMA_HOME"

#--offline
mvn --file $DCMA_HOME/dcma-webapp/pom.xml test --activate-profiles stand-alone

#mvn