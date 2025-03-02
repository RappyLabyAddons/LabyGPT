package com.rappytv.labygpt.command;

import com.rappytv.labygpt.GPTAddon;
import com.rappytv.labygpt.api.ChatMessage;
import com.rappytv.labygpt.api.ChatMessage.ChatRole;
import com.rappytv.labygpt.api.GPTRequest;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import java.util.Objects;

public class GPTCommand extends Command {

    private final GPTAddon addon;

    public GPTCommand(GPTAddon addon) {
        super("gpt");
        this.addon = addon;

        this.translationKey("labygpt.commands.gpt");
        this.withSubCommand(new ClearSubCommand());
        this.withSubCommand(new HistorySubCommand());
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        if (this.addon.configuration().openAI().bearer().isEmpty()) {
            this.displayMessage(
                Component.empty()
                    .append(GPTAddon.prefix)
                    .append(Component.translatable(
                        this.getTranslationKey("noKey"),
                        NamedTextColor.RED
                    ))
            );
            return true;
        }
        if (arguments.length < 1) {
            this.displayMessage(
                Component.empty()
                    .append(GPTAddon.prefix)
                    .append(Component.translatable(
                        this.getTranslationKey("noQuery"),
                        NamedTextColor.RED
                    ))
            );
            return true;
        }

        if (!this.addon.configuration().saveHistory()) {
            GPTRequest.queryHistory.clear();
        }
        String model = !this.addon.configuration().gpt().overrideModel().get().isBlank()
            ? this.addon.configuration().gpt().overrideModel().get()
            : this.addon.configuration().gpt().model().get();
        GPTRequest.sendRequestAsync(
            String.join(" ", arguments),
            this.addon.configuration().openAI().bearer(),
            this.addon.configuration().openAI().shareUsername() ? this.labyAPI.getName() : "",
            model.toLowerCase(),
            this.addon.configuration().gpt().behavior().get(),
            (response) -> {
                if (!response.successful() || response.output() == null) {
                    this.displayMessage(
                        Component.empty()
                            .append(GPTAddon.prefix)
                            .append(Component.text(
                                Objects.requireNonNullElse(
                                    response.error(),
                                    this.getTranslationKey("requestError")
                                ),
                                NamedTextColor.RED
                            ))
                    );
                } else {
                    this.displayMessage(
                        Component.empty()
                            .append(GPTAddon.prefix)
                            .append(Component.text(response.output(), NamedTextColor.WHITE)));
                }
            }
        );

        return true;
    }

    public static class ClearSubCommand extends SubCommand {

        public ClearSubCommand() {
            super("clear");

            this.translationKey("labygpt.commands.clear");
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(GPTRequest.queryHistory.isEmpty()) {
                this.displayMessage(
                    Component.empty()
                        .append(GPTAddon.prefix)
                        .append(Component.translatable(
                            this.getTranslationKey("alreadyEmpty"),
                            NamedTextColor.RED
                        ))
                );
                return true;
            }
            GPTRequest.queryHistory.clear();
            this.displayMessage(
                Component.empty()
                    .append(GPTAddon.prefix)
                    .append(Component.translatable(
                        this.getTranslationKey("success"),
                        NamedTextColor.GREEN
                    ))
            );
            return true;
        }
    }

    public static class HistorySubCommand extends SubCommand {

        public HistorySubCommand() {
            super("history");

            this.translationKey("labygpt.commands.history");
        }

        @Override
        public boolean execute(String prefix, String[] arguments) {
            if(GPTRequest.queryHistory.size() < 2) {
                this.displayMessage(
                    Component.empty()
                        .append(GPTAddon.prefix)
                        .append(Component.translatable(
                            this.getTranslationKey("empty"),
                            NamedTextColor.RED
                        ))
                );
                return true;
            }

            Component component = Component.empty();
            for(int i = 0; i < GPTRequest.queryHistory.size(); i++) {
                ChatMessage message = GPTRequest.queryHistory.get(i);
                String name = message.name.isEmpty() ? this.labyAPI.getName() : message.name;
                if(message.role != ChatRole.DEVELOPER)
                    component
                        .append(Component.text(i == 1 ? "" : "\n"))
                        .append(Component.text("[", NamedTextColor.DARK_GRAY))
                        .append(Component.text(name, message.role == ChatRole.ASSISTANT ? NamedTextColor.BLUE : NamedTextColor.YELLOW))
                        .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                        .append(Component.text(message.content, NamedTextColor.GRAY));
            }

            this.displayMessage(component);
            return true;
        }
    }
}