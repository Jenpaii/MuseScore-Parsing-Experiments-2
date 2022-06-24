import java.util.HashMap;
import java.util.Map;

public class Score {
    private Map<Integer, Part> parts = new HashMap<>();

    public Score() {
        parts = new HashMap<>();
    }

    public void addPart(Part part) {
        parts.put(part.getScorePartId(), part);
    }
    public Part getPart(int scorePartId) {
        return parts.get(scorePartId);
    }

    public String partsToString() {
        StringBuilder pb = new StringBuilder();
        pb.append("{");
        for (Part part : parts.values()) {
            pb.append("\n\n").append(part.getScorePartId()).append("=").append(part);
        } pb.append("}");
        return pb.toString();
    }

    @Override
    public String toString() {
        return "Score{" +
                "parts=" + partsToString() +
                '}';
    }
}


