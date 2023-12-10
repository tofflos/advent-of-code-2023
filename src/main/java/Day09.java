import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

void main() throws IOException {
    var report = Files.lines(Paths.get("09.txt"))
            .map(line -> Arrays.stream(line.split(" "))
                    .map(Integer::parseInt)
                    .toList())
            .toList();

    var part1 = report.stream().mapToInt(this::extrapolate1).sum();
    System.out.println(STR."Part 01: \{part1}");


    var part2 = report.stream().mapToInt(this::extrapolate2).sum();
    System.out.println(STR."Part 02: \{part2}");
}

List<Integer> deltas(List<Integer> values) {
    var deltas = new ArrayList<Integer>();

    for (int i = 0; i < values.size() - 1; i++) {
        deltas.add(values.get(i + 1) - values.get(i));
    }

    return deltas;
}

int extrapolate1(List<Integer> values) {
    var stack = new ArrayDeque<List<Integer>>();

    stack.add(new ArrayList<>(values));
    var current = stack.getLast();

    while (current.stream().anyMatch(i -> i != 0)) {
        current = deltas(current);
        stack.add(current);
    }

    return stack.stream().mapToInt(List::getLast).sum();
}

int extrapolate2(List<Integer> values) {
    var stack = new ArrayDeque<List<Integer>>();

    stack.add(new ArrayList<>(values));
    var current = stack.getLast();

    while (current.stream().anyMatch(i -> i != 0)) {
        current = deltas(current);
        stack.add(current);
    }

    var sum = 0;

    while (!stack.isEmpty()) {
        current = stack.removeLast();
        sum = current.getFirst() - sum;
    }

    return sum;
}
