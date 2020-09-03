package org.fca.awsprocessor;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.fca.awsprocessor.AWSContext;
import org.fca.metadata.Metadata;
import org.fca.metadata.MetadataCollection;
import org.fca.metadata.MetadataTestsHelper;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.ObjectTestsHelper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AWSContextTests {

    @Test
    public void readObjectTest() throws IOException {

        String objectUUID = "myObjectUUID";
        String objectName = "myObjectName";
        String objectData = "Test Object 1 Data";
        String mimetype = "text/plain";
        String receptionBucket = "org.fca.s3.reception";
        String publishedBucket = "org.fca.s3.published";

        // Create test object
        {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(mimetype);
            String key = String.format("%s/object/%s",objectUUID,objectName);
            ByteArrayInputStream stream = new ByteArrayInputStream(objectData.getBytes());
            this.putObject(Regions.EU_WEST_2,receptionBucket,key,stream,metadata);
        }

        // Check object and metadata can be read correctly
        {
            AWSContext awsContext = AWSContext.builder()
                    .setObjectUUID(objectUUID)
                    .build();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            awsContext.getObject(objectName).transferTo(out);
            assertEquals(objectData,out.toString());
            Map<String,String> metadata = awsContext.getObjectMetadata(objectName);
            assertEquals(1,metadata.size());
            assertEquals(mimetype,metadata.get("content-type"));
        }

        String metadataName = "mymetadata";
        String metadataValue = "myvalue";

        // Publish object
        {
            AWSContext awsContext = AWSContext.builder()
                    .setObjectUUID(objectUUID)
                    .build();
            Map<String,String> metadata = new HashMap<String,String>();
            metadata.put(metadataName,metadataValue);
            awsContext.publishObject(objectName,metadata);
        }

        // Check published object and metadata can be read correctly
        {
            AWSContext awsContext = AWSContext.builder()
                    .setObjectUUID(objectUUID)
                    .build();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            String key = String.format("%s/objects/%s",objectUUID,objectName);
            this.getObject(Regions.EU_WEST_2,publishedBucket,key).transferTo(out);
            assertEquals(out.toString(),objectData);
        }

        // Delete published object
        {
            String key = String.format("%s/derived/%s",objectUUID,objectName);
            this.deleteObject(Regions.EU_WEST_2, receptionBucket, key);
        }

        // Delete original object
        {
            String key = String.format("%s/object/%s",objectUUID,objectName);
            this.deleteObject(Regions.EU_WEST_2, receptionBucket, key);
        }

    }

    @Test
    public void readDerivedObjectTest() throws IOException {

        String objectUUID = "myDerivedObjectUUID";
        String objectName = "myDerivedObjectName";
        String objectData = "Test Object 1 Data";
        String mimetype = "text/plain";
        String receptionBucket = "org.fca.s3.reception";
        String publishedBucket = "org.fca.s3.published";

        // Create test object
        {
            AWSContext awsContext = AWSContext.builder()
                    .setObjectUUID(objectUUID)
                    .build();
            Map<String,String> metadata = new HashMap<String,String>();
            metadata.put("content-type",mimetype);
            ByteArrayInputStream stream = new ByteArrayInputStream(objectData.getBytes());
            awsContext.putDerivedObject(stream,objectName,metadata);
        }

        // Check derived object and metadata can be read correctly
        {
            AWSContext awsContext = AWSContext.builder()
                    .setObjectUUID(objectUUID)
                    .build();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            awsContext.getDerivedObject(objectName).transferTo(out);
            assertEquals(objectData,out.toString());
            Map<String,String> metadata = awsContext.getDerivedObjectMetadata(objectName);
            assertEquals(1,metadata.size());
            assertEquals(mimetype,metadata.get("content-type"));
        }

        String metadataName = "mymetadata";
        String metadataValue = "myvalue";

        // Publish object
        {
            AWSContext awsContext = AWSContext.builder()
                    .setObjectUUID(objectUUID)
                    .build();
            Map<String,String> metadata = new HashMap<String,String>();
            metadata.put(metadataName,metadataValue);
            awsContext.publishDerivedObject(objectName,metadata);
        }

        // Check published object and metadata can be read correctly
        {
            AWSContext awsContext = AWSContext.builder()
                    .setObjectUUID(objectUUID)
                    .build();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            String key = String.format("%s/objects/%s",objectUUID,objectName);
            this.getObject(Regions.EU_WEST_2,publishedBucket,key).transferTo(out);
            assertEquals(out.toString(),objectData);
        }

        // Delete published object
        {
            String key = String.format("%s/derived/%s",objectUUID,objectName);
//            this.deleteObject(Regions.EU_WEST_2, receptionBucket, key);
        }

        // Delete original object
        {
            String key = String.format("%s/object/%s",objectUUID,objectName);
            this.deleteObject(Regions.EU_WEST_2, receptionBucket, key);
        }

    }

    @Test
    public void readMetadataRecords() throws IOException {

        String metadataTable = "org.fca.dynamodb.metadata";
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
            Map<String,Object> values = new HashMap<String,Object>();
            values.put(metadataKey,referenceMetadata.toJSON());
            putRecord(Regions.EU_WEST_2,metadataTable,hashKey,referenceMetadata.getObjectUUID(),sortKey,referenceMetadata.getTimestamp(),values);
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
            Map<String,Object> values = new HashMap<String,Object>();
            values.put(metadataKey,referenceMetadata.toJSON());
            putRecord(Regions.EU_WEST_2,metadataTable,hashKey,referenceMetadata.getObjectUUID(),sortKey,referenceMetadata.getTimestamp(),values);
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
            Map<String,Object> values = new HashMap<String,Object>();
            values.put(metadataKey,referenceMetadata.toJSON());
            putRecord(Regions.EU_WEST_2,metadataTable,hashKey,referenceMetadata.getObjectUUID(),sortKey,referenceMetadata.getTimestamp(),values);
        }

        MetadataCollection referenceObject = MetadataCollection.builder()
                .setMetadata(referenceCollection)
                .build();

        AWSContext awsContext = AWSContext.builder()
                .setObjectUUID(objectUUID)
                .build();
        MetadataCollection testObject = awsContext.getContentMetadata();

        MetadataTestsHelper.checkTestMetadataCollection(referenceObject,testObject);

    }

    private InputStream getObject(Regions region, String bucket, String key) throws IOException {

        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .build();

        S3Object object = s3.getObject(bucket,key);
        if (object == null) return null;

        return object.getObjectContent();

    }

    private ObjectMetadata getObjectMetadata(Regions region, String bucket, String key) throws IOException {

        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .build();

        S3Object object = s3.getObject(bucket,key);
        if (object == null) return null;

        return object.getObjectMetadata();

    }

    private void putObject(Regions region, String bucket, String key, InputStream stream, ObjectMetadata metadata) {

        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .build();

        s3.putObject(bucket,key,stream,metadata);

    }

    private void deleteObject(Regions region, String bucket, String key) throws IOException {

        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .build();

        s3.deleteObject(bucket,key);

    }

    private void putRecord(Regions region, String tableName, String hashKey, Object hashValue, String sortKey, Object sortValue, Map<String,Object> values) {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(region)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable(tableName);

        Item item = new Item();
        item.withPrimaryKey(new PrimaryKey(hashKey,hashValue,sortKey,sortValue));
        for (String key: values.keySet()) item.withString(key,values.get(key).toString());

        table.putItem(item);

    }

}
