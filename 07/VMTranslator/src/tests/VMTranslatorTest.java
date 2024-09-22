package tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;
import vmtranslator.VMTranslator;

public class VMTranslatorTest {

    @Test
    public void basicTest(){
        // Arrange
        String tBasicTestAsm = readFile("../../07/BasicTest/BasicTest.test.asm");
        // Act
        new VMTranslator("../../07/BasicTest/BasicTest.vm");
        String basicTestAsm = readFile("../../07/BasicTest/BasicTest.asm");
        // Assert
        Assert.assertEquals(tBasicTestAsm, basicTestAsm);
    }

    @Test
    public void pointerTest(){
        // Arrange
        String tPointerTestAsm = readFile("../../07/PointerTest/PointerTest.test.asm");
        // Act
        new VMTranslator("../../07/PointerTest/PointerTest.vm");
        String pointerTestAsm = readFile("../../07/PointerTest/PointerTest.asm");
        // Assert
        Assert.assertEquals(tPointerTestAsm, pointerTestAsm);
    }

    @Test
    public void simpleAdd(){
        // Arrange
        String tSimpleAddAsm = readFile("../../07/SimpleAdd/SimpleAdd.test.asm");
        // Act
        new VMTranslator("../../07/SimpleAdd/SimpleAdd.vm");
        String simpleAddAsm = readFile("../../07/SimpleAdd/SimpleAdd.asm");
        // Assert
        Assert.assertEquals(tSimpleAddAsm, simpleAddAsm);
    }

    @Test
    public void stackTest(){
        // Arrange
        String tStackTestAsm = readFile("../../07/StackTest/StackTest.test.asm");
        // Act
        new VMTranslator("../../07/StackTest/StackTest.vm");
        String stackTestAsm = readFile("../../07/StackTest/StackTest.asm");
        // Assert
        Assert.assertEquals(tStackTestAsm, stackTestAsm);
    }

    @Test
    public void staticTest(){
        // Arrange
        String tStaticTestAsm = readFile("../../07/StaticTest/StaticTest.test.asm");
        // Act
        new VMTranslator("../../07/StaticTest/StaticTest.vm");
        String staticTestAsm = readFile("../../07/StaticTest/StaticTest.asm");
        // Assert
        Assert.assertEquals(tStaticTestAsm, staticTestAsm);
    }

    private String readFile(String filePath) {
        String content = "";
        try {
            content = Files.readString(Path.of(filePath));
        } catch (IOException e) {
            return "";
        }
        return content;
    }

}
