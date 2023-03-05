package com.xm666.realisticcruelty.event;

import com.xm666.realisticcruelty.CruelConfig;
import com.xm666.realisticcruelty.network.GorePacket;
import com.xm666.realisticcruelty.network.HandleGore;
import com.xm666.realisticcruelty.network.HitArg;
import com.xm666.realisticcruelty.network.HitType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class ClientGoreEvent {
    public static void onPlayerAttackTarget(AttackEntityEvent event) {
        Entity target = event.getTarget();
        if (target.level.isClientSide) {
            Player attacker = event.getEntity();
            HitType hitType = HitType.MELEE;
            int amount = CruelConfig.clientMelee.get();
            GorePacket packet = new GorePacket(target.getId(), attacker.getId(), hitType, 0, 0, 0, 0, 0, 0, amount);
            HandleGore.handle(packet);
        }
    }

    public static void onProjectileImpact(ProjectileImpactEvent event) {
        Projectile direct = event.getProjectile();
        if (direct.level.isClientSide) {
            HitResult hitResult = event.getRayTraceResult();
            if (hitResult instanceof EntityHitResult entityHitResult) {
                Entity entity = entityHitResult.getEntity();
                AABB aabb = entity.getBoundingBox();
                HitType hitType = HitType.INDIRECT;
                Vec3 rotation = hitType.getRotation(direct);
                Vec3 location = hitType.getHitArg(aabb, new HitArg(hitType.getPosition(direct), rotation)).position();
                int amount = CruelConfig.clientTrident.get();
                HandleGore.gore((ClientLevel) entity.level, location, rotation, amount);
            }
        }
    }
}
