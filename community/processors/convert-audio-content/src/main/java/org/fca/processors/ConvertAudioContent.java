package org.fca.processors;

import org.fca.forms.AudioForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.AudioContent;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ConvertAudioContent implements Processor {

    public ConvertAudioContent() { }

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

            Ffmpeg.Processor processor = new Ffmpeg(Arrays.asList(new String[]{ "-ar","16000","-f","wav" }))
                    .newProcessor();

            try {

                InputStream stream = context.getObject(identifiedObject.getObjectName());

                if (stream != null) {

                    InputStream convertedStream = processor.process(stream);

                    if (convertedStream != null) {

                        String audioUUID = UUID.randomUUID().toString();

                        Map<String,String> headers = new HashMap<>();
                        headers.put("Content-Type","audio/wav");
                        context.putDerivedObject(convertedStream,audioUUID,headers);

                        AudioContent audioContent = AudioContent.builder()
                                .setMicroFormatUUID(UUID.randomUUID().toString())
                                .setAudioType("WAV")
                                .setAudioUUID(audioUUID)
                                .build();
                        updates.add(audioContent);

                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            processor.close();

        }

        return updates;

    }

}
