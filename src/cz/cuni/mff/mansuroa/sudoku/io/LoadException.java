package cz.cuni.mff.mansuroa.sudoku.io;

import java.io.IOException;

/**
 * Chyba pri nacitani dat ze souboru.
 * 
 * @author Alexandr Mansurov
 */
public class LoadException extends IOException {

    public LoadException() {
    }
    
    LoadException(Exception e) {
        super(e);
    }
}
