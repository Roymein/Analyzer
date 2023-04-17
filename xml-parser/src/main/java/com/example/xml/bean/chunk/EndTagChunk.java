package com.example.xml.bean.chunk;

import com.example.xml.XmlParser;
import com.example.xml.bean.Xml;

public class EndTagChunk extends Chunk {
    private int nameSpaceUri;
    private String name;

    public EndTagChunk(int nameSpaceUri, String name) {
        super(Xml.END_TAG_CHUNK_TYPE);
        this.nameSpaceUri = nameSpaceUri;
        this.name = name;
    }

    public int getNameSpaceUri() {
        return nameSpaceUri;
    }

    public void setNameSpaceUri(int nameSpaceUri) {
        this.nameSpaceUri = nameSpaceUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toXmlString() {
        if (name.equals("manifest"))
            Xml.BLANK.setLength(0);
        else
            Xml.BLANK.setLength(Xml.BLANK.length() - Xml.blank.length());

        return XmlParser.format("\n%s</%s>", Xml.BLANK, name);
    }
}
