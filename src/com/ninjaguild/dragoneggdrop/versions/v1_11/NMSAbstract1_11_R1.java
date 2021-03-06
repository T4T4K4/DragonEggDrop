/*
    DragonEggDrop
    Copyright (C) 2016  NinjaStix
    ninjastix84@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.ninjaguild.dragoneggdrop.versions.v1_11;

import java.util.Arrays;

import com.ninjaguild.dragoneggdrop.versions.DragonBattle;
import com.ninjaguild.dragoneggdrop.versions.NMSAbstract;

import net.minecraft.server.v1_11_R1.EnderDragonBattle;
import net.minecraft.server.v1_11_R1.EntityEnderDragon;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import net.minecraft.server.v1_11_R1.WorldProvider;
import net.minecraft.server.v1_11_R1.WorldProviderTheEnd;

import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.block.CraftChest;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEnderDragon;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;

/**
 * An abstract implementation of necessary net.minecraft.server and
 * org.bukkit.craftbukkit methods that vary between versions causing
 * version dependencies. Allows for version independency through
 * abstraction per Bukkit/Spigot release
 * <p>
 * <b><i>Supported Minecraft Versions:</i></b> 1.11.0, 1.11.1 and 1.11.2
 * 
 * @author Parker Hawke - 2008Choco
 */
public class NMSAbstract1_11_R1 implements NMSAbstract {
	
	@Override
	public DragonBattle getEnderDragonBattleFromWorld(World world) {
		if (world == null) return null;
		
		CraftWorld craftWorld = (CraftWorld) world;
		WorldProvider worldProvider = craftWorld.getHandle().worldProvider;
		
		if (!(worldProvider instanceof WorldProviderTheEnd)) return null;
		return new DragonBattle1_11_R1(((WorldProviderTheEnd) worldProvider).t());
	}

	@Override
	public DragonBattle getEnderDragonBattleFromDragon(EnderDragon dragon) {
		if (dragon == null) return null;
		
		EntityEnderDragon nmsDragon = ((CraftEnderDragon) dragon).getHandle();
		return new DragonBattle1_11_R1(nmsDragon.db());
	}
	
	@Override
	public boolean hasBeenPreviouslyKilled(EnderDragon dragon) {
		if (dragon == null) return false;
		
		EnderDragonBattle battle = ((DragonBattle1_11_R1) this.getEnderDragonBattleFromDragon(dragon)).getHandle();
		return battle.d();
	}
	
	@Override
	public int getEnderDragonDeathAnimationTime(EnderDragon dragon) {
		if (dragon == null) return -1;
		
		EntityEnderDragon nmsDragon = ((CraftEnderDragon) dragon).getHandle();
		return nmsDragon.bG;
	}
	
	@Override
	public void setChestName(Chest chest, String name) {
		if (chest == null || name == null) return;
		
		CraftChest craftChest = (CraftChest) chest;
		craftChest.getTileEntity().a(name);
	}

	@Override
	public void sendActionBar(String message, Player... players) {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
		Arrays.stream(players).forEach(p -> ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet));
	}

	@Override
	public void broadcastActionBar(String message, World world) {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
		world.getPlayers().forEach(p -> ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet));
	}
}