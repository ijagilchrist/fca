package org.fca.forms;

public class ArchiveForms extends Forms {

    public static final String FORM_ARCHIVE = "archive";

    public ArchiveForms() {
        super(Forms.builder()
                .setMimetypeToForm("application/zip", FORM_ARCHIVE,"ZIP")
        );
    }

}
