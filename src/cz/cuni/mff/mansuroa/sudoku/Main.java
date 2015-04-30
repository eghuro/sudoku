package cz.cuni.mff.mansuroa.sudoku;

import cz.cuni.mff.mansuroa.sudoku.gui.Viewer;

/**
 * Vstupni bod aplikace obsahujici metodu main.
 * 
 * @author Alexandr Mansurov
 */
public final class Main {
    private Main(){}
    
    /**
     * Metoda main vytvori uzivatelske rozhrani.
     * 
     * @param args na parametry prikazove radky se nebere ohled
     */
    public static void main(String[] args) {
        Viewer v = Viewer.ViewerFactory.getViewerFactory().createViewer(9);
    }
}
