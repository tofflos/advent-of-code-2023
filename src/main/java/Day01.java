
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;


void main() throws IOException {
    System.out.println(STR."Part 01: \{ process(this::tokenizer1) }");
    System.out.println(STR."Part 02: \{ process(this::tokenizer2) }");
}

int process(Function<String, List<String>> tokenizer) throws IOException {
    return Files.readString(Paths.get("01.txt")).lines()
            .map(tokenizer)
            .map(tokens -> tokens.getFirst() + tokens.getLast())
            .mapToInt(Integer::parseInt)
            .sum();
}

List<String> tokenizer1(String line) {
    return Pattern.compile("(\\d)").matcher(line).results().map(MatchResult::group).toList();
}

List<String> tokenizer2(String line) {
    var matcher = Pattern.compile("(?=(\\d|one|two|three|four|five|six|seven|eight|nine))").matcher(line);
    var list = new ArrayList<String>();

    while (matcher.find()) {
        var number = matcher.group(1);

        list.add(switch (number) {
            case "one" -> "1";
            case "two" -> "2";
            case "three" -> "3";
            case "four" -> "4";
            case "five" -> "5";
            case "six" -> "6";
            case "seven" -> "7";
            case "eight" -> "8";
            case "nine" -> "9";
            default -> number;
        });
    }

    return list;
}
