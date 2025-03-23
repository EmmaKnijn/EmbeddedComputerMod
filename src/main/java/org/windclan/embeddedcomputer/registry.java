/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package org.windclan.embeddedcomputer;

import com.mojang.serialization.Codec;
import dan200.computercraft.api.component.ComputerComponent;
import dan200.computercraft.api.peripheral.PeripheralLookup;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.ComponentType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.minecraft.util.Identifier;

import org.windclan.embeddedcomputer.embedded.EmbeddedComputerPeripheral;
import org.windclan.embeddedcomputer.embedded.IEmbeddedComputer;
import org.windclan.embeddedcomputer.embedded.block.EmbeddedComputerBlock;
import org.windclan.embeddedcomputer.embedded.block.EmbeddedComputerBlockEntity;
import org.windclan.embeddedcomputer.embedded.item.ComputerBlockItem;
import org.windclan.embeddedcomputer.storage.harddrive.HardDriveBlock;
import org.windclan.embeddedcomputer.storage.harddrive.HardDriveBlockEntity;
import org.windclan.embeddedcomputer.platform.registry1;
import org.windclan.embeddedcomputer.storage.harddrive.HardDriveItem;
import org.windclan.embeddedcomputer.storage.harddrive.HardDrivePeripheral;
import org.windclan.embeddedcomputer.storage.items.DebugMediaItem;
import org.windclan.embeddedcomputer.storage.items.FlashCardItem;
import org.windclan.embeddedcomputer.storage.items.ZipDiskItem;

import java.util.function.UnaryOperator;

public class registry {
    public static Block EMBEDDED_COMPUTER = Registry.register(
            Registries.BLOCK,
            Identifier.of("embeddedcomputer","embedded_computer"),
            new EmbeddedComputerBlock(AbstractBlock.Settings.create().pistonBehavior(PistonBehavior.IGNORE).solid())
    );
    public static BlockEntityType<EmbeddedComputerBlockEntity> EMBEDDED_COMPUTER_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of("embeddedcomputer", "embedded_computer_entity"),
            BlockEntityType.Builder.create(EmbeddedComputerBlockEntity::new, EMBEDDED_COMPUTER).build(null)
    );
    public static Item EMBEDDED_COMPUTER_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of("embeddedcomputer", "embedded_computer"),
            new ComputerBlockItem(EMBEDDED_COMPUTER)
    );

    public static final Block HARD_DRIVE = Registry.register(
            Registries.BLOCK,
            Identifier.of("embeddedcomputer","hard_drive"),
            new HardDriveBlock(AbstractBlock.Settings.create().pistonBehavior(PistonBehavior.IGNORE).solid())
    );
    public static final BlockEntityType HARD_DRIVE_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,Identifier.tryParse("embeddedcomputer","hard_drive_entity"), BlockEntityType.Builder.create(HardDriveBlockEntity::new,HARD_DRIVE).build(null));
    public static Item HARD_DRIVE_ITEM = Registry.register(
            Registries.ITEM,
            Identifier.of("embeddedcomputer", "hard_drive"),
            new HardDriveItem(HARD_DRIVE, new Item.Settings().maxCount(1))
    );

    public static final Item DEBUG_MEDIA_ITEM = Registry.register(Registries.ITEM, Identifier.tryParse("embeddedcomputer", "debug_rock"), new DebugMediaItem(new Item.Settings()));
    public static final Item ZIP_DISK_ITEM = Registry.register(Registries.ITEM, Identifier.tryParse("embeddedcomputer", "zip_disk"), new ZipDiskItem(new Item.Settings()));
    public static final Item FLASH_CARD_ITEM = Registry.register(Registries.ITEM, Identifier.tryParse("embeddedcomputer", "flash_card"), new FlashCardItem(new Item.Settings()));

    public static final ComputerComponent<IEmbeddedComputer> EMBEDDED_COMPONENT = ComputerComponent.create("embeddedcomputer","embedded_computer");


    public static final ComponentType<Integer> id = register("id", builder -> builder
            .codec(Codec.INT)
            .packetCodec(PacketCodecs.INTEGER));

    public static final ComponentType<String> uuid = register("uuid", builder -> builder
            .codec(Codec.STRING)
            .packetCodec(PacketCodecs.STRING));

    public static <T> ComponentType<T> register(String path, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of("embeddedcomputer", path), builderOperator.apply(ComponentType.builder()).build());
    }

    public void registerPeripherals() {
        PeripheralLookup.get().registerForBlockEntities(EmbeddedComputerPeripheral::getPeripheral,EMBEDDED_COMPUTER_ENTITY);
        PeripheralLookup.get().registerForBlockEntities(HardDrivePeripheral::getPeripheral,HARD_DRIVE_ENTITY);
    }
    public void registerItemGroups() {
        var a = new registry1();
        a.registerItemGroups();
    }
}
