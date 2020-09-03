package org.fca.awschoreography;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class AWSChoreographyTests {

    @Nonnull
    private final Regions region;

    @Nonnull
    private final String objectUUID;

    @Nonnull
    private final String queueName;

    @Nonnull
    private final String microFormat;

    @Nonnull
    private final String metadataTable;

    @Nonnull
    private final String registryTable;

    @Nonnull
    private final String sourceBucket;

    @Nonnull
    private final String targetBucket;

    private AWSChoreographyTests(Builder builder) {
        this.region = Objects.requireNonNull(builder.region, "region");
        this.objectUUID = Objects.requireNonNull(builder.objectUUID, "objectUUID");
        this.queueName = Objects.requireNonNull(builder.queueName, "queueName");
        this.microFormat = Objects.requireNonNull(builder.microFormat, "microFormat");
        this.metadataTable = Objects.requireNonNull(builder.metadataTable, "metadataTable");
        this.registryTable = Objects.requireNonNull(builder.registryTable, "registryTable");
        this.sourceBucket = Objects.requireNonNull(builder.sourceBucket, "sourceBucket");
        this.targetBucket = Objects.requireNonNull(builder.targetBucket, "targetBucket");
    }

    @Nonnull
    public Regions getRegion() {
        return region;
    }

    @Nonnull
    public String getObjectUUID() {
        return objectUUID;
    }

    @Nonnull
    public String getQueueName() {
        return queueName;
    }

    @Nonnull
    public String getMicroFormat() {
        return microFormat;
    }

    @Nonnull
    public String getMetadataTable() {
        return metadataTable;
    }

    @Nonnull
    public String getRegistryTable() {
        return registryTable;
    }

    @Nonnull
    public String getSourceBucket() {
        return sourceBucket;
    }

    @Nonnull
    public String getTargetBucket() {
        return targetBucket;
    }

    public boolean checkQueue(long timeout) throws InterruptedException {

        return checkQueue(1000,timeout);

    }

    public boolean checkQueue(long checkInterval, long timeout) throws InterruptedException {

        long expiry = System.currentTimeMillis() + timeout;

        while (System.currentTimeMillis()<expiry && !this.checkQueue()) {
            synchronized (this) {
                this.wait(checkInterval);
            }
        }
        return this.checkQueue();

    }

    public boolean checkQueue() {

        AmazonSQS sqs = AmazonSQSClientBuilder
                .standard()
                .withRegion(this.region)
                .build();

        ListQueuesResult queues = sqs.listQueues(this.queueName);
        return queues.getQueueUrls().size() == 1;

    }

    public boolean checkMetadataEntry(long timeout) throws InterruptedException {

        return checkMetadataEntry(1000,timeout);

    }

    public boolean checkMetadataEntry(long checkInterval, long timeout) throws InterruptedException {

        long expiry = System.currentTimeMillis() + timeout;

        while (System.currentTimeMillis()<expiry && !this.checkMetadataEntry()) {
            synchronized (this) {
                this.wait(checkInterval);
            }
        }
        return this.checkMetadataEntry();

    }

    public boolean checkMetadataEntry() {

        int count = 0;

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(this.region)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(metadataTable);

        QuerySpec spec = new QuerySpec()
                .withHashKey("ObjectUUID",objectUUID)
                .withConsistentRead(true);
        ItemCollection<QueryOutcome> items = table.query(spec);

        Iterator<Item> iterator = items.iterator();
        Item item = null;
        while (iterator.hasNext()) {
            item = iterator.next();
            count++;
        }

        return count == 1;

    }

    public boolean checkRegistryEntry(long timeout) throws InterruptedException {

        return checkRegistryEntry(1000,timeout);

    }

    public boolean checkRegistryEntry(long checkInterval, long timeout) throws InterruptedException {

        long expiry = System.currentTimeMillis() + timeout;

        while (System.currentTimeMillis()<expiry && !this.checkRegistryEntry()) {
            synchronized (this) {
                this.wait(checkInterval);
            }
        }
        return this.checkRegistryEntry();

    }

    public boolean checkRegistryEntry() {

        int count = 0;

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(this.region)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable(registryTable);

        QuerySpec spec = new QuerySpec()
                .withHashKey("MicroFormat",this.microFormat)
                .withConsistentRead(true);
        ItemCollection<QueryOutcome> items = table.query(spec);

        Iterator<Item> iterator = items.iterator();
        Item item = null;
        while (iterator.hasNext()) {
            item = iterator.next();
            if (!item.get("Choreography").equals(this.queueName)) continue;
            count++;
        }

        return count == 1;

    }

    public int countDerivedObjects(int expected, long timeout) throws InterruptedException {

        return countDerivedObjects(expected, 5000,timeout);

    }

    public int countDerivedObjects(int expected, long checkInterval, long timeout) throws InterruptedException {

        long expiry = System.currentTimeMillis() + timeout;

        while (System.currentTimeMillis()<expiry && countDerivedObjects() < expected) {
            synchronized (this) {
                this.wait(checkInterval);
            }
        }
        return this.countDerivedObjects();

    }

    public int countDerivedObjects() {

        int count = 0;

        String prefix = String.format("%s/%s/",this.objectUUID,"derived");

        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .build();
        ListObjectsRequest listRequest = new ListObjectsRequest()
                .withBucketName(this.sourceBucket)
                .withPrefix(prefix)
                .withDelimiter("/");
        ObjectListing items = s3.listObjects(listRequest);
        for (S3ObjectSummary summary: items.getObjectSummaries()) {
            count++;
        }

        return count;

    }

    public void deleteDerivedObjects() {

        String prefix = String.format("%s/%s/",this.objectUUID,"derived");

        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .build();
        ListObjectsRequest listRequest = new ListObjectsRequest()
                .withBucketName(this.sourceBucket)
                .withPrefix(prefix)
                .withDelimiter("/");
        ObjectListing items = s3.listObjects(listRequest);
        for (S3ObjectSummary summary: items.getObjectSummaries()) {
            s3.deleteObject(this.sourceBucket,summary.getKey());
        }

    }

    public void deleteQueue() {

        AmazonSQS sqs = AmazonSQSClientBuilder
                .standard()
                .withRegion(this.region)
                .build();
        try {
            sqs.deleteQueue(queueName);
            synchronized (this) {
                this.wait(60000);
            }
        } catch (QueueDoesNotExistException | InterruptedException e) {

        }

    }

    public void deleteMetadataEntries() {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(this.region)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(metadataTable);

        QuerySpec spec = new QuerySpec()
                .withHashKey("ObjectUUID",objectUUID)
                .withConsistentRead(true);
        ItemCollection<QueryOutcome> items = table.query(spec);

        Iterator<Item> iterator = items.iterator();
        Item item = null;
        while (iterator.hasNext()) {
            item = iterator.next();
            DeleteItemSpec deleteSpec = new DeleteItemSpec()
                    .withPrimaryKey(new PrimaryKey("ObjectUUID",objectUUID,"Timestamp",item.get("Timestamp")));
            table.deleteItem(deleteSpec);
        }

    }

    public void deleteRegistryEntry() {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(this.region)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable(registryTable);

        QuerySpec spec = new QuerySpec()
                .withHashKey("MicroFormat",this.microFormat)
                .withConsistentRead(true);
        ItemCollection<QueryOutcome> items = table.query(spec);

        Iterator<Item> iterator = items.iterator();
        Item item = null;
        while (iterator.hasNext()) {
            item = iterator.next();
            if (!item.get("Choreography").equals(this.queueName)) continue;
            DeleteItemSpec deleteSpec = new DeleteItemSpec()
                    .withPrimaryKey(new PrimaryKey("MicroFormat",item.get("MicroFormat"),"Choreography",item.get("Choreography")));
            table.deleteItem(deleteSpec);
        }

    }
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Regions region = Regions.EU_WEST_2;
        private String objectUUID;
        private String queueName;
        private String microFormat;
        private String metadataTable;
        private String registryTable;
        private String sourceBucket;
        private String targetBucket;

        private Builder() {
        }

        public Builder setRegion(Regions region) {
            this.region = region;
            return this;
        }

        public Builder setObjectUUID(String objectUUID) {
            this.objectUUID = objectUUID;
            return this;
        }

        public Builder setQueueName(String queueName) {
            this.queueName = queueName;
            return this;
        }

        public Builder setMicroFormat(String microFormat) {
            this.microFormat = microFormat;
            return this;
        }

        public Builder setMetadataTable(String metadataTable) {
            this.metadataTable = metadataTable;
            return this;
        }

        public Builder setRegistryTable(String registryTable) {
            this.registryTable = registryTable;
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

        public Builder of(AWSChoreographyTests aWSChoreographyTests) {
            this.region = aWSChoreographyTests.region;
            this.objectUUID = aWSChoreographyTests.objectUUID;
            this.queueName = aWSChoreographyTests.queueName;
            this.microFormat = aWSChoreographyTests.microFormat;
            this.metadataTable = aWSChoreographyTests.metadataTable;
            this.registryTable = aWSChoreographyTests.registryTable;
            this.sourceBucket = aWSChoreographyTests.sourceBucket;
            this.targetBucket = aWSChoreographyTests.targetBucket;
            return this;
        }

        public AWSChoreographyTests build() {
            return new AWSChoreographyTests(this);
        }

    }

}
