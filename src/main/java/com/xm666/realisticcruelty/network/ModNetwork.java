package com.xm666.realisticcruelty.network;

import com.xm666.realisticcruelty.CruelMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class ModNetwork {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CruelMod.MOD_ID),
            () -> "",
            (string) -> true,
            (string) -> true
    );

    public static void register() {
        CHANNEL.registerMessage(0, GorePacket.class, GorePacket::write, GorePacket::read, GorePacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
