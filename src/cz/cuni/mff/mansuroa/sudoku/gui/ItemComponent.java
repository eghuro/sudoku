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
    
    public static final String EMPTY = "";
    public static final int ERR_VALUE = -1;
    
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
     * 
     * @param value nova hodnota
     * @throws ValueException pokud hodnota neni platna
     */
    public void setValue(String value ) throws ValueException {
        int val;
        if (value.equals(EMPTY)) {
            val = UNASSIGNED;
        } else {
            val = Integer.parseInt(value);
        }
        this.setValue(val);
    }
    
    private void setValue(int value) {
        this.value = value;
        super.setText(""+value);
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
}