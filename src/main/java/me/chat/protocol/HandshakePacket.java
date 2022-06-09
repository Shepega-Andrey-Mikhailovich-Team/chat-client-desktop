package me.chat.protocol;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HandshakePacket extends AbstractPacket {

    String hostname;

    public HandshakePacket() {
        super(0);
    }

    @Override
    protected void read(ByteBuf buf) {
        this.hostname = HandshakePacket.readString(buf);
    }

    @Override
    protected void write(ByteBuf buf) {
        HandshakePacket.writeString(buf, this.hostname);
    }
}

