package com.xm666.realisticcruelty.particle;

import com.xm666.realisticcruelty.CruelConfig;
import com.xm666.realisticcruelty.math.InverseFunction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

public class BloodSplashParticle extends TextureSheetParticle {
    public static final InverseFunction fadeIn = new InverseFunction(0.9, 0.8, true);
    public static final InverseFunction fadeOut = new InverseFunction(0.9, 0.2, false);

    public BloodSplashParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.lifetime = 20;
        this.quadSize = 0.05F;
        this.gravity = 1.0F;
        this.xd = (Math.random() * 2 - 1) * 0.15D;
        this.yd = Math.random() * 0.2D + 0.1D;
        this.zd = (Math.random() * 2 - 1) * 0.15D;
    }

    public void tick() {
        super.tick();
        if (this.onGround) {
            int i = this.lifetime + 1 - 5;
            if (this.age < i) {
                this.age = i;
                float f = Mth.randomBetween(this.random, 0.3F, 1.0F) * CruelConfig.splashAmount.get();
                SoundEvent soundevent = SoundEvents.BEEHIVE_DRIP;
                this.level.playLocalSound(this.x, this.y, this.z, soundevent, SoundSource.BLOCKS, f, 1.0F, false);
            }
        }
    }

    public float getQuadSize(float tick) {
        float time = this.age + tick;
        float[] size = {this.quadSize};
        Process.f(time, 5, this.lifetime + 1 - 5, 5,
                (f) -> size[0] *= fadeIn.f(f),
                () -> {
                },
                (f) -> this.alpha = (float) fadeOut.f(f)
        );
        return size[0];
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        public BloodSplashParticle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            BloodSplashParticle particle = new BloodSplashParticle(level, x, y, z);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}
