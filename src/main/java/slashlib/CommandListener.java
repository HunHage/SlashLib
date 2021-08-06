package slashlib;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.interaction.SlashCommandEvent;
import reactor.core.publisher.Mono;
import slashlib.context.SlashCommandContextBuilder;

public class CommandListener {
    private final SlashLib slashLib;

    protected CommandListener(SlashLib slashLib) {
        this.slashLib = slashLib;
    }

    public Mono<Void> receiveSlashCommandEvent(SlashCommandEvent event) {
        return Mono.justOrEmpty(event.getInteraction().getCommandInteraction())
            .map(aci -> slashLib.getCommandRegister().getCommands().searchForCommand(event, aci))
            .flatMap(pair -> pair.getKey().setRequestData(pair.getValue())
                .collectData()
                .map(SlashCommandContextBuilder::build)
                .flatMap(context -> pair.getKey().execute(context)))
            .then();
    }

    public void registerAsListener(GatewayDiscordClient gateway) {
        gateway.on(SlashCommandEvent.class, this::receiveSlashCommandEvent).subscribe();
    }

    public void registerAsListener(EventDispatcher eventDispatcher) {
        eventDispatcher.on(SlashCommandEvent.class, this::receiveSlashCommandEvent).subscribe();
    }
}
