package vmtranslator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

    private String currentCommand;
    private Scanner scanner;

    public Parser(File vmFile){
        try {
            this.scanner = new Scanner(vmFile);            
        } catch (FileNotFoundException e){
            this.scanner = new Scanner("");
        }
    }

    public boolean hasMoreLines(){
        return scanner.hasNextLine();
    }

    public void advance(){
        String command;
        do {
           command = scanner.nextLine().trim(); 
        } while (hasMoreLines() && (command.isBlank() || command.startsWith("//")));
        if (command.isBlank() || command.startsWith("//"))
            command = "";
        currentCommand = command;
    }

    public CommandType commandType(){
        if (currentCommand.startsWith("push")) return CommandType.C_PUSH;
        if (currentCommand.startsWith("pop")) return CommandType.C_POP;
        return CommandType.C_ARITHMETIC;
    }

    public String arg1(){
        String[] args = currentCommand.split(" ");
        if (commandType() == CommandType.C_PUSH || commandType() == CommandType.C_POP)
            return args[1];
        if (commandType() == CommandType.C_ARITHMETIC)
            return args[0];
        return "";
    }

    public int arg2(){
        String[] args = currentCommand.split(" ");
        return Integer.parseInt(args[2]);
    }
}
