package com.pond.model;

public class TPredator extends TLivingEntity {
    private TPond pond;

    public TPredator(int x, int y, TPond pond) {
        super(x, y, 180, 2.8, 160);
        this.pond = pond;
    }

    @Override
    public void act(TPond pond) {
        THerbivore nearest = pond.findNearestHerbivore(this);

        if (nearest != null && distanceTo(nearest) <= perceptionRadius) {
            if (distanceTo(nearest) < 35) {
                moveTowards(nearest);
                if (distanceTo(nearest) < 22) {
                    attack(nearest);
                }
            } else {
                moveTowards(nearest);
            }
        } else {
            randomMove();
            health -= 0.3;
            if (health < 120) health = 120;
        }
    }

    public void removeEntity(THerbivore herb) {
        pond.removeLivingEntity(herb);
    }

    private void attack(THerbivore herb) {
        int damage = 18;
        herb.setHealth(herb.getHealth() - damage);
        health += damage / 2;
        if (health > 200) health = 200;

        if (!herb.isAlive()) {
            removeEntity(herb);
        }
    }

    private void moveTowards(THerbivore target) {
        int dx = target.getX() - x;
        int dy = target.getY() - y;
        double dist = distanceTo(target);
        if (dist > 20) {
            x += (int)(dx * speed / dist * 1.2);
            y += (int)(dy * speed / dist * 1.2);
        }
    }

    private void randomMove() {
        x += (int)(Math.random() * 35 - 17.5);
        y += (int)(Math.random() * 35 - 17.5);
        x = Math.max(35, Math.min(765, x));
        y = Math.max(35, Math.min(565, y));
    }
}
