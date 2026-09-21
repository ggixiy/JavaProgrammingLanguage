package model;

import model.pass.SkiPass;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PassRegistry {
    private final Map<String, SkiPass> passes = new HashMap<>();

    public void register(SkiPass pass) {
        if (passes.containsKey(pass.getId())) {
            throw new IllegalArgumentException("Картка з ID " + pass.getId() + " вже існує");
        }
        passes.put(pass.getId(), pass);
    }

    public Optional<SkiPass> findById(String id) {
        return Optional.ofNullable(passes.get(id));
    }

    public boolean blockPass(String id) {
        return findById(id)
                .map(p -> { p.block(); return true; })
                .orElse(false);
    }
}
