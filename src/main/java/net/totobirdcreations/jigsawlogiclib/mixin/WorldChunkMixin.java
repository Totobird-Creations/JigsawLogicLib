package net.totobirdcreations.jigsawlogiclib.mixin;


import net.minecraft.world.chunk.WorldChunk;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(WorldChunk.class)
public class WorldChunkMixin {


    @Redirect(
            method = "loadBlockEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"
            )
    )
    private void warn(Logger logger, String message, Object insert0, Object insert1) {}


}
