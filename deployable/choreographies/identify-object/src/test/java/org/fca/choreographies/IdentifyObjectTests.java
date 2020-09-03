package org.fca.choreographies;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.fca.awschoreography.AWSChoreographyTests;
import org.fca.awsprocessor.AWSContext;
import org.fca.metadata.Metadata;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.UnidentifiedObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IdentifyObjectTests {

    @Test
    public void testChoreography() throws InterruptedException {

        String metadataTable = "org.fca.dynamodb.test.metadata";
        String registryTable = "org.fca.dynamodb.test.registry";
        String sourceBucket = "org.fca.s3.test.reception";
        String targetBucket = "org.fca.s3.test.published";

        String microFormat = "object.unidentified";

        String queueName = "IdentifyObjectTest";
        String objectUUID = "002-Audio-mp3";
        String objectName = "Audio-mp3";

        AWSChoreographyTests awsChoreographyTests = AWSChoreographyTests.builder()
                .setObjectUUID(objectUUID)
                .setQueueName(queueName)
                .setMicroFormat(microFormat)
                .setMetadataTable(metadataTable)
                .setRegistryTable(registryTable)
                .setSourceBucket(sourceBucket)
                .setTargetBucket(targetBucket)
                .build();
        tidyTest(awsChoreographyTests);

        UnidentifiedObject unidentifiedObject = UnidentifiedObject.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .build();
        List<MicroFormat> microFormats = new ArrayList<>();
        microFormats.add(unidentifiedObject);
        Metadata metadata = Metadata.builder()
                .setMetadataUUID(UUID.randomUUID().toString())
                .setParentUUID(objectUUID)
                .setObjectUUID(objectUUID)
                .setTimestamp(System.currentTimeMillis())
                .setSource("UnitTest")
                .setMicroFormats(microFormats)
                .build();

        AWSContext.Builder awsContextBuilder = AWSContext.builder()
                .setMetadataTable(metadataTable)
                .setMetadataTable(registryTable)
                .setSourceBucket(sourceBucket)
                .setTargetBucket(targetBucket);

        IdentifyObject choreography = new IdentifyObject(queueName,awsContextBuilder);
        new Thread(choreography).start();

        AmazonSQS sqs = AmazonSQSClientBuilder
                .standard()
                .withRegion(Regions.EU_WEST_2)
                .build();

        synchronized (this) {
            this.wait(60000);
        }

        sqs.sendMessage(queueName,metadata.toJSON());

        long expiry = System.currentTimeMillis()+600000;

        while (System.currentTimeMillis()<expiry && !awsChoreographyTests.checkQueue()) {
            synchronized (this) {
                this.wait(1000);
            }
        }
        assertEquals(true,awsChoreographyTests.checkQueue(),"createQueueFailed");

        while (System.currentTimeMillis()<expiry && !awsChoreographyTests.checkRegistryEntry()) {
            synchronized (this) {
                this.wait(1000);
            }
        }
        assertEquals(true,awsChoreographyTests.checkRegistryEntry(),"createRegistryEntryFailed");

        while (System.currentTimeMillis()<expiry && !awsChoreographyTests.checkMetadataEntry()) {
            synchronized (this) {
                this.wait(1000);
            }
        }
        assertEquals(true,awsChoreographyTests.checkMetadataEntry(),"createMetadataEntryFailed");

        choreography.stop();

        tidyTest(awsChoreographyTests);

    }

    private void tidyTest(AWSChoreographyTests awsChoreographyTests) {
        awsChoreographyTests.deleteQueue();
        awsChoreographyTests.deleteMetadataEntries();
        awsChoreographyTests.deleteRegistryEntry();
    }

}
