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
        LinkedList l = lexical.getTokenList();
        Parsing parser = new Parsing(l);
        parser.controllerParsing();
      
        
                System.out.println("\n000000000\n" +
                "000000000\n" +
                "00_____00\n" +
                "000000000\n" +
                "000000000\n" +
                "00\n" +
                "00\n" +
                "00\n" +
                "\n" +
                "_0000000\n" +
                "000000000\n" +
                "00_____00\n" +
                "000000000\n" +
                "000000000\n" +
                "00_____00\n" +
                "00_____00\n" +
                "00_____00\n" +
                "\n" +
                "000000000\n" +
                "000000000\n" +
                "00_____00\n" +
                "000000000\n" +
                "000000000\n" +
                "00__00\n" +
                "00____00\n" +
                "00_____00\n" +
                "\n" +
                "_0000000\n" +
                "000000000\n" +
                "00_____00\n" +
                "000000000\n" +
                "000000000\n" +
                "00_____00\n" +
                "00_____00\n" +
                "00_____00\n" +
                "\n" +
                "000000000\n" +
                "000000000\n" +
                "00_____00\n" +
                "00000000\n" +
                "00000000\n" +
                "00_____00\n" +
                "000000000\n" +
                "000000000\n" +
                "\n" +
                "000000000\n" +
                "000000000\n" +
                "00\n" +
                "00000\n" +
                "00000\n" +
                "00\n" +
                "000000000\n" +
                "000000000\n" +
                "\n" +
                "00_____00\n" +
                "000____00\n" +
                "0000___00\n" +
                "00_00__00\n" +
                "00__00_00\n" +
                "00___0000\n" +
                "00____000\n" +
                "00_____00\n" +
                "\n" +
                "000000000\n" +
                "000000000\n" +
                "00 \n" +
                "000000000\n" +
                "000000000\n" +
                "_______00\n" +
                "000000000\n" +
                "000000000 "
                + "\n\nFelicidades meu amor! Te amo!");
    }
    
}
 