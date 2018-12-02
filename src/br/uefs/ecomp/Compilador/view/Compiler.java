/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.view;

import br.uefs.ecomp.Compilador.model.Lexical;
import br.uefs.ecomp.Compilador.model.Parsing;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author Adriel Brito & Natália Rosa
 */
public class Compiler {
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Lexical lexical = new Lexical(); 
        lexical.readInputFileName();
        }
    
}
 