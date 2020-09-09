package org.fca.processors;

import org.fca.forms.EMailForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.TextContent;
import org.fca.microformats.metadata.EMailMetadata;
import org.fca.microformats.metadata.TikaMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtractEMailMetadataTest {

    @Test
    public void rfc822Test() throws IOException {

        String objectUUID = "005-EMail-eml";
        String objectName = "EMail-eml";

        IdentifiedObject inputObject = IdentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectForm(EMailForms.FORM_EMAIL)
                .setObjectMimetype("message/rfc822")
                .build();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        ExtractRFC822Metadata testProcessor = new ExtractRFC822Metadata();

        List<MicroFormat> testOutput = testProcessor.process(context,inputObject);

        assertEquals(3,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TikaMetadata);
        assertTrue(testOutput.get(1) instanceof TextContent);
        assertTrue(testOutput.get(2) instanceof EMailMetadata);

        EMailMetadata metadata = (EMailMetadata)testOutput.get(2);

        assertEquals(1,metadata.getAttachments().size(),"attachments");

        context.deleteContext();

    }

    @Test
    public void outlookTest() throws IOException {

        String objectUUID = "006-EMail-msg";
        String objectName = "EMail-msg";

        IdentifiedObject inputObject = IdentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectForm(EMailForms.FORM_EMAIL)
                .setObjectMimetype("application/vnd.ms-outlook")
                .build();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        ExtractOutlookMetadata testProcessor = new ExtractOutlookMetadata();

        List<MicroFormat> testOutput = testProcessor.process(context,inputObject);

        assertEquals(3,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TikaMetadata);
        assertTrue(testOutput.get(1) instanceof TextContent);
        assertTrue(testOutput.get(2) instanceof EMailMetadata);

        EMailMetadata metadata = (EMailMetadata)testOutput.get(2);

        assertEquals(0,metadata.getAttachments().size(),"attachments");

        context.deleteContext();

    }

}
