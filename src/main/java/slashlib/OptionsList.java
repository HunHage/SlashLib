package slashlib;

import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

import java.util.List;
import java.util.Optional;

/**
 * A wrapper class for a list of {@link ApplicationCommandInteractionOption}.
 *
 * Since Top level commands have their options contained within an {@link ApplicationCommandInteraction}
 *  but subcommands have their options contained within an {@link ApplicationCommandInteractionOption}.
 *
 * Since access to these needs to be uniform this wrapper class adds functionality to the options list.
 */
public class OptionsList {
    private final List<ApplicationCommandInteractionOption> options;

    public OptionsList(List<ApplicationCommandInteractionOption> options) {
        this.options = options;
    }

    public List<ApplicationCommandInteractionOption> getOptions() {
        return this.options;
    }

    public Optional<ApplicationCommandInteractionOption> getOption(String name) {
        return this.options.stream().filter(option -> option.getName().equals(name)).findFirst();
    }
}
