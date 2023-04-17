package com.example.xml;

import com.example.xml.bean.Attribute;
import com.example.xml.bean.Xml;
import com.example.xml.bean.chunk.*;
import com.example.xml.utils.BytesReader;
import com.example.xml.utils.TypedValue;
import com.example.xml.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    private final BytesReader reader;

    private final List<String> stringChunkList = new ArrayList<>();
    private final List<Chunk> chunkList = new ArrayList<>();

    public XmlParser(InputStream in) {
        this.reader = new BytesReader(Utils.readAll(in), true);
    }

    public void parse() {
        parseHeader();
        parseStringChunk();
        parseResourceIdChunk();
        parseXmlContentChunk();
        generateXml();
    }

    private void parseHeader() {
        try {
            Xml.nameSpaceMap.clear();
            String magicNumber = reader.readHexString(4);
            System.out.printf("magic number: %s", magicNumber);

            int fileSize = reader.readInt();
            System.out.printf("file size: %d", fileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseStringChunk() {
        try {
            String chunkType = reader.readHexString(4);
            System.out.printf(" chunk type: %s", chunkType);

            int chunkSize = reader.readInt();
            System.out.printf(" chunk size: %d", chunkSize);

            int stringCount = reader.readInt();
            System.out.printf(" string count: %d", stringCount);

            int styleCount = reader.readInt();
            System.out.printf(" style count: %d", styleCount);

            reader.skip(4);  // unknown

            int stringPoolOffset = reader.readInt();
            System.out.printf(" string pool offset: %d", stringPoolOffset);

            int stylePoolOffset = reader.readInt();
            System.out.printf(" style pool offset: %d", stylePoolOffset);

            // 每个 string 的偏移量
            List<Integer> stringPoolOffsets = new ArrayList<>(stringCount);
            for (int i = 0; i < stringCount; i++) {
                stringPoolOffsets.add(reader.readInt());
            }

            // 每个 style 的偏移量
            List<Integer> stylePoolOffsets = new ArrayList<>(styleCount);
            for (int i = 0; i < styleCount; i++) {
                stylePoolOffsets.add(reader.readInt());
            }

            System.out.print(" string pool:");
            for (int i = 1; i <= stringCount; i++) { // 没有读最后一个字符串
                String string;
                if (i == stringCount) {
                    int lastStringLength = reader.readShort() * 2;
                    string = new String(moveBlank(reader.readOrigin(lastStringLength)));
                    reader.skip(2);
                } else {
                    reader.skip(2);
                    byte[] content = reader.readOrigin(stringPoolOffsets.get(i) - stringPoolOffsets.get(i - 1) - 4);
                    reader.skip(2);
                    string = new String(moveBlank(content));

                }
                System.out.printf("   %s", string);
                stringChunkList.add(string);
            }


            System.out.print(" style pool:");
            for (int i = 1; i < styleCount; i++) {
                reader.skip(2);
                byte[] content = reader.readOrigin(stylePoolOffsets.get(i) - stylePoolOffsets.get(i - 1) - 4);
                reader.skip(2);
                String string = new String(content);
                System.out.printf("   %s", string);
            }

            reader.moveTo(chunkSize + 8); // 0000 May have existed before ResourceIdChunk, presumably for alignment

        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("parse StringChunk error!");
        }
    }

    private void parseResourceIdChunk() {
        try {
            String chunkType = reader.readHexString(4);

            System.out.printf(" chunk type: %s", chunkType);

            int chunkSize = reader.readInt();
            System.out.printf(" chunk size: %d", chunkSize);

            int resourcesIdChunkCount = (chunkSize - 8) / 4;
            for (int i = 0; i < resourcesIdChunkCount; i++) {
                String resourcesId = reader.readHexString(4);
                System.out.printf(" resource id[%d]: %s", i, resourcesId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXmlContentChunk() {
        try {
            while (reader.avaliable() > 0) {
                int chunkType = reader.readInt();
                switch (chunkType) {
                    case Xml.START_NAMESPACE_CHUNK_TYPE:
                        parseStartNamespaceChunk();
                        break;
                    case Xml.START_TAG_CHUNK_TYPE:
                        parseStartTagChunk();
                        break;
                    case Xml.END_TAG_CHUNK_TYPE:
                        parseEndTagChunk();
                        break;
                    case Xml.END_NAMESPACE_CHUNK_TYPE:
                        parseEndNamespaceChunk();
                        break;
                    case Xml.TEXT_CHUNK_TYPE:
                        parseTextChunk();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print(" parse XmlContentChunk error!");
        }
    }

    private void parseStartNamespaceChunk() {
        System.out.print("\nparse Start NameSpace Chunk");
        System.out.printf(" chunk type: 0x%x", Xml.START_NAMESPACE_CHUNK_TYPE);

        try {
            int chunkSize = reader.readInt();
            System.out.printf(" chunk size: %d", chunkSize);

            int lineNumber = reader.readInt();
            System.out.printf(" line number: %d", lineNumber);

            reader.skip(4); // 0xffffffff

            int prefix = reader.readInt();
            System.out.printf(" prefix: %s", stringChunkList.get(prefix));

            int uri = reader.readInt();
            System.out.printf(" uri: %s", stringChunkList.get(uri));

            StartNameSpaceChunk startNameSpaceChunk = new StartNameSpaceChunk(chunkSize, lineNumber, prefix, uri);
            chunkList.add(startNameSpaceChunk);

            Xml.nameSpaceMap.put(stringChunkList.get(prefix), stringChunkList.get(uri));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("parse Start NameSpace Chunk error!");
        }
    }

    private void parseStartTagChunk() {
        System.out.print("\nparse Start Tag Chunk");
        System.out.printf(" chunk type: 0x%x", Xml.START_TAG_CHUNK_TYPE);

        try {
            int chunkSize = reader.readInt();
            System.out.printf(" chunk size: %d", chunkSize);

            int lineNumber = reader.readInt();
            System.out.printf(" line number: %d", lineNumber);

            reader.skip(4); // 0xffffffff

            int namespaceUri = reader.readInt();
            if (namespaceUri == -1)
                System.out.print(" namespace uri: null");
            else
                System.out.printf(" namespace uri: %s", stringChunkList.get(namespaceUri));

            int name = reader.readInt();
            System.out.printf(" name: %s", stringChunkList.get(name));

            reader.skip(4); // flag 0x00140014

            int attributeCount = reader.readInt();
            System.out.printf(" attributeCount: %d", attributeCount);

            int classAttribute = reader.readInt();
            System.out.printf(" class attribute: %s", classAttribute);

            List<Attribute> attributes = new ArrayList<>();
            // Each attribute has five attributes of 4 bytes each
            for (int i = 0; i < attributeCount; i++) {

                System.out.printf(" Attribute[%d]", i);

                int namespaceUriAttr = reader.readInt();
                if (namespaceUriAttr == -1)
                    System.out.print("   namespace uri: null");
                else
                    System.out.printf("   namespace uri: %s", stringChunkList.get(namespaceUriAttr));

                int nameAttr = reader.readInt();
                if (nameAttr == -1)
                    System.out.print("   name: null");
                else
                    System.out.printf("   name: %s", stringChunkList.get(nameAttr));

                int valueStr = reader.readInt();
                if (valueStr == -1)
                    System.out.print("   valueStr: null");
                else
                    System.out.printf("   valueStr: %s", stringChunkList.get(valueStr));

                int type = reader.readInt() >> 24;
                System.out.printf("   type: %d", type);

                int data = reader.readInt();
                String dataString = type == TypedValue.TYPE_STRING ? stringChunkList.get(data) : TypedValue.coerceToString(type, data);
                System.out.printf("   data: %s", dataString);

                Attribute attribute = new Attribute(namespaceUriAttr == -1 ? null : stringChunkList.get(namespaceUriAttr),
                        stringChunkList.get(nameAttr), valueStr, type, dataString);
                attributes.add(attribute);
            }
            StartTagChunk startTagChunk = new StartTagChunk(namespaceUri, stringChunkList.get(name), attributes);
            chunkList.add(startTagChunk);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("parse Start NameSpace Chunk error!");
        }
    }

    private void parseEndTagChunk() {
        System.out.println("\nparse End Tag Chunk");
        System.out.printf("chunk type: 0x%x", Xml.END_TAG_CHUNK_TYPE);

        try {
            int chunkSize = reader.readInt();
            System.out.printf("chunk size: %d", chunkSize);

            int lineNumber = reader.readInt();
            System.out.printf("line number: %d", lineNumber);

            reader.skip(4); // 0xffffffff

            int namespaceUri = reader.readInt();
            if (namespaceUri == -1)
                System.out.print("namespace uri: null");
            else
                System.out.printf("namespace uri: %s", stringChunkList.get(namespaceUri));

            int name = reader.readInt();
            System.out.printf("name: %s", stringChunkList.get(name));

            EndTagChunk endTagChunk = new EndTagChunk(namespaceUri, stringChunkList.get(name));
            chunkList.add(endTagChunk);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("parse End Tag Chunk error!");
        }
    }

    private void parseEndNamespaceChunk() {
        System.out.print("\nparse End NameSpace Chunk");
        System.out.printf("chunk type: 0x%x", Xml.END_NAMESPACE_CHUNK_TYPE);

        try {
            int chunkSize = reader.readInt();
            System.out.printf("chunk size: %d", chunkSize);

            int lineNumber = reader.readInt();
            System.out.printf("line number: %d", lineNumber);

            reader.skip(4); // 0xffffffff

            int prefix = reader.readInt();
            System.out.printf("prefix: %s", stringChunkList.get(prefix));

            int uri = reader.readInt();
            System.out.printf("uri: %s", stringChunkList.get(uri));

            EndNameSpaceChunk endNameSpaceChunk = new EndNameSpaceChunk(chunkSize, lineNumber, prefix, uri);
            chunkList.add(endNameSpaceChunk);

            Xml.nameSpaceMap.put(stringChunkList.get(prefix), stringChunkList.get(uri));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("parse End NameSpace Chunk error!");
        }
    }

    private void parseTextChunk() {
        System.out.print("\nparse Text Chunk");
    }

    private void generateXml() {
        Xml xml = new Xml(stringChunkList, null, chunkList);
        System.out.println("\n" + xml.toString());
    }

    public static String format(String format, Object... params) {
        return String.format(format, params);
    }

    public static String getNamespacePrefix(String prefix) {
        if (prefix == null || prefix.length() == 0) {
            return "";
        }
        return prefix + ":";
    }

    public static byte[] moveBlank(byte[] data) {
        List<Byte> byteList = new ArrayList<>();
        for (Byte b : data) {
            if (b != 0) byteList.add(b);
        }
        byte[] result = new byte[byteList.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = byteList.get(i);
        return result;
    }

}
