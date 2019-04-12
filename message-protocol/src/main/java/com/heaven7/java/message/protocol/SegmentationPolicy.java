package com.heaven7.java.message.protocol;

/**
 * the segmentation policy
 * @author heaven7
 */
public interface SegmentationPolicy {

    /**
     * get the segment length, of every segment
     * @return the length of segment
     */
    int getSecureSegmentLength();

}
