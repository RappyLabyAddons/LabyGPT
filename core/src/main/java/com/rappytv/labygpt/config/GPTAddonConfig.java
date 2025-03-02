package com.rappytv.labygpt.config;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
@SpriteTexture("settings")
public class GPTAddonConfig extends AddonConfig {

    @SpriteSlot(size = 32)
    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
    @SpriteSlot(size = 32, x = 2, y = 1)
    @SwitchSetting
    private final ConfigProperty<Boolean> saveHistory = new ConfigProperty<>(true);
    @SpriteSlot(size = 32, x = 1)
    private final OpenAISubConfig openAI = new OpenAISubConfig();
    @SpriteSlot(size = 32, x = 1)
    private final GPTSubConfig gpt = new GPTSubConfig();

    @Override
    public ConfigProperty<Boolean> enabled() {
        return this.enabled;
    }
    public OpenAISubConfig openAI() {
        return this.openAI;
    }
    public GPTSubConfig gpt() {
        return this.gpt;
    }
    public Boolean saveHistory() {
        return this.saveHistory.get();
    }
}
