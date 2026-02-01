package com.pond.model;

public class THerbivore extends TLivingEntity {
    public THerbivore(int x, int y) {
        super(x, y, 80, 2.2, 120);
    }

    @Override
    public void act(TPond pond) {
        TPlant nearestPlant = pond.findNearestPlant(this);
        TPredator nearestPredator = pond.findNearestPredator(this);

        if (nearestPredator != null && distanceTo(nearestPredator) < 80) {
            fleeFrom(nearestPredator);
        }
        else if (nearestPlant != null && distanceTo(nearestPlant) <= perceptionRadius) {
            moveTowards(nearestPlant);
            if (distanceTo(nearestPlant) < 20) {
                eat(nearestPlant);
            }
        }
        else {
            randomMove();
            health -= 0.8;
            if (health < 40) health = 40;
        }
    }

    private void fleeFrom(TPredator predator) {
        int dx = x - predator.getX();
        int dy = y - predator.getY();
        double dist = distanceTo(predator);
        x += (int)(dx * speed * 1.8 / dist);
        y += (int)(dy * speed * 1.8 / dist);
        x = Math.max(25, Math.min(775, x));
        y = Math.max(25, Math.min(575, y));
    }

    private void randomMove() {
        x += (int)(Math.random() * 25 - 12.5);
        y += (int)(Math.random() * 25 - 12.5);
        x = Math.max(25, Math.min(775, x));
        y = Math.max(25, Math.min(575, y));
    }

    public boolean isAlive() { return super.isAlive(); }
    public void eat(int amount) {
        health += amount;
        if (health > 110) health = 110;
    }

    private void eat(TPlant plant) {
        int nutrition = plant.getHealth() / 3;
        eat(nutrition);
        plant.setHealth(plant.getHealth() - nutrition);
        if (plant.getHealth() < 10) plant.setHealth(10);
    }

    private void moveTowards(TPlant target) {
        int dx = target.getX() - x;
        int dy = target.getY() - y;
        double dist = distanceTo(target);
        if (dist > 18) {
            x += (int)(dx * speed / dist);
            y += (int)(dy * speed / dist);
        }
    }
}
