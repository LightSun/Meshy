package com.heaven7.java.message.protocol.util;

/**
 * @author heaven7
 */
public final class Version {

    private final int major;
    private final int minor;
    private final int micro;
    private final int update;
    private final String buildNumber;

    //init version like '1.8.0_151-b12'
    public Version(String version){
        try{
            String[] strs = version.split("\\.");
            major = Integer.valueOf(strs[0]);
            minor = Integer.valueOf(strs[1]);
            String left = strs[2];
            int index = left.indexOf("_");
            if(index > 0){
                micro = Integer.valueOf(left.substring(0, index));
                String str = left.substring(index + 1);
                index = str.indexOf("-");
                update = Integer.valueOf(str.substring(0, index));
                buildNumber = str.substring(index + 1);
            }else {
                micro = Integer.valueOf(left);
                update = -1;
                buildNumber = "none";
            }
        }catch (RuntimeException e){
            throw new RuntimeException("wrong version code. must like '1.8.0_151-b12' or '1.8.0' . wrong version = " + version);
        }
    }

    public int getMajor() {
        return major;
    }
    public int getMinor() {
        return minor;
    }
    public int getMicro() {
        return micro;
    }
    public int getUpdate() {
        return update;
    }
    public String getBuildNumber() {
        return buildNumber;
    }
}
