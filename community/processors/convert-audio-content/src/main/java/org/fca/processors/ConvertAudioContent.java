package org.fca.processors;

import org.fca.forms.AudioForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.AudioContent;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;

import java.util.*;

public class ConvertAudioContent extends ConvertFFmpegContent {

    public ConvertAudioContent() {
        super(AudioForms.FORM_AUDIO,new String[]{ "-ar","16000","-f","wav" },"audio/wav");
    }

    @Override
    public String getInputMicroFormatType() {
        return IdentifiedObject.getQualifiedMicroFormatType(AudioForms.FORM_AUDIO);
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = new ArrayList<>();
        outputMicroFormatTypes.add(AudioContent.MICROFORMAT_TYPE);
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        IdentifiedObject identifiedObject =
                (microFormat instanceof IdentifiedObject) ? (IdentifiedObject)microFormat : null;
        Objects.requireNonNull(identifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        if (identifiedObject.getObjectForm().equals(AudioForms.FORM_AUDIO)) {

            List<MicroFormat> conversion = super.process(context,identifiedObject);

            if (conversion.size() == 1 && conversion.get(0) instanceof ConvertedMedia) {

                AudioContent audioContent = AudioContent.builder()
                        .setMicroFormatUUID(UUID.randomUUID().toString())
                        .setAudioType("WAV")
                        .setAudioUUID(((ConvertedMedia)conversion.get(0)).getMediaUUID())
                        .build();
                updates.add(audioContent);

            }

        }

        return updates;

    }

}
