package com.heaven7.java.message.protocol;

/**
 * the segmentation policy.
 * this is used for {@linkplain MessageSecure}.
 * @see MessageSecureWrapper
 * @author heaven7
 */
public interface SegmentationPolicy {

    /**
     * get the secure segment length of every segment. often return the max length.
     * @return the length of segment
     */
    int getSecureSegmentLength();

}
