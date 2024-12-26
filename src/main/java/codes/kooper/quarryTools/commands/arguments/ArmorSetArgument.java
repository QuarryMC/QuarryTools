package codes.kooper.quarryTools.commands.arguments;

import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.items.ArmorItems;
import codes.kooper.shaded.litecommands.argument.Argument;
import codes.kooper.shaded.litecommands.argument.parser.ParseResult;
import codes.kooper.shaded.litecommands.argument.resolver.ArgumentResolver;
import codes.kooper.shaded.litecommands.invocation.Invocation;
import codes.kooper.shaded.litecommands.suggestion.SuggestionContext;
import codes.kooper.shaded.litecommands.suggestion.SuggestionResult;
import org.bukkit.command.CommandSender;

public class ArmorSetArgument extends ArgumentResolver<CommandSender, ArmorItems.ArmorSet> {

    @Override
    protected ParseResult<ArmorItems.ArmorSet> parse(Invocation<CommandSender> invocation, Argument<ArmorItems.ArmorSet> argument, String s) {
        try {
            ArmorItems.ArmorSet armorSet = QuarryTools.getInstance().getArmorItems().getArmorSet(s.toLowerCase());
            if (armorSet == null) {
                return ParseResult.failure("Could not find an armor set with that name.");
            }
            return ParseResult.success(armorSet);
        } catch (IllegalArgumentException e) {
            return ParseResult.failure("An error has occurred while parsing this armor set.");
        }
    }

    @Override
    public SuggestionResult suggest(
            Invocation<CommandSender> invocation,
            Argument<ArmorItems.ArmorSet> argument,
            SuggestionContext context
    ) {
        return SuggestionResult.of(QuarryTools.getInstance().getArmorItems().getArmorSets().keySet());
    }
}
