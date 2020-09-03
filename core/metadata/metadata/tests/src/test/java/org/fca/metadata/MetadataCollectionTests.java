package org.fca.metadata;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.CompletedObject;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.microformats.object.ObjectTestsHelper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MetadataCollectionTests {

    @Test
    public void createMetadataCollection() {

        String objectUUID = UUID.randomUUID().toString();
        List<Metadata> referenceMetadata = new ArrayList<Metadata>();

        {
            String objectForm = "document";
            String objectMimetype = "text/html";
            List<MicroFormat> referenceMicroformats = new ArrayList<MicroFormat>();
            referenceMicroformats.add(ObjectTestsHelper.createTestUnidentifiedObject());
            referenceMicroformats.add(ObjectTestsHelper.createTestIdentifiedObject(objectForm,objectMimetype));
            referenceMicroformats.add(ObjectTestsHelper.createTestCompletedObject(objectForm));
            referenceMetadata.add(MetadataTestsHelper.createTestMetadata(objectUUID,referenceMicroformats));
        }

        {
            String objectForm = "email";
            String objectMimetype = "message/rfc822";
            List<MicroFormat> referenceMicroformats = new ArrayList<MicroFormat>();
            referenceMicroformats.add(ObjectTestsHelper.createTestUnidentifiedObject());
            referenceMicroformats.add(ObjectTestsHelper.createTestIdentifiedObject(objectForm,objectMimetype));
            referenceMicroformats.add(ObjectTestsHelper.createTestCompletedObject(objectForm));
            referenceMetadata.add(MetadataTestsHelper.createTestMetadata(objectUUID,referenceMicroformats));
        }

        MetadataCollection referenceObject = MetadataCollection.builder()
                .setMetadata(referenceMetadata)
                .build();

        MetadataCollection testObject = MetadataCollection.builder()
                .setMetadata(referenceObject.getMetadata())
                .build();

        MetadataTestsHelper.checkTestMetadataCollection(referenceObject,testObject);

    }

    @Test
    public void createMetadataRoundTripToJSON() {

        String objectUUID = UUID.randomUUID().toString();
        List<Metadata> referenceMetadata = new ArrayList<Metadata>();

        {
            String objectForm = "document";
            String objectMimetype = "text/html";
            List<MicroFormat> referenceMicroformats = new ArrayList<MicroFormat>();
            referenceMicroformats.add(ObjectTestsHelper.createTestUnidentifiedObject());
            referenceMicroformats.add(ObjectTestsHelper.createTestIdentifiedObject(objectForm,objectMimetype));
            referenceMicroformats.add(ObjectTestsHelper.createTestCompletedObject(objectForm));
            referenceMetadata.add(MetadataTestsHelper.createTestMetadata(objectUUID,referenceMicroformats));
        }

        {
            String objectForm = "email";
            String objectMimetype = "message/rfc822";
            List<MicroFormat> referenceMicroformats = new ArrayList<MicroFormat>();
            referenceMicroformats.add(ObjectTestsHelper.createTestUnidentifiedObject());
            referenceMicroformats.add(ObjectTestsHelper.createTestIdentifiedObject(objectForm,objectMimetype));
            referenceMicroformats.add(ObjectTestsHelper.createTestCompletedObject(objectForm));
            referenceMetadata.add(MetadataTestsHelper.createTestMetadata(objectUUID,referenceMicroformats));
        }

        MetadataCollection referenceObject = MetadataCollection.builder()
                .setMetadata(referenceMetadata)
                .build();

        String json = MetadataCollection.builder()
                .setMetadata(referenceObject.getMetadata())
                .build()
                .toJSON();
        assertNotNull(json);

        MetadataCollection testObject = MetadataCollection.builder()
                .fromJSON(json)
                .build();
        assertNotNull(testObject);

        MetadataTestsHelper.checkTestMetadataCollection(referenceObject,testObject);

    }

}
