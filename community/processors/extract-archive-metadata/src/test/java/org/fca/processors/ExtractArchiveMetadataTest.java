package org.fca.processors;

import org.fca.forms.ArchiveForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.metadata.ArchiveMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtractArchiveMetadataTest {

    @Test
    public void archiveTest() throws IOException {

        String objectUUID = "001-Archive-zip";
        String objectName = "Archive-zip";

        IdentifiedObject inputObject = IdentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectForm(ArchiveForms.FORM_ARCHIVE)
                .setObjectMimetype("application/zip")
                .build();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        ExtractZipMetadata testProcessor = new ExtractZipMetadata();

        List<MicroFormat> testOutput = testProcessor.process(context,inputObject);

        assertEquals(1,testOutput.size());
        assertTrue(testOutput.get(0) instanceof ArchiveMetadata);

        context.deleteContext();

    }

}
