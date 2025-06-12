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

    public CodeWriter(File outputFile) {
        this.outputFileName = outputFile.getName().substring(0, outputFile.getName().lastIndexOf("."));
        try {
            this.writer = new FileWriter(outputFile);
            writer.write("");
        } catch (IOException e) {
            this.writer = null;
            e.printStackTrace();
        }
    }

    public void writeArithmetic(String command) {
        try {
            writer.append("// " + command).append(System.lineSeparator());
            String arithmeticCommands = generateArithmeticCommands(command);
            writer.append(arithmeticCommands);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writePushPop(CommandType commandType, String segment, int index) {
        String command = commandType == CommandType.C_POP ? "pop" : "push";
        try {
            // add comment
            writer.append("// " + command + " " + segment + " " + index).append(System.lineSeparator());
            String pushPopCommands = commandType == CommandType.C_POP ? pop(segment, index) : push(segment, index);
            writer.append(pushPopCommands);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateArithmeticCommands(String command) {
        StringBuilder arithmeticCommand = new StringBuilder();
        // pop top of the stack
        arithmeticCommand.append(popStack());
        // pop next item from stack
        if (!command.equals("not") && !command.equals("neg")) {
            // assign item to D register
            appendLine(arithmeticCommand, "D=M");
            arithmeticCommand.append(popStack());
        }
        // if not comparison command, perform binary operations
        if (!command.equals("eq") && !command.equals("lt") && !command.equals("gt")) {
            String binaryOperationCommands = getBinaryOperationCommands(command);
            appendLine(arithmeticCommand, binaryOperationCommands);
            arithmeticCommand.append(push());
        } else {
            String comparisonCommands = getComparisonCommands(command);
            appendLine(arithmeticCommand, comparisonCommands);
        }
        return arithmeticCommand.toString();
    }

    private String getComparisonCommands(String command) {
        StringBuilder tempBuilder = new StringBuilder();
        // check equality of M and D registers to 0; result is stored in D register
        appendLine(tempBuilder, "D=M-D");
        // set value of top of stack to false; default value
        appendLine(tempBuilder, "M=0");
        // generate comparison sequence
        String label = getComparisonLabel(command);
        appendLine(tempBuilder, "@" + label);
        String comparisonOperationCommands = getComparisonOperationCommands(command);
        appendLine(tempBuilder, comparisonOperationCommands);
        // set value of top of stack to true if compare is true
        appendLine(tempBuilder, "@SP");
        appendLine(tempBuilder, "A=M");
        appendLine(tempBuilder, "M=-1");
        // add label here if compare is false; jump here
        appendLine(tempBuilder, "(" + label + ")");
        appendLine(tempBuilder, "@SP");
        tempBuilder.append("M=M+1");
        return tempBuilder.toString();
    }

    private String push(String segment, int index) {
        StringBuilder tempBuilder = new StringBuilder();
        // initialize the address value
        tempBuilder.append(initializeValue(segment, index));
        // get current address of segments except `constant`
        if (!segment.equals("constant")) {
            String address = getAddress(segment, index);
            appendLine(tempBuilder, address);
        }
        // temp segment
        if (segment.equals("temp"))
            appendLine(tempBuilder, "A=D+A");
        // local, argument, this, that segments
        if (segment.equals("local") || segment.equals("argument") || segment.equals("this") || segment.equals("that"))
            appendLine(tempBuilder, "A=D+M");
        // assign segment address to D register except `constant`
        if (!segment.equals("constant"))
            appendLine(tempBuilder, "D=M");
        // increment stack pointer
        tempBuilder.append(push());
        return tempBuilder.toString();
    }

    private String pop(String segment, int index) {
        StringBuilder tempBuilder = new StringBuilder();
        // initialize the address value
        tempBuilder.append(initializeValue(segment, index));
        // get current address of segments
        String address = getAddress(segment, index);
        // if segment is not static or pointer, load address to memory
        if (!segment.equals("static") && !segment.equals("pointer"))
            appendLine(tempBuilder, address);
        // if temp segment, add 5 to address
        if (segment.equals("temp"))
            appendLine(tempBuilder, "D=D+A");
        // if local, argument, this, that segments, add index D to address M
        if (segment.equals("local") || segment.equals("argument") || segment.equals("this") || segment.equals("that"))
            appendLine(tempBuilder, "D=D+M");
        // assign address to @R15 if segment is not static and pointer
        if (!segment.equals("static") && !segment.equals("pointer")) {
            appendLine(tempBuilder, "@R15");
            appendLine(tempBuilder, "M=D");
        }
        // decrement stack pointer
        tempBuilder.append(pop());
        if (!segment.equals("static") && !segment.equals("pointer")) {
            appendLine(tempBuilder, "@R15");
            appendLine(tempBuilder, "A=M");
        } else {
            appendLine(tempBuilder, address);
        }
        appendLine(tempBuilder, "M=D");
        return tempBuilder.toString();
    }

    private String initializeValue(String segment, int index) {
        if (!segment.equals("static") && !segment.equals("pointer")) {
            StringBuilder tempBuilder = new StringBuilder();
            appendLine(tempBuilder, "@" + index);
            appendLine(tempBuilder, "D=A");
            return tempBuilder.toString();
        }
        return "";
    }

    private String getAddress(String segment, int index) {
        switch (segment) {
            case "temp":
                return "@5";
            case "local":
                return "@LCL";
            case "argument":
                return "@ARG";
            case "this":
                return "@THIS";
            case "that":
                return "@THAT";
            case "static":
                return "@" + outputFileName + "." + index;
            case "pointer":
                return index == 0 ? "@THIS" : "@THAT";
            default:
                return "";
        }
    }

    private String getComparisonLabel(String command) {
        switch (command) {
            case "eq":
                return "EQ_" + eq_count++;
            case "gt":
                return "GT_" + gt_count++;
            case "lt":
                return "LT_" + lt_count++;
            default:
                return "";
        }
    }

    private static String getComparisonOperationCommands(String command) {
        switch (command) {
            case "eq":
                return "D;JNE";
            case "gt":
                return "D;JLE";
            case "lt":
                return "D;JGE";
            default:
                return "";
        }
    }

    private static String getBinaryOperationCommands(String command) {
        switch (command) {
            case "neg":
                return "D=-M";
            case "not":
                return "D=!M";
            case "and":
                return "D=D&M";
            case "or":
                return "D=D|M";
            case "add":
                return "D=D+M";
            case "sub":
                return "D=M-D";
            default:
                return "";
        }
    }

    private static String push() {
        StringBuilder tempBuilder = new StringBuilder();
        appendLine(tempBuilder, "@SP");
        appendLine(tempBuilder, "A=M");
        appendLine(tempBuilder, "M=D");
        appendLine(tempBuilder, "@SP");
        appendLine(tempBuilder, "M=M+1");
        return tempBuilder.toString();
    }

    private static String pop() {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append(popStack());
        appendLine(tempBuilder, "D=M");
        return tempBuilder.toString();
    }

    private static String popStack() {
        StringBuilder tempBuilder = new StringBuilder();
        appendLine(tempBuilder, "@SP");
        appendLine(tempBuilder, "AM=M-1");
        return tempBuilder.toString();
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendLine(StringBuilder sb, String line) {
        sb.append(line).append(System.lineSeparator());
    }
}
