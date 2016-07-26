#!/bin/bash

#############################################################################
# File: compile.sh
# Author: Ashish Tibrewal
# Date: 12.10.2015
# Description: Script to compile all the source files and run the simulator
#############################################################################

SRC=src       # Source directory
BIN=bin       # Binary directory
DATA=data     # Data directory
ENTRY=Run     # Program entry point
MEM=-Xmx2048m # Flag to specify extra memory
DUMP=-XX:+HeapDumpOnOutOfMemoryError # Flag to dump heap
echo "Current working directory: `pwd`"
echo "Checking if $DATA directory exists..."
if [ -d "$DATA" ]; then
  echo "$DATA directory exists."
else
  echo "$DATA directory doesn't exist. Creating $DATA..."
  mkdir $DATA
fi
echo "Checking if NHS data files are present..."
if [ -e "$1" ]; then
  echo "$1 exists."
else
  echo "$1 doesn't exist. Downloading $1..."
  (cd $DATA; curl -O http://datagov.ic.nhs.uk/T201202ADD%20REXT.CSV)
fi
if [ -e "$2" ]; then
  echo "$2 exists."
else
  echo "$2 doesn't exist. Downloading $2..."
  (cd $DATA; curl -O http://datagov.ic.nhs.uk/T201109PDP%20IEXT.CSV)
fi
echo "Checking if $BIN directory exists..."
if [ -d "$BIN" ]; then
  echo "$BIN directory exists."
else
  echo "$BIN directory doesn't exist. Creating $BIN..."
  mkdir $BIN
fi
echo "Moving to source directory..."
if [ -d "$SRC" ]; then
  cd $SRC 
  echo "Compiling source files..."
  javac -Xlint:unchecked -d ../$BIN/ *.java   # Could use javac -d ../bin/ Run.java (Since this is the main entry point to the program)
  if [ $? -eq 0 ]           # Check if the previous command retured with exit status 0. If true, only then run the program.
  then
    echo "Compilation successful."
    echo "Moving to $BIN directory..."
    cd ../$BIN
    echo "Checking arguments passed..."
    if [ "$#" -ne 2 ]; then
      echo "Illegal number of arguments. Check arguments and re-run script."
    else
      echo "Initialising JVM to run the program..."
      java $MEM $DUMP Run "$1" "$2"
    fi
  else
    echo "Compilation failed."
  fi
fi
