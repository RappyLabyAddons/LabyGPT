package com.rappytv.labygpt;

import com.rappytv.labygpt.command.GPTCommand;
import com.rappytv.labygpt.config.GPTAddonConfig;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class GPTAddon extends LabyAddon<GPTAddonConfig> {

    public static final Component prefix = Component.empty()
        .append(Component.text("[", NamedTextColor.DARK_GRAY))
        .append(Component.text("LabyGPT", NamedTextColor.BLUE))
        .append(Component.text("] ", NamedTextColor.DARK_GRAY));
    public static final String[] models = new String[]{"gpt-3.5-turbo", "gpt-4-turbo", "gpt-4", "chatgpt-4o-latest", "gpt-4o", "gpt-4o-mini"};

    @Override
    protected void enable() {
        this.registerSettingCategory();

        this.registerCommand(new GPTCommand(this));
    }

    @Override
    protected Class<? extends GPTAddonConfig> configurationClass() {
        return GPTAddonConfig.class;
    }
}
