package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.BigBoiGame;
import com.mygdx.game.Screens.GameScreen;

public class MovingPlatform extends Sprite{

    private Body body;
    private World world;
    private RectangleMapObject object;
    private PolygonShape shape;
    private FixtureDef fdef;
    private TextureRegion region;
    private TiledMap map;
    private Vector2 initialPosition;
    private Fixture fixture;

    public MovingPlatform(final GameScreen screen, final Vector2 position){
        super(new Texture(Gdx.files.internal("moving_platform.png")));
        this.world = screen.getWorld();
        this.object = object;
        this.initialPosition = position;
        region = new TextureRegion(getTexture(), 0, 0, 17, 134);
        shape = new PolygonShape();
        definePlatform();
    }

    public void definePlatform(){
        BodyDef bdef = new BodyDef();
        Rectangle rect =  new Rectangle();
        rect.width = 134;
        rect.height = 17;
        rect.setPosition(initialPosition);
        bdef.type = BodyDef.BodyType.KinematicBody;
        bdef.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));
        body = world.createBody(bdef);

        shape = new PolygonShape();
        fdef = new FixtureDef();


        shape.setAsBox(rect.getWidth() / 2 , rect.getHeight() / 2 );
        fdef.shape = shape;
        fdef.filter.categoryBits = BigBoiGame.MOVING_PLATFORM_BIT;
        fdef.filter.maskBits = BigBoiGame.PLAYER_BIT | BigBoiGame.SHOT_BIT | BigBoiGame.SHIELD_BIT;
        body.createFixture(fdef).setUserData(this);
        if(body.getPosition().y >= Gdx.graphics.getHeight() -50){
            body.setLinearVelocity(0,-90);
        }
        if(body.getPosition().y <= 50){
            body.setLinearVelocity(0, 90);
        }

        setPosition(rect.getX(), rect.getY());
        setRegion(region);

    }

    public void update(float delta){
       // handle wrapping
        // update position

        if(body.getPosition().y > Gdx.graphics.getHeight() && body.getLinearVelocity().y > 0){

            body.setTransform(new Vector2(body.getPosition().x, 0), body.getAngle());
            setPosition(body.getPosition().x - getWidth()/2, 0);
        } else if(body.getPosition().y < 0 && body.getLinearVelocity().y < 0){

            body.setTransform(new Vector2(body.getPosition().x, Gdx.graphics.getHeight()), body.getAngle());
            setPosition(body.getPosition().x - getWidth()/2, Gdx.graphics.getHeight() - getHeight()/2);
        } else {
            setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        }
    }

    @Override
    public void draw(Batch batch, float a){
        super.draw(batch, a);
    }

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
    }
}
