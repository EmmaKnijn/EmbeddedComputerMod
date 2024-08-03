package org.luminacc.additions.embedded.block;

import dan200.computercraft.shared.computer.core.ComputerState;
import dan200.computercraft.shared.util.BlockEntityHelpers;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import net.minecraft.state.property.EnumProperty;
import org.luminacc.additions.registry;

import static java.util.Objects.isNull;

public class EmbeddedComputerBlock<T extends EmbeddedComputerBlockEntity> extends HorizontalFacingBlock  implements BlockEntityProvider {
    public static EnumProperty powered = EnumProperty.of("state", ComputerState.class);
    public EmbeddedComputerBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(powered,ComputerState.OFF));
    }
    private final BlockEntityTicker<T> ticker = (level, pos, state, computer) -> computer.serverTick();
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING,powered);
    }
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EmbeddedComputerBlockEntity(pos,state);
    }
    // update for peripherals
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        var comp1 = world.getBlockEntity(pos);
        if (!isNull(comp1) && comp1 instanceof EmbeddedComputerBlockEntity) {
            var comp = (EmbeddedComputerBlockEntity) comp1;
            comp.neighborChanged(pos);
        }
    }
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        var comp1 = world.getBlockEntity(pos);
        if (!isNull(comp1) && comp1 instanceof EmbeddedComputerBlockEntity) {
            var comp = (EmbeddedComputerBlockEntity) comp1;
            comp.updateInputsImmediately();
        }
    }

    //turn on computer
    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        var blockEntity1 = world.getBlockEntity(blockPos);
        if (!world.isClient() && !isNull(blockEntity1)) {
            var blockEntity = (EmbeddedComputerBlockEntity) blockEntity1;
            var computer = blockEntity.getServerComputer();
            if (isNull(computer)) {
                computer = blockEntity.createServerComputer();
                computer.turnOn();
            }
            else {
                computer.reboot();
            }
        }
        return ActionResult.success(true);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
    @Override
    public BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType type) {
        return world.isClient ? null : BlockEntityHelpers.createTickerHelper(type, (BlockEntityType) registry.EMBEDDED_COMPUTER_ENTITY, ticker);
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
