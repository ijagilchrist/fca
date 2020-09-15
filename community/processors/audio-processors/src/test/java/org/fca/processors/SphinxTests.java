package org.fca.processors;

import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SphinxTests {

    @Test
    public void testAudioTranscription() throws IOException, InterruptedException {

        String sphinxdata = System.getenv("SPHINXDATA");
        String acousticModel = new File(sphinxdata,"models/en/acoustic").getAbsolutePath();
        String dictionary = new File(sphinxdata,"models/en/dictionary/en-us.dic").getAbsolutePath();
        String languageModel = new File(sphinxdata,"models/en/language/en-us.lm.bin").getAbsolutePath();

        String objectUUID = "002-Audio-mp3";
        String objectName = "Audio-mp3";
        List<String> ffmpegOptions = Arrays.asList(new String[] {"-ar", "16000", "-ac", "1", "-f", "wav"});
        List<String> sphinxOptions = Arrays.asList(new String[] {});

        Ffmpeg.Processor ffmpegProcessor = new Ffmpeg(ffmpegOptions).newProcessor();

        Sphinx.Processor testProcessor = new Sphinx(sphinxOptions,acousticModel,dictionary,languageModel).newProcessor();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        InputStream input = context.getObject(objectName);
        InputStream ffmpegOutput = ffmpegProcessor.process(input);

        InputStream output = testProcessor.process(ffmpegOutput);

        assertNotNull(output,"output");
        input.close();
        ffmpegOutput.close();
        output.close();

        ffmpegProcessor.close();
//        testProcessor.close();
        context.deleteContext();

    }

//    @Test
//    public void testVideoConversion() throws IOException, InterruptedException {
//
//        String objectUUID = "008-Video-mp4";
//        String objectName = "Video-mp4";
//        List<String> options = Arrays.asList(new String[] {"-ar", "16000", "-ac", "1", "-f", "wav"});
//
//        Ffmpeg.Processor testProcessor = new Ffmpeg(options).newProcessor();
//
//        MockContext context = new MockContext(objectUUID);
//        context.loadObject(objectName);
//
//        InputStream input = context.getObject(objectName);
//        InputStream output = testProcessor.process(input);;
//
//        assertNotNull(output,"output");
//        input.close();
//        output.close();
//
//        testProcessor.close();
//        context.deleteContext();
//
//    }

}
