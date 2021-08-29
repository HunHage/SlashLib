# SlashLib: A Slash Command Framework for [Discord4J](https://github.com/Discord4J/Discord4J)
- Simple setup, extend abstract classes to create the structure of your commands.
- Class types are checked to better describe issues when creating the command structure.
- Command Structure is checked when comparing local commands against Discord for invalid configurations.
- Commands are checked against Discord and only created/modified/deleted if needed.
- Commands can specify what permissions they need the bot to have.
- Commands can specify what data they require before they are called (Guild, MessageChannel, GuildChannel, or Member)
- A custom command listener can be used in place of the built-in one.
- A helper class is included to [clean up reading options for commands](https://github.com/HunHage/SlashLib/blob/master/src/main/java/net/exploitables/slashlib/utility/OptionsList.java).
- A helper class is included to [properly create options for commands](https://github.com/HunHage/SlashLib/blob/master/src/main/java/net/exploitables/slashlib/utility/OptionBuilder.java).

SlashLib does not handle:
- Guild Slash Commands
- Slash Command Permissions (although an [example](https://github.com/HunHage/SlashLib/blob/master/src/test/java/net/exploitables/slashlib/examples/ExamplePermissionUsage.java) is provided on how you can implement them)

# Getting Started
Simple full-working examples are provided in the [test package](https://github.com/HunHage/SlashLib/tree/master/src/test/java/net/exploitables/slashlib).

To visualize the relationship between how Discord describes commands and how SlashLib abstracts them, here's an example of a valid command structure similar to [how Discord describes it](https://discord.com/developers/docs/interactions/slash-commands#nested-subcommands-and-groups): 
```
Command (ping)
Command
- Sub Command (print)
Command
- Sub Command (echo)
- Sub Command Group
- - Sub Command (info)
```

You'll need to extend abstract classes representing each of these levels and command types, the equivalent would be:
```
TopCommand (ping)
TopGroupCommand
- SubCommand (print)
TopGroupCommand
- SubCommand (echo)
- MidGroupCommand
- - SubCommand (info)
```
