package org.fca.choreographies;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.fca.awschoreography.AWSChoreographyTests;
import org.fca.awsprocessor.AWSContext;
import org.fca.forms.DocumentForms;
import org.fca.metadata.Metadata;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.IdentifiedObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngestDocumentTests {

    @Test
    public void testDocxChoreography() throws InterruptedException {

        String metadataTable = "org.fca.dynamodb.test.metadata";
        String registryTable = "org.fca.dynamodb.test.registry";
        String sourceBucket = "org.fca.s3.test.reception";
        String targetBucket = "org.fca.s3.test.published";

        String microFormat = "object.identified.document";
		
        String queueName = "IngestDocumentTest";
        String objectUUID = "003-Document-docx";
        String objectName = "Document-docx";
		
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
                .setObjectMimetype("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                .setObjectForm(DocumentForms.FORM_DOCUMENT)
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

        IngestDocument choreography = new IngestDocument(queueName,awsContextBuilder);
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
    public void testPDFChoreography() throws InterruptedException {

        String metadataTable = "org.fca.dynamodb.test.metadata";
        String registryTable = "org.fca.dynamodb.test.registry";
        String sourceBucket = "org.fca.s3.test.reception";
        String targetBucket = "org.fca.s3.test.published";

        String microFormat = "object.identified.document";
		
        String queueName = "IngestDocumentTest";
        String objectUUID = "004-Document-pdf";
        String objectName = "Document-pdf";
		
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
                .setObjectMimetype("application/pdf")
                .setObjectForm(DocumentForms.FORM_DOCUMENT)
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

        IngestDocument choreography = new IngestDocument(queueName,awsContextBuilder);
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
    public void testHtmlChoreography() throws InterruptedException {

        String metadataTable = "org.fca.dynamodb.test.metadata";
        String registryTable = "org.fca.dynamodb.test.registry";
        String sourceBucket = "org.fca.s3.test.reception";
        String targetBucket = "org.fca.s3.test.published";

        String microFormat = "object.identified.document";
		
        String queueName = "IngestDocumentTest";
        String objectUUID = "009-Document-html";
        String objectName = "Document-html";
		
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
                .setObjectMimetype("text/html")
                .setObjectForm(DocumentForms.FORM_DOCUMENT)
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

        IngestDocument choreography = new IngestDocument(queueName,awsContextBuilder);
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
