package org.fca.choreographies;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.fca.awschoreography.AWSChoreographyTests;
import org.fca.awsprocessor.AWSContext;
import org.fca.metadata.Metadata;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.AudioContent;
import org.fca.processors.Ffmpeg;
import org.fca.processors.Ffmpeg.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AudioSTTTests {

    @Test
    public void testChoreography() throws InterruptedException, IOException {

        String metadataTable = "org.fca.dynamodb.test.metadata";
        String registryTable = "org.fca.dynamodb.test.registry";
        String sourceBucket = "org.fca.s3.test.reception";
        String targetBucket = "org.fca.s3.test.published";

        String microFormat = "content.audio";
		
        String queueName = "AudioSTT";
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

        String audioUUID = UUID.randomUUID().toString();

        AudioContent audioContent = AudioContent.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setAudioUUID(audioUUID)
                .setAudioType("WAV")
                .build();
        List<MicroFormat> microFormats = new ArrayList<>();
        microFormats.add(audioContent);
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

        AWSContext awsContext = awsContextBuilder
                .of(awsContextBuilder.setObjectUUID(objectUUID).build())
                .build();

        Ffmpeg.Processor processor = new Ffmpeg(Arrays.asList(new String[]{ "-ar","16000","-f","wav" }))
                .newProcessor();
        awsContext.putDerivedObject(processor.process(awsContext.getObject(objectName)),audioUUID,new HashMap<>());

        AudioSTT choreography = new AudioSTT(queueName,awsContextBuilder);
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

//        tidyTest(awsChoreographyTests);

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
