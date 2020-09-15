package org.fca.processors;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.AudioContent;
import org.fca.microformats.content.TextContent;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LanguageSttAudioRuTests {

    @Test
    public void testAudioConversion() throws IOException, InterruptedException {

        String objectUUID = "008-Video-mp4";
        String objectName = "Video-mp4";

        LanguageSttAudioRu testProcessor = new LanguageSttAudioRu();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        String audioUUID = UUID.randomUUID().toString();

        InputStream in = context.getObject(objectName);
        Ffmpeg.Processor convertProcessor = new Ffmpeg(Arrays.asList(new String[]{ "-ar","16000","-f","wav" })).newProcessor();
        context.putDerivedObject(convertProcessor.process(in),audioUUID,new HashMap<>());

        AudioContent testInput = AudioContent.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setAudioType("WAV")
                .setAudioUUID(audioUUID)
                .setLanguage("ru")
                .build();

        List<MicroFormat> testOutput = testProcessor.process(context,testInput);
        assertEquals(1,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TextContent);

        convertProcessor.close();

        context.deleteContext();

    }

}
