# SlashLib: An Interaction Command Framework for [Discord4J](https://github.com/Discord4J/Discord4J)
- Supports `CHAT_INPUT`, `AUTOCOMPLETE`, `USER`, and `MESSAGE` interactions.
- Supports interactions in private message channels and per-guild.
- Simple setup, extend abstract classes to create the structure of your commands.
- Interaction structure is validated locally to help catch errors.
- Interactions are checked against Discord and only created/modified/deleted if needed.
- Interactions can specify what permissions they need the bot/user to have.
- Interactions can specify what data they require before they are called (Guild, MessageChannel, GuildChannel, etc)
- A custom interaction listener can be used in place of, extend, or replace the built-in one.
- Custom context classes can extend the provided ones to fetch custom data before command execution.
  
Some helper classes are included to:
- [Clean up reading options for commands](https://github.com/HunHage/SlashLib/blob/master/src/main/java/net/exploitables/slashlib/utility/OptionsList.java).
- [Properly create options for commands](https://github.com/HunHage/SlashLib/blob/master/src/main/java/net/exploitables/slashlib/utility/OptionBuilder.java).

SlashLib does not handle:
- Discord Interaction Permissions, but there is no conflict if they are used.

# Getting Started
Simple full-working examples are provided in the [test package](https://github.com/HunHage/SlashLib/tree/master/src/test/java/net/exploitables/slashlib).
<br />
- `basic`: Use most features offered by SlashLib.
- `context`: Extend context classes to add your custom classes to the command lifecycle.
- `guild`: Create/Remove guild commands.
- `receiver`: Use a custom receiver to add pre-/post-interaction logic or use custom logic.
- `response`: Builds on `context`, adds a custom class to the command lifecycle to get post-execution information.

To visualize the relationship between how Discord describes commands and how SlashLib abstracts them, here's an example of a valid command structure similar to [how Discord describes it](https://discord.com/developers/docs/interactions/application-commands#subcommands-and-subcommand-groups): 
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
