package net.totobirdcreations.jigsawlogiclib.mixin;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureTemplate;
import net.totobirdcreations.jigsawlogiclib.logic.LogicBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(StructureTemplate.class)
public abstract class StructureTemplateMixin {


    @Redirect(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/BlockEntity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"
            )
    )
    void readNbt(BlockEntity entity, NbtCompound nbt) {
        if (entity instanceof LogicBlockEntity logicEntity) {
            nbt.putBoolean("canRun", true);
            logicEntity.readNbt(nbt);
        } else {
            entity.readNbt(nbt);
        }
    }


}
