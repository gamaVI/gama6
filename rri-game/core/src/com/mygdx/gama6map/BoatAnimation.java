package com.mygdx.gama6map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.gama6map.utils.Geolocation;
import com.mygdx.gama6map.utils.MapRasterTiles;
import com.mygdx.gama6map.utils.ZoomXY;

public class BoatAnimation {
    private final Geolocation[] geolocations;
    private final Vector2[] positions;
    private final Vector2[] interpolatedPositions;

    public BoatAnimation(Geolocation[] geolocations, ZoomXY beginTile, int numInterpolatedPoints){
        this.geolocations = geolocations;
        positions = positionsFromGeolocations(geolocations, beginTile);
        interpolatedPositions = getInterpolatedPositions(positions, numInterpolatedPoints);
    }

    public Actor create() {
        Image boat = new Image(new Texture("home.png"));//boat
        boat.setWidth(50f);
        boat.setHeight(50f);

        boat.setPosition(interpolatedPositions[0].x, interpolatedPositions[0].y);
        boat.setRotation(10);
        SequenceAction sequenceAction = new SequenceAction();
        int factorRotation = 20;
        float duration = 2f;
        for(int i=1; i<interpolatedPositions.length; i++){
            factorRotation *= -1;
            sequenceAction.addAction(
                    Actions.parallel(
                            Actions.moveTo(interpolatedPositions[i].x, interpolatedPositions[i].y, duration),
                            Actions.rotateBy(factorRotation, duration)
                    )
            );
        }
        sequenceAction.addAction(Actions.removeActor());
        boat.addAction(sequenceAction);

        return boat;
    }

    public Geolocation[] getGeolocations() {
        return geolocations;
    }

    public Vector2[] getPositions() {
        return positions;
    }

    public Vector2[] getInterpolatedPositions() {
        return interpolatedPositions;
    }

    static private Vector2[] positionsFromGeolocations(Geolocation[] geolocations, ZoomXY beginTile){
        Vector2[] positions = new Vector2[geolocations.length];
        for(int i=0; i<geolocations.length; i++)
            positions[i] = MapRasterTiles.getPixelPosition(geolocations[i].lat, geolocations[i].lng, beginTile.x, beginTile.y);
        return positions;
    }

    static private Vector2[] getInterpolatedPositions(Vector2[] positions, int num){
        Vector2[] interpolatedAll = new Vector2[positions.length + (positions.length - 1) * num];

        for(int i=0; i<positions.length - 1; i++){
            interpolatedAll[i * (num + 1)] = positions[i];
            Vector2[] interpolatedPos = getInterpolatedPositions(positions[i], positions[i+1], num, 5);
            for(int j=0; j<interpolatedPos.length; j++){
                interpolatedAll[i * (num + 1) + j + 1] = interpolatedPos[j];
            }
        }
        interpolatedAll[interpolatedAll.length - 1] = positions[positions.length - 1];
        return interpolatedAll;
    }

    static private Vector2[] getInterpolatedPositions(Vector2 point1, Vector2 point2, int num, float deviation){
        Vector2[] positions = new Vector2[num];

        // linear equation
        float m = (point2.y - point1.y) / (point2.x - point1.x);
        float b = point1.y - m * point1.x;

        float distanceX = point2.x - point1.x;
        float deltaX = distanceX / (num + 1);
        float x, y;
        for(int i=0; i<num; i++){
            x = point1.x + deltaX + deltaX*i;
            y = m*x+b;
            if(i%2 == 0) {
                y += deviation;
                x += deviation;
            }
            positions[i] = new Vector2(x, y);
        }

        return positions;
    }
}
