package net.totobirdcreations.jigsawlogiclib.logic;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.totobirdcreations.jigsawlogiclib.init.Main;


public class LogicScreenHandler extends ScreenHandler {

    public BlockPos pos;
    public String command;
    public String metadata;


    public LogicScreenHandler(int syncId, PlayerInventory ignored, PacketByteBuf buf) {
        this(syncId, buf);
    }
    public LogicScreenHandler(int syncId, PacketByteBuf buf) {
        this(syncId);
        this.pos = buf.readBlockPos();
        this.command = buf.readString();
        this.metadata = buf.readString();
    }
    public LogicScreenHandler(int syncId, LogicBlockEntity entity) {
        this(syncId);
        this.pos = entity.getPos();
        this.command = entity.data.command.toString();
        this.metadata = entity.data.metadata;
    }
    public LogicScreenHandler(int syncId) {
        super(Main.SCREEN_HANDLER, syncId);
    }


    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return player.isCreativeLevelTwoOp();
    }


}
