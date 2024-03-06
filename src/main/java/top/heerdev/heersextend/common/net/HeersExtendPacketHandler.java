package top.heerdev.heersextend.common.net;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import top.heerdev.heersextend.HeersExtend;

public class HeersExtendPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(HeersExtend.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        INSTANCE.messageBuilder(NbtFilterUpdatedPacket.class,0)
                .encoder(NbtFilterUpdatedPacket::write)
                .decoder(NbtFilterUpdatedPacket::new)
                .consumerMainThread(NbtFilterUpdatedPacket::handle)
                .add();
    }
}
