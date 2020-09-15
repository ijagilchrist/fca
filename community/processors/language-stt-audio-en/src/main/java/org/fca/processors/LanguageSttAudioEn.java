package org.fca.processors;

import org.fca.forms.AudioForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.AudioContent;
import org.fca.microformats.content.TextContent;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LanguageSttAudioEn implements Processor {

    private final static String sphinxdata = System.getenv("SPHINXDATA");
    private final static String acousticModel = new File(sphinxdata,"models/en/acoustic").getAbsolutePath();
    private final static String dictionary = new File(sphinxdata,"models/en/dictionary/en-us.dic").getAbsolutePath();
    private final static String languageModel = new File(sphinxdata,"models/en/language/en-us.lm.bin").getAbsolutePath();

    public LanguageSttAudioEn() { }

    @Override
    public String getInputMicroFormatType() {
        return AudioContent.qualifiedMicroFormatType("en");
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = new ArrayList<>();
        outputMicroFormatTypes.add(TextContent.qualifiedMicroFormatType("en"));
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        AudioContent audioContent =
                (microFormat instanceof AudioContent) ? (AudioContent)microFormat : null;
        Objects.requireNonNull(audioContent);

        List<MicroFormat> updates = new ArrayList<>();

		// Select first 30 secs of audio for test
        Ffmpeg.Processor ffmpegProcessor = new Ffmpeg(Arrays.asList(new String[]{ "-ar","16000","-ac","1","-to","00:00:30","-f","wav" }))
                .newProcessor();

        try {

            InputStream stream = context.getDerivedObject(audioContent.getAudioUUID());

            if (stream != null) {

                InputStream convertedStream = ffmpegProcessor.process(stream);

                if (convertedStream != null) {

                    Sphinx.Processor sphinxProcessor = new Sphinx(Arrays.asList(new String[]{}),acousticModel,dictionary,languageModel)
                            .newProcessor();

                    InputStream outputStream = sphinxProcessor.process(convertedStream);

                    if (outputStream != null) {

                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                        outputStream.transferTo(buffer);

                        TextContent textContent = TextContent.builder()
                                .setMicroFormatUUID(UUID.randomUUID().toString())
                                .setValue(buffer.toString())
                                .setLanguage("en")
                                .build();
                        updates.add(textContent);

                    }

                    sphinxProcessor.close();

                }

                ffmpegProcessor.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return updates;

    }

}
