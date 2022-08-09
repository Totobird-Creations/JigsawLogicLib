package net.totobirdcreations.jigsawlogiclib.mixin;


import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.totobirdcreations.jigsawlogiclib.logic.LogicBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {

    @Redirect(method = "place",
            at = @At(
                    value = "INVOKE",
                target = "Lnet/minecraft/world/ServerWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
            )
    )
    boolean setBlockState(ServerWorldAccess world, BlockPos pos, BlockState state, int flags) {
        /*return state.isOf(Main.BLOCK)
                ? world.setBlockState(pos, state, flags)
                : world.setBlockState(pos, Blocks.SPONGE.getDefaultState(), flags);*/
        return world.setBlockState(pos, state, flags);
    }

    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/BlockEntity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"
            )
    )
    void readNbt(BlockEntity entity, NbtCompound nbt) {
        entity.readNbt(nbt);
        if (entity instanceof LogicBlockEntity logicEntity) {
            logicEntity.run(null);
        }
    }

}
