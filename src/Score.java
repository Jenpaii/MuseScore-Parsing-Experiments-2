import java.util.ArrayList;

public class Score {
    private ArrayList<Part> parts;

    public Score() {
        parts = new ArrayList<>();
    }

    public void addPart(Part part) {
        parts.add(part);
    }
    @Override
    public String toString() {
        StringBuilder partBuilder = new StringBuilder();
        for (Part part : parts) {
            partBuilder.append(part).append("\n");
        }
        return "Score{" +
                "parts=" + partBuilder +
                '}';
    }
}


