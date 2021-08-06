package slashlib;

import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import slashlib.commands.BaseCommand;
import slashlib.commands.GroupCommand;
import slashlib.context.SlashCommandContextBuilder;

import java.util.List;

/**
 * A representation of the command structure used for slash commands.
 *
 * Some notes on my (HunHage) observations about slash commands
 * - Each "path" in the command tree must lead to a command.
 * -- This means if /command sub_command is a command then /command is not a callable command.
 * -- Same goes for /command group_command sub_command for command and group_command.
 * - The "name" of any type of command is just the name of that command.
 * -- /command group_command sub_command each have their own names and don't contain each other.
 * -- Discord seems to only navigate down, not up. See the next note section about options.
 * - The Group and Sub commands are options of the above command.
 * -- Knowing about the "path" and "name" means that if a command is not callable then it will
 *     have one option of type GROUP_COMMAND or SUB_COMMAND. We can assume that if there is 0 options,
 *     1 option that is not group/sub, or 2+ options of any type then the command is callable. This is key.
 *
 * With the above observations a tree is the optimal choice for organizing slash commands by name.
 * That is what this class exists to do.
 *
 * Where Discord slash commands exist as:
 * Root
 * - Command
 * - Command
 * -- SubCommand
 * -- SubCommand
 * -- GroupCommand
 * --- SubCommand
 *
 * The Tree will use classes as:
 * CommandStructure
 * - Command
 * - GroupCommand
 * -- SubCommand
 * -- SubCommand
 * -- GroupCommand
 * --- SubCommand
 *
 * Commands need no knowledge of their parent or involvement in the tree.
 * Commands with children are always GroupCommands.
 * Commands with parents are always SubCommands.
 *
 * Since only Commands and SubCommands are callable, GroupCommands are used for each uncallable command.
 *
 * This class extends GroupCommand as it simplifies the search process. This could be viewed as a RootGroupCommand.
 */
public class CommandStructure extends GroupCommand {
    /**
     * Create the Command Structure used for Slash Commands
     */
    public CommandStructure() {
        super("root", "a faux root command");
    }

    /**
     * Get the Command called with a slash command.
     * Since Discord only allows callable commands to be called, we will have a command to return.
     *
     * @param aci the ApplicationCommandInteraction for a Slash Command
     * @return the Command being called
     */
    public Pair<BaseCommand, SlashCommandContextBuilder>
            searchForCommand(SlashCommandEvent event, ApplicationCommandInteraction aci) {
        List<ApplicationCommandInteractionOption> options = aci.getOptions();
        // If there is only one option and it's a SubCommand or SubCommandGroup option then search for it
        if (options.size() == 1 && options.get(0).getType().getValue() < 3) {
            Pair<BaseCommand, List<ApplicationCommandInteractionOption>> command =
                    searchForCommand(this.getSubCommand(aci.getName().orElse(null)), options);
            return new Pair<>(command.getKey(), new SlashCommandContextBuilder(event, aci, command.getValue()));
        } else {
            return new Pair<>(this.getSubCommand(aci.getName().orElse(null)), new SlashCommandContextBuilder(event, aci, options));
        }
    }

    /**
     * Search for a second or third level command recursively
     * @param command the command to check and search
     * @param options the options for this command
     * @return a callable command
     */
    private Pair<BaseCommand, List<ApplicationCommandInteractionOption>>
            searchForCommand(BaseCommand command, List<ApplicationCommandInteractionOption> options) {
        // If there is only one option and it's a SubCommand or SubCommandGroup option then search for it
        if (options.size() == 1 && options.get(0).getType().getValue() < 3) {
            ApplicationCommandInteractionOption option = options.get(0);
            return searchForCommand(command.getSubCommand(option.getName()), option.getOptions());
        } else {
            return new Pair<>(command, options);
        }
    }
}
