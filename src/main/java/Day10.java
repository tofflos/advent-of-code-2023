import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

void main() throws IOException {
    var lines = Files.readAllLines(Paths.get("10.txt"));
    var start = start(lines);
    var distances = distances(start, lines);
    var max = max(distances);

    System.out.println(STR."Part 01: \{max}");
}

int[][] distances(Location start, List<String> lines) {
    var distances = lines.stream()
            .map(line -> new int[line.length()])
            .peek(arr -> Arrays.fill(arr, Integer.MAX_VALUE))
            .toArray(int[][]::new);

    distances[start.y][start.x] = 0;

    var deque = new ArrayDeque<Location>(List.of(start));

    while (!deque.isEmpty()) {
        var current = deque.pop();
        var neighbours = neighbours(current, lines);

        for (var neighbour : neighbours) {
            if (!neighbours(neighbour, lines).contains(current))
                continue;

            if (distances[current.y][current.x] + 1 < distances[neighbour.y][neighbour.x]) {
                distances[neighbour.y][neighbour.x] = distances[current.y][current.x] + 1;
                deque.add(neighbour);
            }
        }
    }

    return distances;
}

Location start(List<String> lines) {
    for (int y = 0; y < lines.size(); y++) {
        var line = lines.get(y);

        for (int x = 0; x < line.length(); x++) {
            if (line.charAt(x) == 'S') {
                return new Location(x, y);
            }
        }
    }

    throw new IllegalArgumentException("Unable to find start location.");
}

int max(int[][] distances) {
    var max = Integer.MIN_VALUE;

    for (int y = 0; y < distances.length; y++) {
        for (int x = 0; x < distances[y].length; x++) {
            if (distances[y][x] != Integer.MAX_VALUE && distances[y][x] > max) {
                max = distances[y][x];
            }
        }
    }

    return max;
}

Set<Location> neighbours(Location location, List<String> lines) {
    var connections = Map.of(
            '|', new int[][]{{0, -1}, {0, 1}},
            '-', new int[][]{{1, 0}, {-1, 0}},
            'L', new int[][]{{0, -1}, {1, 0}},
            'J', new int[][]{{0, -1}, {-1, 0}},
            '7', new int[][]{{0, 1}, {-1, 0}},
            'F', new int[][]{{0, 1}, {1, 0}},
            '.', new int[][]{},
            'S', new int[][]{{0, -1}, {1, 0}, {0, 1}, {-1, 0}}
    );

    return Arrays.stream(connections.get(lines.get(location.y).charAt(location.x)))
            .map(delta -> new Location(location.x + delta[0], location.y + delta[1]))
            .filter(n -> 0 <= n.x && n.x < lines.getFirst().length() &&
                    0 <= n.y && n.y < lines.size())
            .collect(Collectors.toSet());
}

record Location(int x, int y) {

}