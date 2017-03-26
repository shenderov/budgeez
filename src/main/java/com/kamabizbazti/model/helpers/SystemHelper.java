package com.kamabizbazti.model.helpers;

import com.kamabizbazti.model.entities.EVersion;
import com.kamabizbazti.model.interfaces.ISystemHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class SystemHelper implements ISystemHelper {

    private EVersion version;

    public EVersion getVersion() {
        if (version == null) {
            Manifest mf = new Manifest();
            try {
                InputStream mfStream = getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
                mf.read(mfStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Attributes attributes = mf.getMainAttributes();
            String ver = attributes.getValue(Attributes.Name.SPECIFICATION_VERSION) + "." + attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            version = new EVersion();
            version.setVersion(ver);
            version.setBuildDate(attributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE));
        }
        return version;
    }
}
