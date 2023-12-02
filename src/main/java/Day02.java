import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

void main() throws IOException {

    var games = Files.readString(Paths.get("02.txt")).lines().map(Game::of).toList();

    var part1 = games.stream()
            .filter(game -> game.cubes.stream()
                    .noneMatch(cubes ->
                            cubes.getOrDefault("red", 0) > 12 ||
                                    cubes.getOrDefault("green", 0) > 13 ||
                                    cubes.getOrDefault("blue", 0) > 14))
            .mapToInt(Game::id)
            .sum();

    var part2 = games.stream()
            .map(game -> game.cubes.stream()
                    .flatMap(map -> map.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::max)))
            .mapToInt(max -> max.values().stream().reduce(1, Math::multiplyExact))
            .sum();

    System.out.println(STR. "Part 01: \{ part1 }" );
    System.out.println(STR. "Part 02: \{ part2 }" );
}

record Game(int id, List<Map<String, Integer>> cubes) {
    record Cube(int count, String color) { }

    static Game of(String line) {

        var t = line.split(":");

        return new Game(Integer.parseInt(t[0].replace("Game ", "")),
                Arrays.stream(t[1].split(";"))
                        .map(hand -> Arrays.stream(hand.split(","))
                                .map(s -> s.trim().split(" "))
                                .map(arr -> new Cube(Integer.parseInt(arr[0]), arr[1]))
                                .collect(Collectors.toMap(Cube::color, Cube::count, Integer::sum))).toList());
    }
}
