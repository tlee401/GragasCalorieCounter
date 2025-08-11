public class ExerciseEntry extends APICaller implements Loggable {
    private double Calorie;
    private String Name;

    public ExerciseEntry(String Name) throws Exception {
        this.Name = Name;
        this.Calorie = APICall("nutrition", Name);
    }

    @Override
    public double GetCalorie() {
        return Calorie;
    }

    @Override
    public String GetName() {
        return Name;
    }

    @Override
    public String toString() {
        return "ExerciseEntry{" +
               "Name='" + Name + '\'' +
               ", Calorie=" + Calorie +
               '}';
    }
}
