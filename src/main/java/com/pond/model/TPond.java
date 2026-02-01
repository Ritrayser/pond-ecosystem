package com.pond.model;

import java.util.ArrayList;

public class TPond {
    private ArrayList<TLivingEntity> livingEntities = new ArrayList<>();
    private ArrayList<TPlant> plants = new ArrayList<>();
    private int tickCount = 0;

    public void addPlant(int x, int y) {
        TPlant plant = new TPlant(x, y);
        plants.add(plant);
        livingEntities.add(plant);
    }

    public void addLivingEntity(TLivingEntity entity) {
        livingEntities.add(entity);
    }

    public void removeLivingEntity(TLivingEntity entity) {
        livingEntities.remove(entity);
        if (entity instanceof TPlant) {
            plants.remove((TPlant) entity);
        }
    }

    public ArrayList<TLivingEntity> getLivingEntities() { return livingEntities; }
    public ArrayList<TPlant> getPlants() { return plants; }

    public TPlant findNearestPlant(THerbivore herb) {
        TPlant nearest = null;
        double minDist = herb.getPerceptionRadius();
        for (TPlant plant : plants) {
            if (plant.isAlive()) {
                double dist = herb.distanceTo(plant);
                if (dist < minDist) {
                    minDist = dist;
                    nearest = plant;
                }
            }
        }
        return nearest;
    }

    public THerbivore findNearestHerbivore(TPredator pred) {
        THerbivore nearest = null;
        double minDist = pred.getPerceptionRadius();
        for (TLivingEntity entity : livingEntities) {
            if (entity instanceof THerbivore herb && herb.isAlive()) {
                double dist = pred.distanceTo(herb);
                if (dist < minDist) {
                    minDist = dist;
                    nearest = herb;
                }
            }
        }
        return nearest;
    }

    public TPredator findNearestPredator(THerbivore herb) {
        TPredator nearest = null;
        double minDist = 100;
        for (TLivingEntity entity : livingEntities) {
            if (entity instanceof TPredator pred && pred.isAlive()) {
                double dist = herb.distanceTo(pred);
                if (dist < minDist) {
                    minDist = dist;
                    nearest = pred;
                }
            }
        }
        return nearest;
    }

    public void update() {
        tickCount++;
        for (int i = livingEntities.size() - 1; i >= 0; i--) {
            TLivingEntity entity = livingEntities.get(i);
            if (entity.isAlive()) {
                entity.act(this);
            } else {
                removeLivingEntity(entity);
            }
        }
    }

    public int getTickCount() { return tickCount; }
}
