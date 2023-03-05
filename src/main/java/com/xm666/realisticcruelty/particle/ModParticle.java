package com.xm666.realisticcruelty.particle;

import com.xm666.realisticcruelty.CruelMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticle {
    public static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CruelMod.MOD_ID);
    public static final RegistryObject<SimpleParticleType> BLOOD = REGISTER.register("blood", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLOOD_SPLAT = REGISTER.register("blood_splat", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLOOD_FOG = REGISTER.register("blood_fog", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLOOD_SPLASH = REGISTER.register("blood_splash", () -> new SimpleParticleType(true));

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.register(ModParticle.BLOOD.get(), BloodParticle.Provider::new);
        event.register(ModParticle.BLOOD_SPLAT.get(), BloodSplatParticle.Provider::new);
        event.register(ModParticle.BLOOD_FOG.get(), BloodFogParticle.Provider::new);
        event.register(ModParticle.BLOOD_SPLASH.get(), BloodSplashParticle.Provider::new);
    }
}
