/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.featherwhisker.embeddedcomputer;

import dan200.computercraft.api.peripheral.PeripheralLookup;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.minecraft.util.Identifier;

import org.featherwhisker.embeddedcomputer.embedded.EmbeddedComputerPeripheral;
import org.featherwhisker.embeddedcomputer.embedded.block.EmbeddedComputerBlock;
import org.featherwhisker.embeddedcomputer.embedded.block.EmbeddedComputerBlockEntity;
import org.featherwhisker.embeddedcomputer.embedded.item.ComputerBlockItem;

import org.featherwhisker.embeddedcomputer.storage.harddrive.HardDriveBlock;
import org.featherwhisker.embeddedcomputer.storage.harddrive.HardDriveBlockEntity;

import org.featherwhisker.embeddedcomputer.platform.registry1;
import org.featherwhisker.embeddedcomputer.storage.harddrive.HardDriveItem;
import org.featherwhisker.embeddedcomputer.storage.harddrive.HardDrivePeripheral;
import org.featherwhisker.embeddedcomputer.storage.items.FlashCardItem;
import org.featherwhisker.embeddedcomputer.storage.items.ZipDiskItem;

public class registry {
    public static Block EMBEDDED_COMPUTER = Registry.register(
            Registries.BLOCK,
            new Identifier("embeddedcomputer","embedded_computer"),
            new EmbeddedComputerBlock(AbstractBlock.Settings.create().pistonBehavior(PistonBehavior.IGNORE).solid())
    );
    public static BlockEntityType<EmbeddedComputerBlockEntity> EMBEDDED_COMPUTER_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier("embeddedcomputer", "embedded_computer_entity"),
            BlockEntityType.Builder.create(EmbeddedComputerBlockEntity::new, EMBEDDED_COMPUTER).build(null)
    );
    public static Item EMBEDDED_COMPUTER_ITEM = Registry.register(
            Registries.ITEM,
            new Identifier("embeddedcomputer", "embedded_computer"),
            new ComputerBlockItem(EMBEDDED_COMPUTER)
    );

    public static final Block HARD_DRIVE = Registry.register(
            Registries.BLOCK,
            new Identifier("embeddedcomputer","hard_drive"),
            new HardDriveBlock(AbstractBlock.Settings.create().pistonBehavior(PistonBehavior.IGNORE).solid())
    );
    public static final BlockEntityType HARD_DRIVE_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,Identifier.of("embeddedcomputer","hard_drive_entity"), BlockEntityType.Builder.create(HardDriveBlockEntity::new,HARD_DRIVE).build(null));
    public static Item HARD_DRIVE_ITEM = Registry.register(
            Registries.ITEM,
            new Identifier("embeddedcomputer", "hard_drive"),
            new HardDriveItem(HARD_DRIVE, new FabricItemSettings().maxCount(1))
    );

    public static final Item ZIP_DISK_ITEM = Registry.register(Registries.ITEM, Identifier.of("embeddedcomputer", "zip_disk"), new ZipDiskItem(new Item.Settings()));
    public static final Item FLASH_CARD_ITEM = Registry.register(Registries.ITEM, Identifier.of("embeddedcomputer", "flash_card"), new FlashCardItem(new Item.Settings()));

    public void registerPeripherals() {
        PeripheralLookup.get().registerForBlockEntities(EmbeddedComputerPeripheral::getPeripheral,EMBEDDED_COMPUTER_ENTITY);
        PeripheralLookup.get().registerForBlockEntities(HardDrivePeripheral::getPeripheral,HARD_DRIVE_ENTITY);
    }
    public void registerItemGroups() {
        var a = new registry1();
        a.registerItemGroups();
    }
}
