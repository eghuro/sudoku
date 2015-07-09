package cz.cuni.mff.mansuroa.sudoku.gui;

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
        this.setHorizontalAlignment(JTextField.CENTER);
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