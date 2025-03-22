package org.windclan.embeddedcomputer.peripherals.generics;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import net.minecraft.block.entity.PistonBlockEntity;

public class PistonPeripheral implements GenericPeripheral {
    @Override
    public String id() {
        return new String("embeddedcomputer:piston");
    }
    @LuaFunction(mainThread = true)
    public final void isExtending(PistonBlockEntity piston) {
        piston.isExtending();
    }
}
