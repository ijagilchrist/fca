package org.fca.processors;

import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FfmpegTests {

    @Test
    public void testAudioConversion() throws IOException, InterruptedException {

        String objectUUID = "002-Audio-mp3";
        String objectName = "Audio-mp3";
        List<String> options = Arrays.asList(new String[] {"-ar", "16000", "-ac", "1", "-f", "wav"});

        Ffmpeg.Processor testProcessor = new Ffmpeg(options).newProcessor();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        InputStream input = context.getObject(objectName);
        InputStream output = testProcessor.process(input);;

        assertNotNull(output,"output");
        input.close();
        output.close();

        testProcessor.close();
        context.deleteContext();

    }

    @Test
    public void testVideoConversion() throws IOException, InterruptedException {

        String objectUUID = "008-Video-mp4";
        String objectName = "Video-mp4";
        List<String> options = Arrays.asList(new String[] {"-ar", "16000", "-ac", "1", "-f", "wav"});

        Ffmpeg.Processor testProcessor = new Ffmpeg(options).newProcessor();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        InputStream input = context.getObject(objectName);
        InputStream output = testProcessor.process(input);;

        assertNotNull(output,"output");
        input.close();
        output.close();

        testProcessor.close();
        context.deleteContext();

    }

}
