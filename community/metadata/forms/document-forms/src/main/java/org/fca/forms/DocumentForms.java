package org.fca.forms;

public class DocumentForms extends Forms {

    public static final String FORM_DOCUMENT = "document";

    public DocumentForms() {
        super(Forms.builder()
                .setMimetypeToForm("application/ms-word.document.macroEnabled.12", FORM_DOCUMENT,"DOC")
                .setMimetypeToForm("application/ms-word.template.macroEnabled.12", FORM_DOCUMENT,"DOT")
                .setMimetypeToForm("application/vnd.openxmlformats-officedocument.wordprocessingml.document", FORM_DOCUMENT,"DOCX")
                .setMimetypeToForm("application/vnd.openxmlformats-officedocument.wordprocessingml.template", FORM_DOCUMENT,"DOTX")
                .setMimetypeToForm("application/pdf", FORM_DOCUMENT,"PDF")
                .setMimetypeToForm("text/html", FORM_DOCUMENT,"HTML")
        );
    }

}
