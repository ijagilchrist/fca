package org.fca.forms;

public class ImageForms extends Forms {

    public static final String FORM_IMAGE = "image";

    public ImageForms() {
        super(Forms.builder()
                .setMimetypeToForm("image/bmp",FORM_IMAGE,"Bitmap")
                .setMimetypeToForm("image/gif",FORM_IMAGE,"GIF")
                .setMimetypeToForm("image/jpeg",FORM_IMAGE,"JPEG")
                .setMimetypeToForm("image/png",FORM_IMAGE,"PNG")
                .setMimetypeToForm("image/tiff",FORM_IMAGE,"TIFF")
        );
    }

}
