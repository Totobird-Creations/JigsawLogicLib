package net.totobirdcreations.jigsawlogiclib.init;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.totobirdcreations.jigsawlogiclib.Lang;
import net.totobirdcreations.jigsawlogiclib.api.Example;
import net.totobirdcreations.jigsawlogiclib.api.LogicCommandManager;
import net.totobirdcreations.jigsawlogiclib.logic.LogicBlock;
import net.totobirdcreations.jigsawlogiclib.logic.LogicBlockEntity;
import net.totobirdcreations.jigsawlogiclib.logic.LogicScreenHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main implements ModInitializer {

	public static final String     NAME   = "jigsawlogiclib";
	public static final Identifier ID     = new Identifier(NAME, "logic");
	public static final Logger     LOGGER = LoggerFactory.getLogger("JigsawLogicLib");

	public static final Identifier CHANNEL = new Identifier(NAME, "update");


	public static final Block BLOCK = Registry.register(
			Registry.BLOCK, ID,
			new LogicBlock(FabricBlockSettings
					.copyOf(Blocks.JIGSAW)
			)
	);
	public static final Item ITEM = Registry.register(
			Registry.ITEM, ID,
			new BlockItem(BLOCK, copySettings(Items.JIGSAW))
	);
	public static final BlockEntityType<LogicBlockEntity> BLOCK_ENTITY = Registry.register(
			Registry.BLOCK_ENTITY_TYPE, ID,
			FabricBlockEntityTypeBuilder.create(
					LogicBlockEntity::new,
					BLOCK
			).build(null)
	);
	public static final ScreenHandlerType<LogicScreenHandler> SCREEN_HANDLER = Registry.register(
			Registry.SCREEN_HANDLER, ID,
			new ExtendedScreenHandlerType<>(LogicScreenHandler::new)
	);
	static {
		ServerPlayNetworking.registerGlobalReceiver(
				CHANNEL,
				(server, player, handler, buf, sender) -> receive(server, player, buf)
		);
	}


	public static FabricItemSettings copySettings(Item item) {
		ItemStack stack = new ItemStack(item);
		FabricItemSettings settings = new FabricItemSettings()
				.group(item.getGroup())
				.rarity(item.getRarity(stack))
				.recipeRemainder(item.getRecipeRemainder())
				.maxDamage(item.getMaxDamage())
				.maxCount(item.getMaxCount())
				.food(item.getFoodComponent());
		if (item.isFireproof()) {
			settings.fireproof();
		}
		return settings;
	}


	public static void receive(MinecraftServer server, ServerPlayerEntity player, PacketByteBuf buf) {
		BlockPos pos = buf.readBlockPos();
		Identifier command;
		try {
			command = new Identifier(buf.readString());
		} catch (InvalidIdentifierException e) {
			command = new Identifier("");
		}
		Identifier finalCommand = command; // Fix for: Variable used in lambda expression should be final or effectively final.
		String metadata = buf.readString();
		boolean run = buf.readBoolean();
		server.execute(() -> receive(player, pos, finalCommand, metadata, run));
	}
	public static void receive(ServerPlayerEntity player, BlockPos pos, Identifier command, String metadata, boolean run) {
		if (player.isCreativeLevelTwoOp()) {
			ServerWorld world = player.getWorld();
			if (world.getBlockEntity(pos) instanceof LogicBlockEntity entity) {
				entity.data.command = command;
				entity.data.metadata = metadata;
				entity.markDirty();
				if (run) {
					entity.run(null, player);
				} else {
					player.sendMessage(Text.translatable(Lang.SET_SUCCESS, command));
				}
			}
		}
	}


	@Override
	public void onInitialize() {
		LogicCommandManager.register(
				new Identifier("empty"),
				(metadata, world, blockPos) -> {}
		);

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			Example.enable();
		}
	}

}
