package org.fca.forms;

public class VideoForms extends Forms {

    public static final String FORM_VIDEO = "video";

    public VideoForms() {
        super(Forms.builder()
                .setMimetypeToForm("video/mp4", FORM_VIDEO,"MP4")
                .setMimetypeToForm("video/quicktime", FORM_VIDEO,"MP4")
        );
    }

}
