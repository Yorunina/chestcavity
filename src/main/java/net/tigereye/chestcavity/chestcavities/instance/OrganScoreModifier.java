package net.tigereye.chestcavity.chestcavities.instance;

import java.util.UUID;

public record OrganScoreModifier(UUID id, String name, double amount, Operation operation) {

    public OrganScoreModifier(String id, String name, double amount, String operation) {
        this(UUID.fromString(id), name, amount, Operation.valueOf(operation));
    }

    public enum Operation {
        ADDITION,
        MULTIPLY_BASE,
        MULTIPLY_TOTAL,
        CONSTANT
    }
}
