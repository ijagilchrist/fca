package org.fca.microformats.object;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectTestsHelper {

    public static UnidentifiedObject createTestUnidentifiedObject() {

        String microFormatUUID = UUID.randomUUID().toString();
        String objectUUID = UUID.randomUUID().toString();
        String objectName = UUID.randomUUID().toString();

        UnidentifiedObject testObject = UnidentifiedObject.builder()
                .setMicroFormatUUID(microFormatUUID)
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .build();

        assertEquals(UnidentifiedObject.MICROFORMAT_TYPE,testObject.getMicroFormatType());
        assertEquals(microFormatUUID,testObject.getMicroFormatUUID());
        assertEquals(objectUUID,testObject.getObjectUUID());
        assertEquals(objectName,testObject.getObjectName());

        return testObject;

    }

    public static void checkTestUnidentifiedObject(UnidentifiedObject referenceObject, UnidentifiedObject testObject) {

        assertEquals(UnidentifiedObject.MICROFORMAT_TYPE,testObject.getMicroFormatType());
        assertEquals(referenceObject.getMicroFormatUUID(),testObject.getMicroFormatUUID());
        assertEquals(referenceObject.getObjectUUID(),testObject.getObjectUUID());
        assertEquals(referenceObject.getObjectName(),testObject.getObjectName());

    }

    public static IdentifiedObject createTestIdentifiedObject(String objectForm, String objectMimetype) {

        String microFormatUUID = UUID.randomUUID().toString();
        String objectUUID = UUID.randomUUID().toString();
        String objectName = UUID.randomUUID().toString();

        IdentifiedObject testObject = IdentifiedObject.builder()
                .setMicroFormatUUID(microFormatUUID)
                .setObjectUUID(objectUUID)
                .setObjectName(objectName)
                .setObjectForm(objectForm)
                .setObjectMimetype(objectMimetype)
                .build();

        assertEquals(IdentifiedObject.MICROFORMAT_TYPE,testObject.getMicroFormatType());
        assertEquals(microFormatUUID,testObject.getMicroFormatUUID());
        assertEquals(objectUUID,testObject.getObjectUUID());
        assertEquals(objectName,testObject.getObjectName());
        assertEquals(objectForm,testObject.getObjectForm());
        assertEquals(objectMimetype,testObject.getObjectMimetype());

        return testObject;

    }

    public static void checkTestIdentifiedObject(IdentifiedObject referenceObject, IdentifiedObject testObject) {

        assertEquals(IdentifiedObject.MICROFORMAT_TYPE,testObject.getMicroFormatType());
        assertEquals(referenceObject.getMicroFormatUUID(),testObject.getMicroFormatUUID());
        assertEquals(referenceObject.getObjectUUID(),testObject.getObjectUUID());
        assertEquals(referenceObject.getObjectName(),testObject.getObjectName());
        assertEquals(referenceObject.getObjectForm(),testObject.getObjectForm());
        assertEquals(referenceObject.getObjectMimetype(),testObject.getObjectMimetype());

    }

    public static CompletedObject createTestCompletedObject(String objectForm) {

        String microFormatUUID = UUID.randomUUID().toString();
        String objectUUID = UUID.randomUUID().toString();

        CompletedObject testObject = CompletedObject.builder()
                .setMicroFormatUUID(microFormatUUID)
                .setObjectUUID(objectUUID)
                .setObjectForm(objectForm)
                .build();

        assertEquals(CompletedObject.MICROFORMAT_TYPE,testObject.getMicroFormatType());
        assertEquals(microFormatUUID,testObject.getMicroFormatUUID());
        assertEquals(objectUUID,testObject.getObjectUUID());
        assertEquals(objectForm,testObject.getObjectForm());

        return testObject;

    }

    public static void checkTestCompletedObject(CompletedObject referenceObject, CompletedObject testObject) {

        assertEquals(CompletedObject.MICROFORMAT_TYPE,testObject.getMicroFormatType());
        assertEquals(referenceObject.getMicroFormatUUID(),testObject.getMicroFormatUUID());
        assertEquals(referenceObject.getObjectUUID(),testObject.getObjectUUID());
        assertEquals(referenceObject.getObjectForm(),testObject.getObjectForm());

    }

}
