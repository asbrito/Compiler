/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.controller;

import br.uefs.ecomp.Compilador.model.Lexical;
import br.uefs.ecomp.Compilador.model.Parsing;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author Adriel Brito & Natália Rosa
 */
public class Compiler {
    private File inputFileName[];
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File dir = new File ("teste");
        dir.mkdir();
        File file = new File(dir.getAbsolutePath());
       
        File inputFileName[] = file.listFiles();
        for (File arquivos : inputFileName) {
            
            if(arquivos.getName().endsWith(".txt") && !(arquivos.getName().startsWith("Output_"))){
                Lexical lexical = new Lexical(); 
                lexical.setLine(1);
                lexical.CheckFiles(arquivos.getAbsolutePath());
                Parsing parser = new Parsing(lexical.getTokenList(), arquivos.getName());
                parser.controllerParsing(lexical.getTokenList(), arquivos.getName());
            }
        }
    }
}
 