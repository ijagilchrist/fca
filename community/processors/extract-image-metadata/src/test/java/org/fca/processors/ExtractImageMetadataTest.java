package org.fca.processors;

import org.fca.forms.ImageForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.TextContent;
import org.fca.microformats.metadata.ImageMetadata;
import org.fca.microformats.metadata.TikaMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtractImageMetadataTest {

    @Test
    public void imageTest() throws IOException {

        String objectUUID = "007-Image-jpg";
        String objectName = "Image-jpg";

        IdentifiedObject inputObject = IdentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectForm(ImageForms.FORM_IMAGE)
                .setObjectMimetype("image/jpg")
                .build();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        ExtractJPGMetadata testProcessor = new ExtractJPGMetadata();

        List<MicroFormat> testOutput = testProcessor.process(context,inputObject);

        assertEquals(2,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TikaMetadata);
        assertTrue(testOutput.get(1) instanceof ImageMetadata);

        context.deleteContext();

    }

}
