package org.fca.microformats.object;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnidentifiedObjectTests {

    @Test
    public void createObject() {

        UnidentifiedObject referenceObject = ObjectTestsHelper.createTestUnidentifiedObject();

        UnidentifiedObject testObject = UnidentifiedObject.builder()
                .setMicroFormatUUID(referenceObject.getMicroFormatUUID())
                .setObjectUUID(referenceObject.getObjectUUID())
                .setObjectName(referenceObject.getObjectName())
                .build();

        ObjectTestsHelper.checkTestUnidentifiedObject(referenceObject,testObject);

    }

}
