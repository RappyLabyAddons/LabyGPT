package com.rappytv.labygpt.commands.subcommands;

import com.rappytv.labygpt.GPTAddon;
import com.rappytv.labygpt.api.GPTMessage;
import com.rappytv.labygpt.api.GPTRole;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class GPTHistorySubCommand extends SubCommand {

    public GPTHistorySubCommand() {
        super("history");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        if(GPTAddon.queryHistory.size() < 2) {
            displayMessage(Component.empty().append(GPTAddon.prefix).append(Component.translatable("labygpt.messages.emptyHistory", NamedTextColor.RED)));
            return true;
        }

        Component component = Component.empty();
        for(int i = 0; i < GPTAddon.queryHistory.size(); i++) {
            GPTMessage message = GPTAddon.queryHistory.get(i);
            if(message.role == GPTRole.System) continue;
            String name = message.name.isEmpty() ? labyAPI.getName() : message.name;

            component
                .append(Component.text(i == 1 ? "" : "\n"))
                .append(Component.text("[", NamedTextColor.DARK_GRAY))
                .append(Component.text(name, message.role == GPTRole.Assistant ? NamedTextColor.BLUE : NamedTextColor.RED))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(message.content.replace("\n\n", ""), NamedTextColor.WHITE));
        }

        displayMessage(component);
        return true;
    }
}
