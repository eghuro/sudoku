package cz.cuni.mff.mansuroa.sudoku;

/**
 * Vstupni bod aplikace obsahujici metodu main
 * @author Alexandr Mansurov <alexander.mansurov@gmail.com>
 */
public class Main {
    /**
     * Metoda main vytvori uzivatelske rozhrani.
     * @param args na parametry prikazove radky se nebere ohled
     */
    public static void main(String[] args) {
        Viewer v = Viewer.ViewerFactory.getViewerFactory().createViewer(9);
    }
}
