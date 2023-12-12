import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

void main() throws IOException {
    var springs = Files.lines((Paths.get("12.txt"))).map(Spring::of).toList();

    var part1 = springs.stream().mapToInt(Spring::arrangements).sum();
    System.out.println(STR."Part 01: \{part1}");
}

record Spring(String row, List<Integer> sizes) {

    static Pattern PATTERN = Pattern.compile("#+");

    int arrangements() {
        var deque = new ArrayDeque<String>();

        var count = 0;
        deque.push(this.row);

        while (!deque.isEmpty()) {
            var current = deque.pop();
            var index = current.indexOf('?');

            if (index != -1) {
                var t = new StringBuilder(current);
                t.setCharAt(index, '.');
                deque.add(t.toString());
                t.setCharAt(index, '#');
                deque.add(t.toString());
            } else if (PATTERN.matcher(current).results().map(MatchResult::group).map(String::length).toList().equals(this.sizes)) {
                count++;
            }
        }

        return count;
    }

    static Spring of(String line) {
        var t = line.split(" ");

        return new Spring(t[0], Arrays.stream(t[1].split(",")).map(Integer::parseInt).toList());
    }
}