package org.fca.processors;

import org.fca.forms.VideoForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.metadata.VideoMetadata;
import org.fca.microformats.metadata.TikaMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtractVideoMetadataTest {

    @Test
    public void videoTest() throws IOException {

        String objectUUID = "008-Video-mp4";
        String objectName = "Video-mp4";

        IdentifiedObject inputObject = IdentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectForm(VideoForms.FORM_VIDEO)
                .setObjectMimetype("video/mp4")
                .build();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        ExtractMP4Metadata testProcessor = new ExtractMP4Metadata();

        List<MicroFormat> testOutput = testProcessor.process(context,inputObject);

        assertEquals(2,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TikaMetadata);
        assertTrue(testOutput.get(1) instanceof VideoMetadata);

        context.deleteContext();

    }

}
