/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.border.listeners;

import me.denarydev.border.ColoredBorder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import world.bentobox.bentobox.api.events.island.IslandProtectionRangeChangeEvent;

import java.util.Objects;

public class PlayerListener implements Listener {

    private final ColoredBorder addon;

    public PlayerListener(ColoredBorder addon) {
        this.addon = addon;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        addon.sendBorderToPlayer(event.getPlayer());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        addon.sendBorderToPlayer(event.getPlayer());
    }

    @EventHandler
    public void onIslandRangeChange(IslandProtectionRangeChangeEvent event) {
        addon.sendBorderToPlayer(Objects.requireNonNull(Bukkit.getPlayer(event.getPlayerUUID())));
    }
}
