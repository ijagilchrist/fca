package org.fca.processors;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.TextContent;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import java.util.*;

public abstract class LanguageTranslationText implements Processor {

    private String language;

    public LanguageTranslationText(String language) {
        this.language = Objects.requireNonNull(language,"language");
    }

    @Override
    public String getInputMicroFormatType() {
        return TextContent.qualifiedMicroFormatType(this.language);
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = new ArrayList<>();
        outputMicroFormatTypes.add(TextContent.qualifiedMicroFormatType("unknown"));
        outputMicroFormatTypes.add(TextContent.qualifiedMicroFormatType(this.language));
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        TextContent textContent =
                (microFormat instanceof TextContent) ? (TextContent)microFormat : null;
        Objects.requireNonNull(textContent);

        List<MicroFormat> updates = new ArrayList<>();

        if (textContent.getLanguage().equals(this.language)) {

            String translation = this.translate(textContent.getValue(),this.language);

            if (translation != null) {

                textContent = TextContent.builder()
                        .of(textContent)
                        .setTranslation(translation)
                        .build();
                updates.add(textContent);

            } else {

                textContent = TextContent.builder()
                        .of(textContent)
                        .setLanguage("unknown")
                        .build();
                updates.add(textContent);

            }

        }

        return updates;

    }

    protected abstract String translate(String value, String language);

}
