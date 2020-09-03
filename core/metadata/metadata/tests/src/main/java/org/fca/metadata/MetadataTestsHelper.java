package org.fca.metadata;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.MicroFormats;
import org.fca.microformats.object.CompletedObject;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.microformats.object.ObjectTestsHelper;
import org.fca.microformats.object.UnidentifiedObject;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MetadataTestsHelper {

    public static Metadata createTestMetadata(String objectUUID, List<MicroFormat> microFormats) {

        Long timestamp = System.currentTimeMillis();
        String metadataUUID = UUID.randomUUID().toString();
        String parentUUID = UUID.randomUUID().toString();
        String source = UUID.randomUUID().toString();

        Metadata testObject = Metadata.builder()
                .setObjectUUID(objectUUID)
                .setTimestamp(timestamp)
                .setMetadataUUID(metadataUUID)
                .setParentUUID(parentUUID)
                .setSource(source)
                .setMicroFormats(microFormats)
                .build();

        assertEquals(objectUUID, testObject.getObjectUUID());
        assertEquals(timestamp, testObject.getTimestamp());
        assertEquals(metadataUUID, testObject.getMetadataUUID());
        assertEquals(parentUUID, testObject.getParentUUID());
        assertEquals(source, testObject.getSource());
        assertEquals(microFormats, testObject.getMicroFormats());

        return testObject;

    }

    public static void checkTestMetadata(Metadata referenceObject, Metadata testObject) {

        assertEquals(referenceObject.getObjectUUID(), testObject.getObjectUUID());
        assertEquals(referenceObject.getTimestamp(), testObject.getTimestamp());
        assertEquals(referenceObject.getMetadataUUID(), testObject.getMetadataUUID());
        assertEquals(referenceObject.getParentUUID(), testObject.getParentUUID());
        assertEquals(referenceObject.getSource(), testObject.getSource());
        assertEquals(referenceObject.getMicroFormats().size(), testObject.getMicroFormats().size());

        Iterator<MicroFormat> itReference = referenceObject.getMicroFormats().iterator();
        Iterator<MicroFormat> itTest = testObject.getMicroFormats().iterator();
        while (itReference.hasNext() && itTest.hasNext()) {
            MetadataTestsHelper.checkTestMicroFormat(itReference.next(), itTest.next());
        }

    }

    public static MetadataCollection createTestMetadataCollection(List<Metadata> metadata) {

        MetadataCollection metadataCollection = MetadataCollection.builder()
                .setMetadata(metadata)
                .build();

        return metadataCollection;

    }

    public static void checkTestMetadataCollection(MetadataCollection referenceObject, MetadataCollection testObject) {

        assertEquals(referenceObject.getMetadata().size(), testObject.getMetadata().size());

        Iterator<Metadata> itReference = referenceObject.getMetadata().iterator();
        Iterator<Metadata> itTest = testObject.getMetadata().iterator();
        while (itReference.hasNext() && itTest.hasNext()) {
            MetadataTestsHelper.checkTestMetadata(itReference.next(), itTest.next());
        }

    }

    private static void checkTestMicroFormat(MicroFormat reference, MicroFormat test) {
        if (reference instanceof UnidentifiedObject && test instanceof UnidentifiedObject) {
            ObjectTestsHelper.checkTestUnidentifiedObject((UnidentifiedObject)reference,(UnidentifiedObject)test);
        } else if (reference instanceof IdentifiedObject && test instanceof IdentifiedObject) {
            ObjectTestsHelper.checkTestIdentifiedObject((IdentifiedObject)reference,(IdentifiedObject)test);
        } else if (reference instanceof CompletedObject && test instanceof CompletedObject) {
            ObjectTestsHelper.checkTestCompletedObject((CompletedObject)reference,(CompletedObject)test);
        } else {
            fail();
        }
    }

}