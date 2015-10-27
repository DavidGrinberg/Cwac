package com.cwac.codec;

import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by David on 10/25/2015.
 */
public class MeetingCodec extends DocumentCodec {
    private CodecRegistry codecRegistry;

    public MeetingCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }
}
