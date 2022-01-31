package me.char321.sfadvancements.api.criteria;

public class MultiBlockCriterion extends Criterion {
    private final String machineid;

    public MultiBlockCriterion(String id, int count, String name, String machineid) {
        super(id, count, name);
        this.machineid = machineid;
    }

    public String getMachineId() {
        return machineid;
    }
}
