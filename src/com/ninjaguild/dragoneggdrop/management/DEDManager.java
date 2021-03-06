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

package com.ninjaguild.dragoneggdrop.management;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.ninjaguild.dragoneggdrop.DragonEggDrop;
import com.ninjaguild.dragoneggdrop.dragon.DragonTemplate;
import com.ninjaguild.dragoneggdrop.utils.RandomCollection;

import org.apache.commons.lang.Validate;
import org.bukkit.World;

/**
 * The central manager holding all information regarding world wrappers,
 * loaded dragon templates, and the currently active dragon battle
 */
public class DEDManager {

	private final RandomCollection<DragonTemplate> dragonTemplates = new RandomCollection<>();
	private final Map<UUID, EndWorldWrapper> worldWrappers = new HashMap<>();
	private final DragonEggDrop plugin;
	
	/**
	 * Construct a new DEDManager object. This object should mainly be
	 * managed by the {@link DragonEggDrop} class
	 * 
	 * @param plugin an instance of the DragonEggDrop plugin
	 */
	public DEDManager(DragonEggDrop plugin) {
		this.plugin = plugin;
		this.reloadDragonTemplates();
	}
	
	/**
	 * Register a template to the DEDManager in order for it to be used when generating
	 * dragons in the respawn process
	 * 
	 * @param template the template to register
	 */
	public void registerTemplate(DragonTemplate template) {
		Validate.notNull(template, "Cannot register null templates");
		
		for (DragonTemplate registeredTemplate : dragonTemplates.values()) {
			if (registeredTemplate.getIdentifier().equals(template.getIdentifier())) {
				throw new UnsupportedOperationException("Cannot register two templates with the same identifier (" + template.getIdentifier() + ")");
			}
		}
		
		this.dragonTemplates.add(template.getSpawnWeight(), template);
	}
	
	/**
	 * Get a collection of all loaded dragon templates
	 * 
	 * @return all dragon templates
	 */
	public Collection<DragonTemplate> getDragonTemplates() {
		return dragonTemplates.toCollection();
	}
	
	/**
	 * Get a weighted random dragon template pooled from all loaded templates. 
	 * 
	 * @return a random dragon template. null if none
	 * 
	 * @see #getDragonTemplates()
	 */
	public DragonTemplate getRandomTemplate() {
		return dragonTemplates.next();
	}
	
	/**
	 * Get a template based on its identifier (see {@link DragonTemplate#getIdentifier()}).
	 * This search is case-sensitive.
	 * 
	 * @param template the template's unique identifier
	 * @return the resulting template, or null if none exists
	 */
	public DragonTemplate getTemplate(String template) {
		return dragonTemplates.toCollection().stream()
				.filter(t -> t.getIdentifier().equals(template))
				.findFirst().orElse(null);
	}
	
	/**
	 * Clear all loaded dragon templates
	 */
	public void clearTemplates() {
		this.dragonTemplates.clear();
	}
	
	/**
	 * Load and parse all dragon template files from the "dragons" folder.
	 * This method implicitly invokes {@link #clearTemplates()} before loading any
	 * other templates
	 */
	public void reloadDragonTemplates() {
		this.dragonTemplates.clear();
		
		for (DragonTemplate template : DragonTemplate.loadTemplates())
			this.dragonTemplates.add(template.getSpawnWeight(), template);
	}
	
	/**
	 * Get the world wrapper for the specified world
	 * 
	 * @param world the world to get
	 * @return the world's respective wrapper
	 */
	public EndWorldWrapper getWorldWrapper(World world) {
		if (world == null) return null;
		
		UUID worldId = world.getUID();
		if (!worldWrappers.containsKey(worldId))
			this.worldWrappers.put(worldId, new EndWorldWrapper(plugin, world));
		return this.worldWrappers.get(worldId);
	}
	
	/**
	 * Get the map containing all world wrappers
	 * 
	 * @return all world wrappers
	 */
	public Collection<EndWorldWrapper> getWorldWrappers() {
		return worldWrappers.values();
	}
	
	/**
	 * Clear all world wrapper data. This deletes all information to do with
	 * active battles, as well as the state of a world according to DragonEggDrop
	 */
	public void clearWorldWrappers() {
		this.worldWrappers.clear();
	}
	
	/**
	 * The type of trigger that allowed an Ender Dragon to 
	 * commence its respawning process
	 */
	public enum RespawnType {
		
		/** 
		 * A player joined the world
		 */
		JOIN,
		
		/** 
		 * The ender dragon was killed
		 */
		DEATH
	}
}