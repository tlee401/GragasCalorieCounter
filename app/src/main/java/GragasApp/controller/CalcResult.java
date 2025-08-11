package GragasApp.controller;

public class CalcResult {
  private final double bmr;
  private final double tdee;
  public CalcResult(double bmr, double tdee) {
    this.bmr = bmr;
    this.tdee = tdee;
  }
  public double getBmr()  {
    return bmr;
  }
  public double getTdee() {
    return tdee;
  }
}
