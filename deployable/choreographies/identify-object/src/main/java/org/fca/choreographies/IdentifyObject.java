package org.fca.choreographies;

import org.fca.awschoreography.AWSChoreography;
import org.fca.awsprocessor.AWSContext;
import org.fca.choreography.Choreography;
import org.fca.processors.IdentifyForm;
import org.fca.processors.IdentifyMimetype;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class IdentifyObject implements Runnable {

    private String queueName;
    private AWSContext.Builder awsContextBuilder;
    private AWSChoreography awsChoreography;

    public static void main(String args[]) {
        new IdentifyObject().run();
    }

    public IdentifyObject() {
        this.queueName = "IdentifyObject";
        this.awsContextBuilder = AWSContext.builder();
    }

    public IdentifyObject(String queueName, AWSContext.Builder awsContextBuilder) {
        this.queueName = queueName;
        this.awsContextBuilder = awsContextBuilder;
    }
    @Override
    public void run() {

        Choreography choreography = Choreography.builder()
                .setInitialProcessor(new IdentifyMimetype())
                .setProcessor(new IdentifyForm())
                .build();

        this.awsChoreography = AWSChoreography.builder()
                .setQueueName(this.queueName)
                .setAWSContextBuilder(this.awsContextBuilder)
                .setMetadataTable("org.fca.dynamodb.test.metadata")
                .setRegistryTable("org.fca.dynamodb.test.registry")
                .setChoreography(choreography)
                .build();

        awsChoreography.run();

    }

    public void stop() {
        this.awsChoreography.stop();
    }

}
