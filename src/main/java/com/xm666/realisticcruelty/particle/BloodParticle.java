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

public class BloodParticle extends TextureSheetParticle {
    public static final InverseFunction fadeIn = new InverseFunction(0.9, 0.8, true);
    public static final InverseFunction fadeOut = new InverseFunction(0.9, 0.2, false);

    public BloodParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.lifetime = 60;
        this.gravity = 1.5F;
        this.friction = 0.95F;
        this.quadSize = 0.1F;
    }

    public void tick() {
        super.tick();
        if (this.onGround) {
            int i = this.lifetime + 1 - 10;
            if (this.age < i) {
                this.age = i;
                if (CruelConfig.splatEnable.get()) {
                    this.level.addParticle(ModParticle.BLOOD_SPLAT.get(), this.x, this.y + 0.1D, this.z, 0.0D, 0.0D, 0.0D);
                }
                for (int j = CruelConfig.splashAmount.get(); j-- > 0; ) {
                    this.level.addParticle(ModParticle.BLOOD_SPLASH.get(), this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
                }
                float f = Mth.randomBetween(this.random, 0.3F, 1.0F) * CruelConfig.soundVolume.get();
                SoundEvent soundevent = SoundEvents.BEEHIVE_DRIP;
                this.level.playLocalSound(this.x, this.y, this.z, soundevent, SoundSource.BLOCKS, f, 1.0F, false);
            }
        }
    }

    public float getQuadSize(float tick) {
        float time = this.age + tick;
        float[] size = {this.quadSize};
        Process.f(time, 5, this.lifetime + 1 - 10, 10,
                (f) -> this.alpha = (float) fadeIn.f(f),
                () -> this.alpha = 1.0F,
                (f) -> size[0] *= fadeOut.f(f)
        );
        return size[0];
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        public BloodParticle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            BloodParticle particle = new BloodParticle(level, x, y, z);
            particle.pickSprite(this.sprites);
            particle.setParticleSpeed(xd, yd, zd);
            return particle;
        }
    }
}