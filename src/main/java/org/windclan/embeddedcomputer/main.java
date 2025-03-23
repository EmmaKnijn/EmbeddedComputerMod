/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.windclan.embeddedcomputer;

import dan200.computercraft.api.ComputerCraftAPI;
import net.fabricmc.api.ModInitializer;
import org.windclan.embeddedcomputer.embedded.EmbeddedComputerAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class main implements ModInitializer {

    public static Logger log = LoggerFactory.getLogger("EmbeddedComputer");
    @Override
    public void onInitialize() {
        var a = new registry();
        a.registerPeripherals();
        a.registerItemGroups();
        ComputerCraftAPI.registerAPIFactory(computer -> {
            var embedded = computer.getComponent(registry.EMBEDDED_COMPONENT);
            return embedded == null ? null : new EmbeddedComputerAPI(embedded);
        });
    }
}
