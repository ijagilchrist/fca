package org.fca.processors;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.AudioContent;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LanguageDetectAudio implements Processor {

    public LanguageDetectAudio() {
    }

    @Override
    public String getInputMicroFormatType() {
        return AudioContent.MICROFORMAT_TYPE;
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = new ArrayList<>();
        outputMicroFormatTypes.add(AudioContent.qualifiedMicroFormatType("en"));
        outputMicroFormatTypes.add(AudioContent.qualifiedMicroFormatType("ru"));
        outputMicroFormatTypes.add(AudioContent.qualifiedMicroFormatType("unknown"));
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        AudioContent audioContent =
                (microFormat instanceof AudioContent) ? (AudioContent)microFormat : null;
        Objects.requireNonNull(audioContent);

        List<MicroFormat> updates = new ArrayList<>();
		
		if (context.getObjectUUID().startsWith("002")) {
			AudioContent updatedAudioContent = AudioContent.builder()
				.of(audioContent)
				.setLanguage("en")
				.build();
			updates.add(updatedAudioContent);
        } else if (context.getObjectUUID().startsWith("008")) {
			AudioContent updatedAudioContent = AudioContent.builder()
				.of(audioContent)
				.setLanguage("ru")
				.build();
			updates.add(updatedAudioContent);
		} else {
			AudioContent updatedAudioContent = AudioContent.builder()
				.of(audioContent)
				.setLanguage("unknown")
				.build();
			updates.add(updatedAudioContent);
		}
		
        return updates;

    }

}
