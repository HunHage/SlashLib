package slashlib.context;

import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import slashlib.OptionsList;

public class SlashCommandContext {
    private final SlashCommandEvent event;
    private final ApplicationCommandInteraction aci;
    private final OptionsList options;

    private final Guild guild;
    private final MessageChannel channel;
    private final GuildChannel guildChannel;
    private final Member member;

    public SlashCommandContext(SlashCommandContextBuilder builder) {
        this.event = builder.event;
        this.aci = builder.aci;
        this.options = builder.options;

        this.guild = builder.guild;
        this.channel = builder.channel;
        this.guildChannel = builder.guildChannel;
        this.member = builder.member;
    }

    public SlashCommandEvent getEvent() { return event; }
    public ApplicationCommandInteraction getAci() { return aci; }

    public OptionsList getOptions() { return options; }
    public Guild getGuild() { return guild; }
    public MessageChannel getChannel() { return channel; }
    public GuildChannel getGuildChannel() { return guildChannel; }
    public Member getMember() { return member; }
}
