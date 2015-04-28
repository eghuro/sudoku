package cz.cuni.mff.mansuroa.sudoku;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class ItemComponent extends JTextField{
    static final int COLUMNS = 1;
    static final String EMPTY = "";
    static final int UNASSIGNED = 0;
    final int SIZE;
    public static final int ERR_VALUE = -1;
    private Dimension dimension;
    
    public ItemComponent(int size)
    {
        super(EMPTY,COLUMNS);
        this.SIZE = size;
        this.dimension = super.getSize();
        
        super.setInputVerifier(ItemComponent.getInputVerifier(size));
        super.addComponentListener(ItemComponent.getComponentListener());
    }
    
    public void setValue(String s)
    {
        super.setText(s);
    }
    
    public int getVal()
    {
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
    
    private static InputVerifier getInputVerifier(int size) {
        return new InputVerifier(){
            @Override
            public boolean verify(JComponent input) {
                ItemComponent ic = (ItemComponent) input;
                String text = ic.getText();
                try{
                    int val = Integer.parseInt(text);
                    if ((val > 0) && (val <= size)) {
                        ic.setValue(text);
                    } else {
                        ic.setValue(EMPTY);
                    }
                }catch(Exception e) {
                    ic.setValue("");
                }
                return true;
            }
        };
    }
    
    private static ComponentListener getComponentListener() {
        return new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension newDimension = e.getComponent().getSize();
                ItemComponent ic = (ItemComponent)e.getComponent();
                ic.setFont(newDimension);
                ic.dimension = newDimension;
            }
        };
    }
    
    public class ValueException extends RuntimeException {}
    
    private void setFont(Dimension newDimension) {
        Font font = super.getFont();
        float newSize = ItemComponent.getNewSize(this.dimension,newDimension,font);
        Font newFont = font.deriveFont(newSize);
        super.setFont(newFont);
    }
    
    private static float getArea(Dimension d) {
        return (float)(d.getHeight() * d.getWidth());
    }
    
    private static float getNewSize(Dimension oldDimension, Dimension newDimension, Font oldFont) { 
        float newArea = ItemComponent.getArea(newDimension);      
        return newArea / 100;
    }
}
