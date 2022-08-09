package net.totobirdcreations.jigsawlogiclib.api;


import net.minecraft.block.Blocks;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;


public class Example {

    public static void enable() {
        LogicCommandManager.register(
                new Identifier("logic_example", "put_sign"),
                (metadata, world, pos) -> {

                    world.setBlockState(pos, Blocks.DIAMOND_BLOCK.getDefaultState());

                    BlockPos signPos = pos.up(1);
                    world.setBlockState(signPos, Blocks.WARPED_SIGN.getDefaultState()
                            .with(Properties.ROTATION, world.getRandom().nextInt(16))
                    );
                    if (world.getBlockEntity(signPos) instanceof SignBlockEntity entity) {
                        entity.setTextOnRow(1, Text.literal(metadata));
                    }

                }
        );
    }

}
