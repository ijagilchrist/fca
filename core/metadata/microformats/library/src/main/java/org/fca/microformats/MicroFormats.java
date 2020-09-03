package org.fca.microformats;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@javax.annotation.ParametersAreNonnullByDefault
public class MicroFormats {

    @javax.annotation.Nonnull
    private static final Map<String,Class<? extends MicroFormat>> microFormats;

    static {
        microFormats = Collections.synchronizedMap(new HashMap<String,Class<? extends MicroFormat>>());
    }

    public static void register(String microFormatType, Class<? extends MicroFormat> pojoClass) {
        MicroFormats.microFormats.put(microFormatType,pojoClass);
    }

    public static Class<? extends MicroFormat> find(String microFormatType) {
        return microFormats.get(microFormatType);
    }

}
