/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.featherwhisker.embeddedcomputer.embedded;

import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.util.ComponentMap;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.featherwhisker.embeddedcomputer.ComputerComponents;
import org.jetbrains.annotations.Nullable;

public class ServerEmbeddedComputer extends ServerComputer {
    public ServerEmbeddedComputer(ServerWorld level, BlockPos position, int computerID, @Nullable String label,EmbeddedComputerBrain brain) {
        super(level, position, computerID, label, ComputerFamily.ADVANCED, 10, 10, ComponentMap.builder().add(ComputerComponents.EMBEDDED,brain).build());
    }
}
