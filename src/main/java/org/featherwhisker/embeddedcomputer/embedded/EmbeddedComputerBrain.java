/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.featherwhisker.embeddedcomputer.embedded;

import org.featherwhisker.embeddedcomputer.embedded.block.EmbeddedComputerBlockEntity;

public class EmbeddedComputerBrain implements IEmbeddedComputer{
    private final EmbeddedComputerBlockEntity owner;
    public EmbeddedComputerBrain(EmbeddedComputerBlockEntity owner){
        this.owner = owner;
    }

    @Override
    public EmbeddedComputerBlockEntity getOwner() {
        return this.owner;
    }
}
