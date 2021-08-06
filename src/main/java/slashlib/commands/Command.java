package slashlib.commands;

/**
 * A class representing a top-level slash command.
 */
public abstract class Command extends BaseCommand {
    public Command(String name, String description) {
        super(name, description, null);
    }
}
