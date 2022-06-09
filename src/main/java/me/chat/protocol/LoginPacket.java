package me.chat.protocol;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginPacket extends AbstractPacket {

    private String userName, reason;
    private boolean allowed;

    public LoginPacket() {
        super(1);
    }

    @Override
    protected void read(ByteBuf byteBuf) {
        this.userName = HandshakePacket.readString(byteBuf);
        this.allowed = byteBuf.readBoolean();
        this.reason = HandshakePacket.readString(byteBuf);
    }

    @Override
    protected void write(ByteBuf byteBuf) {
        HandshakePacket.writeString(byteBuf, this.userName);
        byteBuf.writeBoolean(this.allowed);
        HandshakePacket.writeString(byteBuf, this.reason);
    }
}
