package cz.cuni.mff.mansuroa.sudoku;

import java.util.Arrays;

/**
 * Sudoku solver. 
 * Je pouzit open source solver, ktery byl prizpusoben pro integraci do zbytku 
 * aplikace.
 *
 * JSON Sudoku solver is covered under the Creative Commons Attribution 3.0
 * Unported License http://creativecommons.org/licenses/by/3.0/
 *
 * @author: Kevin Coulombe
 * http://www.byteauthor.com/2010/08/sudoku-solver-update/ modified
 */
public class Solver {

    private Solver() {
    }

    /**
     * Vyresi zadane sudoku. 
     * Vyzkousi nektere heuristiky a pripadne zkusi reseni hrubou silou.
     *
     * @param sudoku sudoku k vyreseni
     * @throws SolverException argument "null" nebo reseni neexistuje
     */
    public static void solve(Sudoku sudoku) throws SolverException {
        if (sudoku != null) {
            final int placedNumbers = solveBoard(sudoku);
            if (placedNumbers != 81) {
                throw new SolverException("No solution exists!");
            }
        } else {
            throw new SolverException(new NullPointerException("Argument null"));
        }
    }

    /**
     * na i-tem miste je takove cislo, ktere ma v binarnim zapisu prave jednu 1 
     * a to na i-tem miste zprava
     */
    private final static int[] allowedBitFields = new int[]{
        0,
        1,
        1 << 1,
        1 << 2,
        1 << 3,
        1 << 4,
        1 << 5,
        1 << 6,
        1 << 7,
        1 << 8,};

    private final static int allAllowed = arraySum(allowedBitFields);

    /**
     * Vyresi sudoku pro zadanou matici.
     *
     * @param board zadani
     * @return pocet umistenych cisel
     */
    private static int solveBoard(final Sudoku board) {
        final int size = board.getSize();
        final int[][] allowedValues = new int[size][size];
        int placedNumberCount = 0;

        for (int[] allowedValuesRow : allowedValues) {
            Arrays.fill(allowedValuesRow, allAllowed);
        }

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.isset(y, x)) {
                    allowedValues[x][y] = 0;
                    applyAllowedValuesMask(board, allowedValues, x, y);
                    placedNumberCount++;
                }
            }
        }

        return solveBoard(board, allowedValues, placedNumberCount);
    }

    /**
     * Vyresi sudoku pro zadanou matici a povolene hodnoty.
     *
     * @param board resene sudoku
     * @param allowedValues povolene hodnoty
     * @param placedNumberCount pocet zatim umistenych cisel
     * @return novy pocet zatim umistenych cisel
     */
    private static int solveBoard(final Sudoku board, final int[][] allowedValues, int placedNumberCount) {
        int lastPlacedNumbersCount = 0;

        while (placedNumberCount - lastPlacedNumbersCount > 3 && placedNumberCount < 68 && placedNumberCount > 10) {
            lastPlacedNumbersCount = placedNumberCount;
            placedNumberCount += moveNothingElseAllowed(board, allowedValues);
            placedNumberCount += moveNoOtherRowOrColumnAllowed(board, allowedValues);
            placedNumberCount += moveNothingElseAllowed(board, allowedValues);

            if (placedNumberCount < 35) {
                applyNakedPairs(allowedValues);
                applyLineCandidateConstraints(allowedValues);
            }
        }

        if (placedNumberCount < 81) {
            final Sudoku bruteForcedBoard = attemptBruteForce(board, allowedValues, placedNumberCount);

            if (bruteForcedBoard != null) {
                placedNumberCount = 0;

                for (int x = 0; x < 9; x++) {
                    for (int y = 0; y < 9; y++) {
                        board.setValue(y, x, bruteForcedBoard.getValue(y, x));

                        if (bruteForcedBoard.isset(y, x)) {
                            placedNumberCount++;
                        }
                    }
                }
            }
        }

        return placedNumberCount;
    }

    /**
     * Zkus resit hrubou silou.
     *
     * @param board zadani
     * @param allowedValues povolene hodnoty
     * @param placedNumberCount pocet umistenych cislic
     * @return vyresene sudoku
     */
    private static Sudoku attemptBruteForce(final Sudoku board, final int[][] allowedValues, final int placedNumberCount) {
        for (int x = 0; x < board.getSize(); x++) {
            final int[] allowedValuesRow = allowedValues[x];

            for (int y = 0; y < board.getSize(); y++) {
                if (!board.isset(y,x)) {
                    for (int value = 1; value <= 9; value++) {
                        if ((allowedValuesRow[y] & allowedBitFields[value]) > 0) {
                            final Sudoku testBoard = board.copy();
                            final int[][] testAllowedValues = copyGameMatrix(allowedValues);
                            setValue(testBoard, testAllowedValues, value, x, y);

                            final int placedNumbers = solveBoard(testBoard, testAllowedValues, placedNumberCount + 1);

                            if (placedNumbers == 81) {
                                return testBoard;
                            }
                        }
                    }

                    return null;
                }
            }
        }

        return null;
    }

    /**
     * Nejaka hodnota je povolena pouze v jednom poli v nejakem radku nebo
     * sloupci.
     *
     * @param board resene sudoku
     * @param allowedValues povolene hodnoty
     * @return pocet vyresenych poli
     */
    private static int moveNoOtherRowOrColumnAllowed(final Sudoku board, final int[][] allowedValues) {
        int moveCount = 0;

        for (int value = 1; value <= 9; value++) {
            final int allowedBitField = allowedBitFields[value];

            for (int x = 0; x < 9; x++) {
                int allowedY = -1;
                final int[] allowedValuesRow = allowedValues[x];

                for (int y = 0; y < 9; y++) {
                    if ((allowedValuesRow[y] & allowedBitField) > 0) {
                        if (allowedY < 0) {
                            allowedY = y;
                        } else {
                            allowedY = -1;
                            break;
                        }
                    }
                }

                if (allowedY >= 0) {
                    setValue(board, allowedValues, value, x, allowedY);
                    moveCount++;
                }
            }

            for (int y = 0; y < 9; y++) {
                int allowedX = -1;

                for (int x = 0; x < 9; x++) {
                    if ((allowedValues[x][y] & allowedBitField) > 0) {
                        if (allowedX < 0) {
                            allowedX = x;
                        } else {
                            allowedX = -1;
                            break;
                        }
                    }
                }

                if (allowedX >= 0) {
                    setValue(board, allowedValues, value, allowedX, y);
                    moveCount++;
                }
            }
        }

        return moveCount;
    }

    /**
     * Pouze jedna hodnota je povolena.
     *
     * @param board zadani
     * @param allowedValues povolene hodoty
     * @return pocet vyresenych poli
     */
    private static int moveNothingElseAllowed(final Sudoku board,
            final int[][] allowedValues) {

        int moveCount = 0;

        for (int x = 0; x < 9; x++) {
            final int[] allowedValuesRow = allowedValues[x];

            for (int y = 0; y < 9; y++) {
                final int currentAllowedValues = allowedValuesRow[y];
                if (countSetBits(currentAllowedValues) == 1) {
                    setValue(board, allowedValues, getLastSetBitIndex(currentAllowedValues), x, y);
                    moveCount++;
                }
            }
        }

        return moveCount;
    }

    /**
     * Pokud v nejakem radku nebo sloupci maji dve pole pouze dve stejne mozne
     * hodnoty, pak zadne dalsi pole na danem radku nebo sloupci nebude
     * obsahovat tyto dve hodnoty
     *
     * @param allowedValues povolene hodnoty
     */
    private static void applyNakedPairs(final int[][] allowedValues) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                final int value = allowedValues[x][y];

                if (countSetBits(value) == 2) {
                    for (int scanningY = y + 1; scanningY < 9; scanningY++) {
                        if (allowedValues[x][scanningY] == value) {
                            final int removeMask = ~value;

                            for (int applyY = 0; applyY < 9; applyY++) {
                                if (applyY != y && applyY != scanningY) {
                                    allowedValues[x][applyY] &= removeMask;
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                final int value = allowedValues[x][y];
                if (value != 0 && countSetBits(value) == 2) {
                    for (int scanningX = x + 1; scanningX < 9; scanningX++) {
                        if (allowedValues[scanningX][y] == value) {
                            final int removeMask = ~value;

                            for (int applyX = 0; applyX < 9; applyX++) {
                                if (applyX != x && applyX != scanningX) {
                                    allowedValues[applyX][y] &= removeMask;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Analyzuje radky a sloupce a hleda v jakem ze tri bloku je nektera hodnota
     * v danem radku ci sloupci. Pokud muze byt pouze v jednom bloku, pak urcite
     * neni v jinem radku resp. sloupci v tom bloku.
     *
     * @param allowedValues povolene hodnoty
     */
    private static void applyLineCandidateConstraints(final int[][] allowedValues) {
        for (int value = 1; value <= 9; value++) {
            final int valueMask = allowedBitFields[value]; //ma jednicku na miste "value" z prava
            final int valueRemoveMask = ~valueMask; //ma jednicky vsude krome "value"
            final int[] sectionAvailabilityColumn = new int[9];

            for (int x = 0; x < 9; x++) {
                final int finalX = x;

                for (int y = 0; y < 9; y++) {
                    if ((allowedValues[finalX][y] & valueMask) != 0) {
                        //value je pripustna na [finalX][y]
                        //1<<(y/3) je "blok" ve smeru y
                        sectionAvailabilityColumn[finalX] |= (1 << (y / 3));
                        // oznaci "blok", kde je value pripustna
                    }
                    //v section availability column[x] ty bloky, kde je value pripustna
                }
                //ALF: pokud jsem zpracoval cely (3line) blok
                if (finalX == 2 || finalX == 5 || finalX == 8) { // posledni v bloku
                    for (int scanningX = finalX - 2; scanningX <= finalX; scanningX++) { //blok
                        final int bitCount = countSetBits(sectionAvailabilityColumn[scanningX]); //kolik je pripustnych hodnot

                        if (bitCount == 1) { //value se smi vyskytnout pouze v jednom bloku - vyskrta se z ostatnich
                            //ALF - exactly in one section  - tohle mu nefunguje spravne
                            for (int applyX = finalX - 2; applyX <= finalX; applyX++) {
                                if (scanningX != applyX) { //ALF: Not the line where the value is in exactly one section 
                                    for (int applySectionY = 0; applySectionY < 3; applySectionY++) { // ALF: The all sections on the other line
                                        if ((sectionAvailabilityColumn[scanningX] & (1 << applySectionY)) != 0) { // ALF: WTF if can contain the value  
                                            for (int applyY = applySectionY * 3; applyY < (applySectionY + 1) * 3; applyY++) { // ALF: Take all comuns of that section and remove them
                                                allowedValues[applyX][applyY] &= valueRemoveMask;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //ALF What is doing here - in two sections
                        if (bitCount == 2 && scanningX < finalX) {
                            for (int scanningSecondPairX = scanningX + 1; scanningSecondPairX <= finalX; scanningSecondPairX++) {
                                if (sectionAvailabilityColumn[scanningX] == sectionAvailabilityColumn[scanningSecondPairX]) {
                                    final int applyX;

                                    if (scanningSecondPairX != finalX) {
                                        applyX = finalX; // ALF: LAst line
                                    } else if (scanningSecondPairX - scanningX > 1) {
                                        applyX = scanningSecondPairX - 1; // ALF: Middle line
                                    } else {
                                        applyX = scanningX - 1; // Firstr line
                                    }

                                    for (int applySectionY = 0; applySectionY < 3; applySectionY++) {
                                        if ((sectionAvailabilityColumn[scanningX] & (1 << applySectionY)) != 0) {
                                            for (int applyY = applySectionY * 3; applyY < (applySectionY + 1) * 3; applyY++) {
                                                allowedValues[applyX][applyY] &= valueRemoveMask;
                                            }
                                        }
                                    }

                                    break;
                                }
                            }
                        }
                    }
                }
            }

            final int[] sectionAvailabilityRow = new int[9];

            for (int y = 0; y < 9; y++) {
                final int finalY = y;

                for (int x = 0; x < 9; x++) {
                    if ((allowedValues[x][finalY] & valueMask) != 0) {
                        sectionAvailabilityRow[finalY] |= (1 << (x / 3));
                    }
                }

                if (finalY == 2 || finalY == 5 || finalY == 8) {
                    for (int scanningY = finalY - 2; scanningY <= finalY; scanningY++) {
                        final int bitCount = countSetBits(sectionAvailabilityRow[scanningY]);

                        if (bitCount == 1) {
                            for (int applyY = finalY - 2; applyY <= finalY; applyY++) {
                                if (scanningY != applyY) {
                                    for (int applySectionX = 0; applySectionX < 3; applySectionX++) {
                                        if ((sectionAvailabilityRow[scanningY] & (1 << applySectionX)) != 0) {
                                            for (int applyX = applySectionX * 3; applyX < (applySectionX + 1) * 3; applyX++) {
                                                allowedValues[applyX][applyY] &= valueRemoveMask;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (bitCount == 2 && scanningY < finalY) {
                            for (int scanningSecondPairY = scanningY + 1; scanningSecondPairY <= finalY; scanningSecondPairY++) {
                                if (sectionAvailabilityRow[scanningY] == sectionAvailabilityRow[scanningSecondPairY]) {
                                    final int applyY;

                                    if (scanningSecondPairY != finalY) {
                                        applyY = finalY;
                                    } else if (scanningSecondPairY - scanningY > 1) {
                                        applyY = scanningSecondPairY - 1;
                                    } else {
                                        applyY = scanningY - 1;
                                    }

                                    for (int applySectionX = 0; applySectionX < 3; applySectionX++) {
                                        if ((sectionAvailabilityRow[scanningY] & (1 << applySectionX)) != 0) {
                                            for (int applyX = applySectionX * 3; applyX < (applySectionX + 1) * 3; applyX++) {
                                                allowedValues[applyX][applyY] &= valueRemoveMask;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Nastav hodnotu
     *
     * @param board resene sudoku
     * @param allowedValues povolene hodnoty
     * @param value hodnota
     * @param x souradnice
     * @param y souradnice
     */
    private static void setValue(final Sudoku board, final int[][] allowedValues, final int value, final int x, final int y) {
        board.setValue(y, x, value);
        allowedValues[x][y] = 0;
        applyAllowedValuesMask(board, allowedValues, x, y);
    }

    /**
     * Vrati index posledniho nastaveneho bitu.
     *
     * @param value hodnota
     * @return index bitu
     */
    private static int getLastSetBitIndex(int value) {
        int bitIndex = 0;

        while (value > 0) {
            bitIndex++;
            value >>= 1;
        }

        return bitIndex;
    }

    /**
     * Nastav masku povolenych hodnot.
     *
     * @param board resene sudoku
     * @param allowedValues povolene hodnoty
     * @param x souradnice
     * @param y souradnice
     */
    private static void applyAllowedValuesMask(final Sudoku board,
            final int[][] allowedValues, final int x, final int y) {

        final int mask = ~allowedBitFields[board.getValue(y, x)];

        for (int maskApplyX = 0; maskApplyX < 9; maskApplyX++) {
            allowedValues[maskApplyX][y] &= mask;
        }

        //ALF: More readable will be to use the notiion from the previous loop, but (two dimensional adressing)
        final int[] allowedValuesRow = allowedValues[x];

        for (int maskApplyY = 0; maskApplyY < 9; maskApplyY++) {
            allowedValuesRow[maskApplyY] &= mask;
        }

        int sectionX1 = 0;
        int sectionX2 = 0;

        //ALF: Efficient, but unreadable. I would prefere much easier version, where the bit is clread in whole block, and not only in remaining 4 boxes. Or you shoud get a comment here
        //ALF: Awfull, you used "nice" math in Verifier amd here uha vase such a strange case?
        switch (x) {
            case 0:
                sectionX1 = x + 1; //ALF: In the word case you should have a constants here, not computation
                sectionX2 = x + 2; //ALF: It is enought here to set only X1, since X2 can be3 easily computed as X1 + 3
                break;
            case 1:
                sectionX1 = x - 1;
                sectionX2 = x + 1;
                break;
            case 2:
                sectionX1 = x - 2;
                sectionX2 = x - 1;
                break;
            case 3:
                sectionX1 = x + 1;
                sectionX2 = x + 2;
                break;
            case 4:
                sectionX1 = x - 1;
                sectionX2 = x + 1;
                break;
            case 5:
                sectionX1 = x - 2;
                sectionX2 = x - 1;
                break;
            case 6:
                sectionX1 = x + 1;
                sectionX2 = x + 2;
                break;
            case 7:
                sectionX1 = x - 1;
                sectionX2 = x + 1;
                break;
            case 8:
                sectionX1 = x - 2;
                sectionX2 = x - 1;
                break;

        }

        int sectionY1 = 0;
        int sectionY2 = 0;

        switch (y) {
            case 0:
                sectionY1 = y + 1;
                sectionY2 = y + 2;
                break;
            case 1:
                sectionY1 = y - 1;
                sectionY2 = y + 1;
                break;
            case 2:
                sectionY1 = y - 2;
                sectionY2 = y - 1;
                break;
            case 3:
                sectionY1 = y + 1;
                sectionY2 = y + 2;
                break;
            case 4:
                sectionY1 = y - 1;
                sectionY2 = y + 1;
                break;
            case 5:
                sectionY1 = y - 2;
                sectionY2 = y - 1;
                break;
            case 6:
                sectionY1 = y + 1;
                sectionY2 = y + 2;
                break;
            case 7:
                sectionY1 = y - 1;
                sectionY2 = y + 1;
                break;
            case 8:
                sectionY1 = y - 2;
                sectionY2 = y - 1;
                break;
        }

        final int[] allowedValuesRow1 = allowedValues[sectionX1];
        final int[] allowedValuesRow2 = allowedValues[sectionX2];

        allowedValuesRow1[sectionY1] &= mask;
        allowedValuesRow1[sectionY2] &= mask;
        allowedValuesRow2[sectionY1] &= mask;
        allowedValuesRow2[sectionY2] &= mask;
    }

    /**
     * Secte nastavene bity
     *
     * @param value cislo
     * @return pocet nastave
     */
    private static int countSetBits(int value) {
        int count = 0;

        while (value > 0) {
            value = value & (value - 1);
            count++;
        }

        return count;
    }

    /**
     * Secte hodnoty v poli
     *
     * @param array pole
     * @return soucet
     */
    private static int arraySum(final int[] array) {
        int sum = 0;

        for (int value : array) {
            sum += value;
        }

        return sum;
    }

    /**
     * Kopiruje pole
     *
     * @param matrix puvodni
     * @return kopie
     */
    private static int[][] copyGameMatrix(final int[][] matrix) {
        return new int[][] {
            Arrays.copyOf(matrix[0], 9),
            Arrays.copyOf(matrix[1], 9),
            Arrays.copyOf(matrix[2], 9),
            Arrays.copyOf(matrix[3], 9),
            Arrays.copyOf(matrix[4], 9),
            Arrays.copyOf(matrix[5], 9),
            Arrays.copyOf(matrix[6], 9),
            Arrays.copyOf(matrix[7], 9),
            Arrays.copyOf(matrix[8], 9)
        };
    }
}
