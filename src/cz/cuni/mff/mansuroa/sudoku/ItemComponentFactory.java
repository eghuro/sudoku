/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

import javax.swing.InputVerifier;
import javax.swing.JComponent;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class ItemComponentFactory {
    private ItemComponentFactory(){}
    
    public static ItemComponentFactory INSTANCE=new ItemComponentFactory();
    public static ItemComponentFactory getInstance()
    {
        return INSTANCE;
    }
    
    public ItemComponent createComponent(int size)
    {
        ItemComponent ic = new ItemComponent(size);//, getFormat());
        ic.setInputVerifier(new InputVerifier(){

            @Override
            public boolean verify(JComponent input) {
                ItemComponent ic = (ItemComponent) input;
                String text = ic.getText();
                try{
                    int val = Integer.parseInt(text);
                    if ((val > 0) && (val <= size)) {
                        ic.setValue(text);
                    } else {
                        ic.setValue("");
                    }
                }catch(Exception e) {
                    ic.setValue("");
                }
                return true;
            }
        });
        return ic;
    }
}
