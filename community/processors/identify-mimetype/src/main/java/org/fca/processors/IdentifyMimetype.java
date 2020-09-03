package org.fca.processors;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.fca.forms.Forms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.microformats.object.UnidentifiedObject;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class IdentifyMimetype implements Processor {

    @Override
    public String getInputMicroFormatType() {
        return UnidentifiedObject.MICROFORMAT_TYPE;
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = new ArrayList<>();
        outputMicroFormatTypes.add(IdentifiedObject.getQualifiedMicroFormatType(Forms.FORM_UNKNOWN));
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        UnidentifiedObject unidentifiedObject =
                (microFormat instanceof UnidentifiedObject) ? (UnidentifiedObject)microFormat : null;
        Objects.requireNonNull(unidentifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        TikaConfig tika = null;
        try {
            tika = new TikaConfig();
            Metadata metadata = new Metadata();

            InputStream stream = context.getObject(unidentifiedObject.getObjectName());
            if (stream != null) {

//                metadata.set(Metadata.RESOURCE_NAME_KEY, context);
                MediaType mediaType = tika.getDetector().detect(
                        TikaInputStream.get(stream),metadata);
                IdentifiedObject identifiedObject = IdentifiedObject.builder()
                        .setObjectUUID(context.getObjectUUID())
                        .setMicroFormatUUID(UUID.randomUUID().toString())
                        .setObjectName(unidentifiedObject.getObjectName())
                        .setObjectForm(Forms.FORM_UNKNOWN)
                        .setObjectMimetype(mediaType.getType()+"/"+mediaType.getSubtype())
                        .build();
                updates.add(identifiedObject);
                stream.close();

            }

        } catch (TikaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return updates;

    }

}
