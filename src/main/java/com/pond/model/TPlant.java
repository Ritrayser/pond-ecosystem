package com.pond.model;

public class TPlant extends TLivingEntity {
    public TPlant(int x, int y) {
        super(x, y, 50, 0.0, 0);
    }

    @Override
    public void act(TPond pond) {
        // Растения растут
        if (health < 100) {
            health += 2;
            if (health > 100) health = 100;
        }
    }
}
