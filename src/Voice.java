public class Voice {

    private Part parentPart;
    private Staff parentStaff;
    private int voiceNumber;

    public Voice (Part parentPart, Staff parentStaff, int voiceNumber) {
        this.parentPart = parentPart;
        this.parentStaff = parentStaff;
        this.voiceNumber = voiceNumber;
    }

    @Override
    public String toString() {
        return "Voice{" +
                "parentPart=" + parentPart.getScorePartId() +
                ", parentStaff=" + parentStaff.getStaffNumber() +
                ", voiceNumber=" + voiceNumber +
                '}';
    }
}
