package org.fca.metadata;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.ObjectTestsHelper;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MetadataTests {

    @Test
    public void createMetadata() {

        String objectUUID = UUID.randomUUID().toString();
        String objectForm = "document";
        String objectMimetype = "text/html";
        List<MicroFormat> referenceMicroformats = new ArrayList<MicroFormat>();
        referenceMicroformats.add(ObjectTestsHelper.createTestUnidentifiedObject());
        referenceMicroformats.add(ObjectTestsHelper.createTestIdentifiedObject(objectForm,objectMimetype));
        referenceMicroformats.add(ObjectTestsHelper.createTestCompletedObject(objectForm));

        Metadata referenceObject = MetadataTestsHelper.createTestMetadata(objectUUID,referenceMicroformats);

        Metadata testObject = Metadata.builder()
                .setObjectUUID(objectUUID)
                .setTimestamp(referenceObject.getTimestamp())
                .setSource(referenceObject.getSource())
                .setParentUUID(referenceObject.getParentUUID())
                .setMetadataUUID(referenceObject.getMetadataUUID())
                .setMicroFormats(referenceObject.getMicroFormats())
                .build();

        MetadataTestsHelper.checkTestMetadata(referenceObject,testObject);

    }

    @Test
    public void createMetadataRoundTripToJSON() {

        String objectUUID = UUID.randomUUID().toString();
        String objectForm = "document";
        String objectMimetype = "text/html";
        List<MicroFormat> referenceMicroformats = new ArrayList<MicroFormat>();
        referenceMicroformats.add(ObjectTestsHelper.createTestUnidentifiedObject());
        referenceMicroformats.add(ObjectTestsHelper.createTestIdentifiedObject(objectForm,objectMimetype));
        referenceMicroformats.add(ObjectTestsHelper.createTestCompletedObject(objectForm));

        Metadata referenceObject = MetadataTestsHelper.createTestMetadata(objectUUID,referenceMicroformats);

        String json = Metadata.builder()
                .setObjectUUID(objectUUID)
                .setTimestamp(referenceObject.getTimestamp())
                .setSource(referenceObject.getSource())
                .setParentUUID(referenceObject.getParentUUID())
                .setMetadataUUID(referenceObject.getMetadataUUID())
                .setMicroFormats(referenceObject.getMicroFormats())
                .build()
                .toJSON();
        assertNotNull(json);

        Metadata testObject = Metadata.builder()
                .fromJSON(json)
                .build();
        assertNotNull(testObject);

        MetadataTestsHelper.checkTestMetadata(referenceObject,testObject);

    }

}
