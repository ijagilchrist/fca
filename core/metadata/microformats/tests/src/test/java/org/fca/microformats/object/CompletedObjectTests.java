package org.fca.microformats.object;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompletedObjectTests {

    @Test
    public void createObject() {

        String objectForm = "document";

        CompletedObject referenceObject = ObjectTestsHelper.createTestCompletedObject(objectForm);

        CompletedObject testObject = CompletedObject.builder()
                .setMicroFormatUUID(referenceObject.getMicroFormatUUID())
                .setObjectUUID(referenceObject.getObjectUUID())
                .setObjectForm(referenceObject.getObjectForm())
                .build();

        ObjectTestsHelper.checkTestCompletedObject(referenceObject,testObject);

    }

    @Test
    public void createObjectFromIdentifiedObject() {

        String objectForm = "document";
        String objectMimetype = "text/html";

        IdentifiedObject referenceObject = ObjectTestsHelper.createTestIdentifiedObject(objectForm,objectMimetype);

        CompletedObject testObject = CompletedObject.builder()
                .of(referenceObject)
                .build();

        assertEquals(CompletedObject.MICROFORMAT_TYPE,testObject.getMicroFormatType());
        assertEquals(referenceObject.getMicroFormatUUID(),testObject.getMicroFormatUUID());
        assertEquals(referenceObject.getObjectUUID(),testObject.getObjectUUID());
        assertEquals(referenceObject.getObjectForm(),testObject.getObjectForm());

    }

    @Test
    public void createObjectFromUnidentifiedObject() {

        UnidentifiedObject referenceObject = ObjectTestsHelper.createTestUnidentifiedObject();
        CompletedObject testObject = CompletedObject.builder()
                .of(referenceObject)
                .build();

        assertEquals(CompletedObject.MICROFORMAT_TYPE,testObject.getMicroFormatType());
        assertEquals(referenceObject.getMicroFormatUUID(),testObject.getMicroFormatUUID());
        assertEquals(referenceObject.getObjectUUID(),testObject.getObjectUUID());
        assertEquals(CompletedObject.FORM_UNKNOWN,testObject.getObjectForm());

    }

}
