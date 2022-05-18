package com.mcsmds.restrictedtweaker;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class DebounceDebug {

    private static Map<String, Timer> timermap = new HashMap<String, Timer>();

    public static void debug(String message, String classname) {
        String timerid = message + classname;
        if (timermap.get(timerid) != null) {
            timermap.get(timerid).cancel();
        }
        timermap.put(timerid, new Timer());
        timermap.get(timerid).schedule(new TimerTask() {
            @Override
            public void run() {
                showDebug(message, classname);
                timermap.remove(timerid);
            }
        }, 100);
    }

    private static void showDebug(String message, String classname) {
        if (!RestrictedTweaker.Configs.debug || FMLCommonHandler.instance().getSide().isServer())
            return;
        Minecraft mc = Minecraft.getMinecraft();
        GuiNewChat guinewchat = mc.ingameGUI.getChatGUI();

        TextComponentTranslation hoverText = new TextComponentTranslation("复制到聊天框");
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, classname);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText);

        ITextComponent part1 = new TextComponentString("")
                .setStyle(new Style().setClickEvent(clickEvent).setHoverEvent(hoverEvent));
        ITextComponent part2 = new TextComponentTranslation("debug.prefix")
                .setStyle(new Style().setColor(TextFormatting.YELLOW).setBold(true));
        ITextComponent part3 = new TextComponentString(classname);

        guinewchat.printChatMessage(part1.appendSibling(part2).appendText(" " + message + " ").appendSibling(part3));
    }

}