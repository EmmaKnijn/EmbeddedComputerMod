/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.windclan.embeddedcomputer.embedded;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.WritableMount;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import dan200.computercraft.shared.computer.core.ServerComputer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;
import org.windclan.embeddedcomputer.storage.harddrive.HardDrivePeripheral;
import org.jetbrains.annotations.Nullable;
import org.windclan.embeddedcomputer.embedded.block.EmbeddedComputerBlockEntity;

import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Scanner;
import java.util.Set;

import static dan200.computercraft.shared.pocket.items.PocketComputerItem.getServerComputer;
import static java.util.Objects.isNull;
import static org.windclan.embeddedcomputer.main.log;

public class EmbeddedComputerPeripheral implements IPeripheral {
    public final EmbeddedComputerBlockEntity comp;
    public EmbeddedComputerPeripheral(AbstractComputerBlockEntity owner) {
        super();
        this.comp = (EmbeddedComputerBlockEntity) owner;
    }


    @Override
    public String getType() {
        return "embeddedcomputer";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        boolean same = false;
        if (this == other) same = true;
        if (other instanceof EmbeddedComputerPeripheral comp1 && comp1.comp == comp) same = true;
        return same;
    }

    public ServerEmbeddedComputer getServerComp() {
        var comp1 = comp.getServerComputer();
        return (ServerEmbeddedComputer) comp1;
    }

    @LuaFunction
    public final boolean isOn() {
        var comp1= getServerComp();
        return !isNull(comp1) && comp1.isOn();
    }
    @LuaFunction
    public final void reboot() {
        var comp1 = getServerComp();
        if (!isNull(comp1)) comp1.reboot();
    }
    @LuaFunction(mainThread = true)
    public final boolean format() {
        var comp1 = getServerComp();
        WritableMount mnt;
        if (!isNull(comp1)) {
            try {
                mnt = comp1.createRootMount();
                if (mnt.exists(".LOCKED")) {
                    return false;
                }
                try {
                    mnt.delete("/");
                    comp1.reboot();
                    return true;
                }catch(Exception ignored){
                    comp1.reboot();
                    return false;
                }
            }catch(Exception ex){
                log.info(ex.getMessage(),ex.fillInStackTrace());
                return false;
            }
        }
        return false;
    }

    @LuaFunction(mainThread = true)
    public final void unlock(String pass1) {
        ServerComputer comp1 = getServerComp();
        if (!isNull(comp)) {
            WritableMount mnt;
            SeekableByteChannel root = null;
            Scanner scan = null;
            try {
                mnt = comp1.createRootMount();
                if (mnt.exists(".LOCKED")) {
                    String pass = "";
                    root = comp1.createRootMount().openForRead(".LOCKED");
                    scan = new Scanner(root);
                    while (scan.hasNext()) {
                        pass+=scan.next();
                    }
                    scan.close();
                    if (!pass.equals(pass1)) {
                        root.close();
                        return;
                    }
                    mnt.delete(".LOCKED");
                    return;
                }
            } catch (Exception ex) {
                log.warn(ex.toString());
                if (!isNull(root)) {
                    try {
                        root.close();
                    } catch (Exception ignored) {}
                }
                if (!isNull(scan)) {
                    scan.close();
                }
                return;
            }
        }
    }

    // Generic functions
    @Override
    public void attach(IComputerAccess computer) {}
    @Override
    public void detach(IComputerAccess computer) {}

    @Override
    public Object getTarget() {
        return comp;
    }

    public static IPeripheral getPeripheral(BlockEntity blockEntity, Direction direction) {
        return ((EmbeddedComputerBlockEntity)blockEntity).peripheral();
    }
}
