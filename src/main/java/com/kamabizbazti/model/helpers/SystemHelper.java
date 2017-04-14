package com.kamabizbazti.model.helpers;

import com.kamabizbazti.model.entities.external.EVersion;
import com.kamabizbazti.model.interfaces.IDateHelper;
import com.kamabizbazti.model.interfaces.ISystemHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemHelper implements ISystemHelper {

    @Autowired
    private IDateHelper dateHelper;

    private final String buildProperties = "build.properties";

    private EVersion version;

    public EVersion getVersion() {
        if (version == null) {
            Properties properties = getProperties(buildProperties);
            String copyRightYears = getCopyrightYears(properties.getProperty("build_initiate_release_date"));
            version = new EVersion();
            version.setName(properties.getProperty("build_name"));
            version.setDescription(properties.getProperty("build_description"));
            version.setVersion(properties.getProperty("build_version") + "." + properties.getProperty("build_number"));
            version.setTimestamp(properties.getProperty("build_timestamp"));
            version.setCopyrights(properties.getProperty("build_author") + " © " + copyRightYears);
        }
        return version;
    }

    private Properties getProperties(String fileName) {
        Properties properties = new Properties();
        InputStream stream = null;
        try {
            stream = getClass().getClassLoader().getResourceAsStream(fileName);
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }

    private String getCopyrightYears(String initiateYear) {
        String currentYear = dateHelper.getYear(System.currentTimeMillis());
        if (currentYear.equals(initiateYear)) {
            return initiateYear;
        } else
            return initiateYear + "-" + currentYear;
    }
}
