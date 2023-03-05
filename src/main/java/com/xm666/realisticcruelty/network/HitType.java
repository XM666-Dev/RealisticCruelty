package com.xm666.realisticcruelty.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.util.thread.EffectiveSide;

public enum HitType {
    MELEE {
        public Vec3 getPosition(Entity entity) {
            return entity.getEyePosition(HitType.getPartialTick());
        }

        public Vec3 getRotation(Entity entity) {
            return entity.getViewVector(HitType.getPartialTick());
        }

        public HitArg getHitArg(AABB aabb, HitArg hitArg) {
            return HitType.getHitArgRay(aabb, hitArg);
        }
    },
    INDIRECT {
        public Vec3 getPosition(Entity entity) {
            return entity.getPosition(HitType.getPartialTick());
        }

        public Vec3 getRotation(Entity entity) {
            return entity.getDeltaMovement().normalize();
        }

        public HitArg getHitArg(AABB aabb, HitArg hitArg) {
            return HitType.getHitArgRay(aabb, hitArg);
        }
    },
    EXPLODE {
        public Vec3 getPosition(Entity entity) {
            return entity.getPosition(HitType.getPartialTick());
        }

        public Vec3 getRotation(Entity entity) {
            return Vec3.ZERO;
        }

        public HitArg getHitArg(AABB aabb, HitArg hitArg) {
            return HitType.getHitArgOrb(aabb, hitArg);
        }
    };

    public static HitArg getHitArgRay(AABB aabb, HitArg hitArg) {
        Vec3 position = hitArg.position();
        Vec3 rotation = hitArg.rotation();
        double[] doubles = ModUtil.rayClip(aabb, position, rotation.x, rotation.y, rotation.z);
        if (doubles == null) {
            return null;
        }
        position = new Vec3(doubles[1], doubles[2], doubles[3]);
        return new HitArg(position, rotation);
    }

    public static HitArg getHitArgOrb(AABB aabb, HitArg hitArg) {
        Vec3 position = hitArg.position();
        Vec3 vec3 = ModUtil.orbClip(aabb, position);
        Vec3 rotation = position.subtract(vec3).normalize();
        return new HitArg(vec3, rotation);
    }


    public static float getPartialTick() {
        if (EffectiveSide.get().isClient() && FMLEnvironment.dist.isClient()) {
            return Minecraft.getInstance().getPartialTick();
        }
        return 1.0F;
    }

    public static HitType getHitType(DamageSource damageSource) {
        if (damageSource.isExplosion()) {
            return HitType.EXPLODE;
        } else if (damageSource instanceof IndirectEntityDamageSource) {
            return HitType.INDIRECT;
        }
        return HitType.MELEE;
    }

    public abstract Vec3 getPosition(Entity entity);

    public abstract Vec3 getRotation(Entity entity);

    public abstract HitArg getHitArg(AABB aabb, HitArg hitArg);
}
