package org.fca.awsprocessor;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.fca.metadata.Metadata;
import org.fca.metadata.MetadataCollection;
import org.fca.processor.Context;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@ParametersAreNonnullByDefault
public class AWSContext implements Context {

    @Nonnull
    private final Regions region;

    @Nonnull
    private final String sourceBucket;

    @Nonnull
    private final String targetBucket;

    @Nonnull
    private final String metadataTable;

    @Nonnull
    private final String objectUUID;

    private AWSContext(Builder builder) {
        this.region = Objects.requireNonNull(builder.region, "region");
        this.sourceBucket = Objects.requireNonNull(builder.sourceBucket, "sourceBucket");
        this.targetBucket = Objects.requireNonNull(builder.targetBucket, "targetBucket");
        this.metadataTable = Objects.requireNonNull(builder.metadataTable, "metadataTable");
        this.objectUUID = Objects.requireNonNull(builder.objectUUID, "objectUUID");
    }

    @Nonnull
    public Regions getRegion() {
        return region;
    }

    @Nonnull
    public String getSourceBucket() {
        return sourceBucket;
    }

    @Nonnull
    public String getTargetBucket() {
        return targetBucket;
    }

    @Nonnull
    public String getMetadataTable() {
        return metadataTable;
    }

    @Nonnull
    public String getObjectUUID() {
        return objectUUID;
    }

    @Override
    public InputStream getObject(String objectName) throws IOException {
        String key = String.format("%s/object/%s",this.objectUUID,objectName);
        return this.getObject(this.region,this.sourceBucket,key);
    }

    @Override
    public Map<String,String> getObjectMetadata(String objectName) throws IOException {
        String key = String.format("%s/object/%s",this.objectUUID,objectName);
        return this.getObjectMetadata(this.region,this.sourceBucket,key);
    }

    @Override
    public String putObject(InputStream stream, String objectName, Map<String, String> objectMetadata) throws IOException {
        String objectUUID = String.format("%s-%s",this.objectUUID,UUID.randomUUID().toString().substring(0,6));
        String key = String.format("%s/derived/%s",objectUUID,objectName);
        this.putObject(this.region,this.sourceBucket,key,stream,objectMetadata);
        return objectUUID;
    }

    @Override
    public InputStream getDerivedObject(String objectName) throws IOException {
        String key = String.format("%s/derived/%s",this.objectUUID,objectName);
        return this.getObject(this.region,this.sourceBucket,key);
    }

    @Override
    public Map<String,String> getDerivedObjectMetadata(String objectName) throws IOException {
        String key = String.format("%s/derived/%s",this.objectUUID,objectName);
        return this.getObjectMetadata(this.region,this.sourceBucket,key);
    }

    @Override
    public void putDerivedObject(InputStream stream, String objectName, Map<String,String> objectMetadata) throws IOException {
        String key = String.format("%s/derived/%s",this.objectUUID,objectName);
        this.putObject(this.region,this.sourceBucket,key,stream,objectMetadata);
    }

    @Override
    public void publishObject(String objectName, Map<String,String> objectMetadata) throws IOException {
        String sourceKey = String.format("%s/object/%s",this.objectUUID,objectName);
        String targetKey = String.format("%s/objects/%s",this.objectUUID,objectName);
        this.copyObject(this.region,this.sourceBucket,sourceKey,this.targetBucket,targetKey,objectMetadata);
    }

    @Override
    public void publishDerivedObject(String objectName, Map<String,String> objectMetadata) throws IOException {
        String sourceKey = String.format("%s/derived/%s",this.objectUUID,objectName);
        String targetKey = String.format("%s/objects/%s",this.objectUUID,objectName);
        this.copyObject(this.region,this.sourceBucket,sourceKey,this.targetBucket,targetKey,objectMetadata);
    }

    @Override
    public void publishMetadata(MetadataCollection metadata) throws IOException {
        String key = String.format("%s/metadata/metadata.json",this.objectUUID);
        ByteArrayInputStream stream = new ByteArrayInputStream(metadata.toJSON().getBytes());
        Map<String,String> objectMetadata = new HashMap<String,String>();
        objectMetadata.put("content-type","application/json");
        this.putObject(this.region,this.targetBucket,key,stream,objectMetadata);
    }

    @Override
    public MetadataCollection getContentMetadata() throws IOException {
        List<Map<String,Object>> records = this.getRecords(this.region,this.metadataTable,"ObjectUUID",this.objectUUID);
        List<Metadata> collection = new ArrayList<Metadata>();
        for (Map<String,Object> record: records) {
            Object timestamp = Objects.requireNonNull(record.get("Timestamp"), "Timestamp");
            Object metadataJSON = Objects.requireNonNull(record.get("Metadata"), "Metadata");
            if ((timestamp instanceof Number) && (metadataJSON instanceof String)) {
                Metadata metadata = Objects.requireNonNull(Metadata.builder()
                        .fromJSON((String)metadataJSON)
                        .build(),"Metadata");
                collection.add(metadata);
            }
        }
        return MetadataCollection.builder().setMetadata(collection).build();
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

    private Map<String,String> getObjectMetadata(Regions region, String bucket, String key) throws IOException {

        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .build();

        S3Object object = s3.getObject(bucket,key);
        if (object == null) return null;

        ObjectMetadata metadata = object.getObjectMetadata();

        return this.fromAWSHeaders(metadata);

    }

    private void putObject(Regions region, String bucket, String key, InputStream stream, Map<String,String> headers) {

        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .build();
        ObjectMetadata metadata = toAWSHeaders(headers);
        s3.putObject(bucket,key,stream,metadata);

    }

    public void copyObject(Regions resgion, String srcBucket, String srcKey, String destBucket, String destKey, Map<String,String> headers) throws IOException {

        Map<String,String> objectHeaders = this.getObjectMetadata(region,srcBucket,srcKey);
        if (headers != null) objectHeaders.putAll(headers);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        InputStream in = this.getObject(region,srcBucket,srcKey);
        in.transferTo(buffer);

        ByteArrayInputStream out = new ByteArrayInputStream(buffer.toByteArray());
        this.putObject(region,destBucket,destKey,out,objectHeaders);

    }

    private List<Map<String,Object>> getRecords(Regions region, String tableName, String hashKey, String hashValue) {

        List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(region)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable(tableName);

        QuerySpec spec = new QuerySpec()
                .withHashKey(hashKey,hashValue)
                .withConsistentRead(true);
        ItemCollection<QueryOutcome> items = table.query(spec);

        Iterator<Item> iterator = items.iterator();
        Item item = null;
        while (iterator.hasNext()) {
            item = iterator.next();
            records.add(item.asMap());
        }

        return records;

    }

    private Map<String,String> fromAWSHeaders(ObjectMetadata objectMetadata) {
        Map<String,String> headers = new HashMap<String,String>();
        String contentType = objectMetadata.getContentType();
        if (contentType != null) headers.put("content-type",contentType);
        for (String name: objectMetadata.getUserMetadata().keySet()) {
            String value = objectMetadata.getUserMetadata().get(name);
            headers.put(name,value);
        }

        return headers;
    }

    private ObjectMetadata toAWSHeaders(Map<String,String> headers) {
        ObjectMetadata metadata = new ObjectMetadata();
        for (String name: headers.keySet()) {
            String value = headers.get(name);
            if (name.equalsIgnoreCase("Content-Type")) {
                metadata.setContentType(value);
            } else {
                metadata.addUserMetadata(name,value);
            }
        }
        return metadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Regions region = Regions.EU_WEST_2;
        private String sourceBucket = "org.fca.s3.reception";
        private String targetBucket = "org.fca.s3.published";
        private String metadataTable = "org.fca.dynamodb.metadata";
        private String objectUUID;

        private Builder() {
        }

        public Builder setRegion(Regions region) {
            this.region = region;
            return this;
        }

        public Builder setSourceBucket(String sourceBucket) {
            this.sourceBucket = sourceBucket;
            return this;
        }

        public Builder setTargetBucket(String targetBucket) {
            this.targetBucket = targetBucket;
            return this;
        }

        public Builder setMetadataTable(String metadataTable) {
            this.metadataTable = metadataTable;
            return this;
        }

        public Builder setObjectUUID(String objectUUID) {
            this.objectUUID = objectUUID;
            return this;
        }

        public Builder of(AWSContext aWSContext) {
            this.region = aWSContext.region;
            this.sourceBucket = aWSContext.sourceBucket;
            this.targetBucket = aWSContext.targetBucket;
            this.metadataTable = aWSContext.metadataTable;
            this.objectUUID = aWSContext.objectUUID;
            return this;
        }

        public AWSContext build() {
            return new AWSContext(this);
        }

    }

}
