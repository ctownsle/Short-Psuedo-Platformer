package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.BigBoiGame;
import com.mygdx.game.Screens.GameScreen;

public class Barrier extends Sprite {

    private World world;
    public Body body;
    private GameScreen screen;
    //boolean destroyed;
    boolean hitSomething;
    boolean fireRight;
    private TextureRegion region;
    float stateTime;

    public Barrier(final GameScreen screen, final float x, final float y){
        super(screen.getBossAtlas().findRegion("hadtohandsbarrier"));
        this.screen = screen;
        this.world = screen.getWorld();

        region = new TextureRegion(getTexture(), 225, 182, 74, 41);

        //setBounds(x, y, 74, 41);
        setRegion(region);
        defineBarrier(x, y);
    }

    public void defineBarrier(final float x, final float y){
        BodyDef bdef = new BodyDef();
        Rectangle rect = new Rectangle();
        rect.height = 41;
        rect.width = 74;
        rect.setPosition(new Vector2(x,y));
        bdef.type = BodyDef.BodyType.StaticBody;

        body = world.createBody(bdef);
        bdef.position.set(rect.getX(), (rect.getY() + rect.getHeight()/2));
        //setPosition(body.getPosition().x, body.getPosition().y);
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(rect.getWidth() / 2 , rect.getHeight() / 2 );
        fdef.filter.categoryBits = BigBoiGame.BARRIER_BIT;
        fdef.filter.maskBits = BigBoiGame.PLAYER_BIT | BigBoiGame.SHOT_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
        body.setTransform((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2), body.getAngle());
        setPosition(rect.getX(), rect.getY());
        setRegion(region);
    }
    @Override
    public void draw(Batch b, float a){
        super.draw(b, a);
    }
}
