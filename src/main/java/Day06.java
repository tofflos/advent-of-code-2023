import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


record Race(long limit, long record) {

}

void main() throws IOException {

    var pattern = Pattern.compile("\\d+");

    var numbers1 = Files.lines(Paths.get("06.txt"))
            .map(line -> pattern.matcher(line).results()
                    .map(MatchResult::group)
                    .map(Integer::parseInt)
                    .toList())
            .toList();

    var races1 = IntStream.range(0, numbers1.getFirst().size())
            .mapToObj(i -> new Race(numbers1.getFirst().get(i), numbers1.getLast().get(i)))
            .toList();

    var part1 = races1.stream()
            .map(race -> distances(race.limit).stream().filter(distance -> distance > race.record).count())
            .reduce(1L, Math::multiplyExact);

    System.out.println(STR."Part 01: \{part1}");

    var numbers2 = Files.lines(Paths.get("06.txt"))
            .map(line -> pattern.matcher(line).results()
                    .map(MatchResult::group)
                    .collect(Collectors.joining()))
            .map(Long::parseLong)
            .toList();

    var races2 = List.of(new Race(numbers2.getFirst(), numbers2.getLast()));

    var part2 = races2.stream()
            .map(race -> distances(race.limit).stream().filter(distance -> distance > race.record).count())
            .reduce(1L, Math::multiplyExact);

    System.out.println(STR."Part 02: \{part2}");
}

static List<Long> distances(long duration) {
    return LongStream.range(0L, duration).map(l -> (duration - l) * l).boxed().toList();
}
