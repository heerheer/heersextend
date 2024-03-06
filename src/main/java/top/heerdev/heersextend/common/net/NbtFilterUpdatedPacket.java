package top.heerdev.heersextend.common.net;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraftforge.network.NetworkEvent;
import top.heerdev.heersextend.common.block.entity.NbtFilterEntity;

import java.util.function.Supplier;

public class NbtFilterUpdatedPacket implements Packet<ServerGamePacketListener> {
    private String message = "";
    private BlockPos pos;


    public NbtFilterUpdatedPacket(FriendlyByteBuf buffer) {
        message = buffer.readUtf();
        pos = buffer.readBlockPos();
    }

    public NbtFilterUpdatedPacket(String message, BlockPos pos) {
        this.message = message;
        this.pos = pos;
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeUtf(this.message);
        pBuffer.writeBlockPos(this.pos);
    }

    @Override
    public void handle(ServerGamePacketListener pHandler) {

    }

    public static void handle(NbtFilterUpdatedPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getSender().level().getBlockEntity(msg.pos) instanceof NbtFilterEntity be){
                be.SetFilterString(msg.message);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
