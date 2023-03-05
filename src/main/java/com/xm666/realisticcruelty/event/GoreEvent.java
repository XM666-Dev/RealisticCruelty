package com.xm666.realisticcruelty.event;

import com.xm666.realisticcruelty.network.GorePacket;
import com.xm666.realisticcruelty.network.HitType;
import com.xm666.realisticcruelty.network.ModNetwork;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

public class GoreEvent {
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.getLevel();
        if (level.isClientSide) return;
        DamageSource source = event.getSource();
        Entity direct = source.getDirectEntity();
        if (direct == null) {
            direct = source.getEntity();
            if (direct == null) return;
        }
        int entityId = entity.getId(), directId = direct.getId();
        HitType hitType = HitType.getHitType(source);
        Vec3 position = hitType.getPosition(direct);
        Vec3 rotation = hitType.getRotation(direct);
        double x = position.x, y = position.y, z = position.z, xd = rotation.x, yd = rotation.y, zd = rotation.z;
        float amount = event.getAmount();
        GorePacket packet = new GorePacket(entityId, directId, hitType, x, y, z, xd, yd, zd, amount);
        int i = level.getServer().getPlayerList().getViewDistance() * 16;
        ModNetwork.CHANNEL.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(entity.getX(), entity.getY(), entity.getZ(), i, level.dimension())), packet);
    }
}