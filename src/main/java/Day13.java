import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

void main() throws IOException {
    var patterns = Arrays.stream(Files.readString(Paths.get("13.txt")).split("\r\n\r\n"))
            .map(pattern -> pattern.lines().toList())
            .toList();

    var rows = patterns.stream()
            .mapToInt(this::mirror)
            .filter(i -> i != -1)
            .sum();

    var cols = patterns.stream()
            .map(this::transpose)
            .mapToInt(this::mirror)
            .filter(i -> i != -1)
            .sum();

    var part1 = cols + 100 * rows;

    System.out.println(STR."Part 01: \{part1}");
}

int mirror(List<String> lines) {
    for (int i = 0; i < lines.size() - 1; i++) {
        if (lines.get(i).equals(lines.get(i + 1))) {
            var b = true;

            for (int j = 0; j < i && 0 <= i - j - 1 && i + j + 2 < lines.size(); j++) {

                if (!lines.get(i - j - 1).equals(lines.get(i + j + 2))) {
                    b = false;
                    break;
                }
            }

            if (b) {
                return i + 1;
            }
        }
    }

    return -1;
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