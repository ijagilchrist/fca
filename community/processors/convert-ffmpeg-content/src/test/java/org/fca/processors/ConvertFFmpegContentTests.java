package org.fca.processors;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConvertFFmpegContentTests {

    @Test
    public void testAudioConversion() throws IOException {

        String objectUUID = "002-Audio-mp3";
        String objectName = "Audio-mp3";
        String form = "audio";
        String[] options = { "-ar","16000","-f","wav" };
        String contentType = "audio/wav";

        IdentifiedObject testInput = IdentifiedObject.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setObjectForm(form)
                .setObjectMimetype("audio/mpeg")
                .build();

        ConvertFFmpegContent testProcessor = new ConvertFFmpegContent(form,options,contentType);

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        List<MicroFormat> testOutput = testProcessor.process(context,testInput);
        assertEquals(1,testOutput.size());
        assertTrue(testOutput.get(0) instanceof ConvertedMedia);

        context.deleteContext();

    }

    @Test
    public void testVideoConversion() throws IOException {

        String objectUUID = "008-Video-mp4";
        String objectName = "Video-mp4";
        String form = "video";
        String[] options = { "-ar","16000","-f","wav" };
        String contentType = "audio/wav";

        IdentifiedObject testInput = IdentifiedObject.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setObjectForm(form)
                .setObjectMimetype("video/mp4")
                .build();

        ConvertFFmpegContent testProcessor = new ConvertFFmpegContent(form,options,contentType);

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        List<MicroFormat> testOutput = testProcessor.process(context,testInput);
        assertEquals(1,testOutput.size());
        assertTrue(testOutput.get(0) instanceof ConvertedMedia);

        context.deleteContext();

    }

}
