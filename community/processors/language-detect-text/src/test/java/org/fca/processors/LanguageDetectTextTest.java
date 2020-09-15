package org.fca.processors;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.TextContent;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LanguageDetectTextTest {

    @Test
    public void frenchLanguageTest() throws IOException {

        String objectUUID = "007-Image-jpg";

        TextContent textContent = TextContent.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setValue("Je suis fatigue")
                .build();

        MockContext context = new MockContext(objectUUID);

        LanguageDetectText testProcessor = new LanguageDetectText();

        List<MicroFormat> testOutput = testProcessor.process(context,textContent);

        assertEquals(1,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TextContent);

        assertEquals("fr",((TextContent)testOutput.get(0)).getLanguage(),"detectedLanguage");

    }

    @Test
    public void englishLanguageTest() throws IOException {

        String objectUUID = "007-Image-jpg";

        TextContent textContent = TextContent.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setValue("I am tired of reading")
                .build();

        MockContext context = new MockContext(objectUUID);

        LanguageDetectText testProcessor = new LanguageDetectText();

        List<MicroFormat> testOutput = testProcessor.process(context,textContent);

        assertEquals(1,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TextContent);

        assertEquals("en",((TextContent)testOutput.get(0)).getLanguage(),"detectedLanguage");

    }

}
