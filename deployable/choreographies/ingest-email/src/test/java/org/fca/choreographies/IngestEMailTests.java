package org.fca.choreographies;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.fca.awschoreography.AWSChoreographyTests;
import org.fca.awsprocessor.AWSContext;
import org.fca.forms.EMailForms;
import org.fca.metadata.Metadata;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.IdentifiedObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngestEMailTests {

    @Test
    public void testOutlookChoreography() throws InterruptedException {

        String metadataTable = "org.fca.dynamodb.test.metadata";
        String registryTable = "org.fca.dynamodb.test.registry";
        String sourceBucket = "org.fca.s3.test.reception";
        String targetBucket = "org.fca.s3.test.published";

        String microFormat = "object.identified.email";
		
        String queueName = "IngestEMailTest";
        String objectUUID = "006-EMail-msg";
        String objectName = "EMail-msg";
		
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
                .setObjectMimetype("application/outlook")
                .setObjectForm(EMailForms.FORM_EMAIL)
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

        IngestEMail choreography = new IngestEMail(queueName,awsContextBuilder);
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

        choreography.stop();

        tidyTest(awsChoreographyTests);

    }

    @Test
    public void testRFC822Choreography() throws InterruptedException {

        String metadataTable = "org.fca.dynamodb.test.metadata";
        String registryTable = "org.fca.dynamodb.test.registry";
        String sourceBucket = "org.fca.s3.test.reception";
        String targetBucket = "org.fca.s3.test.published";

        String microFormat = "object.identified.email";
		
        String queueName = "IngestEMailTest";
        String objectUUID = "005-EMail-eml";
        String objectName = "EMail-eml";
		
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
                .setObjectMimetype("message/rfc822")
                .setObjectForm(EMailForms.FORM_EMAIL)
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

        IngestEMail choreography = new IngestEMail(queueName,awsContextBuilder);
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
    }

}
