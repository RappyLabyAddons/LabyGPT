package com.rappytv.labygpt.config;

import com.rappytv.labygpt.GPTAddon;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingListener;
import net.labymod.api.configuration.settings.annotation.SettingListener.EventType;
import net.labymod.api.configuration.settings.type.SettingElement;
import java.util.Collections;

public class GPTSubConfig extends Config {

    @SpriteSlot(size = 32, x = 1)
    @DropdownSetting
    private final ConfigProperty<String> model = new ConfigProperty<>(GPTAddon.models[0]);
    @SpriteSlot(size = 32, x = 1)
    @TextFieldSetting
    private final ConfigProperty<String> overrideModel = new ConfigProperty<>("");
    @SpriteSlot(size = 32, x = 1, y = 1)
    @TextFieldSetting
    private final ConfigProperty<String> behavior = new ConfigProperty<>("You are a helpful assistant.");

    @SuppressWarnings("unchecked")
    @SettingListener(target = "model", type = EventType.INITIALIZE)
    public void initialize(SettingElement setting) {
        DropdownWidget<String> widget = (DropdownWidget<String>) setting.asElement().getWidgets()[0];
        widget.addAll(GPTAddon.models);
        setting.setWidgets(Collections.singletonList(widget)
            .toArray(new Widget[0]));
    }

    public ConfigProperty<String> model() {
        return this.model;
    }
    public ConfigProperty<String> overrideModel() {
        return this.overrideModel;
    }
    public ConfigProperty<String> behavior() {
        return this.behavior;
    }
}
