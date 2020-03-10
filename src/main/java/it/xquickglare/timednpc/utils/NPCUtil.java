package it.xquickglare.timednpc.utils;

import com.mojang.authlib.GameProfile;
import it.xquickglare.timednpc.TimedNPCPlugin;
import it.xquickglare.timednpc.personal.PersonalNPC;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public final class NPCUtil {

    private NPCUtil() {
    }

    /**
     * This method shows a PersonalNPC to a Player
     * @param npc The PersonalNPC
     * @param player The Player
     */
    public static void spawn(PersonalNPC npc, Player player) {
        if (npc.getEntityId() == -1)
            npc.setEntityId(ReflectionUtil.<AtomicInteger>getStaticField(Entity.class, "entityCount").incrementAndGet());
        
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        
        GameProfile profile = new GameProfile(UUID.randomUUID(), npc.getName());
        profile.getProperties().putAll("textures", entityPlayer.getProfile().getProperties().get("textures"));
       
        int yaw = (int) ((npc.getLocation().getYaw() % 360F) * 256F / 360F);
        int pitch = (int) (((MathHelper.a(npc.getLocation().getPitch(), -90F, 90F)) % 360F) * 256F / 360F);
        
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
        ReflectionUtil.setField(packet, "a", npc.getEntityId());
        ReflectionUtil.setField(packet, "b", profile.getId());
        ReflectionUtil.setField(packet, "c", npc.getLocation().getX());
        ReflectionUtil.setField(packet, "d", npc.getLocation().getY());
        ReflectionUtil.setField(packet, "e", npc.getLocation().getZ());
        ReflectionUtil.setField(packet, "f", (byte) yaw);
        ReflectionUtil.setField(packet, "g", (byte) pitch);
        
        sendTablist(entityPlayer, profile, npc.getName(), PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        entityPlayer.playerConnection.sendPacket(packet);
        
        Bukkit.getScheduler().runTaskLater(TimedNPCPlugin.getInstance(), () -> sendTablist(entityPlayer, profile, npc.getName(), PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER), 10);
    }

    /**
     * This method removes a PersonalNPC from a Player
     * @param npc The PersonalNPC
     * @param player The Player
     */
    public static void despawn(PersonalNPC npc, Player player) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(npc.getEntityId());
        
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    /**
     * This method sends to a Player the packet to add/remove a Player in the tablist
     * @param player The Player that have to see the tablist
     * @param profile The GameProfile of the added/removed Player
     * @param name The name of the added/removed Player
     * @param action The action to do add/remove
     */
    private static void sendTablist(EntityPlayer player, GameProfile profile, String name, PacketPlayOutPlayerInfo.EnumPlayerInfoAction action) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        Object data = ReflectionUtil.getInstance("net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo$PlayerInfoData", 0, packet, profile, 0, EnumGamemode.CREATIVE, CraftChatMessage.fromString(name)[0]);

        ReflectionUtil.setField(packet, "a", action);        
        ReflectionUtil.setField(packet, "b", Collections.singletonList(data));        
        
        player.playerConnection.sendPacket(packet);
    }
    
}
