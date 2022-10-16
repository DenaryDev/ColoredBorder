/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.border.commands;

import me.denarydev.border.ColoredBorder;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.metadata.MetaDataValue;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static me.denarydev.border.ColoredBorder.BORDER_COLOR_KEY;

public class BorderCommand extends CompositeCommand {
    private final ColoredBorder addon;

    public BorderCommand(ColoredBorder addon) {
        super(addon, Objects.requireNonNull(addon.getPlugin().getCommandsManager().getCommand("island")), "border");
        this.addon = addon;
    }

    @Override
    public void setup() {
        setPermission("island.border");
        setParametersHelp("border.parameters");
        setDescription("border.description");
        setOnlyPlayer(true);
    }

    @Override
    public boolean canExecute(User user, String label, List<String> args) {
        if (getIslands().getIslandAt(user.getLocation()).isEmpty()) {
            user.sendMessage("general.errors.not-on-island");
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        if (args.size() == 1) {
            final var color = ColoredBorder.Color.parseColor(args.get(0));
            if (color != null) {
                user.putMetaData(BORDER_COLOR_KEY, new MetaDataValue(color.name()));
                addon.sendBorderToPlayer(user.getPlayer(), color);
                user.sendMessage("border.changed", "[color]",
                        user.getTranslation("colors." + color.name().toLowerCase()));
            } else {
                user.sendMessage("border.colors", "[colors]",
                        Arrays.stream(ColoredBorder.Color.values())
                                .map(c -> c.name().toLowerCase())
                                .collect(Collectors.joining(", "))
                );
            }
            return true;
        }
        showHelp(this, user);
        return false;
    }

    @Override
    public Optional<List<String>> tabComplete(@NotNull User sender, @NotNull String alias, List<String> args) {
        final var options = new ArrayList<String>();
        if (args.size() == 2) {
            options.addAll(Util.tabLimit(
                    Arrays.stream(ColoredBorder.Color.values())
                            .map(color -> color.name().toLowerCase())
                            .toList(), args.get(1))
            );
        }
        return Optional.of(options);
    }
}
