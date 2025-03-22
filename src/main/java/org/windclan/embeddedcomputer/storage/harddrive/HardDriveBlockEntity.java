/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.windclan.embeddedcomputer.storage.harddrive;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.WritableMount;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.windclan.embeddedcomputer.registry;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.isNull;

public class HardDriveBlockEntity extends BlockEntity  {
    public HardDriveBlockEntity(BlockPos pos, BlockState state) {
        super(registry.HARD_DRIVE_ENTITY,pos, state);
    }
    private final HardDrivePeripheral periph = new HardDrivePeripheral(this);
    public static int id = -1;
    public String mount;

    public static void tick(World world1, BlockPos pos, BlockState state1, BlockEntity be) {

    }

    public WritableMount makeMount() {
        if (id < 0) {
            id = ComputerCraftAPI.createUniqueNumberedSaveDir(world.getServer(), "hdd");
        }
        return ComputerCraftAPI.createSaveDirMount(world.getServer(), "hdd/" + id, 25000000); // 25 Megabytes
    }
    public boolean attach(IComputerAccess computer, @Nullable String str) {
        if (isNull(str)) {
            str = "drive";
        }
        mount = computer.mountWritable(str,makeMount());
        return !isNull(mount);
    }
    public boolean detach(IComputerAccess computer, @Nullable String str) {
        if (isNull(str)) {
            str = "drive";
        }
        try {
            computer.unmount(str);
            return true;
        } catch(Exception ignored) {
            return false;
        }
    }
    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("id", id);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (!nbt.contains("id")) id = -1;
        else id = nbt.getInt("id");
    }
    public IPeripheral peripheral() {
        return periph;
    }


}
