package com.cwac.codec;

import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by David on 10/25/2015.
 */
public class CwacCodecProvider implements CodecProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == User.class) {
            return (Codec<T>) new UserCodec(registry);
        }
        if (clazz == Meeting.class) {
            return (Codec<T>) new MeetingCodec(registry);
        }
        return null;
    }
}
