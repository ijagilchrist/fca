package org.fca.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fca.forms.Forms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.MicroFormats;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.microformats.object.ObjectTestsHelper;
import org.fca.microformats.object.UnidentifiedObject;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IdentifyMimetypeTest {

    @Test
    public void imageTest() throws IOException {

        String objectUUID = "007-Image-jpg";
        String objectName = "Image-jpg";

        UnidentifiedObject inputObject = UnidentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setMicroFormatUUID(UUID.randomUUID().toString())
                .build();

        MockContext context = new MockContext(objectUUID);
		context.loadObject(objectName);

        IdentifyMimetype testProcessor = new IdentifyMimetype();

        List<MicroFormat> testOutput = testProcessor.process(context,inputObject);

        assertEquals(1,testOutput.size());
        assertTrue(testOutput.get(0) instanceof IdentifiedObject);

        IdentifiedObject testObject = (IdentifiedObject)testOutput.get(0);

        IdentifiedObject referenceObject = IdentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setObjectForm(Forms.FORM_UNKNOWN)
                .setObjectMimetype("image/jpeg")
                .setMicroFormatUUID(testObject.getMicroFormatUUID())
                .build();

        ObjectTestsHelper.checkTestIdentifiedObject(referenceObject,testObject);

		context.deleteContext();
		
    }

}
