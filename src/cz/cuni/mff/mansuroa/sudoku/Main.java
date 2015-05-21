package cz.cuni.mff.mansuroa.sudoku;

import cz.cuni.mff.mansuroa.sudoku.gui.Viewer;
import cz.cuni.mff.mansuroa.sudoku.gui.ViewerFactory;

/**
 * Vstupni bod aplikace obsahujici metodu main.
 */
public final class Main {
    private Main(){}
    
    /**
     * Metoda main vytvori uzivatelske rozhrani.
     * 
     * @param args na parametry prikazove radky se nebere ohled
     */
    public static void main(String[] args) {
        Viewer v = ViewerFactory.getViewerFactory().createViewer(9); //ALF: Violating contract. Calling methods from Swing/AWT outside Event Dispatching thread
    }
}
