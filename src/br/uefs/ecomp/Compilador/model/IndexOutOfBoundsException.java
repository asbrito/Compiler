/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model;

/**
 *
 * @author nati_
 */
public class IndexOutOfBoundsException extends Exception{
    private final String mensagem;
    
    public IndexOutOfBoundsException(){
        this.mensagem = "IndexOutOfBoundsException!";
    }

    public String getMensagem() {
        return mensagem;
    }

}
