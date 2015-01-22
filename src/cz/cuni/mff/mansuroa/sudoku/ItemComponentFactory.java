/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

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
    
    public ItemComponent getComponent()
    {
        return new ItemComponent();
    }
}
