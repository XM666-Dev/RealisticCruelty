package com.xm666.realisticcruelty.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record GorePacket(int entityId, int directId, HitType hitType, double x, double y, double z, double xd,
                         double yd, double zd, float amount) {

    public static void write(GorePacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.entityId);
        buf.writeVarInt(msg.directId);
        buf.writeEnum(msg.hitType);
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
        buf.writeDouble(msg.xd);
        buf.writeDouble(msg.yd);
        buf.writeDouble(msg.zd);
        buf.writeFloat(msg.amount);
    }

    public static GorePacket read(FriendlyByteBuf buf) {
        return new GorePacket(
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readEnum(HitType.class),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readFloat()
        );
    }

    public static void handle(GorePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> HandleGore.handle(msg)));
        ctx.get().setPacketHandled(true);
    }
}
