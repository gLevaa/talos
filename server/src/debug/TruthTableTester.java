package debug;

public class TruthTableTester {
    private final boolean[][] truthTableValues;

    public TruthTableTester(int variables) {
        truthTableValues = new boolean[(int)Math.pow(2, variables)][variables];
        generateTruthTableValues(variables);

        test();
    }

    private void test() {
        for (boolean[] parameters : truthTableValues) {
            System.out.println(fetchNewPage(parameters[0], parameters[1], parameters[2]));
        }
    }

    private void generateTruthTableValues(int n) {
        for (int i = 0; i < Math.pow(2, n); i++) {
            String binaryString = Integer.toBinaryString(i);
            System.out.println(binaryString);

            String paddedBinaryString = String.format("%" + n + "s", binaryString).replace(' ', '0');
            System.out.println(paddedBinaryString + "\n");

            for (int j = 0; j < paddedBinaryString.length(); j++) {
                truthTableValues[i][j] = paddedBinaryString.charAt(j) != '0';
            }
        }
    }

    private String fetchNewPage(boolean A, boolean B, boolean C) {
        // LOGICALLY CORRECT

        // Source empty and post empty
        if (A && B) {
            return  "DEAD";
        // Source not empty and posts low
        } else if (!B && (A || C)) {
            return "Source";
        } else {
            return "Post";
        }
    }
}
