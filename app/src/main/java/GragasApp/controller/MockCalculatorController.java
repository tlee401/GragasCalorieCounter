package GragasApp.controller;

// import GragasApp.model.*;

/**
 * Mock CalculatorController for testing the GUI.
 * Provides placeholder calculations.
 */
public class MockCalculatorController implements CalculatorController {

    public CalcResult calculateBmrAndTdee() {
        System.out.println("Mock: Calculating BMR and TDEE.");
        // Return some static mock values for demonstration
        return new CalcResult(2000, 2500);
    }
}
