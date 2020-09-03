package org.fca.awschoreography;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import org.fca.awsprocessor.AWSContext;
import org.fca.choreography.Choreography;
import org.fca.metadata.Metadata;
import org.fca.microformats.MicroFormat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class AWSChoreography implements Runnable {

    @Nonnull
    private final AWSContext.Builder awsContextBuilder;

    @Nonnull
    private final Regions region;

    @Nonnull
    private final String metadataTable;

    @Nonnull
    private final String registryTable;

    @Nonnull
    private final String queueName;

    @Nonnull
    private final Choreography choreography;

    private boolean stop;

    private AWSChoreography(Builder builder) {
        this.awsContextBuilder = Objects.requireNonNull(builder.awsContextBuilder,"awsContextBuilder");
        this.region = Objects.requireNonNull(builder.region,"region");
        this.metadataTable = Objects.requireNonNull(builder.metadataTable,"metadataTable");
        this.registryTable = Objects.requireNonNull(builder.registryTable,"registryTable");
        this.queueName = Objects.requireNonNull(builder.queueName, "queueName");
        this.choreography = Objects.requireNonNull(builder.choreography, "choreography");
    }

    @Override
    public void run() {

        this.createQueue(this.queueName);
        this.registerChoreography(this.registryTable,this.choreography.getInputMicroFormatType(),this.queueName);

        this.stop = false;
        while (!this.stop) {

            List<String> messages = this.receiveMetadata(this.queueName);

            for (String message: messages) {

                Metadata inputMetadata = Metadata.builder()
                        .fromJSON(message)
                        .build();
                Objects.requireNonNull(inputMetadata);

                String objectUUID = inputMetadata.getObjectUUID();
                AWSContext awsContext = this.awsContextBuilder
                        .setObjectUUID(objectUUID)
                        .build();

                for (MicroFormat inputMicroFormat: inputMetadata.getMicroFormats()) {
                    List<MicroFormat> outputMicroformats = this.choreography.process(awsContext,inputMicroFormat);
                    Metadata outputMetadata = Metadata.builder()
                            .setObjectUUID(objectUUID)
                            .setMetadataUUID(UUID.randomUUID().toString())
                            .setParentUUID(inputMetadata.getMetadataUUID())
                            .setTimestamp(System.currentTimeMillis())
                            .setMicroFormats(outputMicroformats)
                            .setSource(this.queueName)
                            .build();
                    this.postOutput(this.metadataTable,outputMetadata);
                }

            }

        }

    }

    public void stop() { this.stop = true; }

    private void createQueue(String queueName) {

        AmazonSQS sqs = AmazonSQSClientBuilder
                .standard()
                .withRegion(region)
                .build();

        if (sqs.listQueues(queueName).getQueueUrls().size() == 0) {
            CreateQueueRequest request = new CreateQueueRequest()
                    .withQueueName(queueName)
                    .addAttributesEntry("ReceiveMessageWaitTimeSeconds", "20");

            try {
                sqs.createQueue(request);
            } catch (AmazonSQSException e) {
                if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                    throw e;
                }
            }
        }

    }

    private void registerChoreography(String registryTable, String inputMicroFormatType, String queueName) {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(region)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable(registryTable);

        Item item = new Item();
        item.withPrimaryKey(new PrimaryKey("MicroFormat",inputMicroFormatType,"Choreography",queueName));

        table.putItem(item);

    }

    private List<String> receiveMetadata(String queueName) {

        AmazonSQS sqs = AmazonSQSClientBuilder
                .standard()
                .withRegion(region)
                .build();

        List<String> messages = new ArrayList<String>();

        for (Message message: sqs.receiveMessage(queueName).getMessages()) {

            sqs.deleteMessage(queueName, message.getReceiptHandle());

            messages.add(message.getBody());

        }

        return messages;

    }

    private void postOutput(String metadataTable, Metadata metadata) {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(region)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable(metadataTable);

        Item item = new Item();
        item.withPrimaryKey(new PrimaryKey("ObjectUUID",metadata.getObjectUUID(),"Timestamp",metadata.getTimestamp()));
        item.with("Metadata",metadata.toJSON());

        table.putItem(item);

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private AWSContext.Builder awsContextBuilder = AWSContext.builder();
        private Regions region = Regions.EU_WEST_2;
        private String metadataTable = "org.fca.dynamodb.metadata";
        private String registryTable = "org.fca.dynamodb.registry";
        private String queueName;
        private Choreography choreography;

        private Builder() {
        }

        public Builder setAWSContextBuilder(AWSContext.Builder awsContextBuilder) {
            this.awsContextBuilder = awsContextBuilder;
            return this;
        }

        public Builder setRegion(Regions region) {
            this.region = region;
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

        public Builder setQueueName(String queueName) {
            this.queueName = queueName;
            return this;
        }

        public Builder setChoreography(Choreography choreography) {
            this.choreography = choreography;
            return this;
        }

        public Builder of(AWSChoreography awsChoreography) {
            this.queueName = awsChoreography.queueName;
            this.choreography = awsChoreography.choreography;
            return this;
        }

        public AWSChoreography build() {
            return new AWSChoreography(this);
        }

    }

}
