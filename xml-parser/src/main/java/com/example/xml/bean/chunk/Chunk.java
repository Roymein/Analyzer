package com.example.xml.bean.chunk;

public abstract class Chunk {
    int chunkType;
    int chunkSize;
    int lineNumber;

    Chunk(int chunkType){
        this.chunkType=chunkType;
    }

    public abstract String toXmlString();
}
