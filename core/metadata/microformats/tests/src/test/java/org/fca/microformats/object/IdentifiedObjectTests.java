package org.fca.microformats.object;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IdentifiedObjectTests {

    @Test
    public void createObject() {

        String objectForm = "document";
        String objectMimetype = "text/html";
        IdentifiedObject referenceObject = ObjectTestsHelper.createTestIdentifiedObject(objectForm,objectMimetype);

        IdentifiedObject testObject = IdentifiedObject.builder()
                .setMicroFormatUUID(referenceObject.getMicroFormatUUID())
                .setObjectUUID(referenceObject.getObjectUUID())
                .setObjectName(referenceObject.getObjectName())
                .setObjectForm(referenceObject.getObjectForm())
                .setObjectMimetype(referenceObject.getObjectMimetype())
                .build();

        ObjectTestsHelper.checkTestIdentifiedObject(referenceObject,testObject);

    }

    @Test
    public void createObjectFromUnidentifiedObject() {

        String objectForm = "document";
        String objectMimetype = "text/html";

        UnidentifiedObject referenceObject = ObjectTestsHelper.createTestUnidentifiedObject();

        IdentifiedObject testObject = IdentifiedObject.builder()
                .of(referenceObject)
                .setObjectForm(objectForm)
                .setObjectMimetype(objectMimetype)
                .build();

        assertEquals(IdentifiedObject.MICROFORMAT_TYPE,testObject.getMicroFormatType());
        assertEquals(referenceObject.getMicroFormatUUID(),testObject.getMicroFormatUUID());
        assertEquals(referenceObject.getObjectUUID(),testObject.getObjectUUID());
        assertEquals(referenceObject.getObjectName(),testObject.getObjectName());
        assertEquals(objectForm,testObject.getObjectForm());
        assertEquals(objectMimetype,testObject.getObjectMimetype());

    }

}
