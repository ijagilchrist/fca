package org.fca.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fca.forms.AudioForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.metadata.AudioMetadata;
import org.fca.microformats.metadata.TikaMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtractAudioMetadataTest {

    @Test
    public void audioTest() throws IOException {

        String objectUUID = "002-Audio-mp3";
        String objectName = "Audio-mp3";

        IdentifiedObject inputObject = IdentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .setObjectForm(AudioForms.FORM_AUDIO)
                .setObjectMimetype("audio/mpeg")
                .build();

        MockContext context = new MockContext(objectUUID);
        context.loadObject(objectName);

        ExtractMP3Metadata testProcessor = new ExtractMP3Metadata();

        List<MicroFormat> testOutput = testProcessor.process(context,inputObject);

        assertEquals(2,testOutput.size());
        assertTrue(testOutput.get(0) instanceof TikaMetadata);
        assertTrue(testOutput.get(1) instanceof AudioMetadata);

        context.deleteContext();

    }

}
