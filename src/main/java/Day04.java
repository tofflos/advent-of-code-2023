import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

void main() throws IOException {

    var cards = Files.lines(Paths.get("04.txt")).map(Card::of).toList();

    var part1 = cards.stream()
            .map(this::winners)
            .mapToInt(winners -> (int) Math.pow(2, winners.size() - 1))
            .sum();

    System.out.println(STR."Part 01: \{part1}");

    var quantities = cards.stream().collect(Collectors.groupingBy(card -> card.id, Collectors.counting()));

    cards.forEach(card -> quantities.entrySet().stream()
            .skip(card.id)
            .limit(winners(card).size())
            .forEach(entry -> entry.setValue(quantities.get(card.id) + entry.getValue())));

    var part2 = quantities.values().stream().mapToLong(Long::longValue).sum();

    System.out.println(STR."Part 02: \{part2}");
}

Set<Integer> winners(Card card) {
    var set1 = new HashSet<Integer>();
    Arrays.stream(card.winners).forEach(set1::add);
    var set2 = new HashSet<Integer>();
    Arrays.stream(card.actuals).forEach(set2::add);

    set1.retainAll(set2);

    return set1;
}

record Card(int id, int[] winners, int[] actuals) {
    static Pattern PATTERN = Pattern.compile("\\d+");

    static Card of(String line) {
        var arr = line.split("[:|]");

        var matcher = PATTERN.matcher(arr[0]);
        matcher.find();
        var id = Integer.parseInt(matcher.group());

        matcher = PATTERN.matcher(arr[1]);
        var winners = matcher.results().map(MatchResult::group).mapToInt(Integer::parseInt).toArray();

        matcher = PATTERN.matcher(arr[2]);
        var actuals = matcher.results().map(MatchResult::group).mapToInt(Integer::parseInt).toArray();

        return new Card(id, winners, actuals);
    }
}