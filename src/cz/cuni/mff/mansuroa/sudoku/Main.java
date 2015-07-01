package cz.cuni.mff.mansuroa.sudoku;

import cz.cuni.mff.mansuroa.sudoku.gui.ViewerFactory;
import javax.swing.SwingUtilities;

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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ViewerFactory.getViewerFactory().createViewer(9);
            }
        });
        
    }
}
