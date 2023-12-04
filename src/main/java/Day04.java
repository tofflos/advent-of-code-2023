import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day04 {

    record Card(int id, int[] winners, int[] actuals) {

    }

    public static void main(String[] args) throws IOException {
        var pattern = Pattern.compile("\\d+");

        var part1 = Files.lines(Paths.get("04.txt"))
                .map(line -> {
                    var arr = line.split("[:|]");

                    var matcher = pattern.matcher(arr[0]);
                    matcher.find();
                    var id = Integer.parseInt(matcher.group());

                    matcher = pattern.matcher(arr[1]);
                    var winners = matcher.results().map(MatchResult::group).mapToInt(Integer::parseInt).toArray();

                    matcher = pattern.matcher(arr[2]);
                    var actuals = matcher.results().map(MatchResult::group).mapToInt(Integer::parseInt).toArray();

                    return new Card(id, winners, actuals);
                })
                .mapToInt(card -> {
                    var set1 = new HashSet<Integer>();
                    Arrays.stream(card.winners).forEach(set1::add);
                    var set2 = new HashSet<Integer>();
                    Arrays.stream(card.actuals).forEach(set2::add);

                    set1.retainAll(set2);

                    return (int) Math.pow(2, set1.size() - 1);
                })
                .sum();

        System.out.println(part1);
    }
}
