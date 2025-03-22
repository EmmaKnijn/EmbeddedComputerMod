/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.windclan.embeddedcomputer.embedded;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.WritableMount;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.core.apis.handles.WriteHandle;
import dan200.computercraft.shared.computer.core.ServerComputer;

import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Set;

import static java.util.Objects.isNull;
import static org.windclan.embeddedcomputer.main.log;

public class EmbeddedComputerAPI implements ILuaAPI {
    private final IEmbeddedComputer brain;
    public EmbeddedComputerAPI(IEmbeddedComputer brain) {
        this.brain = brain;
    }
    @Override
    public String[] getNames() {
        return new String[]{"embedded"};
    }
    @LuaFunction(mainThread = true)
    public final void format() { //
        ServerComputer comp = brain.getOwner().getServerComputer();
        if (!isNull(comp)) {
            try {
                WritableMount fs = comp.createRootMount();
                try {
                    fs.delete("/");
                }catch(Exception ignored){}
                comp.reboot();
            }catch(Exception ignored){
                log.info(ignored.getMessage(),ignored.fillInStackTrace());
            }
        }
    }
}