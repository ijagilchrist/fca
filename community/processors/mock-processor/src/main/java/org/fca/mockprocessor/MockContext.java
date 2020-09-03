package org.fca.mockprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fca.metadata.MetadataCollection;
import org.fca.processor.Context;

import java.io.*;
import java.util.*;

public class MockContext implements Context {

    private final String objectUUID;
    private final String root;

    public MockContext(String objectUUID) {
        this.objectUUID = objectUUID;
        this.root = "mock_data_"+UUID.randomUUID().toString();
    }

    public MockContext(String objectUUID, String root) {
        this.objectUUID = objectUUID;
        this.root = root;
    }

    @Override
    public String getObjectUUID() {
        return objectUUID;
    }

    @Override
    public InputStream getObject(String objectName) throws IOException {
        String filename = String.format("reception/%s/object/%s",this.objectUUID,objectName);
        return this.getObjectInputStream(filename);
    }

    @Override
    public Map<String,String> getObjectMetadata(String objectName) throws IOException {
        String filename = String.format("reception/%s/object/%s.metadata",this.objectUUID,objectName);
        return this.getObjectMetadataAsMap(filename);
    }

    @Override
    public String putObject(InputStream stream, String objectName, Map<String, String> objectMetadata) throws IOException {
        String objectUUID = String.format("%s-%s",this.objectUUID,UUID.randomUUID().toString().substring(0,6));
        {
            String filename = String.format("reception/%s/object/%s",objectUUID,objectName);
            OutputStream out = this.getObjectOutputStream(filename);
            stream.transferTo(out);
            out.close();
        }
        {
            String filename = String.format("reception/%s/object/%s.metadata",objectUUID,objectName);
            OutputStream out = this.getObjectOutputStream(filename);
            ObjectMapper mapper = new ObjectMapper();
            out.write(mapper.writeValueAsBytes(objectMetadata));
            out.close();
        }
        return objectUUID;
    }

    @Override
    public InputStream getDerivedObject(String objectName) throws IOException {
        String filename = String.format("reception/%s/derived/%s",this.objectUUID,objectName);
        return this.getObjectInputStream(filename);
    }

    @Override
    public Map<String,String> getDerivedObjectMetadata(String objectName) throws IOException {
        String filename = String.format("reception/%s/derived/%s.metadata",this.objectUUID,objectName);
        return this.getObjectMetadataAsMap(filename);
    }

    @Override
    public void putDerivedObject(InputStream stream, String objectName, Map<String,String> objectMetadata) throws IOException {
        {
            String filename = String.format("reception/%s/derived/%s",this.objectUUID,objectName);
            OutputStream out = this.getObjectOutputStream(filename);
            stream.transferTo(out);
            out.close();
        }
        {
            String filename = String.format("reception/%s/derived/%s.metadata",this.objectUUID,objectName);
            OutputStream out = this.getObjectOutputStream(filename);
            ObjectMapper mapper = new ObjectMapper();
            out.write(mapper.writeValueAsBytes(objectMetadata));
            out.close();
        }
    }

    @Override
    public void publishObject(String objectName, Map<String,String> objectMetadata) throws IOException {
        {
            String sourceFilename = String.format("reception/%s/object/%s",this.objectUUID,objectName);
            String targetFilename = String.format("published/%s/objects/%s",this.objectUUID,objectName);
            InputStream stream = this.getObjectInputStream(sourceFilename);
            OutputStream out = this.getObjectOutputStream(targetFilename);
            stream.transferTo(out);
            out.close();
            stream.close();
        }
        {
            String sourceFilename = String.format("reception/%s/object/%s.metadata",this.objectUUID,objectName);
            String targetFilename = String.format("published/%s/objects/%s.metadata",this.objectUUID,objectName);
            Map<String,String> metadata = this.getObjectMetadataAsMap(sourceFilename);
            if (metadata != null) {
                metadata.putAll(objectMetadata);
                OutputStream out = this.getObjectOutputStream(targetFilename);
                ObjectMapper mapper = new ObjectMapper();
                out.write(mapper.writeValueAsBytes(metadata));
                out.close();
            }
        }
    }

    @Override
    public void publishDerivedObject(String objectName, Map<String,String> objectMetadata) throws IOException {
        {
            String sourceFilename = String.format("reception/%s/derived/%s",this.objectUUID,objectName);
            String targetFilename = String.format("published/%s/objects/%s",this.objectUUID,objectName);
            InputStream stream = this.getObjectInputStream(sourceFilename);
            OutputStream out = this.getObjectOutputStream(targetFilename);
            stream.transferTo(out);
            out.close();
            stream.close();
        }
        {
            String sourceFilename = String.format("reception/%s/derived/%s.metadata",this.objectUUID,objectName);
            String targetFilename = String.format("published/%s/objects/%s.metadata",this.objectUUID,objectName);
            Map<String,String> metadata = this.getObjectMetadataAsMap(sourceFilename);
            if (metadata != null) {
                metadata.putAll(objectMetadata);
                OutputStream out = this.getObjectOutputStream(targetFilename);
                ObjectMapper mapper = new ObjectMapper();
                out.write(mapper.writeValueAsBytes(metadata));
                out.close();
            }
        }
    }

    @Override
    public void publishMetadata(MetadataCollection metadata) throws IOException {
        String filename = String.format("published/%s/metadata/metadata.json",this.objectUUID);
        ByteArrayInputStream stream = new ByteArrayInputStream(metadata.toJSON().getBytes());
        OutputStream out = this.getObjectOutputStream(filename);
        stream.transferTo(out);
        out.close();
        stream.close();
    }

    @Override
    public MetadataCollection getContentMetadata() throws IOException {
        String filename = String.format("metadata/%s/metadata.json",this.objectUUID);
        InputStream stream = this.getObjectInputStream(filename);
        ByteArrayOutputStream metadata = new ByteArrayOutputStream();
        stream.transferTo(metadata);
        metadata.close();
        stream.close();
        return MetadataCollection.builder()
                .fromJSON(metadata.toString())
                .build();
    }

    public void loadObject(String objectName) throws IOException {
        String sourceFilename = String.format("data/%s/%s",this.objectUUID,objectName);
        String targetFilename = String.format("reception/%s/object/%s",this.objectUUID,objectName);
        InputStream stream = new FileInputStream(new File("c:/Development/",sourceFilename));
        OutputStream out = this.getObjectOutputStream(targetFilename);
        stream.transferTo(out);
        out.close();
        stream.close();
    }

    public void deleteContext() {
        this.deleteDirectory(new File(root));
    }

    public InputStream getObjectInputStream(String path) throws IOException {

        File file = new File(root,path);
        if (file.exists()) return new FileInputStream(file);

        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

    }

    public Map<String,String> getObjectMetadataAsMap(String path) throws IOException {

        ByteArrayOutputStream json = new ByteArrayOutputStream();
        InputStream stream = this.getObjectInputStream(path);
        stream.transferTo(json);
        json.close();
        stream.close();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json.toString(),Map.class);
    }

    public OutputStream getObjectOutputStream(String path) throws IOException {

        File file = new File(root,path);
        file.getParentFile().mkdirs();
        return new FileOutputStream(file);

    }

    private void deleteDirectory(File directory) {
        for (File file: directory.listFiles()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                file.delete();
            }
        }
        directory.delete();
    }
}
