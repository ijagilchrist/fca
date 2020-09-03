package org.fca.processors;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.AudioContent;
import org.fca.microformats.content.VideoContent;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConvertVideoContentTests {

    @Test
    public void testVideoConversion() throws IOException {

        String objectUUID = "008-Video-mp4";
        String objectName = "Video-mp4";
        String form = "video";

        IdentifiedObject testInput = IdentifiedObject.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setObjectForm(form)
                .setObjectMimetype("video/mpeg")
                .build();

        ConvertVideoContent testProcessor = new ConvertVideoContent();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        List<MicroFormat> testOutput = testProcessor.process(context,testInput);
        assertEquals(2,testOutput.size());
        assertTrue(testOutput.get(0) instanceof VideoContent);
        assertTrue(testOutput.get(1) instanceof AudioContent);

        context.deleteContext();

    }

}
