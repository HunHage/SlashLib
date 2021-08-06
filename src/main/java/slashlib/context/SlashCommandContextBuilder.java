package slashlib.context;

import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import reactor.core.publisher.Mono;
import slashlib.OptionsList;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandContextBuilder {
    final SlashCommandEvent event;
    final ApplicationCommandInteraction aci;
    final OptionsList options;

    private final List<Mono<Integer>> collectionSink;

    Guild guild;
    MessageChannel channel;
    GuildChannel guildChannel;
    Member member;

    public SlashCommandContextBuilder(SlashCommandEvent event,
                                      ApplicationCommandInteraction aci,
                                      List<ApplicationCommandInteractionOption> options) {
        this.event = event;
        this.aci = aci;
        this.options = new OptionsList(options);

        this.collectionSink = new ArrayList<>();
        // Certainly an area for improvement
        // Due to how collectData() works an error is thrown if Mono#zip() returns empty
        // This is because some methods of collecting data like requireGuildChannel() can go empty without error
        // So at least one entry must exist for commands that require no data
        this.collectionSink.add(Mono.just(1));
    }

    public Mono<SlashCommandContextBuilder> collectData() {
        return Mono.zip(collectionSink, (array) -> 1)
            .switchIfEmpty(Mono.error(new DataMissingException(this, "Couldn't collect all data!")))
            //.doOnEach(signal -> System.out.println("Error Signal: " + signal.hasError()))
            .thenReturn(this);
    }

    public SlashCommandContext build() {
        return new SlashCommandContext(this);
    }

    public SlashCommandContextBuilder requireGuild() {
        collectionSink.add(event.getInteraction().getGuild()
            .doOnNext(guild -> this.guild = guild)
            .doOnEach(guildSignal -> System.out.println("Guild Signal: " + guildSignal.hasValue()))
            .map(guild -> 1));
        return this;
    }

    public SlashCommandContextBuilder requireChannel() {
        collectionSink.add(event.getInteraction().getChannel()
            .doOnNext(channel -> this.channel = channel)
            .map(channel -> 1));
        return this;
    }

    public SlashCommandContextBuilder requireGuildChannel() {
        collectionSink.add(event.getInteraction().getChannel()
            .doOnNext(channel -> this.channel = channel)
            .ofType(GuildChannel.class)
            .doOnNext(channel -> this.guildChannel = channel)
            .map(channel -> 1));
        return this;
    }

    public SlashCommandContextBuilder requireMember() {
        collectionSink.add(Mono.justOrEmpty(event.getInteraction().getMember())
            .doOnNext(member -> this.member = member)
            .map(member -> 1));
        return this;
    }
}
