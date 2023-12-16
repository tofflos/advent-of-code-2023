import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

void main() throws IOException {
    var lines = Files.readAllLines(Paths.get("14.txt"));

    var platform1 = lines.stream().map(String::toCharArray).toArray(char[][]::new);
    tilt1(platform1, 0, -1);
    var part1 = weight(platform1);
    System.out.println(STR."Part 01: \{part1}");

    var platform2 = lines.stream().map(String::toCharArray).toArray(char[][]::new);
    var part2 = cycle(platform2, 1000000000);
    System.out.println(STR."Part 02: \{part2}");
}

int weight(char[][] platform) {
    var weight = 0;

    for (int y = 0; y < platform.length; y++) {
        for (int x = 0; x < platform[y].length; x++) {
            var c = platform[y][x];

            if (c == 'O') {
                weight += platform.length - y;
            }
        }
    }

    return weight;
}

void tilt1(char[][] platform, int dx, int dy) {
    for (int y = 0; y < platform.length; y++) {
        for (int x = 0; x < platform[y].length; x++) {
            var cy = y;
            var cx = x;

            if (platform[cy][cx] != 'O') {
                continue;
            }

            var px = cx + dx;
            var py = cy + dy;

            while (0 <= py && py < platform.length && 0 <= px && px < platform[py].length && platform[py][px] == '.') {
                platform[cy][cx] = '.';
                platform[py][px] = 'O';

                cx = px;
                cy = py;
                px = cx + dx;
                py = cy + dy;
            }
        }
    }
}

void tilt2(char[][] platform, int dx, int dy) {
    for (int y = 0; y < platform.length; y++) {
        for (int x = 0; x < platform[y].length; x++) {
            var cy = platform.length - 1 - y;
            var cx = platform[cy].length - 1 - x;

            if (platform[cy][cx] != 'O') {
                continue;
            }

            var px = cx + dx;
            var py = cy + dy;

            while (0 <= py && py < platform.length && 0 <= px && px < platform[py].length && platform[py][px] == '.') {
                platform[cy][cx] = '.';
                platform[py][px] = 'O';

                cx = px;
                cy = py;
                px = cx + dx;
                py = cy + dy;
            }
        }
    }
}

int cycle(char[][] platform, int count) {
    var logs = new ArrayList<Log>();
    var currentIndex = 0;
    var previousIndex = -1;

    for (currentIndex = 0; currentIndex < count; currentIndex++) {
        tilt1(platform, 0, -1);
        tilt1(platform, -1, 0);
        tilt2(platform, 0, 1);
        tilt2(platform, 1, 0);

        var currentLog = new Log(weight(platform), Arrays.deepToString(platform));
        previousIndex = logs.lastIndexOf(currentLog);

        if (previousIndex != -1) {
            System.out.println(STR."Loop with weight \{currentLog.weight} at indexes: \{previousIndex} \{currentIndex}");
            break;
        }

        logs.add(currentLog);
    }

    return logs.get(previousIndex + (count - 1 - previousIndex) % (currentIndex - previousIndex)).weight;
}

record Log(int weight, String platform) {

}