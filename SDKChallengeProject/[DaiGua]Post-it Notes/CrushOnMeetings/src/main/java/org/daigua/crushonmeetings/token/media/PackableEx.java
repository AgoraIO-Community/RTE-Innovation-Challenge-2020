package org.daigua.crushonmeetings.token.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
