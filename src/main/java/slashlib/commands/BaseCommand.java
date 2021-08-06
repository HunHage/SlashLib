package slashlib.commands;

import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData;
import discord4j.rest.util.ApplicationCommandOptionType;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;
import slashlib.context.SlashCommandContext;
import slashlib.context.SlashCommandContextBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A class which represents all types of Slash Commands.
 */
public abstract class BaseCommand {
    // Command Name
    private final String name;
    // Command Description
    private final String description;
    // The type of command this is
    private final ApplicationCommandOptionType type;
    // Options for the command
    private final List<ApplicationCommandOptionData> options;
    // Permission needed by the calling user
    private final PermissionSet userPermissions;
    // Permissions needed by the bot
    private final PermissionSet botPermissions;

    // Sometimes null: SubCommands
    protected final Map<String, BaseCommand> subCommands;

    protected BaseCommand(String name,
                          String description,
                          @Nullable ApplicationCommandOptionType type) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.options = new ArrayList<>();
        this.userPermissions = PermissionSet.none();
        this.botPermissions = PermissionSet.none();

        if (type == ApplicationCommandOptionType.SUB_COMMAND_GROUP) {
            this.subCommands = new HashMap<>();
        } else {
            this.subCommands = null;
        }
    }

    public abstract Mono<SlashCommandContext> execute(SlashCommandContext context);

    public abstract SlashCommandContextBuilder setRequestData(SlashCommandContextBuilder context);

    public ApplicationCommandOptionData asOptionData() {
        return ApplicationCommandOptionData.builder()
            .name(this.name)
            .description(this.description)
            .type(this.type.getValue())
            .options(this.options)
            .build();
    }

    public ApplicationCommandRequest asRequest() {
        return ApplicationCommandRequest.builder()
            .name(this.name)
            .description(this.description)
            .addAllOptions(this.options)
            .defaultPermission(userPermissions.isEmpty())
            .build();
    }

    public void addOption(ApplicationCommandOptionData option) {
        this.options.add(option);
    }

    public void addOption(Consumer<ImmutableApplicationCommandOptionData.Builder> option) {
        ImmutableApplicationCommandOptionData.Builder builder = ApplicationCommandOptionData.builder();
        option.accept(builder);
        this.options.add(builder.build());
    }

    public BaseCommand getSubCommand(String name) {
        return subCommands.get(name);
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public ApplicationCommandOptionType getType() { return type; }
    public List<ApplicationCommandOptionData> getOptions() { return options; }
    public PermissionSet getUserPermission() { return userPermissions; }
    public PermissionSet getBotPermissions() { return botPermissions; }
    public Map<String, BaseCommand> getSubCommands() { return subCommands; }
}
