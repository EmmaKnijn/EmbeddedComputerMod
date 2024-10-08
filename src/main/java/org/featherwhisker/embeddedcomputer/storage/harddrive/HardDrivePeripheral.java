/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.featherwhisker.embeddedcomputer.storage.harddrive;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HardDrivePeripheral implements IPeripheral {
    public HardDriveBlockEntity hdd;
    private static String mount;
    public HardDrivePeripheral(BlockEntity blockEntity) {
        hdd = (HardDriveBlockEntity) blockEntity;
    }

    @Override
    public String getType() {
        return "harddrive";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        boolean same = false;
        if (this == other) same = true;
        if (other instanceof HardDrivePeripheral drive && drive.hdd == hdd) same = true;
        return same;
    }

    @LuaFunction(mainThread = true)
    public boolean mount(IComputerAccess computer, @Nullable String str) {
        if (Objects.equals(str, "rom")) {
            return false;
        }
        mount = str;
        return hdd.attach(computer,str);
    }
    @LuaFunction(mainThread = true)
    public boolean unmount(IComputerAccess computer, @Nullable String str) {
        return hdd.detach(computer,str);
    }
    public static IPeripheral getPeripheral(BlockEntity blockEntity, Direction direction) {
        return new HardDrivePeripheral(blockEntity);
    }
    @Override
    public Object getTarget() {
        return hdd;
    }

    @Override
    public void detach(IComputerAccess computer) {
        hdd.detach(computer,mount);
    }
}
