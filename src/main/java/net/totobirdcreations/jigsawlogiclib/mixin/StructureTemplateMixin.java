package net.totobirdcreations.jigsawlogiclib.mixin;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.totobirdcreations.jigsawlogiclib.logic.LogicBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(StructureTemplate.class)
public abstract class StructureTemplateMixin {


    private BlockPos structureOrigin;


    @Inject(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/BlockEntity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"
            )
    )
    void readNbtInject(ServerWorldAccess world, BlockPos pos, BlockPos pivot, StructurePlacementData data, Random random, int flags, CallbackInfoReturnable<Boolean> callback) {
        this.structureOrigin = pos;
    }


    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/BlockEntity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"
            )
    )
    void readNbtRedirect(BlockEntity entity, NbtCompound nbt) {
        if (entity instanceof LogicBlockEntity) {
            nbt.putIntArray("structureOrigin", new int[] {
                    structureOrigin.getX(),
                    structureOrigin.getY(),
                    structureOrigin.getZ()
            });
        }
        entity.readNbt(nbt);
    }


}
