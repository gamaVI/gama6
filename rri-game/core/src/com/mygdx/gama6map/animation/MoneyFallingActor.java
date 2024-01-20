package com.mygdx.gama6map.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MoneyFallingActor extends Actor {
    private final Texture buildingTexture;
    private final Texture moneyTexture;
    private float[] moneyY;
    private float[] moneyX;
    private float[] delay;
    private Color backgroundColor;
    private static final int MONEY_COUNT = 6;

    public MoneyFallingActor(Texture buildingTexture, Texture moneyTexture, double transactionAmount) {
        this.buildingTexture = buildingTexture;
        this.moneyTexture = moneyTexture;
        setBackgroundColor(transactionAmount);
        moneyY = new float[MONEY_COUNT];
        moneyX = new float[MONEY_COUNT];
        delay = new float[MONEY_COUNT];
        setSize(buildingTexture.getWidth(), buildingTexture.getHeight());

        for (int i = 0; i < MONEY_COUNT; i++) {
            moneyY[i] = (float) Math.random() * getHeight();
            moneyX[i] = (float) Math.random() * getWidth();
            delay[i] = (float) Math.random() * 2.0f;
        }
    }

    private void setBackgroundColor(Double transactionAmount) {
        if (transactionAmount > 200000) {
            backgroundColor = Color.RED;
        } else if (transactionAmount >= 100000) {
            backgroundColor = Color.YELLOW;
        } else {
            backgroundColor = Color.GREEN;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (int i = 0; i < MONEY_COUNT; i++) {
            if (delay[i] > 0) {
                delay[i] -= delta;
                continue;
            }

            moneyY[i] -= delta * 100;
            if (moneyY[i] < -moneyTexture.getHeight()) {
                moneyY[i] = getHeight();
                moneyX[i] = (float) Math.random() * getWidth();
                delay[i] = (float) Math.random() * 2.0f;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.setColor(backgroundColor);
        batch.draw(buildingTexture, getX(), getY(), getWidth(), getHeight());
        batch.setColor(Color.WHITE);

        //draw money
        for (int i = 0; i < MONEY_COUNT; i++) {
            if (delay[i] <= 0) {
                batch.draw(moneyTexture, getX() + moneyX[i], getY() + moneyY[i]);
            }
        }
    }
}

