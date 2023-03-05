package com.xm666.realisticcruelty.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.xm666.realisticcruelty.math.InverseFunction;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class BloodFogParticle extends TextureSheetParticle {
    public static final InverseFunction fadeIn = new InverseFunction(0.2, 0.8, true);
    public static float size;
    public final Quaternion quaternion;

    public BloodFogParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.lifetime = 10;
        this.quadSize = BloodFogParticle.size;
        Quaternion quaternion = Minecraft.getInstance().gameRenderer.getMainCamera().rotation().copy();
        quaternion.mul(Vector3f.ZP.rotation(Mth.TWO_PI * (float) Math.random()));
        this.quaternion = quaternion;
    }

    public float getQuadSize(float tick) {
        float time = this.age + tick;
        float size = (float) BloodFogParticle.fadeIn.f(time / (this.lifetime + 1));
        this.alpha = size < 0.8 ? 1 : (1 - size) / 0.2F;
        return this.quadSize * size;
    }

    public void render(VertexConsumer consumer, Camera camera, float tick) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp(tick, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp(tick, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp(tick, this.zo, this.z) - vec3.z());
        Quaternion quaternion = this.quaternion;

        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getQuadSize(tick);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = vector3fs[i];
            vector3f.transform(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float f7 = this.getU0();
        float f8 = this.getU1();
        float f5 = this.getV0();
        float f6 = this.getV1();
        int j = this.getLightColor(tick);
        consumer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        public BloodFogParticle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
            BloodFogParticle particle = new BloodFogParticle(level, x, y, z);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}
