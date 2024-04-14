package com.tugalsan.api.servlet.url.server;

import com.tugalsan.api.file.json.server.TS_FileJsonUtils;
import com.tugalsan.api.file.server.TS_DirectoryUtils;
import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.file.txt.server.TS_FileTxtUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.Properties;

public class TS_SURLConfig implements Serializable {

    final private static TS_Log d = TS_Log.of(TS_SURLConfig.class);
    final private static boolean DEFAULT_ENABLE_TIMEOUT = false;

    private TS_SURLConfig() {//DTO
    }

    private TS_SURLConfig(boolean enableTimeout) {
        this.enableTimeout = enableTimeout;
    }
    public boolean enableTimeout;

    public static TS_SURLConfig of() {
        return new TS_SURLConfig(DEFAULT_ENABLE_TIMEOUT);
    }

    public static TS_SURLConfig of(boolean enableTimeout) {
        return new TS_SURLConfig(enableTimeout);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TS_SURLConfig other = (TS_SURLConfig) obj;
        return this.enableTimeout == other.enableTimeout;
    }

    @Override
    public String toString() {
        return TS_SURLConfig.class.getSimpleName() + "{" + "enableTimeout=" + enableTimeout + '}';
    }

    public Properties toProps() {
        var prop = new Properties();
        prop.put("enableTimeout", enableTimeout);
        return prop;
    }

    public void loadProps(Properties prop) {
        enableTimeout = (Boolean) prop.getOrDefault("enableTimeout", enableTimeout);
    }

    public static TGS_UnionExcuse<TS_SURLConfig> of(Path dir, String appName) {
        var u_createDirectoriesIfNotExists = TS_DirectoryUtils.createDirectoriesIfNotExists(dir);
        if (u_createDirectoriesIfNotExists.isExcuse()) {
            return u_createDirectoriesIfNotExists.toExcuse();
        }
        var filePath = dir.resolve(TS_SURLConfig.class.getSimpleName() + "." + appName + ".json");
        d.cr("of", filePath);

        String jsonString;
        if (!TS_FileUtils.isExistFile(filePath)) {
            u_createDirectoriesIfNotExists = TS_DirectoryUtils.createDirectoriesIfNotExists(filePath.getParent());
            if (u_createDirectoriesIfNotExists.isExcuse()) {
                return u_createDirectoriesIfNotExists.toExcuse();
            }
            var tmp = TS_SURLConfig.of();
            var u_jsonString = TS_FileJsonUtils.toJSON(tmp, true);
            if (u_jsonString.isExcuse()) {
                return u_jsonString.toExcuse();
            }
            jsonString = u_jsonString.value();
            var u_toFile = TS_FileJsonUtils.toFile(jsonString, filePath, false, true);
            if (u_toFile.isExcuse()) {
                return u_toFile.toExcuse();
            }
        }

        var u_toFile = TS_FileTxtUtils.toString(filePath);
        if (u_toFile.isExcuse()) {
            d.ct("of", u_toFile.excuse());
            d.ce("of", "Correcting by writing default file!");
            var tmp = TS_SURLConfig.of();
            var u_jsonString = TS_FileJsonUtils.toJSON(tmp, true);
            if (u_jsonString.isExcuse()) {
                return u_jsonString.toExcuse();
            }
            jsonString = u_jsonString.value();
            TS_FileTxtUtils.toFile(jsonString, filePath, false);
        } else {
            jsonString = u_toFile.value();
        }

        d.ci("of", jsonString);
        return TS_FileJsonUtils.toObject(jsonString, TS_SURLConfig.class);
    }
}
