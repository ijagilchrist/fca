package org.fca.processors;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.AudioContent;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LanguageDetectAudioTests {

    @Test
    public void testAudioDetection() throws IOException {

        String objectUUID = "002-Audio-mp3";
        String objectName = "Audio-mp3";
        String form = "audio";

        String audioUUID = UUID.randomUUID().toString();

        AudioContent testInput = AudioContent.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setAudioUUID(audioUUID)
                .setAudioType("WAV")
                .build();

        LanguageDetectAudio testProcessor = new LanguageDetectAudio();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        List<MicroFormat> testOutput = testProcessor.process(context,testInput);
        assertEquals(1,testOutput.size());
        assertTrue(testOutput.get(0) instanceof AudioContent);

        context.deleteContext();

    }

}
