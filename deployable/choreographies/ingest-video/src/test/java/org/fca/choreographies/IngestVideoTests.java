package org.fca.choreographies;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.fca.awschoreography.AWSChoreographyTests;
import org.fca.awsprocessor.AWSContext;
import org.fca.forms.VideoForms;
import org.fca.metadata.Metadata;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.IdentifiedObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngestVideoTests {

    @Test
    public void testChoreography() throws InterruptedException {

        String metadataTable = "org.fca.dynamodb.test.metadata";
        String registryTable = "org.fca.dynamodb.test.registry";
        String sourceBucket = "org.fca.s3.test.reception";
        String targetBucket = "org.fca.s3.test.published";

        String microFormat = "object.identified.video";
		
        String queueName = "IngestVideoTest";
        String objectUUID = "008-Video-mp4";
        String objectName = "Video-mp4";
		
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

        IdentifiedObject identifiedObject = IdentifiedObject.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setObjectMimetype("video/mp4")
                .setObjectForm(VideoForms.FORM_VIDEO)
                .build();
        List<MicroFormat> microFormats = new ArrayList<>();
        microFormats.add(identifiedObject);
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

        IngestVideo choreography = new IngestVideo(queueName,awsContextBuilder);
        new Thread(choreography).start();

        AmazonSQS sqs = AmazonSQSClientBuilder
                .standard()
                .withRegion(Regions.EU_WEST_2)
                .build();

        synchronized (this) {
            this.wait(60000);
        }

        sqs.sendMessage(queueName,metadata.toJSON());

        long expiry = getExpiry();

        assertEquals(true,awsChoreographyTests.checkQueue(getTimeout(expiry)),"createQueueFailed");

        assertEquals(true,awsChoreographyTests.checkRegistryEntry(getTimeout(expiry)),"createRegistryEntryFailed");

        assertEquals(true,awsChoreographyTests.checkMetadataEntry(getTimeout(expiry)),"createMetadataEntryFailed");

        assertEquals(2,awsChoreographyTests.countDerivedObjects(2,getTimeout(expiry)),"derivedObjects");

        choreography.stop();

        tidyTest(awsChoreographyTests);

    }

    private long getExpiry() {
        return System.currentTimeMillis() + 600000;
    }

    private long getTimeout(long expiry) {
        return expiry - System.currentTimeMillis();
    }

    private void tidyTest(AWSChoreographyTests awsChoreographyTests) {
        awsChoreographyTests.deleteQueue();
        awsChoreographyTests.deleteMetadataEntries();
        awsChoreographyTests.deleteRegistryEntry();
        awsChoreographyTests.deleteDerivedObjects();
    }

}
