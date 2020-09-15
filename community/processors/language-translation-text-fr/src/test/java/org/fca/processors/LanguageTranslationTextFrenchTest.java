package org.fca.processors;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.TextContent;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LanguageTranslationTextFrenchTest {

    @Test
    public void frenchLanguageTest() throws IOException {

        String objectUUID = "dummy";

        TextContent textContent = TextContent.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setValue("Je suis fatigue")
                .setLanguage("fr")
                .build();

        MockContext context = new MockContext(objectUUID);

        LanguageTranslationTextFrench testProcessor = new LanguageTranslationTextFrench();

        List<MicroFormat> testOutput = testProcessor.process(context,textContent);

        assertEquals(1,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TextContent);

        assertEquals("fr",((TextContent)testOutput.get(0)).getLanguage(),"detectedLanguage");
        assertNotNull(((TextContent)testOutput.get(0)).getTranslation(),"translation");

    }

}
