package com.rappytv.labygpt.commands;

import com.rappytv.labygpt.GPTAddon;
import com.rappytv.labygpt.api.GPTRequest;
import com.rappytv.labygpt.commands.subcommands.GPTClearSubCommand;
import com.rappytv.labygpt.commands.subcommands.GPTHistorySubCommand;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.I18n;
import java.util.Objects;

public class GPTCommand extends Command {

    private final GPTAddon addon;

    public GPTCommand(GPTAddon addon) {
        super("gpt");

        this.addon = addon;
        withSubCommand(new GPTClearSubCommand());
        withSubCommand(new GPTHistorySubCommand());
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        if (addon.configuration().openAI().bearer().isEmpty()) {
            displayMessage(Component.empty().append(GPTAddon.prefix)
                .append(Component.translatable("labygpt.messages.noKey", NamedTextColor.RED)));
            return true;
        }
        if (arguments.length < 1) {
            displayMessage(Component.empty().append(GPTAddon.prefix)
                .append(Component.translatable("labygpt.messages.noQuery", NamedTextColor.RED)));
            return true;
        }

        if (!addon.configuration().saveHistory()) {
            GPTAddon.queryHistory.clear();
        }
        GPTRequest request = new GPTRequest();
        request.sendRequestAsync(
            String.join(" ", arguments),
            addon.configuration().openAI().bearer(),
            addon.configuration().openAI().shareUsername() ? labyAPI.getName() : "Player",
            addon.configuration().gpt().model().get().toLowerCase(),
            addon.configuration().gpt().behavior().get()
        ).thenRun(() -> {
            // Callback logic when the request is done:
            if (!request.isSuccessful() || request.getOutput() == null) {
                GPTAddon.queryHistory.remove(GPTAddon.queryHistory.size() - 1);
                displayMessage(Component.empty().append(GPTAddon.prefix).append(Component.text(
                    Objects.requireNonNullElseGet(request.getError(),
                        () -> I18n.translate("labygpt.messages.requestError")),
                    NamedTextColor.RED)));
            } else {
                displayMessage(Component.empty().append(GPTAddon.prefix)
                    .append(Component.text(request.getOutput(), NamedTextColor.WHITE)));
            }
        }).exceptionally((e) -> {
            displayMessage(Component.empty().append(GPTAddon.prefix)
                .append(Component.text(e.getMessage(), NamedTextColor.RED)));
            return null;
        });

        return true;
    }
}