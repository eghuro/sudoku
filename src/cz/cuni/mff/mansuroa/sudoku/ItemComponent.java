/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

import javax.swing.JButton;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class ItemComponent extends JButton{
    public ItemComponent()
    {
        super("X");
    }
    
    public void setValue(String s)
    {
        super.setText(s);
    }
}
