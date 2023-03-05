package com.xm666.realisticcruelty.network;

import com.xm666.realisticcruelty.CruelConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ModUtil {
    public static Vec3 orbClip(AABB aabb, Vec3 vec3) {
        double x = Mth.clamp(vec3.x, aabb.minX, aabb.maxX);
        double y = Mth.clamp(vec3.y, aabb.minY, aabb.maxY);
        double z = Mth.clamp(vec3.z, aabb.minZ, aabb.maxZ);
        return new Vec3(x, y, z);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public static double[] rayClip(AABB aabb, Vec3 position, double xd, double yd, double zd) {
        double[] doubles = new double[4];
        byte b = 0;
        if (xd > 0) {
            if (clipPoint(doubles, xd, yd, zd, aabb.minX, aabb.minY, aabb.maxY, aabb.minZ, aabb.maxZ, position.x, position.y, position.z))
                b = 1;
        } else if (clipPoint(doubles, xd, yd, zd, aabb.maxX, aabb.minY, aabb.maxY, aabb.minZ, aabb.maxZ, position.x, position.y, position.z)) {
            b = 1;
        }

        if (yd > 0) {
            if (clipPoint(doubles, yd, zd, xd, aabb.minY, aabb.minZ, aabb.maxZ, aabb.minX, aabb.maxX, position.y, position.z, position.x))
                b = 2;
        } else if (clipPoint(doubles, yd, zd, xd, aabb.maxY, aabb.minZ, aabb.maxZ, aabb.minX, aabb.maxX, position.y, position.z, position.x)) {
            b = 2;
        }

        if (zd > 0) {
            if (clipPoint(doubles, zd, xd, yd, aabb.minZ, aabb.minX, aabb.maxX, aabb.minY, aabb.maxY, position.z, position.x, position.y))
                b = 3;
        } else if (clipPoint(doubles, zd, xd, yd, aabb.maxZ, aabb.minX, aabb.maxX, aabb.minY, aabb.maxY, position.z, position.x, position.y)) {
            b = 3;
        }
        if (b == 0) {
            return null;
        } else if (b == 2) {
            double d = doubles[1];
            doubles[1] = doubles[3];
            doubles[3] = doubles[2];
            doubles[2] = d;
        } else if (b == 3) {
            double d = doubles[1];
            doubles[1] = doubles[2];
            doubles[2] = doubles[3];
            doubles[3] = d;
        }
        return doubles;
    }

    public static boolean clipPoint(double[] doubles, double xd, double yd, double zd, double minX, double minY, double maxY, double minZ, double maxZ, double x, double y, double z) {
        double d0 = (minX - x) / xd;
        double d1 = y + d0 * yd;
        double d2 = z + d0 * zd;
        if (d0 > doubles[0]) {
            doubles[0] = d0;
            doubles[1] = minX;
            doubles[2] = Mth.clamp(d1, minY, maxY);
            doubles[3] = Mth.clamp(d2, minZ, maxZ);
            return true;
        }
        return false;
    }

    public static void addParticle(ClientLevel level, Vec3 position, ParticleOptions particleOptions, Vec3 rotation, double speed) {
        double d0 = position.x;
        double d1 = position.y;
        double d2 = position.z;
        double rad = Math.toRadians(CruelConfig.bloodSpread.get());
        double[] doubles;
        doubles = ModUtil.VecToRotation(rotation);
        double x = doubles[0] + (Math.random() * 2 - 1) * rad;
        double y = doubles[1] + (Math.random() * 2 - 1) * rad;
        rotation = ModUtil.rotationToVec(x, y).normalize().scale(speed * Math.random());
        double d3 = rotation.x;
        double d4 = rotation.y;
        double d5 = rotation.z;
        level.addParticle(particleOptions, d0, d1, d2, d3, d4, d5);
    }

    public static double[] VecToRotation(Vec3 vec3) {
        double sqrt = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
        double x = -Mth.atan2(vec3.y, sqrt);
        double y = Mth.atan2(vec3.z, vec3.x) - Mth.HALF_PI;
        return new double[]{x, y};
    }

    public static Vec3 rotationToVec(double x, double y) {
        double f1 = -y;
        double f2 = Math.cos(f1);
        double f3 = Math.sin(f1);
        double f4 = Math.cos(x);
        double f5 = Math.sin(x);
        return new Vec3(f3 * f4, -f5, f2 * f4);
    }
}
