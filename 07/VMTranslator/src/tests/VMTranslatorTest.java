package tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;
import vmtranslator.VMTranslator;

public class VMTranslatorTest {

    @Test
    public void basicTest() {
        // Arrange
        String tBasicTestAsm = readFile("../../07/BasicTest/BasicTest.test.asm");
        // Act
        new VMTranslator("../../07/BasicTest/BasicTest.vm");
        String basicTestAsm = readFile("../../07/BasicTest/BasicTest.asm");
        // Assert
        Assert.assertEquals(tBasicTestAsm, basicTestAsm);
    }

    @Test
    public void pointerTest() {
        // Arrange
        String tPointerTestAsm = readFile("../../07/PointerTest/PointerTest.test.asm");
        // Act
        new VMTranslator("../../07/PointerTest/PointerTest.vm");
        String pointerTestAsm = readFile("../../07/PointerTest/PointerTest.asm");
        // Assert
        Assert.assertEquals(tPointerTestAsm, pointerTestAsm);
    }

    @Test
    public void simpleAdd() {
        // Arrange
        String tSimpleAddAsm = readFile("../../07/SimpleAdd/SimpleAdd.test.asm");
        // Act
        new VMTranslator("../../07/SimpleAdd/SimpleAdd.vm");
        String simpleAddAsm = readFile("../../07/SimpleAdd/SimpleAdd.asm");
        // Assert
        Assert.assertEquals(tSimpleAddAsm, simpleAddAsm);
    }

    @Test
    public void stackTest() {
        // Arrange
        String tStackTestAsm = readFile("../../07/StackTest/StackTest.test.asm");
        // Act
        new VMTranslator("../../07/StackTest/StackTest.vm");
        String stackTestAsm = readFile("../../07/StackTest/StackTest.asm");
        // Assert
        Assert.assertEquals(tStackTestAsm, stackTestAsm);
    }

    @Test
    public void staticTest() {
        // Arrange
        String tStaticTestAsm = readFile("../../07/StaticTest/StaticTest.test.asm");
        // Act
        new VMTranslator("../../07/StaticTest/StaticTest.vm");
        String staticTestAsm = readFile("../../07/StaticTest/StaticTest.asm");
        // Assert
        Assert.assertEquals(tStaticTestAsm, staticTestAsm);
    }

    @Test
    public void basicLoop() {
        // Arrange
        String tBasicLoopAsm = readFile("../../07/BasicLoop/BasicLoop.test.asm");
        // Act
        new VMTranslator("../../07/BasicLoop/BasicLoop.vm");
        String basicLoopAsm = readFile("../../07/BasicLoop/BasicLoop.asm");
        // Assert
        Assert.assertEquals(tBasicLoopAsm, basicLoopAsm);
    }

    @Test
    public void fibonacciElement() {
        // Arrange
        String tFibonacciElementAsm = readFile("../../07/FibonacciElement/FibonacciElement.test.asm");
        // Act
        new VMTranslator("../../07/FibonacciElement");
        String fibonacciElementAsm = readFile("../../07/FibonacciElement/FibonacciElement.asm");
        // Assert
        Assert.assertEquals(tFibonacciElementAsm, fibonacciElementAsm);
    }

    @Test
    public void fibonacciSeries() {
        // Arrange
        String tFibonacciSeriesAsm = readFile("../../07/FibonacciSeries/FibonacciSeries.test.asm");
        // Act
        new VMTranslator("../../07/FibonacciSeries/FibonacciSeries.vm");
        String fibonacciSeriesAsm = readFile("../../07/FibonacciSeries/FibonacciSeries.asm");
        // Assert
        Assert.assertEquals(tFibonacciSeriesAsm, fibonacciSeriesAsm);
    }

    @Test
    public void NestedCall() {
        // Arrange
        String tNestedCallAsm = readFile("../../07/NestedCall/NestedCall.test.asm");
        // Act
        new VMTranslator("../../07/NestedCall");
        String nestedCallAsm = readFile("../../07/NestedCall/NestedCall.asm");
        // Assert
        Assert.assertEquals(tNestedCallAsm, nestedCallAsm);
    }

    @Test
    public void SimpleFunction() {
        // Arrange
        String tSimpleFunctionAsm = readFile("../../07/SimpleFunction/SimpleFunction.test.asm");
        // Act
        new VMTranslator("../../07/SimpleFunction/SimpleFunction.vm");
        String simpleFunctionAsm = readFile("../../07/SimpleFunction/SimpleFunction.asm");
        // Assert
        Assert.assertEquals(tSimpleFunctionAsm, simpleFunctionAsm);
    }

    @Test
    public void StaticsTest() {
        // Arrange
        String tStaticsTestAsm = readFile("../../07/StaticsTest/StaticsTest.test.asm");
        // Act
        new VMTranslator("../../07/StaticsTest");
        String staticsTestAsm = readFile("../../07/StaticsTest/StaticsTest.asm");
        // Assert
        Assert.assertEquals(tStaticsTestAsm, staticsTestAsm);
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
