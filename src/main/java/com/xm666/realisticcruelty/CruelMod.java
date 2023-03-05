package com.xm666.realisticcruelty;

import com.xm666.realisticcruelty.network.ModNetwork;
import com.xm666.realisticcruelty.particle.ModParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(CruelMod.MOD_ID)
public class CruelMod {
    public static final String MOD_ID = "realistic_cruelty";

    public CruelMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CruelConfig.register();
        modEventBus.register(CruelConfig.class);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.register(ModParticle.class);
        }
        ModParticle.REGISTER.register(modEventBus);
        ModNetwork.register();
    }
}
