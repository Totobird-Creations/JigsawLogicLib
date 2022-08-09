package net.totobirdcreations.jigsawlogiclib.logic;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.totobirdcreations.jigsawlogiclib.Lang;
import net.totobirdcreations.jigsawlogiclib.init.Main;

import java.util.function.Consumer;


@Environment(EnvType.CLIENT)
public class LogicScreen extends HandledScreen<LogicScreenHandler> {

    private final BlockPos pos;
    private String command;
    private String metadata;

    private Integer currentRowHeight = null;
    private Integer currentFieldY    = null;
    private TextFieldWidget commandField;
    private TextFieldWidget metadataField;


    public LogicScreen(LogicScreenHandler handler, PlayerInventory inventory, Text ignored) {
        super(handler, inventory, Text.empty());
        this.pos = handler.pos;
        this.command = handler.command;
        this.metadata = handler.metadata;
    }



    @Override
    protected void init() {
        super.init();
        assert this.client != null;

        this.backgroundWidth = this.width;
        this.backgroundHeight = this.height;
        this.playerInventoryTitle = Text.empty();

        this.client.keyboard.setRepeatEvents(true);

        this.currentFieldY = 50;

        this.commandField = this.addWidget(this.newTextFieldWidget(
                Lang.COMMAND,
                this.command,
                (value) -> this.command = value
        ));
        this.metadataField = this.addWidget(this.newTextFieldWidget(
                Lang.METADATA,
                this.metadata,
                (value) -> this.metadata = value
        ));

        this.addWidget(this.newButtonWidget(
                ScreenTexts.DONE,
                (button) -> this.update(false),
                4, 0
        ), false);
        this.addWidget(this.newButtonWidget(
                Lang.RUN,
                (button) -> this.update(true),
                4, 1
        ), false);
        this.addWidget(this.newButtonWidget(
                ScreenTexts.CANCEL,
                (button) -> this.close(),
                4, 3
        ));
    }


    private TextFieldWidget newTextFieldWidget(Text text, String value, Consumer<String> changedCallback) {
        TextFieldWidget widget = new TextFieldWidget(
                this.textRenderer,
                this.width / 2 - 154, 0,
                308, 20,
                text
        );
        widget.setText(value);
        widget.setMaxLength(128);
        widget.setChangedListener(changedCallback);
        this.addSelectableChild(widget);
        return widget;
    }

    private ButtonWidget newButtonWidget(Text text, ButtonWidget.PressAction pressedCallback, int split, int splitIndex) {
        final int separation = 8;
        int width = (308 + separation) / split;
        ButtonWidget widget = new ButtonWidget(
                ((this.width / 2 - 154) + (width * splitIndex)), 0,
                width - separation, 20,
                text,
                pressedCallback
        );
        this.addDrawableChild(widget);
        return widget;
    }

    private <T extends ClickableWidget> T addWidget(T widget) {
        return this.addWidget(widget, true);
    }
    private <T extends ClickableWidget> T addWidget(T widget, boolean down) {
        widget.y = this.currentFieldY;
        this.currentRowHeight = Math.max(
                this.currentRowHeight == null ? 0 : this.currentRowHeight,
                widget.getHeight()
        );
        if (down) {
            this.currentFieldY += this.currentRowHeight + 15;
        }
        return widget;
    }



    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.renderBackground(matrices);

        this.drawTitle(matrices);
        this.drawTextFieldLabel(this.commandField, matrices, mouseX, mouseY, delta);
        this.drawTextFieldLabel(this.metadataField, matrices, mouseX, mouseY, delta);

    }


    private void drawTitle(MatrixStack matrices) {
        this.textRenderer.drawWithShadow(
                matrices,
                Lang.TITLE,
                this.width / 2.0f - this.textRenderer.getWidth(Lang.TITLE) / 2.0f, 20,
                Formatting.WHITE.getColorValue()
        );
    }

    private void drawTextFieldLabel(TextFieldWidget widget, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        widget.render(matrices, mouseX, mouseY, delta);
        this.textRenderer.drawWithShadow(
                matrices,
                widget.getMessage(),
                widget.x, widget.y - this.textRenderer.fontHeight - 1.0f,
                Formatting.GRAY.getColorValue()
        );
    }



    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 258) {
            boolean shift = ! hasShiftDown();
            if (! this.changeFocus(shift)) {
                this.changeFocus(shift);
            }
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }



    private void update(boolean run) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(this.pos);
        buf.writeString(this.command);
        buf.writeString(this.metadata);
        buf.writeBoolean(run);
        Packet<?> packet = ClientPlayNetworking.createC2SPacket(Main.CHANNEL, buf);
        ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();
        if (handler != null) {
            handler.sendPacket(packet);
        }
        this.close();
    }

}
