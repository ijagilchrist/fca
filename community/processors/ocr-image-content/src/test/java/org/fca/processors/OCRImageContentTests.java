package org.fca.processors;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.TextContent;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OCRImageContentTests {

    @Test
    public void testAudioConversion() throws IOException {

        String objectUUID = "007-Image-jpg";
        String objectName = "Image-jpg";
        String form = "image";

        IdentifiedObject testInput = IdentifiedObject.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setObjectForm(form)
                .setObjectMimetype("image/jpeg")
                .build();

        OCRImageContent testProcessor = new OCRImageContent();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        List<MicroFormat> testOutput = testProcessor.process(context,testInput);
        assertEquals(1,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TextContent);

        context.deleteContext();

    }

}
