package me.char321.sfadvancements.api.criteria;

public class MultiBlockCriterion extends Criterion {
    private final String machineid;

    public MultiBlockCriterion(String id, int count, String machineid) {
        super(id, count);
        this.machineid = machineid;
    }

    public String getMachineId() {
        return machineid;
    }
}
