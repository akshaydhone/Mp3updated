package com.google.appinventor.common.version;

public final class GitBuildId {
    public static final String ACRA_URI = "@acra.uri@";
    public static final String ANT_BUILD_DATE = "February 23 2019";
    public static final String GIT_BUILD_FINGERPRINT = "cf76352df9f9db2fd652c84d49aa85d94d162cc9";
    public static final String GIT_BUILD_VERSION = "fatal: No names found, cannot describe anything.";

    private GitBuildId() {
    }

    public static String getVersion() {
        String version = GIT_BUILD_VERSION;
        if (version == "" || version.contains(" ")) {
            return "none";
        }
        return version;
    }

    public static String getFingerprint() {
        return GIT_BUILD_FINGERPRINT;
    }

    public static String getDate() {
        return ANT_BUILD_DATE;
    }

    public static String getAcraUri() {
        if (ACRA_URI.equals("${acra.uri}")) {
            return "";
        }
        return ACRA_URI.trim();
    }
}
