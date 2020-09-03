package org.fca.processors;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.fca.forms.ArchiveForms;
import org.fca.forms.AudioForms;
import org.fca.forms.DocumentForms;
import org.fca.forms.EMailForms;
import org.fca.forms.Forms;
import org.fca.forms.ImageForms;
import org.fca.forms.VideoForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.microformats.object.UnidentifiedObject;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class IdentifyForm implements Processor {

    private final Set<Forms> forms;

    public IdentifyForm() {
        this.forms = new HashSet<>();
        this.forms.add(new ArchiveForms());
        this.forms.add(new AudioForms());
        this.forms.add(new DocumentForms());
        this.forms.add(new EMailForms());
        this.forms.add(new ImageForms());
        this.forms.add(new VideoForms());
    }

    @Override
    public String getInputMicroFormatType() {
        return IdentifiedObject.getQualifiedMicroFormatType(Forms.FORM_UNKNOWN);
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = new ArrayList<>();
        outputMicroFormatTypes.add(IdentifiedObject.getQualifiedMicroFormatType(ArchiveForms.FORM_ARCHIVE));
        outputMicroFormatTypes.add(IdentifiedObject.getQualifiedMicroFormatType(AudioForms.FORM_AUDIO));
        outputMicroFormatTypes.add(IdentifiedObject.getQualifiedMicroFormatType(DocumentForms.FORM_DOCUMENT));
        outputMicroFormatTypes.add(IdentifiedObject.getQualifiedMicroFormatType(EMailForms.FORM_EMAIL));
        outputMicroFormatTypes.add(IdentifiedObject.getQualifiedMicroFormatType(ImageForms.FORM_IMAGE));
        outputMicroFormatTypes.add(IdentifiedObject.getQualifiedMicroFormatType(VideoForms.FORM_VIDEO));
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        IdentifiedObject identifiedObject =
                (microFormat instanceof IdentifiedObject) ? (IdentifiedObject)microFormat : null;
        Objects.requireNonNull(identifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        if (identifiedObject.getObjectForm().equals(Forms.FORM_UNKNOWN)) {

            String mimetype = identifiedObject.getObjectMimetype();

            for (Forms form: this.forms) {
                if (form.isKnown(mimetype)) {
                    identifiedObject = IdentifiedObject.builder()
                            .of(identifiedObject)
                            .setObjectForm(form.getForm(mimetype))
                            .build();
                    updates.add(identifiedObject);
                }
            }

        }

        return updates;

    }

}
