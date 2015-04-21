/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

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
    
    public ItemComponent(int size)//, Format f)
    {
        super(EMPTY,COLUMNS);
        this.SIZE = size;
    }
    
    public void setValue(String s)
    {
        this.setText(s);
    }
    
    public int getVal() throws ValueException
    {
        String text = super.getText();
        if (!text.equals(EMPTY)) {
            try {
                // jine chyby jiz odchyceny InputVerifierem
                int x = Integer.parseInt(super.getText());
                return x;
            } catch (NumberFormatException e) {
                //return ERR_VALUE;
                throw new ValueException();
            }
        } else {
            return UNASSIGNED;
        }
    }
    
    public class ValueException extends Exception {
        
    }
}
