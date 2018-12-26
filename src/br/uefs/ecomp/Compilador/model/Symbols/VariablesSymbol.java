/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model.Symbols;

/**
 *
 * @author Adriel Brito & Natália Rosa
 */
public class VariablesSymbol extends Symbol{
    private String value;
    /*0 - normal variable
      1 - array
      2 - twoDimensional array*/
    private int array;
    private int sizeArray;
    private int sizeTwoArray;

    public int getSizeArray() {
        return sizeArray;
    }

    public void setSizeArray(int sizeArray) {
        this.sizeArray = sizeArray;
    }

    public int getSizeTwoArray() {
        return sizeTwoArray;
    }

    public void setSizeTwoArray(int sizeTwoArray) {
        this.sizeTwoArray = sizeTwoArray;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getArray() {
        return array;
    }

    public void setArray(int array) {
        this.array = array;
    }
    
}
