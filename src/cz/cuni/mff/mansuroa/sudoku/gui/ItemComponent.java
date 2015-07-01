package cz.cuni.mff.mansuroa.sudoku.gui;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextField;

/**
 * Komponenta pro zobrazeni policka sudoku ve Vieweru.
 */
public class ItemComponent extends JTextField {
    private static final int COLUMNS = 1;
    private static final int UNASSIGNED = 0;
    
    private static final String EMPTY = "";
    
    private final int row;
    private final int col;
    
    private int value;
    
    /**
     * Vytvori komponentu pro sudoku dane velikosti.
     * 
     * @param row
     * @param col
     */
    public ItemComponent(int row, int col) {
        super(EMPTY, COLUMNS);
        this.row = row;
        this.col = col;
        this.value = UNASSIGNED;
    }     
    
    /**
     * Nastavi hodnotu do dane komponenty.
     * Hodnoty jsou verifikovany InputVerifierem, nastavenym v ItemComponentFactory,
     * ktery vola tuto metodu
     * 
     * @param value nova hodnota
     * 
     */
    
    public void setValue(int value) {
        assert (value >= 0);
        
        this.value = value;
        if (value==UNASSIGNED) {
            super.setText(EMPTY);
        } else {
            super.setText(""+value);
        }
    }
    
    /**
     * Nastaveni hodnoty dane komponenty na "nenastaveno".
     * Specialni metoda slouzi k vetsi citelnosti kodu a prevenci chyb.
     */
    public void setEmpty() {
        setValue(UNASSIGNED);
    }

    /**
     * Vrati hodnotu z dane komponenty.
     * 
     * @return hodnota v dane komponente
     */
    public int getVal() {
        return this.value;
    }
    
    /**
     * Nastavi font pri uprave rozmeru komponenty.
     * 
     * @param newDimension novy rozmer komponenty
     */
    private void setFont(Dimension newDimension) {
        Font font = super.getFont();
        float newSize = ItemComponent.getNewSize(newDimension);
        Font newFont = font.deriveFont(newSize);
        super.setFont(newFont);
    }
    
    /**
     * Vrati obsah komponenty pro jeji rozmer.
     * 
     * @param dimension rozmer komponenty
     * @return obsah komponenty
     */
    private static float getArea(Dimension dimension) {
        return (float) (dimension.getHeight() * dimension.getWidth());
    }
    
    /**
     * Vypocita novy rozmer fontu.
     * 
     * @param newDimension nove velikost komponenty
     * @return nova velikost fontu
     */
    private static float getNewSize(Dimension newDimension) { 
        float newArea = ItemComponent.getArea(newDimension); //ALF: WTF, why this should reasonable work? (Making square of font 10 times small then the height)  
        return newArea / 100;
    }
    
    /**
     * 
     * @return 
     */
    public int getRow() {
        return this.row;
    }
    
    /**
     * 
     * @return 
     */
    public int getCol() {
        return this.col;
    }
    
    public static int getUnassignedValue() {
        return ItemComponent.UNASSIGNED;
    }
}