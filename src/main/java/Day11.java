import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

void main() throws IOException {
    var lines = Files.readAllLines(Paths.get("11.txt"));
    var rows = rows(lines);
    var columns = rows(transpose(lines));
    var galaxies = galaxies(lines);
    var pairs = galaxies.stream()
            .flatMap(g1 -> galaxies.stream()
                    .map(g2 ->new HashSet<>(List.of(g1, g2))))
            .collect(Collectors.toSet());

    var part1 = pairs.stream()
            .map(ArrayList::new)
            .mapToLong(pair -> Galaxy.distance(pair.getFirst(), pair.getLast(), rows, columns, 2L))
            .sum();

    System.out.println(STR."Part 01: \{part1}");

    var part2 = pairs.stream()
            .map(ArrayList::new)
            .mapToLong(pair -> Galaxy.distance(pair.getFirst(), pair.getLast(), rows, columns, 1_000_000L))
            .sum();

    System.out.println(STR."Part 02: \{part2}");
}

Set<Integer> rows(List<String> lines) {
    var rows = new HashSet<Integer>();

    for (int i = 0; i < lines.size(); i++) {
        if (!lines.get(i).contains("#")) {
            rows.add(i);
        }
    }

    return rows;
}

List<String> transpose(List<String> lines) {
    char[][] matrix = new char[lines.getFirst().length()][lines.size()];

    for (int y = 0; y < matrix.length; y++) {
        for (int x = 0; x < matrix[y].length; x++) {
            matrix[y][x] = lines.get(x).charAt(y);
        }
    }

    return Arrays.stream(matrix).map(String::new).toList();
}

List<Galaxy> galaxies(List<String> lines) {
    var galaxies = new ArrayList<Galaxy>();

    for (int y = 0; y < lines.size(); y++) {
        for (int x = 0; x < lines.get(y).length(); x++) {
            if (lines.get(y).charAt(x) == '#') {
                galaxies.add(new Galaxy(x, y));
            }
        }
    }

    return galaxies;
}

record Galaxy(int x, int y) {

    static long distance(Galaxy g1, Galaxy g2, Set<Integer> rows, Set<Integer> columns, long multiplier) {
        var minX = Integer.min(g1.x, g2.x);
        var maxX = Integer.max(g1.x, g2.x);
        var cs = columns.stream().filter(c -> minX < c && c < maxX).collect(Collectors.toSet());

        var minY = Integer.min(g1.y, g2.y);
        var maxY = Integer.max(g1.y, g2.y);
        var rs = rows.stream().filter(r -> minY < r && r < maxY).collect(Collectors.toSet());

        return (maxX - minX - rs.size() + rs.size() * multiplier) + (maxY - minY - cs.size() + cs.size() * multiplier);
    }
}