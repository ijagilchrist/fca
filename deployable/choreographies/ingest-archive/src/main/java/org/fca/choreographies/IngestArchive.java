package org.fca.choreographies;

import org.fca.awschoreography.AWSChoreography;
import org.fca.awsprocessor.AWSContext;
import org.fca.choreography.Choreography;
import org.fca.processors.ExtractZipMetadata;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class IngestArchive implements Runnable {

    private static final String METADATA_TABLE = "org.fca.dynamodb.test.metadata";
    private static final String REGISTRY_TABLE = "org.fca.dynamodb.test.registry";
    private static final String QUEUE_NAME = "IngestArchive";

    private String queueName;
    private AWSContext.Builder awsContextBuilder;
    private AWSChoreography awsChoreography;

    public static void main(String args[]) {
        new IngestArchive().run();
    }

    public IngestArchive() {
        this.queueName = QUEUE_NAME;
        this.awsContextBuilder = AWSContext.builder();
    }

    public IngestArchive(String queueName, AWSContext.Builder awsContextBuilder) {
        this.queueName = queueName;
        this.awsContextBuilder = awsContextBuilder;
    }
    @Override
    public void run() {

        Choreography choreography = Choreography.builder()
                .setInitialProcessor(new ExtractZipMetadata())
                .build();

        this.awsChoreography = AWSChoreography.builder()
                .setQueueName(this.queueName)
                .setAWSContextBuilder(this.awsContextBuilder)
                .setMetadataTable(METADATA_TABLE)
                .setRegistryTable(REGISTRY_TABLE)
                .setChoreography(choreography)
                .build();

        awsChoreography.run();

    }

    public void stop() {
        this.awsChoreography.stop();
    }

}
