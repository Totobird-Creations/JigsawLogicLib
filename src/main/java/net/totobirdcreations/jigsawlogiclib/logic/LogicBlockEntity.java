package net.totobirdcreations.jigsawlogiclib.logic;


import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.totobirdcreations.jigsawlogiclib.api.LogicCommandManager;
import net.totobirdcreations.jigsawlogiclib.init.Main;

import javax.annotation.Nullable;


public class LogicBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {

    public Data data = new Data(
            new Identifier(""),
            ""
    );
    public BlockPos structureOrigin = null;


    public LogicBlockEntity(BlockPos pos, BlockState state) {
        super(Main.BLOCK_ENTITY, pos, state);
    }


    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeString(this.data.command.toString());
        buf.writeString(this.data.metadata);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.data.command = new Identifier(nbt.getString("command"));
        this.data.metadata = nbt.getString("metadata");
        if (nbt.contains("structureOrigin", NbtElement.INT_ARRAY_TYPE)) {
            int[] ints = nbt.getIntArray("structureOrigin");
            this.structureOrigin = new BlockPos(ints[0], ints[1], ints[2]);
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putString("command", this.data.command.toString());
        nbt.putString("metadata", this.data.metadata);
        if (structureOrigin != null) {
            nbt.putIntArray("structureOrigin", new int[]{
                    structureOrigin.getX(),
                    structureOrigin.getY(),
                    structureOrigin.getZ()
            });
        }
    }


    public void tick() {
        if (this.structureOrigin != null && this.world != null) {
            this.run(this.world, this.structureOrigin, null);
        }
    }


    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new LogicScreenHandler(syncId, this);
    }


    public void run(@Nullable World world, BlockPos structureOrigin, @Nullable ServerPlayerEntity player) {
        Identifier command = this.data.command;
        String metadata = this.data.metadata;
        world = world != null ? world : this.world;
        BlockPos blockPos = this.pos;
        if (world != null) {
            BlockState savedState = world.getBlockState(blockPos);
            LogicBlockEntity.Data savedData = this.data;
            world.removeBlock(blockPos, false);
            LogicCommandManager.Response response = LogicCommandManager.run(
                    command, metadata, world, blockPos, structureOrigin
            );
            world.markDirty(blockPos);
            response.sendToPlayer(player, command);
            if (player != null && response != LogicCommandManager.Response.SUCCESS) {
                world.setBlockState(blockPos, savedState);
                if (world.getBlockEntity(blockPos) instanceof LogicBlockEntity entity) {
                    entity.data = savedData;
                }
            }
        }
    }


    public static class Data {
        public Identifier command;
        public String metadata;

        public Data(Identifier command, String metadata) {
            this.command = command;
            this.metadata = metadata;
        }
    }


}
