package com.mygdx.game.HelperAI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.BigBoiGame;
import com.mygdx.game.Screens.GameScreen;

import java.awt.*;
import java.util.Random;

public class ShieldDropper extends Sprite {

    private World world;
    private Screen screen;
    private float stateTimer;
    boolean hitPlayer = false;
    boolean hitGround = false;
    private Body body;
    private TextureRegion region;
    private int random;
    Random r;


    public ShieldDropper(final GameScreen screen, float x, float y){
        super(screen.getFriendlyAiAtlas().findRegion("shield"));
        this.screen = screen;
        this.world = screen.getWorld();

        region = new TextureRegion(getTexture(), 0, 0, 64, 64);
        setBounds(x, y, 64, 64);
        setRegion(region);
        r = new Random();
        random = r.nextInt(30-10) + 10;
        defineShieldDropper();
    }

    public void defineShieldDropper(){
        BodyDef bdef = new BodyDef();
        Rectangle rect = new Rectangle();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(getWidth() / 2, getHeight()/2);
        fdef.shape = shape;
        body = world.createBody(bdef);
        fdef.filter.categoryBits = BigBoiGame.SHIELD_BIT;
        fdef.filter.maskBits = BigBoiGame.MOVING_PLATFORM_BIT | BigBoiGame.PLAYER_BIT
                | BigBoiGame.PLATFORM_BIT;
        body.createFixture(fdef).setUserData(this);
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        setRegion(region);
        body.setLinearVelocity(120, 0);

    }

    public void update(float delta){
        stateTimer += delta;
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        if(stateTimer > random && !hitPlayer && !hitGround){
            body.setLinearVelocity(0, -120);
            stateTimer = 0;
            setNewRandom();
        }
        if(body.getPosition().x > Gdx.graphics.getWidth() - 50 && body.getLinearVelocity().x > 0){
            body.setLinearVelocity(-120, 0);
        }
        if(body.getPosition().x < 50 && body.getLinearVelocity().x < 0){
            body.setLinearVelocity(120, 0);
        }
        if(hitPlayer || body.getPosition().y < 0 || hitGround){
            body.setTransform(new Vector2(50, Gdx.graphics.getHeight() + 50), body.getAngle());
            body.setLinearVelocity(120, 0);
            hitPlayer = false;
            hitGround = false;
            stateTimer = 0;
        }
    }

    @Override
    public void draw(Batch b, float a){
        super.draw(b, a);
    }

    public void setPlayerCollision(boolean flag){
        hitPlayer = flag;
    }

    public boolean isHitPlayer(){
        return hitPlayer;
    }

    public void setGroundCollision(){
        hitGround = true;
    }

    public boolean isHitGround(){
        return hitGround;
    }

    public void setNewRandom(){
        random = r.nextInt(30 -10) + 10;
    }
}
