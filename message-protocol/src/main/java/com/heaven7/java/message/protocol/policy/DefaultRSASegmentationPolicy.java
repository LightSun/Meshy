package com.heaven7.java.message.protocol.policy;

import com.heaven7.java.message.protocol.SegmentationPolicy;

/**
 * @author heaven7
 */
public class DefaultRSASegmentationPolicy implements SegmentationPolicy{

    private int secureSegmentLength;

    public DefaultRSASegmentationPolicy(int secureSegmentLength) {
        this.secureSegmentLength = secureSegmentLength;
    }

    //512 -> 53
    //1024 -> 117
    @Override
    public int getSecureSegmentLength() {
        return secureSegmentLength;
    }
}
