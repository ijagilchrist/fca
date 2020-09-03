package org.fca.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fca.forms.Forms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.microformats.object.ObjectTestsHelper;
import org.fca.mockprocessor.MockContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IdentifyFormTest {

    @Test
    public void imageTest() {

        String objectUUID = "007-Image-jpg";
        String objectName = "Image-jpg";
        String microFormatUUID = UUID.randomUUID().toString();

        IdentifiedObject inputObject = IdentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setMicroFormatUUID(microFormatUUID)
                .setObjectMimetype("image/jpeg")
                .setObjectForm(Forms.FORM_UNKNOWN)
                .build();

        MockContext context = new MockContext(objectUUID);

        IdentifyForm testProcessor = new IdentifyForm();

        List<MicroFormat> testOutput = testProcessor.process(context,inputObject);

        assertEquals(1,testOutput.size());
        assertTrue(testOutput.get(0) instanceof IdentifiedObject);

        IdentifiedObject testObject = (IdentifiedObject)testOutput.get(0);

        IdentifiedObject referenceObject = IdentifiedObject.builder()
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setObjectForm("image")
                .setObjectMimetype("image/jpeg")
                .setMicroFormatUUID(microFormatUUID)
                .build();

        ObjectTestsHelper.checkTestIdentifiedObject(referenceObject,testObject);

    }

}
