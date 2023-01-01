/*
 * Copyright (c) 2023 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.border;

import me.denarydev.border.commands.BorderCommand;
import me.denarydev.border.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.metadata.MetaDataValue;

public class ColoredBorder extends Addon {
    public static final String BORDER_COLOR_KEY = "IslandBorderColor";

    public static ColoredBorder INSTANCE;

    @Override
    public void onLoad() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        registerListener(new PlayerListener(this));
        getPlugin().getCommandsManager().registerCommand(new BorderCommand(this));
    }

    @Override
    public void onDisable() {

    }

    public void sendBorderToPlayer(final Player player) {
        getPlayers().getUser(player.getUniqueId()).getMetaData().ifPresentOrElse(
                (metaData) -> {
                    var value = metaData.get(BORDER_COLOR_KEY);
                    Color color;
                    if (value == null) {
                        color = Color.BLUE;
                    } else {
                        getPlayers().getUser(player.getUniqueId()).putMetaData(BORDER_COLOR_KEY, new MetaDataValue(Color.BLUE.name()));
                        color = Color.parseColor(value.asString());
                    }
                    sendBorderToPlayer(player, color);
                },
                () -> {
                    getPlayers().getUser(player.getUniqueId()).putMetaData(BORDER_COLOR_KEY, new MetaDataValue(Color.BLUE.name()));
                    sendBorderToPlayer(player, Color.BLUE);
                }
        );
    }

    public void sendBorderToPlayer(final Player player, final Color color) {
        final var loc = player.getLocation();

        Bukkit.getScheduler().runTask(getPlugin(), () ->
                getIslands().getIslandAt(loc).ifPresent((island) ->
                        sendBorder(player, color, island.getProtectionRange() * 2D, island.getProtectionCenter())));
    }

    private void sendBorder(final Player player, final Color color, final double size, final Location center) {
        if (center == null || center.getWorld() == null) return;

        final var border = Bukkit.createWorldBorder();
        border.setCenter(center);
        border.setSize(size);
        border.setWarningTime(0);
        border.setWarningDistance(0);

        switch (color) {
            case GREEN -> border.setSize(size + 0.1D, Long.MAX_VALUE);
            case RED -> border.setSize(size - 0.1D, Long.MAX_VALUE);
        }

        player.setWorldBorder(border);
    }

    public enum Color {
        RED,
        GREEN,
        BLUE;

        @Nullable
        public static Color parseColor(@NotNull String s) {
            try {
                return Color.valueOf(s.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }
    }
}
