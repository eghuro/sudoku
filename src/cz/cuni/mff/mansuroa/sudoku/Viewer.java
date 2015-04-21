/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

import cz.cuni.mff.mansuroa.sudoku.ItemComponent.ValueException;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Viewer {
    private static final String TITLE="Sudoku";
    
    private final int SIZE;
    private final JFrame FRAME;
    private final JPanel PANEL;
    private final ItemComponent[][] COMPONENTS;
    private Controller ctrl;

    public Viewer(int size)
    {
        this.SIZE=size;
        this.FRAME=new JFrame(TITLE);
        this.PANEL=new JPanel(new GridLayout(9,9));
        
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.add(PANEL);
        
        COMPONENTS = new ItemComponent[SIZE][SIZE];
        ItemComponentFactory f = ItemComponentFactory.getInstance();
        
        for(int i=0;i<SIZE;++i){
            for(int j=0;j<SIZE;++j){
                COMPONENTS[i][j]=f.createComponent(SIZE);
            }
        }
        
        FRAME.setJMenuBar(createMenu());
    }
    
    private void fillGrid()
    {
        GridBagConstraints c;
        for(int x = 0; x < SIZE; ++x) {
            for(int y=0;y<SIZE;++y){
                c=new GridBagConstraints();
                c.gridx=x;
                c.gridy=y;
                c.fill=GridBagConstraints.BOTH;
                c.gridwidth=1;
                c.gridheight=1;
                PANEL.add(COMPONENTS[x][y],c);
            }
        }
    }
    
    private JMenuBar createMenu()
    {
        JMenuBar menu = new JMenuBar();
        JMenu sudoku = new JMenu("Sudoku");
        
        JMenuItem sol = new JMenuItem(new AbstractAction("Solve") {

            @Override
            public void actionPerformed(ActionEvent e) {
                for(int row = 0; row < SIZE; row++) {
                    for(int col = 0; col < SIZE; col++) {
                        try {
                            int val = COMPONENTS[col][row].getVal();
                            if ((val > 0 ) && (val < 10)) {
                                ctrl.change(row, col, val);
                            }
                        } catch (ValueException ex) {
                            String msg = "INVALID INPUT";
                            JOptionPane.showMessageDialog(null,msg,"SUDOKU",JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        
                    }
                }
                ctrl.solve();
            }
        });
        sudoku.add(sol);
        
        JMenuItem ver = new JMenuItem(new AbstractAction("Verify"){

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean result = ctrl.verify();
                String msg = result ? "VALID" : "INVALID";
                JOptionPane.showMessageDialog(null,msg,"SUDOKU",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        sudoku.add(ver);
        
        JMenuItem clr = new JMenuItem(new AbstractAction("Clear"){

            @Override
            public void actionPerformed(ActionEvent e) {
                ctrl.verify();
            }
        });
        sudoku.add(clr);   
                
        menu.add(sudoku);
        return menu;
    }
    
    public void setUp()
    {
        fillGrid();
        FRAME.pack();
        FRAME.setVisible(true);
    }
    
    public void setValue(int row,int col,String s)
    {
        COMPONENTS[col][row].setValue(s);
    }
    
    public void setController(Controller s)
    {
        this.ctrl=s;
    }
    
    public int getSize() {
        return this.SIZE;
    }
}