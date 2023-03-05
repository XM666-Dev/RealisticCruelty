package com.xm666.realisticcruelty.network;

import com.xm666.realisticcruelty.CruelConfig;
import com.xm666.realisticcruelty.particle.BloodFogParticle;
import com.xm666.realisticcruelty.particle.ModParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class HandleGore {
    public static void handle(GorePacket msg) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        Entity entity = level.getEntity(msg.entityId());
        if (entity == null) {
            return;
        }
        AABB aabb = entity.getBoundingBox();
        Entity direct = level.getEntity(msg.directId());
        Vec3 position, rotation;
        HitType hitType = msg.hitType();
        if (direct == null) {
            position = new Vec3(msg.x(), msg.y(), msg.z());
            rotation = new Vec3(msg.xd(), msg.yd(), msg.zd());
        } else {
            position = hitType.getPosition(direct);
            rotation = hitType.getRotation(direct);
        }
        HitArg hitArg = hitType.getHitArg(aabb, new HitArg(position, rotation));
        if (hitArg == null) {
            return;
        }
        position = hitArg.position();
        rotation = hitArg.rotation();
        HandleGore.gore(level, position, rotation, msg.amount());
    }

    public static void gore(ClientLevel level, Vec3 position, Vec3 rotation, float amount) {
        double sqrt = Math.sqrt(amount);
        BloodFogParticle.size = (float) sqrt * CruelConfig.fogSize.get();
        level.addParticle(ModParticle.BLOOD_FOG.get(), position.x, position.y, position.z, rotation.x, rotation.y, rotation.z);
        double speed = -sqrt * CruelConfig.bloodSpeed.get();
        amount *= CruelConfig.bloodAmount.get();
        while (amount-- > 0) {
            ModUtil.addParticle(level, position, ModParticle.BLOOD.get(), rotation, speed);
        }
    }
}
