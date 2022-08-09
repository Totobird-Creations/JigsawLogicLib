package net.totobirdcreations.jigsawlogiclib.logic;


import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.OperatorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class LogicBlock extends BlockWithEntity implements OperatorBlock {

    public LogicBlock(Settings settings) {
        super(settings);
    }


    @Override
    public LogicBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LogicBlockEntity(pos, state);
    }


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }


    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {}


    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World ignoredWorld, BlockState ignoredState, BlockEntityType<T> ignoredType) {
        return ((world, pos, state, entity) -> {
            if (entity instanceof LogicBlockEntity logicEntity) {logicEntity.tick();}
        });
    }


    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (! world.isClient()) {
            // If server and player is creative level 2 op, open screen.
            if (player.isCreativeLevelTwoOp()) {
                NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
                if (factory != null) {
                    player.openHandledScreen(factory);
                }
                return ActionResult.CONSUME;
            }
            return ActionResult.PASS;
        } else {
            // If client, do nothing.
            return ActionResult.SUCCESS;
        }
    }


}
