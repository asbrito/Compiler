/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.Compilador.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 *
 * @author Adriel Brito & Natália Rosa
 */
public final class Lexical {
    private final LinkedList tokenList;
    private final LinkedList errorList;
    private int line;
    private File inputFileName[];

    public Lexical() throws IOException{
        tokenList = new LinkedList();
        errorList = new LinkedList();
        line = 1;
    }

    public LinkedList getTokenList() {
        return tokenList;
    }
    
    public void CheckFiles(String name) throws FileNotFoundException, IOException{
        char c = 0;
        String previous;
        boolean already = false;
        Token token = null;
        LexicalError erro ;
        BufferedReader file = new BufferedReader(new FileReader(new File(name)));

        while(file.ready()){
            previous = "";
            if(!already && file.ready()){
                do{
                    c = (char) file.read();
                }while(isSpace(c));
            }
            if(isDigit(c) || c == '-'){
                boolean ponto = false, error = false, after = false, negative = true;
                previous = ""+c;
                if(c == '-'){
                    do{
                        c = (char) file.read();
                    }while(isSpace(c));
                    if(isDigit(c)){
                        negative = true;
                        previous +=c;
                    }
                    else{
                        if(c == '-'){
                            token = new Token(Type.ArithmaticOperator,"--",line);
                            tokenList.addLast(token);  
                            already = false;
                        }
                        else{
                            token = new Token(Type.ArithmaticOperator,"-",line);
                            tokenList.addLast(token);  
                            already = true;
                        }
                        negative = false;
                    }
                }
                if(negative){
                    c = (char) file.read();
                    while(!isArithmatic(c) && !isLogical(c) && !isRelational(c) && !isDelimiterWP(c) && !isBarN(c) && !isAsp(c) && !isSpace(c)){                 
                        if(c == '.'){
                            if(!ponto){
                                ponto = true;
                                error = true;
                            }
                            else{
                                break;
                            }
                        }
                        if(ponto && isDigit(c) && !after){
                            error = false;
                            after = true;
                        }
                        
                        if(!isDigit(c)){
                            error = true;
                        }
                        
                        previous+=c;
                        c = (char) file.read();
                    }
                    if(!error){
                        token = new Token(Type.Number,previous, line);
                        tokenList.addLast(token);
                        already = true;
                    }
                    else{
                        erro = new LexicalError(previous,"Numero mal formado", line);
                        errorList.addLast(erro);
                        already = true;
                    }
                }
            }

            else if(isLetter(c)){
                boolean error = false;
                previous = ""+c;
                c = (char) file.read();
                while (!isArithmatic(c) && !isLogical(c) && !isRelational(c) && !isDelimiter(c) && !isBarN(c) && !isAsp(c) && !isSpace(c)){
                    if (!isLetter(c) && !isDigit(c) && c != '_'){
                        error = true;
                    }
                    previous+=c;
                    c = (char) file.read();
                }

                if (!error){
                    if(isKeyword(previous)){
                        token = new Token(Type.Keyword,previous, line);
                        tokenList.addLast(token); 
                    }else{
                        token = new Token(Type.Identifier,previous, line);
                        tokenList.addLast(token);
                    }
                }
                else{
                    erro = new LexicalError(previous,"Identificador mal formado", line);
                    errorList.addLast(erro);
                }

                already = true;
            }
 
            else if(isArithmatic(c)){
                previous = ""+c;
                switch (c) {

                    case '+':
                        c = (char) file.read();
                        if(c == '+'){
                            token = new Token(Type.ArithmaticOperator, "++",line);
                            tokenList.addLast(token);
                            already = false;
                        }
                        else{
                            token = new Token(Type.ArithmaticOperator, previous,line);
                            tokenList.addLast(token);
                            already = true;
                        }
                        break;

                    case '/':
                        c = (char) file.read();

                        if(c == '*'){
                            boolean error = true;
                            do{
                                c = (char) file.read();
                                if(c == '\n'){
                                    line++;
                                }
                                if(c == '*'){
                                    c = (char) file.read();
                                    if(c == '/'){
                                        already = false;
                                        error = false;
                                        break;
                                    }
                                }
                            }while(file.ready());
                            if(error){
                                erro = new LexicalError("","Comentário não fechado", line);
                                errorList.addLast(erro);
                            }
                        }

                        else if(c == '/'){
                            do{
                                if(!file.ready())
                                    break;
                                c = (char) file.read();
                            }while(!isBarN(c));
                        }
                        else{
                            token = new Token(Type.ArithmaticOperator,"/",line);
                            tokenList.addLast(token);
                            already = true;
                        }
                        break;

                    case '*':
                        token = new Token(Type.ArithmaticOperator,"*",line);
                        tokenList.addLast(token);
                        already = false;
                        break;
                }
            }
            else if(isLogical(c)){
                if(c == '!'){
                    c = (char) file.read();
                    if(c == '='){
                        token = new Token(Type.RelationalOperator,"!=",line);
                        tokenList.addLast(token);
                        already = false;
                    }
                    else{
                        token = new Token(Type.LogicalOperator,"!",line);
                        tokenList.addLast(token);
                        already = true;
                    }
                }
                if(c == '&'){
                    c = (char) file.read();
                    if(c == '&'){
                        token = new Token(Type.LogicalOperator,"&&",line);
                        tokenList.addLast(token);
                        already = false;
                    }
                    else{
                        erro = new LexicalError("&","Operador Logico mal formado", line);
                        errorList.addLast(erro);
                        already = true;
                    }
                }
                if(c == '|'){
                    c = (char) file.read();
                    if(c == '|'){
                        token = new Token(Type.LogicalOperator,"||",line);
                        tokenList.addLast(token);
                        already = false;
                    }
                    else{
                        erro = new LexicalError("|","Operador Logico mal formado", line);
                        errorList.addLast(erro);
                        already = true;
                    }
                }
            }


            else if(isRelational(c)){
                if(c == '='){
                    c = (char) file.read();
                    if(c == '='){
                        token = new Token(Type.RelationalOperator,"==",line);
                        tokenList.addLast(token);
                        already = false;
                    }
                    else{
                        token = new Token(Type.RelationalOperator,"=",line);
                        tokenList.addLast(token);
                        already = true;
                    }
                }
                if(c == '<'){
                    c = (char) file.read();
                    if(c == '='){
                        token = new Token(Type.RelationalOperator,"<=",line);
                        tokenList.addLast(token);
                        already = false;
                    }
                    else{
                        token = new Token(Type.RelationalOperator,"<",line);
                        tokenList.addLast(token);
                        already = true;
                    }
                }
                if(c == '>'){
                    c = (char) file.read();
                    if(c == '='){
                        token = new Token(Type.RelationalOperator,">=",line);
                        tokenList.addLast(token);
                        already = false;
                    }
                    else{
                        token = new Token(Type.RelationalOperator,">",line);
                        tokenList.addLast(token);
                        already = true;
                    }
                }
            }


            else if(isDelimiter(c)){
                token = new Token(Type.Delimiter,c+"",line);
                tokenList.addLast(token);
                already = false;
            }     

            else if(isAsp(c)){
                previous = "";
                c = (char) file.read();
                while(c != '"' && file.ready() && !isBarN(c)){
                    previous += c;
                    c = (char) file.read();
                }
                if (c == '"'){
                    token = new Token(Type.String,previous,line);
                    tokenList.addLast(token);
                    already = false;
                }
                else{
                    erro = new LexicalError("","String mal formada", line);
                    errorList.addLast(erro);
                    already = false;
                }
            }

            else if (c == '\n'){
                line++;
                already = false;
            }
            
            else if (c == '\r'){
                already = false;
            }
            
            else if(isSpace(c)){
                already = false;
            }

            else{
                previous ="";
                do{
                    previous+=c;
                    c = (char) file.read();
                }while(!isArithmatic(c) && !isLogical(c) && !isRelational(c) && !isDelimiter(c) && !isBarN(c) && !isAsp(c) && !isSpace(c));
                erro = new LexicalError(previous,"Símbolo não definido", line);
                errorList.addLast(erro);
                already = true;
            }
        }
    }
    
    public void readInputFileName() throws IOException{
        File dir = new File ("");
        dir.mkdir();
        File file = new File(dir.getAbsolutePath());
        inputFileName = file.listFiles();
        for (File arquivos : inputFileName) {
            if(arquivos.getName().endsWith(".txt") && !(arquivos.getName().startsWith("Output_Syntactic"))){
                CheckFiles(arquivos.getAbsolutePath());
                writeOutputFile(arquivos.getName());
                Parsing parser = new Parsing(tokenList, arquivos.getName());
                parser.controllerParsing();
                
            }
        }
    }
    
    public void writeOutputFile(String name) throws IOException{
        File file = new File ("Output_File");
        file.mkdir();
        String dir = file.getAbsolutePath();
        FileWriter file2 = new FileWriter(dir+"\\Output_"+name);
        PrintWriter writefile = new PrintWriter(file2);
        for(int i = 0; i < tokenList.size(); i++){
            writefile.println(((Token)tokenList.get(i)).getLexeme()+" "+((Token)tokenList.get(i)).getType()+"\n");  
        }
        writefile.println(" \r\n ERROR:  \r\n");
        if(errorList.isEmpty()){
            writefile.println("Nenhum erro léxico encontrado!");
        }else{
            for(int i = 0; i < errorList.size(); i++){
                writefile.println("Linha " + ((LexicalError)errorList.get(i)).getLine() +":" + ((LexicalError)errorList.get(i)).getLexeme() + " " + ((LexicalError)errorList.get(i)).getType());
            }
        }
        file2.close();
    }
    
    
    public boolean isDigit(char c){
        return c >= 48 && c <=57 ;
    }
    
    public boolean isLetter(char c){
        return c >= 65 && c <=90 || c >= 97 && c <=122;
    }
    
    public boolean isLogical(char c){
        return c == '!' || c == '|' || c == '&';
    }
    
    public boolean isArithmatic(char c){
        return c == '+' || c == '/' || c == '*' ;
    }
    
    public boolean isRelational(char c){
        return c == '>' || c == '<' || c == '!' || c == '=' ;
    }
    
    public boolean isDelimiter(char c){
        return c == ';' || c == ',' || c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}'|| c == '.';
    }
    
     public boolean isDelimiterWP(char c){
        return c == ';' || c == ',' || c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}';
    }
     
    public boolean isAsp(char c){
        return c == '"';
    }
    
    public boolean isBarN(char c){
        return c == '\r' || c == '\n';
    }
    
    public boolean isSpace(char c){
        return c == '\t'|| c == ' ';
    }
    
    public boolean isKeyword (String s){
        return s.equals("class") || s.equals("const") || s.equals("variables") || s.equals("method") || 
               s.equals("return") || s.equals("main") || s.equals("if") || s.equals("then") ||
               s.equals("else") || s.equals("while") || s.equals("read") || s.equals("write") ||
               s.equals("void") || s.equals("int") || s.equals("float") || s.equals("bool") || 
               s.equals("string") || s.equals("true") || s.equals("false") || s.equals("extends");
    }
}