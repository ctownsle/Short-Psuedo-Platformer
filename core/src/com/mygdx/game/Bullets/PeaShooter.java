package com.mygdx.game.Bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.BigBoiGame;
import com.mygdx.game.Screens.GameScreen;

public class PeaShooter extends Sprite {

    private World world;
    public Body body;
    private GameScreen screen;
    //boolean destroyed;
    boolean hitSomething;
    boolean fireRight;
    private TextureRegion region;
    float stateTime;

    public PeaShooter(final GameScreen screen, float x, float y, boolean direction){
        super(screen.getAtlas().findRegion("bullet"));
        this.fireRight = direction;
        this.screen = screen;
        this.world = screen.getWorld();

        region = new TextureRegion(getTexture(), 0, 0, 32, 32);
        setBounds(x,y, 32, 32);
        setRegion(region);
        definePeaShooter();
    }

    public void definePeaShooter(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 35 : getX() -35, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);
        setPosition(body.getPosition().x, body.getPosition().y);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();

        shape.setRadius(12);
        fdef.filter.categoryBits = BigBoiGame.SHOT_BIT;
        fdef.filter.maskBits = BigBoiGame.BOSS_BIT |
                BigBoiGame.PLATFORM_BIT |
                BigBoiGame.MOVING_PLATFORM_BIT|
                BigBoiGame.BARRIER_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
        body.setLinearVelocity(new Vector2(fireRight ? 120: -120, 0));
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        setRegion(region);
    }

    public void update(float delta){
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
       // System.out.println();
        if(hitSomething || body.getPosition().x > Gdx.graphics.getWidth() || body.getPosition().x < 0){
            world.destroyBody(body);
        }
    }

    public boolean isHitSomething() {
        return hitSomething;
    }

    public void setCollision(){
        hitSomething = true;
    }

    @Override
    public void draw(Batch batch, float a){
        super.draw(batch, a);
    }
}
