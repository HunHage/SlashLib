package slashlib;

import slashlib.commands.BaseCommand;

import java.util.ArrayList;
import java.util.List;

public class SlashLibBuilder {
    CommandListener commandListener;
    CommandRegister commandRegister;
    CommandStructure commandStructure;
    List<BaseCommand> commands;

    protected SlashLibBuilder() {
        this.commandListener = null;
        this.commandRegister = null;
        this.commandStructure = null;
        this.commands = new ArrayList<>();
    }

    public SlashLibBuilder setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
        return this;
    }

    public SlashLibBuilder setCommandRegister(CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        return this;
    }

    public SlashLibBuilder setCommandStructure(CommandStructure commandStructure) {
        this.commandStructure = commandStructure;
        return this;
    }

    public SlashLibBuilder addCommand(BaseCommand command) {
        commands.add(command);
        return this;
    }

    public SlashLib build() {
        return new SlashLib(this);
    }
}
