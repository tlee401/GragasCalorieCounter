public class FoodEntry extends APICaller implements Loggable {
    private double Calorie;
    private String Name;

    public FoodEntry(String Name) throws Exception {
        this.Name = Name;
        this.Calorie = APICall("nutrition", Name);
    }

    @Override
    public double getCalorie() {
        return Calorie;
    }

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public String toString() {
        return "FoodEntry{" +
               "Name='" + Name + '\'' +
               ", Calorie=" + Calorie +
               '}';
    }
}
