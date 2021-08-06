package slashlib;

import discord4j.rest.service.ApplicationService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import slashlib.commands.BaseCommand;

import java.util.List;

public class CommandRegister {
    //private static final Logger logger = LoggerFactory.getLogger(CommandRegister.class);

    private final CommandStructure commands;

    protected CommandRegister() {
        this.commands = new CommandStructure();
    }

    static CommandRegister create(List<BaseCommand> commandList) {
        CommandRegister commandRegister = new CommandRegister();
        for (BaseCommand command : commandList) {
            commandRegister.addCommand(command);
        }
        return commandRegister;
    }

    private void addCommand(BaseCommand command) {
        this.commands.addSubCommand(command);
    }


    public Mono<Void> registerSlashCommands(ApplicationService applicationService, long applicationId) {
        //logger.info("Registering global slash commands ...");
        return Flux.fromIterable(commands.getSubCommands().values())
            .map(BaseCommand::asRequest)
            .collectList()
            .flatMapMany(requests -> applicationService.bulkOverwriteGlobalApplicationCommand(applicationId, requests))
            // Get all created slash commands and set the permission role on them
//            .map(acd -> Tuples.of(acd, commands.getSubCommand(acd.name())))
//            .filter(tuple -> !tuple.getT2().getUserPermission().isEmpty())
//            .flatMap(tuple -> AudyClient.getClient().getGateway().getGuilds()
//                .flatMap(guild -> GuildConfig.getGuildConfig(guild)
//                    .flatMap(gc -> Mono.justOrEmpty(gc.getSlashModRoleId()))
//                    .flatMap(guild::getRoleById)
//                    .flatMap(role -> applicationService.modifyApplicationCommandPermissions(
//                        applicationId,
//                        guild.getId().asLong(),
//                        Long.parseLong(tuple.getT1().id()),
//                        ApplicationCommandPermissionsRequest.builder()
//                            .addPermission(ApplicationCommandPermissionsData.builder()
//                                .id(role.getId().asLong()).build())
//                            .build())).thenReturn(1)))
            .count()
            .doOnNext(count -> {
                //noinspection StatementWithEmptyBody
                if (count > 0) {
                    //logger.info(count + " global slash commands registered.");
                } else {
                    //logger.info("Global slash commands are upto date.");
                }
            })
            .then();
    }

    public CommandStructure getCommands() { return commands; }
}
