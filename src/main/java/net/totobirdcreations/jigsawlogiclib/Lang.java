package net.totobirdcreations.jigsawlogiclib;


import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.totobirdcreations.jigsawlogiclib.init.Main;


public class Lang {

    private static final String      CENTER             = "." + Main.NAME + ".";
    public  static final MutableText TITLE              = Text.translatable("screen"   + CENTER + "title");
    public  static final MutableText COMMAND            = Text.translatable("screen"   + CENTER + "command");
    public  static final MutableText METADATA           = Text.translatable("screen"   + CENTER + "metadata");
    public  static final MutableText RUN                = Text.translatable("screen"   + CENTER + "run");

    public  static final String SET_SUCCESS        = "feedback" + CENTER + "set.success";

    public  static final String RUN_SUCCESS        = "feedback" + CENTER + "run.success";
    public  static final String RUN_FAILED_MISSING = "feedback" + CENTER + "run.failed.missing";
    public  static final String RUN_FAILED_CRASH   = "feedback" + CENTER + "run.failed.crash";

}
