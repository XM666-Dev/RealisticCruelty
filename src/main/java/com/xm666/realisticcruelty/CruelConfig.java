package com.xm666.realisticcruelty;

import com.xm666.realisticcruelty.event.ClientGoreEvent;
import com.xm666.realisticcruelty.event.GoreEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class CruelConfig {
    public static final ForgeConfigSpec client;
    public static final ForgeConfigSpec common;
    public static final ForgeConfigSpec.ConfigValue<Float> bloodAmount;
    public static final ForgeConfigSpec.ConfigValue<Double> bloodSpeed;
    public static final ForgeConfigSpec.ConfigValue<Double> bloodSpread;
    public static final ForgeConfigSpec.ConfigValue<Boolean> splatEnable;
    public static final ForgeConfigSpec.ConfigValue<Float> fogSize;
    public static final ForgeConfigSpec.ConfigValue<Integer> splashAmount;
    public static final ForgeConfigSpec.ConfigValue<Float> soundVolume;
    public static final ForgeConfigSpec.ConfigValue<Integer> clientMelee;
    public static final ForgeConfigSpec.ConfigValue<Integer> clientTrident;
    public static final ForgeConfigSpec.ConfigValue<Boolean> serverEnable;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("particle");
        builder.push("blood");
        bloodAmount = builder.define("amount", 1.0F);
        bloodSpeed = builder.define("speed", 0.3D);
        bloodSpread = builder.define("spread", 60.0D);
        builder.pop();
        builder.push("splat");
        splatEnable = builder.define("enable", true);
        builder.pop();
        builder.push("fog");
        fogSize = builder.define("size", 0.5F);
        builder.pop();
        builder.push("splash");
        splashAmount = builder.define("amount", 3);
        builder.pop();
        soundVolume = builder.define("soundVolume", 1.0F);
        builder.pop();
        builder.push("clientOnlyGore");
        clientMelee = builder.define("melee", 0);
        clientTrident = builder.define("trident", 0);
        builder.pop();
        client = builder.build();
        builder = new ForgeConfigSpec.Builder();
        serverEnable = builder.define("enable", true);
        common = builder.build();
    }

    public static void register() {
        ModLoadingContext context = ModLoadingContext.get();
        context.registerConfig(ModConfig.Type.CLIENT, client);
        context.registerConfig(ModConfig.Type.COMMON, common);
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        ModConfig config = event.getConfig();
        if (config.getModId().equals(CruelMod.MOD_ID)) {
            switch (config.getType()) {
                case CLIENT -> {
                    if (CruelConfig.clientMelee.get() >= 0) {
                        MinecraftForge.EVENT_BUS.addListener(ClientGoreEvent::onPlayerAttackTarget);
                    }
                    if (CruelConfig.clientTrident.get() >= 0) {
                        MinecraftForge.EVENT_BUS.addListener(ClientGoreEvent::onProjectileImpact);
                    }
                }
                case COMMON -> {
                    if (CruelConfig.serverEnable.get()) {
                        MinecraftForge.EVENT_BUS.register(GoreEvent.class);
                    }
                }
            }
        }
    }
}
