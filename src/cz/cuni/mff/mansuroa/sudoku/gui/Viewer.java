package cz.cuni.mff.mansuroa.sudoku.gui;

import cz.cuni.mff.mansuroa.sudoku.Controller;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 * Trida Viewer zobrazuje uzivatelske rozhrani a predava akce Controlleru.
 * Instance se vytvareji pomoci tovarny ViewerFactory
 * API umoznuje predevsim zmenit hodnotu nektereho policka Sudoku, dale
 * pomocne metody pro dalsi komponenty.
 */
public class Viewer {
    private final int size;
    private final JPanel panel;
    private final ItemComponent[][] components;
    private final Controller controller;
    
    /**
     * Vytvori Viewer s danymi parametry.
     * 
     * @param components policka sudoku
     * @param ctrl Controller
     * @param size pocet policek na radku resp. v sloupci, resp. v bloku
     */
    public Viewer(ItemComponent[][] components, Controller ctrl, int size) {
        this.controller = ctrl;
        this.size = size;
        this.panel = new JPanel(new GridLayout(size, size));
        this.components = components;
    }
    
    /**
     * Nastavi hodnotu do prislusneho pole mrizky.
     * 
     * @param row radek
     * @param col sloupec
     * @param value hodnota
     * @throws IllegalArgumentException neplatna pozice
     */
    public void setValue(int row, int col, int value) 
            throws IllegalArgumentException {
        if (validPosition(row, col)) {
            components[col][row].setValue(value);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Vrati rozmer zobrazovaneho Sudoku.
     * 
     * @return pocet policek na radku resp. v sloupci, resp. v bloku
     */
    public int getSize() {
        return this.size;
    }
 
    /**
     * Vyhodnoceni platnosti pozice.
     * 
     * @param row radek
     * @param col sloupec
     * @return radek i sloupec nezaporne, mensi nez rozmer mrizky
     */
    private boolean validPosition(int row, int col) {
        return (row >= 0) && (row < size) && (col >= 0) && (col < size);
    }
    
    /**
     * Vlozi komponentu na panel.
     * 
     * @param ic komponenta
     * @param c gridbag constraints
     */
    public void addComponent(ItemComponent ic, GridBagConstraints c) {
        this.panel.add(ic,c);
    }
    
    /**
     * Pristup k panelu pro zobrazeni dialogu.
     * 
     * @return panel
     */
    public JPanel getPanel() {
        return this.panel;
    }
    
    /**
     * Nastaveni fontu.
     * Font se nastavi vsem komponentam Vieweru (vizualnim polickum sudoku)
     * @param font nastavovany font
     */
    public void setFont(Font font) {
        for(ItemComponent[] ics : components) {
            for(ItemComponent ic : ics) {
                ic.setFont(font);
            }
        }
    }
    
    /**
     * Vrati aktualne nastaveny font.
     * Predpoklada se jednotny font ve vsech polickach
     * @return nastaveny font
     */
    public Font getFont() {
        //predpoklada se stejny font ve vsech ItemComponentach
        return components[0][0].getFont();
    }
}