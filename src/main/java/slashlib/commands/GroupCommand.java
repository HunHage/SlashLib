package slashlib.commands;

import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.ApplicationCommandOptionType;
import reactor.core.publisher.Mono;
import slashlib.context.SlashCommandContext;
import slashlib.context.SlashCommandContextBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Class representing a Group Command at any level.
 */
public abstract class GroupCommand extends BaseCommand {
    protected GroupCommand(String name, String description) {
        super(name, description, ApplicationCommandOptionType.SUB_COMMAND_GROUP);
    }

    public void addSubCommand(BaseCommand command) {
        Objects.requireNonNull(this.subCommands);
        this.subCommands.put(command.getName(),command);
    }

    @Override
    public Mono<SlashCommandContext> execute(SlashCommandContext context) {
        throw new IllegalStateException("GroupCommand execute was invoked.");
    }

    @Override
    public SlashCommandContextBuilder setRequestData(SlashCommandContextBuilder builder) {
        throw new IllegalStateException("GroupCommand setRequestData was invoked.");
    }

    @Override
    public ApplicationCommandOptionData asOptionData() {
        return ApplicationCommandOptionData.builder()
            .name(this.getName())
            .description(this.getDescription())
            .type(this.getType().getValue())
            .options(this.getOptions())
            .build();
    }

    @Override
    public ApplicationCommandRequest asRequest() {
        return ApplicationCommandRequest.builder()
            .name(this.getName())
            .description(this.getDescription())
            .addAllOptions(this.getOptions())
            .build();
    }

    @Override
    public List<ApplicationCommandOptionData> getOptions() {
        Objects.requireNonNull(this.subCommands);
        List<ApplicationCommandOptionData> options = new ArrayList<>();
        for (BaseCommand command : this.subCommands.values()) {
            options.add(command.asOptionData());
        }
        return options;
    }
}
