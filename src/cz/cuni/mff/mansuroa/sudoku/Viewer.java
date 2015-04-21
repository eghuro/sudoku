/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

import java.awt.Component;
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
        
        for(int i=0;i<SIZE;++i){
            for(int j=0;j<SIZE;++j){
                COMPONENTS[i][j]=new ItemComponent(SIZE);
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
        menu.add(makeFileMenu()); 
        menu.add(makeSudokuMenu());
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

    private JMenu makeFileMenu() {
        JMenu file = new JMenu("File");
        file.add(makeLoadItem());
        file.add(makeStoreItem());
        return file;
    }

    private JMenu makeSudokuMenu() {
        JMenu sudoku = new JMenu("Sudoku");
        sudoku.add(makeSolveItem());
        sudoku.add(makeVerifyItem()); 
        sudoku.add(makeClearItem());   
        return sudoku;
    }

    private JMenuItem makeSolveItem() {
        JMenuItem sol = new JMenuItem(new AbstractAction("Solve") {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int row = 0; row < SIZE; row++) {
                    for(int col = 0; col < SIZE; col++) {
                        ctrl.change(row, col, COMPONENTS[col][row].getVal());
                    }
                }
                ctrl.solve();
            }
        });
        return sol;
    }

    private JMenuItem makeVerifyItem() {
        JMenuItem ver = new JMenuItem(new AbstractAction("Verify"){
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean result = ctrl.verify();
                String msg = result ? "VALID" : "INVALID";
                JOptionPane.showMessageDialog(null,msg,"SUDOKU",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return ver;
    }

    private JMenuItem makeClearItem() {
        JMenuItem clr = new JMenuItem(new AbstractAction("Clear"){

            @Override
            public void actionPerformed(ActionEvent e) {
                ctrl.clear();
            }
        });
        return clr;
    }

    private JMenuItem makeLoadItem() {
        JMenuItem load = new JMenuItem(new AbstractAction("Load board") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ctrl.load();
            }
        }); 
        return load;
    }

    private JMenuItem makeStoreItem() {
        JMenuItem store = new JMenuItem(new AbstractAction("Store current board") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ctrl.store();
            }
        });
        return store;
    }
    
    public Component getComponent() {
        return FRAME;
    }
}