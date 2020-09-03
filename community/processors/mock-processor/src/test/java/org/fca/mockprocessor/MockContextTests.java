package org.fca.mockprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fca.metadata.Metadata;
import org.fca.metadata.MetadataCollection;
import org.fca.metadata.MetadataTestsHelper;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.ObjectTestsHelper;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockContextTests {

    @Test
    public void readObjectTest() throws IOException {

        String root = "mock_data_"+UUID.randomUUID().toString();

        String objectUUID = "myObjectUUID";
        String objectName = "myObjectName";
        String objectData = "Test Object 1 Data";
        String mimetype = "text/plain";

        // Create test object
        {
            MockContext mockContext = new MockContext(objectUUID,root);
            String filename = String.format("reception/%s/object/%s",objectUUID,objectName);
            ByteArrayInputStream stream = new ByteArrayInputStream(objectData.getBytes());
            OutputStream out = mockContext.getObjectOutputStream(filename);
            stream.transferTo(out);
            out.close();
            stream.close();
        }

        // Create metadata
        {
            MockContext mockContext = new MockContext(objectUUID,root);
            Map<String,String> metadata = new HashMap<>();
            metadata.put("content-type",mimetype);
            String filename = String.format("reception/%s/object/%s.metadata",objectUUID,objectName);
            ObjectMapper mapper = new ObjectMapper();
            ByteArrayInputStream stream = new ByteArrayInputStream(mapper.writeValueAsString(metadata).getBytes());
            OutputStream out = mockContext.getObjectOutputStream(filename);
            stream.transferTo(out);
            out.close();
            stream.close();
        }

        // Check object and metadata can be read correctly
        {
            MockContext mockContext = new MockContext(objectUUID,root);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            mockContext.getObject(objectName).transferTo(out);
            assertEquals(objectData,out.toString());
            Map<String,String> metadata = mockContext.getObjectMetadata(objectName);
            assertEquals(1,metadata.size());
            assertEquals(mimetype,metadata.get("content-type"));
        }

        String metadataName = "mymetadata";
        String metadataValue = "myvalue";

        // Publish object
        {
            MockContext mockContext = new MockContext(objectUUID,root);
            Map<String,String> metadata = new HashMap<String,String>();
            metadata.put(metadataName,metadataValue);
            mockContext.publishObject(objectName,metadata);
        }

        // Check published object and metadata can be read correctly
        {
            MockContext mockContext = new MockContext(objectUUID,root);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            String filename = String.format("published/%s/objects/%s",objectUUID,objectName);
            InputStream stream = mockContext.getObjectInputStream(filename);
            stream.transferTo(out);
            out.close();
            stream.close();
            assertEquals(out.toString(),objectData);
            String filename2 = String.format("published/%s/objects/%s.metadata",objectUUID,objectName);
            Map<String,String> metadata = mockContext.getObjectMetadataAsMap(filename2);
            assertEquals(2,metadata.size());
            assertEquals(mimetype,metadata.get("content-type"));
            assertEquals(metadataValue,metadata.get(metadataName));
        }


        // Tidy Up
        {
            new MockContext(objectUUID,root).deleteContext();
        }

    }

    @Test
    public void readDerivedObjectTest() throws IOException {

        String root = UUID.randomUUID().toString();

        String objectUUID = "myObjectUUID";
        String objectName = "myObjectName";
        String objectData = "Test Object 2 Data";
        String mimetype = "text/plain";

        // Create test object
        {
            MockContext mockContext = new MockContext(objectUUID,root);
            String filename = String.format("reception/%s/derived/%s",objectUUID,objectName);
            ByteArrayInputStream stream = new ByteArrayInputStream(objectData.getBytes());
            OutputStream out = mockContext.getObjectOutputStream(filename);
            stream.transferTo(out);
            out.close();
            stream.close();
        }

        // Create metadata
        {
            MockContext mockContext = new MockContext(objectUUID,root);
            Map<String,String> metadata = new HashMap<>();
            metadata.put("content-type",mimetype);
            String filename = String.format("reception/%s/derived/%s.metadata",objectUUID,objectName);
            ObjectMapper mapper = new ObjectMapper();
            ByteArrayInputStream stream = new ByteArrayInputStream(mapper.writeValueAsString(metadata).getBytes());
            OutputStream out = mockContext.getObjectOutputStream(filename);
            stream.transferTo(out);
            out.close();
            stream.close();
        }

        // Check object and metadata can be read correctly
        {
            MockContext mockContext = new MockContext(objectUUID,root);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            mockContext.getDerivedObject(objectName).transferTo(out);
            assertEquals(objectData,out.toString());
            Map<String,String> metadata = mockContext.getDerivedObjectMetadata(objectName);
            assertEquals(1,metadata.size());
            assertEquals(mimetype,metadata.get("content-type"));
        }

        String metadataName = "mymetadata2";
        String metadataValue = "myvalue2";

        // Publish object
        {
            MockContext mockContext = new MockContext(objectUUID,root);
            Map<String,String> metadata = new HashMap<String,String>();
            metadata.put(metadataName,metadataValue);
            mockContext.publishDerivedObject(objectName,metadata);
        }

        // Check published object and metadata can be read correctly
        {
            MockContext mockContext = new MockContext(objectUUID,root);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            String filename = String.format("published/%s/objects/%s",objectUUID,objectName);
            InputStream stream = mockContext.getObjectInputStream(filename);
            stream.transferTo(out);
            out.close();
            stream.close();
            assertEquals(out.toString(),objectData);
            String filename2 = String.format("published/%s/objects/%s.metadata",objectUUID,objectName);
            Map<String,String> metadata = mockContext.getObjectMetadataAsMap(filename2);
            assertEquals(2,metadata.size());
            assertEquals(mimetype,metadata.get("content-type"));
            assertEquals(metadataValue,metadata.get(metadataName));
        }


        // Tidy Up
        {
            new MockContext(objectUUID,root).deleteContext();
        }

    }

    @Test
    public void readMetadataRecords() throws IOException {

        String root = UUID.randomUUID().toString();

        String hashKey = "ObjectUUID";
        String sortKey = "Timestamp";
        String metadataKey = "Metadata";

        String objectUUID = UUID.randomUUID().toString();
        List<Metadata> referenceCollection = new ArrayList<Metadata>();

        {
            String objectForm = "document";
            String objectMimetype = "text/html";
            List<MicroFormat> referenceMicroformats = new ArrayList<MicroFormat>();
            referenceMicroformats.add(ObjectTestsHelper.createTestUnidentifiedObject());
            referenceMicroformats.add(ObjectTestsHelper.createTestIdentifiedObject(objectForm,objectMimetype));
            referenceMicroformats.add(ObjectTestsHelper.createTestCompletedObject(objectForm));
            Metadata referenceMetadata = MetadataTestsHelper.createTestMetadata(objectUUID,referenceMicroformats);
            referenceCollection.add(referenceMetadata);
        }

        {
            String objectForm = "document";
            String objectMimetype = "text/html";
            List<MicroFormat> referenceMicroformats = new ArrayList<MicroFormat>();
            referenceMicroformats.add(ObjectTestsHelper.createTestUnidentifiedObject());
            referenceMicroformats.add(ObjectTestsHelper.createTestIdentifiedObject(objectForm,objectMimetype));
            referenceMicroformats.add(ObjectTestsHelper.createTestCompletedObject(objectForm));
            Metadata referenceMetadata = MetadataTestsHelper.createTestMetadata(objectUUID,referenceMicroformats);
            referenceCollection.add(referenceMetadata);
        }

        {
            String objectForm = "document";
            String objectMimetype = "text/html";
            List<MicroFormat> referenceMicroformats = new ArrayList<MicroFormat>();
            referenceMicroformats.add(ObjectTestsHelper.createTestUnidentifiedObject());
            referenceMicroformats.add(ObjectTestsHelper.createTestIdentifiedObject(objectForm,objectMimetype));
            referenceMicroformats.add(ObjectTestsHelper.createTestCompletedObject(objectForm));
            Metadata referenceMetadata = MetadataTestsHelper.createTestMetadata(objectUUID,referenceMicroformats);
            referenceCollection.add(referenceMetadata);
        }

        MetadataCollection referenceObject = MetadataCollection.builder()
                .setMetadata(referenceCollection)
                .build();

        MockContext mockContext = new MockContext(objectUUID,root);

        // Create the test object
        {
            String filename = String.format("metadata/%s/metadata.json",objectUUID);
            ByteArrayInputStream stream = new ByteArrayInputStream(referenceObject.toJSON().getBytes());
            OutputStream out = mockContext.getObjectOutputStream(filename);
            stream.transferTo(out);
            out.close();
            stream.close();
        }

        // Test the read object against the reference
        MetadataCollection testObject = mockContext.getContentMetadata();

        MetadataTestsHelper.checkTestMetadataCollection(referenceObject,testObject);

        // Publish the metadata
        mockContext.publishMetadata(referenceObject);

        // Make sure the published metadata can be read
        {
            String filename = String.format("published/%s/metadata/metadata.json",objectUUID);
            InputStream stream = mockContext.getObjectInputStream(filename);
            ByteArrayOutputStream metadata = new ByteArrayOutputStream();
            stream.transferTo(metadata);
            metadata.close();
            stream.close();
            testObject = MetadataCollection.builder()
                    .fromJSON(metadata.toString())
                    .build();
        }

        MetadataTestsHelper.checkTestMetadataCollection(referenceObject,testObject);


    }

}
