/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.windclan.embeddedcomputer.embedded.block;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.computer.blocks.ComputerBlockEntity;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.windclan.embeddedcomputer.embedded.EmbeddedComputerBrain;
import org.windclan.embeddedcomputer.embedded.EmbeddedComputerPeripheral;
import org.windclan.embeddedcomputer.embedded.ServerEmbeddedComputer;
import org.windclan.embeddedcomputer.registry;

import static java.util.Objects.isNull;

public class EmbeddedComputerBlockEntity extends ComputerBlockEntity {
    private IPeripheral p;
    public EmbeddedComputerBlockEntity(BlockPos pos, BlockState state) {
        super(registry.EMBEDDED_COMPUTER_ENTITY,pos,state,ComputerFamily.ADVANCED);
    }
    private final EmbeddedComputerBrain brain = new EmbeddedComputerBrain(this); // This does nothing. it's just there to make the Computer Component happy lmao
    @Override
    protected ServerComputer createComputer(int id) {
        return new ServerEmbeddedComputer(
                (ServerWorld) getWorld(), getPos(), //id, label,brain
                ServerEmbeddedComputer.properties(id,ComputerFamily.ADVANCED)
                        .label(label)
                        .terminalSize(10,3)
                        .addComponent(registry.EMBEDDED_COMPONENT,brain)
        );
    }
    @Override
    public IPeripheral peripheral() {
        if (!isNull(p)){
            return p;
        }
        return p = new EmbeddedComputerPeripheral(this);
    }
    protected boolean wasOn = false;
    @Override
    public void serverTick() {
        if (isNull(getWorld()) || getWorld().isClient) {
            return; //no.
        }
        if (getComputerID() < 0) {
            return;
        }
        var comp = createServerComputer();
        var currentlyOn = comp.isOn();
        if (currentlyOn != wasOn) {
            wasOn = currentlyOn;
            markDirty();
        }
        if (!currentlyOn) {
            comp.turnOn();
        }
        comp.keepAlive();
        updateBlockState(comp.getState());
    }
}
