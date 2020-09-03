package org.fca.processor;

import org.fca.metadata.MetadataCollection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface Context {

    public String getObjectUUID();

    public InputStream getObject(String objectName) throws IOException;

    public Map<String,String> getObjectMetadata(String objectName) throws IOException;

    public String putObject(InputStream stream, String objectName, Map<String,String> objectMetadata) throws IOException;

    public InputStream getDerivedObject(String derivedObjectName) throws IOException;

    public Map<String,String> getDerivedObjectMetadata(String derivedObjectName) throws IOException;

    public void putDerivedObject(InputStream stream, String derivedObjectName, Map<String,String> objectMetadata) throws IOException;

    public void publishObject(String objectName, Map<String,String> objectMetadata) throws IOException;

    public void publishDerivedObject(String derivedObjectName, Map<String,String> objectMetadata) throws IOException;

    public void publishMetadata(MetadataCollection metadata) throws IOException;

    public MetadataCollection getContentMetadata() throws IOException;

}
