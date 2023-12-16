import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Set;
import java.util.stream.LongStream;

void main() throws IOException {
    var lines = Files.readAllLines(Paths.get("16.txt"));

    var part1 = energized(new Location(0, 0, 1, 0), lines);
    System.out.println(STR."Part 01: \{part1}");

    var part2 = max(lines);
    System.out.println(STR."Part 02: \{part2}");
}

long max(List<String> lines) {
    var top = 0L;

    for (int x = 0; x < lines.getFirst().length(); x++) {
        var t = energized(new Location(x, 0, 0, 1), lines);

        if (top <= t) {
            top = t;
        }
    }

    var left = 0L;

    for (int y = 0; y < lines.size(); y++) {
        var t = energized(new Location(0, y, 1, 0), lines);

        if (left <= t) {
            left = t;
        }
    }

    var bottom = 0L;

    for (int x = 0; x < lines.getLast().length(); x++) {
        var t = energized(new Location(x, lines.size() - 1, 0, -1), lines);

        if (bottom <= t) {
            bottom = t;
        }
    }

    var right = 0L;

    for (int y = 0; y < lines.size(); y++) {
        var t = energized(new Location(lines.size() - 1 - y, 0, 0, -1), lines);

        if (right <= t) {
            right = t;
        }
    }

    return LongStream.of(top, left, bottom, right).max().orElseThrow();
}

long energized(Location start, List<String> lines) {
    var visited = new ArrayDeque<Location>(List.of(start));
    var deque = new ArrayDeque<Location>(List.of(start));

    while (!deque.isEmpty()) {
        var current = deque.pop();

        next(lines.get(current.y).charAt(current.x), current.x, current.y, current.dx, current.dy).stream()
                .filter(n -> 0 <= n.y && n.y < lines.size() && 0 <= n.x && n.x < lines.get(n.y).length())
                .filter(n -> !visited.contains(n))
                .forEach(n -> {
                    visited.push(n);
                    deque.push(n);
                });
    }

    return visited.stream().map(location -> new Location(location.x, location.y, 0, 0)).distinct().count();
}

Set<Location> next(char c, int x, int y, int dx, int dy) {

    if (c == '.') {
        return Set.of(new Location(x + dx, y + dy, dx, dy));
    }

    if (c == '/' && dx == 1 && dy == 0) {
        return Set.of(new Location(x, y - 1, 0, -1));
    }

    if (c == '/' && dx == 0 && dy == -1) {
        return Set.of(new Location(x + 1, y, 1, 0));
    }

    if (c == '/' && dx == -1 && dy == 0) {
        return Set.of(new Location(x, y + 1, 0, 1));
    }

    if (c == '/' && dx == 0 && dy == 1) {
        return Set.of(new Location(x - 1, y, -1, 0));
    }

    if (c == '\\' && dx == 1 && dy == 0) {
        return Set.of(new Location(x, y + 1, 0, 1));
    }

    if (c == '\\' && dx == 0 && dy == -1) {
        return Set.of(new Location(x - 1, y, -1, 0));
    }

    if (c == '\\' && dx == -1 && dy == 0) {
        return Set.of(new Location(x, y - 1, 0, -1));
    }

    if (c == '\\' && dx == 0 && dy == 1) {
        return Set.of(new Location(x + 1, y, 1, 0));
    }

    if (c == '|' && dx == 0 && dy != 0) {
        return Set.of(new Location(x, y + dy, 0, dy));
    }

    if (c == '|' && dx != 0 && dy == 0) {
        return Set.of(new Location(x, y + 1, 0, 1), new Location(x, y - 1, 0, -1));
    }

    if (c == '-' && dx != 0 && dy == 0) {
        return Set.of(new Location(x + dx, y, dx, dy));
    }

    if (c == '-' && dx == 0 && dy != 0) {
        return Set.of(new Location(x + 1, y, 1, 0), new Location(x - 1, y, -1, 0));
    }

    throw new IllegalArgumentException(STR."I don't know what to do given input: \{c} \{x} \{y} \{dx} \{dy}");
}

record Location(int x, int y, int dx, int dy) {
}