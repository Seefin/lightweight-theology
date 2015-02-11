#!/bin/bash
#Script to convert any input file to a java-readable WAV format.
if [[ ! $# -eq 1 ]]; then
    echo "Usage $0 <FILE>"
else
    FILE=$1
    EXTENSION="${FILE##*.}"
    FILENAME="${FILE%.*}"
    BASENAME=$(basename "$FILENAME")
    #echo "FILE: $FILE"
    #echo "EXTENSION: $EXTENSION"
    #echo "FILENAME: $FILENAME"
    #echo "BASENAME: $BASENAME"
    ffmpeg -i "$1" -c:a pcm_s16le "$BASENAME.wav"
fi
