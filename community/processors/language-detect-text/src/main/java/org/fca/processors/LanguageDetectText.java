package org.fca.processors;

import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.TextContent;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import java.util.*;

//        af Afrikaans
//        an Aragonese
//        ar Arabic
//        ast Asturian
//        be Belarusian
//        br Breton
//        ca Catalan
//        bg Bulgarian
//        bn Bengali
//        cs Czech
//        cy Welsh
//        da Danish
//        de German
//        el Greek
//        en English
//        es Spanish
//        et Estonian
//        eu Basque
//        fa Persian
//        fi Finnish
//        fr French
//        ga Irish
//        gl Galician
//        gu Gujarati
//        he Hebrew
//        hi Hindi
//        hr Croatian
//        ht Haitian
//        hu Hungarian
//        id Indonesian
//        is Icelandic
//        it Italian
//        ja Japanese
//        km Khmer
//        kn Kannada
//        ko Korean
//        lt Lithuanian
//        lv Latvian
//        mk Macedonian
//        ml Malayalam
//        mr Marathi
//        ms Malay
//        mt Maltese
//        ne Nepali
//        nl Dutch
//        no Norwegian
//        oc Occitan
//        pa Punjabi
//        pl Polish
//        pt Portuguese
//        ro Romanian
//        ru Russian
//        sk Slovak
//        sl Slovene
//        so Somali
//        sq Albanian
//        sr Serbian
//        sv Swedish
//        sw Swahili
//        ta Tamil
//        te Telugu
//        th Thai
//        tl Tagalog
//        tr Turkish
//        uk Ukrainian
//        ur Urdu
//        vi Vietnamese
//        wa Walloon
//        yi Yiddish
//        zh-cn Simplified Chinese
//        zh-tw Traditional Chinese

public class LanguageDetectText implements Processor {

    private static String[] supportedLanguages = { "en", "fr", "ru" };

    private OptimaizeLangDetector detector;

    public LanguageDetectText() {
        this.detector = new OptimaizeLangDetector();
        detector.loadModels();
    }

    @Override
    public String getInputMicroFormatType() {
        return TextContent.MICROFORMAT_TYPE;
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = new ArrayList<>();
        outputMicroFormatTypes.add(TextContent.qualifiedMicroFormatType("unknown"));
        for (String language: supportedLanguages) {
            outputMicroFormatTypes.add(TextContent.qualifiedMicroFormatType(language));
        }
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        TextContent textContent =
                (microFormat instanceof TextContent) ? (TextContent)microFormat : null;
        Objects.requireNonNull(textContent);

        List<MicroFormat> updates = new ArrayList<>();

        if (textContent.getLanguage() == null) {

            LanguageResult result = this.detector.detect(textContent.getValue());
            String language = (result.isReasonablyCertain()) ? result.getLanguage() : "unknown";

            textContent = TextContent.builder()
                        .of(textContent)
                        .setLanguage(language)
                        .build();
            updates.add(textContent);

        }

        return updates;

    }

}
