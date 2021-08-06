package slashlib;

import slashlib.commands.BaseCommand;

import java.util.List;

public class SlashLib {
    private CommandListener commandListener;
    private CommandRegister commandRegister;

    private SlashLib() {
        this.commandListener = null;
        this.commandRegister = null;
    }

    SlashLib(SlashLibBuilder builder) {
        this.commandListener = builder.commandListener;
        this.commandRegister = builder.commandRegister;
    }

    public static SlashLib create(List<BaseCommand> commands) {
        SlashLib slashLib = new SlashLib();
        slashLib.commandListener = new CommandListener(slashLib);
        slashLib.commandRegister = CommandRegister.create(commands);
        return slashLib;
    }

    SlashLib create(SlashLibBuilder builder) {
        return new SlashLib(builder);
    }

    public CommandListener getCommandListener() { return commandListener; }
    public CommandRegister getCommandRegister() { return commandRegister; }
}
