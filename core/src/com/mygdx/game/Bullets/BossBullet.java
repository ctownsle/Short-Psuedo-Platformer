package com.mygdx.game.Bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.BigBoiGame;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.entities.Pepe;

public class BossBullet extends Sprite {

    private boolean hitSomething;
    private boolean vertical;
    private World world;
    public Body body;
    private GameScreen screen;
    private TextureRegion region;


    public BossBullet(final GameScreen screen, float x, float y, Pepe player, boolean vertical) {
        super(screen.getBossAtlas().findRegion("hadtohandsbullet"));
        this.screen = screen;
        this.world = screen.getWorld();

        region = new TextureRegion(getTexture(), 225, 148, 32, 32);
        setBounds(x, y, 32, 32);
        setRegion(region);
        this.vertical = vertical;
        defineBossBullet(player);

    }

    public void defineBossBullet(Pepe player){
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() -(222/2), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);
        setPosition(body.getPosition().x, body.getPosition().y);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();

        shape.setRadius(14);
        fdef.filter.categoryBits = BigBoiGame.BOSS_BULLET_BIT;
        fdef.filter.maskBits = BigBoiGame.PLAYER_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
        //body.setLinearVelocity(new Vector2(-120, 0));
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        setRegion(region);
       // body.setTransform((getX() + getWidth() / 2), (getY() + getHeight() / 2), body.getAngle());
        if(!vertical)
            body.setLinearVelocity(-120, 0);
        else
            body.setLinearVelocity(0, -120);
    }

    public void update(float delta){
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        // System.out.println();
        if(hitSomething || body.getPosition().x < 0 || body.getPosition().y > Gdx.graphics.getHeight() || body.getPosition().y < 0){
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
