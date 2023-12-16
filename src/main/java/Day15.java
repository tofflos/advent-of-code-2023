import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

void main() throws IOException {
    var sequence = Files.readString(Paths.get("15.txt"));

    var part1 = Arrays.stream(sequence.split(",")).mapToInt(this::hash).sum();
    System.out.println(STR."Part 02 : \{part1}");

    var part2 = power(sequence);
    System.out.println(STR."Part 02 : \{part2}");
}

int hash(String message) {
    var hash = 0;

    for (int i = 0; i < message.length(); i++) {
        hash += message.codePointAt(i);
        hash *= 17;
        hash = hash % 256;
    }

    return hash;
}

int power(String message) {
    var steps = message.split(",");
    var boxes = IntStream.range(0, 255).mapToObj(i -> new ArrayList<Lens>()).toList();

    for (int i = 0; i < steps.length; i++) {
        var step = steps[i];
        var operationIndex = step.indexOf('-');

        if (operationIndex == -1) {
            operationIndex = step.indexOf('=');
        }

        var label = step.substring(0, operationIndex);
        var operation = step.charAt(operationIndex);
        var boxIndex = hash(label);

        switch (operation) {
            case '-':
                boxes.get(boxIndex).removeIf(lens -> label.equals(lens.label));
                break;
            case '=':
                var focalLength = Integer.parseInt(step.substring(operationIndex + 1));
                var lens = new Lens(label, focalLength);
                var lenses = boxes.get(boxIndex);
                var replaceIndices = IntStream.range(0, lenses.size()).filter(n -> label.equals(lenses.get(n).label)).toArray();

                if (replaceIndices.length == 0) {
                    lenses.add(lens);
                } else {
                    Arrays.stream(replaceIndices).forEach(n -> lenses.set(n, lens));
                }

                break;
        }
    }

    var power = 0;

    for (int i = 0; i < boxes.size(); i++) {
        for (int j = 0; j < boxes.get(i).size(); j++) {
            var lenses = boxes.get(i);
            var lens = lenses.get(j);

            power += (i + 1) * (j + 1) * lens.focalLength;
        }
    }

    return power;
}

record Lens(String label, int focalLength) {

}