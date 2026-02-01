package com.pond.model;

public abstract class TLivingEntity {
    protected int x, y;
    protected int health;
    protected double speed;
    protected int perceptionRadius;

    public TLivingEntity(int x, int y, int health, double speed, int radius) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.speed = speed;
        this.perceptionRadius = radius;
    }

    public abstract void act(TPond pond);

    public double distanceTo(TLivingEntity other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public double getSpeed() { return speed; }
    public int getPerceptionRadius() { return perceptionRadius; }
}
