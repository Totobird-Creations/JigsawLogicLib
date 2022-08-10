package net.totobirdcreations.jigsawlogiclib.api;


import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.totobirdcreations.jigsawlogiclib.Lang;
import net.totobirdcreations.jigsawlogiclib.init.Main;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;


public class LogicCommandManager {

    private static final HashMap<Identifier, ArrayList<RunCommandCallback>> commands = new HashMap<>();


    public static void register(Identifier command, RunCommandCallback factory) {
        if (! commands.containsKey(command)) {
            commands.put(command, new ArrayList<>());
        }
        commands.get(command).add(factory);
    }


    public enum Response {
        SUCCESS,
        MISSING,
        CRASH;

        public void sendToPlayer(@Nullable ServerPlayerEntity player, Identifier command) {
            if (player != null) {
                MutableText text = Text.translatable(switch (this) {
                    case SUCCESS -> Lang.RUN_SUCCESS;
                    case MISSING -> Lang.RUN_FAILED_MISSING;
                    case CRASH   -> Lang.RUN_FAILED_CRASH;
                }, command);
                if (this != SUCCESS) {
                    text = text.formatted(Formatting.RED);
                }
                player.sendMessage(text);
            }
        }
    }


    public static Response run(Identifier command, String metadata, World world, BlockPos blockPos, BlockPos structureOrigin) {
        if (commands.containsKey(command)) {
            ArrayList<RunCommandCallback> callbacks = commands.get(command);
            if (callbacks.size() > 0) {
                Response response = Response.SUCCESS;
                for (RunCommandCallback callback : callbacks) {
                    try {
                        callback.run(metadata, world, blockPos, structureOrigin);
                    } catch (Exception e) {
                        Main.LOGGER.error("Jigsaw logic handler registered for `" + command.toString() + "` failed.");
                        e.printStackTrace();
                        response = Response.CRASH;
                    }
                }
                return response;
            }
        }
        Main.LOGGER.warn("No jigsaw logic handler registered for `" + command.toString() + "`. Skipping command.");
        return Response.MISSING;
    }


    public interface RunCommandCallback {
        void run(String metadata, World world, BlockPos blockPos, BlockPos structureOrigin) throws Exception;
    }

}
