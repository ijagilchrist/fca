package org.fca.processors;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.TextContent;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LanguageTranslationTextRussianTest {

    @Test
    public void frenchLanguageTest() throws IOException {

        String objectUUID = "dummy";

        TextContent textContent = TextContent.builder()
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setValue("Я очень устал спать")
                .setLanguage("ru")
                .build();

        MockContext context = new MockContext(objectUUID);

        LanguageTranslationTextRussian testProcessor = new LanguageTranslationTextRussian();

        List<MicroFormat> testOutput = testProcessor.process(context,textContent);

        assertEquals(1,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TextContent);

        assertEquals("ru",((TextContent)testOutput.get(0)).getLanguage(),"detectedLanguage");
        assertNotNull(((TextContent)testOutput.get(0)).getTranslation(),"translation");

    }

}
