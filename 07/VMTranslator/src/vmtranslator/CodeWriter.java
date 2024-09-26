package vmtranslator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

    private FileWriter writer;
    private final String outputFileName;       
    // for comparison label
    private int eq_count = 0;
    private int gt_count = 0;
    private int lt_count = 0;

    public CodeWriter(File outputFile){
        this.outputFileName = outputFile.getName().substring(0, outputFile.getName().lastIndexOf("."));
        try {
            this.writer = new FileWriter(outputFile);
            writer.write("");
        } catch (IOException e) {
            this.writer = null;
            e.printStackTrace();
        }
    }

    public void writeArithmetic(String command){
        try {
            writer.append("// " + command).append(System.lineSeparator());
            String commands = performArithmetic(command);
            writer.append(commands);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String performArithmetic(String command){
        StringBuilder arithmeticCommand = new StringBuilder();
        // pop top of the stack
        arithmeticCommand.append(popItem());
        // pop 2nd item
        if (!command.equals("not") && !command.equals("neg")) {
            // assign item to D
            arithmeticCommand.append("D=M").append(System.lineSeparator());
            arithmeticCommand.append(popItem());
        }
        // if not comparison operator, compute
        if (!command.equals("eq") && !command.equals("lt") && !command.equals("gt")){
            arithmeticCommand.append(compute(command)).append(System.lineSeparator());
            arithmeticCommand.append(pushStackPointer());
        } else {
            arithmeticCommand.append(compare(command)).append(System.lineSeparator());
        }
        return arithmeticCommand.toString();
    }

    private String compare(String command) {
        StringBuilder compareCommand = new StringBuilder();
        // check equality to 0
        compareCommand.append("D=M-D").append(System.lineSeparator());
        // set value of top of stack to false
        compareCommand.append("M=0").append(System.lineSeparator());
        // do compare
        String label = getLabel(command);
        compareCommand.append("@" + label).append(System.lineSeparator());
        compareCommand.append(getCompare(command)).append(System.lineSeparator());
        // set value of top of stack to true if compare is true 
        compareCommand.append("@SP").append(System.lineSeparator());
        compareCommand.append("A=M").append(System.lineSeparator());
        compareCommand.append("M=-1").append(System.lineSeparator());
        // add label here if compare is false; jump here
        compareCommand.append("(" + label + ")").append(System.lineSeparator());
        compareCommand.append("@SP").append(System.lineSeparator());
        compareCommand.append("M=M+1");
        return compareCommand.toString();
    }

    private String getLabel(String command) {
        if (command.equals("eq")) return "EQ_" + eq_count++;
        if (command.equals("gt")) return "GT_" + gt_count++;
        if (command.equals("lt")) return "LT_" + lt_count++;
        else return "";
    }

    private String getCompare(String command){
        if (command.equals("eq")) return "D;JNE";
        if (command.equals("gt")) return "D;JLE";
        if (command.equals("lt")) return "D;JGE";
        else return "";
    }

    private String compute(String command){
        if (command.equals("neg")) return "D=-M";
        if (command.equals("not")) return "D=!M";
        if (command.equals("and")) return "D=D&M";
        if (command.equals("or")) return "D=D|M";
        if (command.equals("add")) return "D=D+M";
        if (command.equals("sub")) return "D=M-D";
        else return "";
    }

    private String popItem(){
        return new StringBuilder()
                    .append("@SP").append(System.lineSeparator())
                    .append("AM=M-1").append(System.lineSeparator())
                    .toString();
    }

    public void writePushPop(CommandType commandType, String segment, int index){
        String command = commandType == CommandType.C_POP ? "pop" : "push";
        try {
            // add comment 
            writer.append("// " + command + " " + segment + " " + index).append(System.lineSeparator());
            String commands = commandType == CommandType.C_POP ? pop(segment, index) : push(segment, index);
            writer.append(commands);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String push(String segment, int index) {
        StringBuilder pushCommand = new StringBuilder();
        // initValueialize the address value
        pushCommand.append(initValue(segment, index));
        // get address
        if (!segment.equals("constant")) 
            pushCommand.append(getAddress(segment, index)).append(System.lineSeparator());
        if (segment.equals("temp")) 
            pushCommand.append("A=D+A").append(System.lineSeparator());
        if (segment.equals("local") || segment.equals("argument") || segment.equals("this") || segment.equals("that"))
            pushCommand.append("A=D+M").append(System.lineSeparator());
        // assign address to D reg
        if (!segment.equals("constant")) 
            pushCommand.append("D=M").append(System.lineSeparator());
        // increment stack pointer
        pushCommand.append(pushStackPointer());
        return pushCommand.toString();
    }

    private String pop(String segment, int index) {
        StringBuilder popCommand = new StringBuilder();
        // initValueialize the address value
        popCommand.append(initValue(segment, index));
        // get address
        String address = getAddress(segment, index);
        if (!segment.equals("static") && !segment.equals("pointer")) 
            popCommand.append(address).append(System.lineSeparator());
        if (segment.equals("temp")) 
            popCommand.append("D=D+A").append(System.lineSeparator());
        if (segment.equals("local") || segment.equals("argument") || segment.equals("this") || segment.equals("that"))
            popCommand.append("D=D+M").append(System.lineSeparator());
        // assign address to @R15 if not static and pointer
        if (!segment.equals("static") && !segment.equals("pointer")) {
            popCommand.append("@R15").append(System.lineSeparator());
            popCommand.append("M=D").append(System.lineSeparator());
        }
        // decrement stack pointer
        popCommand.append(popStackPointer());
        if (!segment.equals("static") && !segment.equals("pointer")) {
            popCommand.append("@R15").append(System.lineSeparator());
            popCommand.append("A=M").append(System.lineSeparator());
        } else {
            popCommand.append(address).append(System.lineSeparator());
        }
        popCommand.append("M=D").append(System.lineSeparator());
        return popCommand.toString();
    }

    private String initValue(String segment, int index){
        if (!segment.equals("static") && !segment.equals("pointer")) {
            return new StringBuilder()
                        .append("@" + index).append(System.lineSeparator())
                        .append("D=A").append(System.lineSeparator())
                        .toString();
        }
        return "";
    }

    private String getAddress(String segment, int index){
        if (segment.equals("temp")) return "@5";
        if (segment.equals("local")) return "@LCL";
        if (segment.equals("argument")) return "@ARG";
        if (segment.equals("this")) return "@THIS";
        if (segment.equals("that")) return "@THAT";
        if (segment.equals("static")) return "@" + outputFileName + "." + index;
        if (segment.equals("pointer")) return index == 0 ? "@THIS" : "@THAT";
        return "";
    }

    private String pushStackPointer(){
        return new StringBuilder()
                    .append("@SP").append(System.lineSeparator())
                    .append("A=M").append(System.lineSeparator())
                    .append("M=D").append(System.lineSeparator())
                    .append("@SP").append(System.lineSeparator())
                    .append("M=M+1").append(System.lineSeparator())
                    .toString();
    }

    private String popStackPointer(){
        return new StringBuilder()
                    .append("@SP").append(System.lineSeparator())
                    .append("M=M-1").append(System.lineSeparator())
                    .append("A=M").append(System.lineSeparator())
                    .append("D=M").append(System.lineSeparator())
                    .toString();
    }

    public void close(){
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
