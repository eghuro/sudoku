package cz.cuni.mff.mansuroa.sudoku.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * Komponenta pro zobrazeni policka sudoku ve Vieweru.
 * 
 * @author Alexandr Mansurov
 */
public class ItemComponent extends JTextField {
    private static final int COLUMNS = 1;
    private static final String EMPTY = "";
    private static final int UNASSIGNED = 0;
    
    private final int SIZE;
    
    public static final int ERR_VALUE = -1;
    
    /**
     * Vytvori komponentu pro sudoku dane velikosti.
     * 
     * @param size velikost sudoku
     */
    private ItemComponent(int size) {
        super(EMPTY, COLUMNS);
        this.SIZE = size; 
    }
    
    /**
     * Nastavi hodnotu do dane komponenty.
     * 
     * @param value nova hodnota
     */
    public void setValue(String value ){
        super.setText(value);
    }
    
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
     * Vyjimka pri neplatne hodnote v komponente.
     */
    public class ValueException extends RuntimeException {}
    
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
        float newArea = ItemComponent.getArea(newDimension);
        return newArea / 100;
    }
    
    /**
     * Tovarna na ItemComponent
     */
    public static class ItemComponentFactory {
        private static final ItemComponentFactory INSTANCE = new ItemComponentFactory();
        private ItemComponentFactory() {}
        
        /**
         * Umozni pristup k jedine instanci tovarny.
         * @return instance tovarny
         */
        public static ItemComponentFactory getInstance() {
            return INSTANCE;
        }
        
        /**
         * Vytvori ItemComponent pro sudoku dane velikosti.
         * 
         * @param size velikost sudoku (nezaporne)
         * @return nova komponenta
         */
        public ItemComponent create(int size) {
            if (size<0) throw new IllegalArgumentException();
            
            ItemComponent component = new ItemComponent(size);
            component.setInputVerifier(ItemComponentFactory.getInputVerifier(size));
            component.addComponentListener(ItemComponentFactory.getComponentListener());
            return component;
        }
        
       /**
        * Vytvori ComponentListener pro zmenu fontu pri zmene rozmeru komponenty.
        * 
        * @return ComponentAdapter implementujici componentResized() a upravujici 
        * velikost textu
        */
       private static ComponentListener getComponentListener() {
           return new ComponentAdapter() {
               @Override
               public void componentResized(ComponentEvent e) {
                   Dimension newDimension = e.getComponent().getSize();
                   ItemComponent ic = (ItemComponent) e.getComponent();
                   ic.setFont(newDimension);
               }
           };
       }
       
        /**
         * Vytvori overovac vstupu pro danou komponentu.
         * 
         * @param size rozmer sudoku
         * @return InputVerifier pro platne vstupni hodnoty
         */
        private static InputVerifier getInputVerifier(int size) {
            return new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    ItemComponent ic = (ItemComponent) input;
                    String text = ic.getText();
                    try {
                        int val = Integer.parseInt(text);
                        if ((val > 0) && (val <= size)) {
                            ic.setValue(text);
                        } else {
                            ic.setValue(EMPTY);
                        }
                    } catch (Exception e) {
                        ic.setValue("");
                    }
                    return true;
                }
            };
        }
    }
}