package GragasApp.model;

/**
 * Utility class for common unit conversions used in the app.
 * 1 pound = 0.45359237 kilograms, and
 * 1 inch = 2.54 centimeters.
 *
 */
final class Units {

    private Units() {}
    static final double LB_TO_KG = 0.45359237;
    static final double CM_PER_IN = 2.54;

    // returns KG given LBs
    static double lbsToKg(double lbs) {
      return lbs * LB_TO_KG;
    }

    // returns LBs given KG
    static double kgToLbs(double kg) {
      return kg / LB_TO_KG;
    }

    // returns cm given inches rounded to nearest int
    static int inchesToCmInt(double inches) {
      return (int) Math.round(inches * CM_PER_IN);
    }

    // returns inches given cm
    static double cmToInches(double cm) {
      return cm / CM_PER_IN;
    }
}
