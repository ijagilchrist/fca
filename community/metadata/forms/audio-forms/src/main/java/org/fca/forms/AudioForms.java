package org.fca.forms;

public class AudioForms extends Forms {

    public static final String FORM_AUDIO = "audio";

    public AudioForms() {
        super(Forms.builder()
                .setMimetypeToForm("audio/mpeg", FORM_AUDIO,"MP3")
        );
    }

}
