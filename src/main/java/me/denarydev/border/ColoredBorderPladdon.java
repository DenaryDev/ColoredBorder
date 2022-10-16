/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.border;

import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.Pladdon;

@SuppressWarnings("unused")
public class ColoredBorderPladdon extends Pladdon {
    @Override
    public Addon getAddon() {
        return new ColoredBorder();
    }
}
