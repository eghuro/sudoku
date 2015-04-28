package cz.cuni.mff.mansuroa.sudoku;

import java.io.IOException;

/**
 * Chyba pri nacitani dat ze souboru.
 * @author Alexandr Mansurov <alexander.mansurov@gmail.com>
 */
class LoadException extends IOException {

    public LoadException() {
    }

    LoadException(Exception e) {
        super(e);
    }
    
}
