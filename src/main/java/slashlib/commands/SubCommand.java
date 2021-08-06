package slashlib.commands;

import discord4j.rest.util.ApplicationCommandOptionType;

/**
 * A Class which represents a sub command, or any top level command that is not top level.
 */
public abstract class SubCommand extends BaseCommand {
    protected SubCommand(String name, String description) {
        super(name, description, ApplicationCommandOptionType.SUB_COMMAND);
    }
}
