package org.fca.processors;

import org.fca.forms.DocumentForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.metadata.DocumentMetadata;
import org.fca.microformats.metadata.TikaMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtractDocumentMetadataTest {

    @Test
    public void mswordTest() throws IOException {

        String objectUUID = "003-Document-docx";
        String objectName = "Document-docx";

        IdentifiedObject inputObject = IdentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectForm(DocumentForms.FORM_DOCUMENT)
                .setObjectMimetype("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                .build();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        ExtractMSWordMetadata testProcessor = new ExtractMSWordMetadata();

        List<MicroFormat> testOutput = testProcessor.process(context,inputObject);

        assertEquals(2,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TikaMetadata);
        assertTrue(testOutput.get(1) instanceof DocumentMetadata);

        DocumentMetadata metadata = (DocumentMetadata)testOutput.get(1);

        assertEquals(1,metadata.getEmbedded().size(),"embedded");

        context.deleteContext();

    }

    @Test
    public void pdfTest() throws IOException {

        String objectUUID = "004-Document-pdf";
        String objectName = "Document-pdf";

        IdentifiedObject inputObject = IdentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectForm(DocumentForms.FORM_DOCUMENT)
                .setObjectMimetype("application/pdf")
                .build();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        ExtractPDFMetadata testProcessor = new ExtractPDFMetadata();

        List<MicroFormat> testOutput = testProcessor.process(context,inputObject);

        assertEquals(2,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TikaMetadata);
        assertTrue(testOutput.get(1) instanceof DocumentMetadata);

        DocumentMetadata metadata = (DocumentMetadata)testOutput.get(1);

        assertEquals(0,metadata.getEmbedded().size(),"embedded");

        context.deleteContext();

    }

    @Test
    public void htmlTest() throws IOException {

        String objectUUID = "009-Document-html";
        String objectName = "Document-html";

        IdentifiedObject inputObject = IdentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectForm(DocumentForms.FORM_DOCUMENT)
                .setObjectMimetype("text/html")
                .build();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        ExtractHTMLMetadata testProcessor = new ExtractHTMLMetadata();

        List<MicroFormat> testOutput = testProcessor.process(context,inputObject);

        assertEquals(2,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TikaMetadata);
        assertTrue(testOutput.get(1) instanceof DocumentMetadata);

        DocumentMetadata metadata = (DocumentMetadata)testOutput.get(1);

        assertEquals(0,metadata.getEmbedded().size(),"embedded");

        context.deleteContext();

    }

}
