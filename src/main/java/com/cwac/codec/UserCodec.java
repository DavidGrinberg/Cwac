package com.cwac.codec;

import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by David on 10/25/2015.
 */
public class UserCodec extends DocumentCodec {
    private CodecRegistry codecRegistry;

    public UserCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }
}