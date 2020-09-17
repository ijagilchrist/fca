package org.fca.choreographies;

import org.fca.awschoreography.AWSChoreography;
import org.fca.awsprocessor.AWSContext;
import org.fca.choreography.Choreography;
import org.fca.processors.LanguageDetectAudio;
import org.fca.processors.LanguageSttAudioEn;
import org.fca.processors.LanguageSttAudioRu;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AudioSTT implements Runnable {

    private static final String METADATA_TABLE = "org.fca.dynamodb.test.metadata";
    private static final String REGISTRY_TABLE = "org.fca.dynamodb.test.registry";
    private static final String QUEUE_NAME = "AudioSTT";

    private String queueName;
    private AWSContext.Builder awsContextBuilder;
    private AWSChoreography awsChoreography;

    public static void main(String args[]) {
        new AudioSTT().run();
    }

    public AudioSTT() {
        this.queueName = QUEUE_NAME;
        this.awsContextBuilder = AWSContext.builder();
    }

    public AudioSTT(String queueName, AWSContext.Builder awsContextBuilder) {
        this.queueName = queueName;
        this.awsContextBuilder = awsContextBuilder;
    }

    @Override
    public void run() {

        Choreography choreography = Choreography.builder()
                .setInitialProcessor(new LanguageDetectAudio())
                .setProcessor(new LanguageSttAudioEn())
                .setProcessor(new LanguageSttAudioRu())
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
