package cz.cuni.mff.mansuroa.sudoku;

import java.io.IOException;

/**
 * Chyba pri ukladani dat do souboru.
 * @author Alexandr Mansurov <alexander.mansurov@gmail.com>
 */
class StoreException extends IOException {
    public StoreException() {}
    public StoreException(Exception e) {
        super(e);
    }
}
