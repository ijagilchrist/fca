package org.fca.forms;

public class EMailForms extends Forms {

    public static final String FORM_EMAIL = "email";

    public EMailForms() {
        super(Forms.builder()
                .setMimetypeToForm("application/vnd.ms-outlook", FORM_EMAIL,"MSG")
                .setMimetypeToForm("message/rfc822", FORM_EMAIL,"EML")
        );
    }

}
