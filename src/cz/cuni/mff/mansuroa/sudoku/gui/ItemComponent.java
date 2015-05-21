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
    }     
    
    /**
     * Nastavi hodnotu do dane komponenty.
     * 
     * @param value nova hodnota
     */
    public void setValue(String value ){
        super.setText(value);
    }

     //ALF: BAD design. Much better would be to have an int field,holding current number of the ItemComponent and set JTextFiled.setText() according this value 
    /**
     * Vrati hodnotu z dane komponenty.
     * 
     * @return hodnota v dane komponente
     * @throws ValueException pokud by v komponente nebyl platny integer
     */
    public int getVal() throws ValueException {
        String text = super.getText();
        if (!text.equals(EMPTY)) {
            try {
                // jine chyby jiz odchyceny InputVerifierem
                int x = Integer.parseInt(super.getText());
                return x;
            } catch (NumberFormatException e) {
                throw new ValueException();
            }
        } else {
            return UNASSIGNED;
        }
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