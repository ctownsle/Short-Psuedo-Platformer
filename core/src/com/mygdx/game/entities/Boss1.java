package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.BigBoiGame;
import com.mygdx.game.Bullets.BossBullet;
import com.mygdx.game.Screens.GameScreen;

import java.util.Random;

public class Boss1 extends Sprite {

    public World world;
    public Body body;
    private boolean hit;
    private float stateTimerVertical;
    private float stateTimerHorizontal = 0;
    private short hp;
    private BossState currentState;
    private CurrentAttack currentAttack;
    private TextureRegion region;
    private Sound sound;
    private GameScreen screen;
    Array<BossBullet> horizontalBullets;
    Array<BossBullet> verticalBullets;
    private Pepe player;
    private short[] possibleValues;
    Random r;

    public enum BossState{
        PHASEA, PHASEB
    }

    public enum CurrentAttack{
        ATTACK1, ATTACK2
    }

    public Boss1(final GameScreen screen, final Pepe player){
        super(screen.getBossAtlas().findRegion("hadtodoittoem"));
        this.world = screen.getWorld();
        this.screen = screen;
        this.player = player;
        currentState = BossState.PHASEA;
        currentAttack = CurrentAttack.ATTACK1;
        region = new TextureRegion(getTexture(), 0, 0, 222, 222);
        defineDoItToEm();
        //leftFacing = new TextureRegion(getTexture(), 35, 32, 35, 37);
        setBounds(0, 0, 222, 222);
        sound = BigBoiGame.manager.get("himoof.wav");
        setRegion(region);
        hp = 100;
        possibleValues = new short[] {0, 32, 64, 96, 128, 160, 192, 224, 256, 288, 320, 352, 384, 416, 448, 480,
        512, 544, 576, 608, 640, 672, 704, 736, 768, 800};
    }

    public void defineDoItToEm(){
        BodyDef bdef = new BodyDef();
        Rectangle rect = new Rectangle();
        bdef.position.set(849, 540);
        rect.width = 222;
        rect.height = 222;
        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        bdef.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));
        shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
        fdef.filter.categoryBits =  BigBoiGame.BOSS_BIT;
        fdef.filter.maskBits = BigBoiGame.PLAYER_BIT | BigBoiGame.SHOT_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
        setPosition(540, 540);
        setRegion(region);

        body.setLinearVelocity(0, -120);
        verticalBullets = new Array<BossBullet>();
        horizontalBullets = new Array<BossBullet>();
        r = new Random();
    }

    public void gotHit(boolean jeff){
        hp--;
        sound.play();

    }

    public void update(float delta){
        if(body.getPosition().y > Gdx.graphics.getHeight() - 100 && body.getLinearVelocity().y > 0){

            body.setLinearVelocity(new Vector2(0, -120));
            setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        } else if(body.getPosition().y < 100 && body.getLinearVelocity().y < 0){
            body.setLinearVelocity(new Vector2(0, 120));
            setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        } else {
            setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight()/2);
        }

        fireHorizontal(delta);

        for(BossBullet shot: horizontalBullets){
            shot.update(delta);
            if(shot.isHitSomething() || shot.body.getPosition().x < 0 || shot.body.getPosition().y > Gdx.graphics.getHeight() || shot.body.getPosition().y < 0){
                horizontalBullets.removeValue(shot, true);
            }
        }

        fireVertical(delta);
        for(BossBullet shot: verticalBullets){
            shot.update(delta);
            if(shot.isHitSomething() || shot.body.getPosition().x < 0 || shot.body.getPosition().y > Gdx.graphics.getHeight() || shot.body.getPosition().y < 0){
                verticalBullets.removeValue(shot, true);
            }
        }
    }

    @Override
    public void draw(Batch b, float a){
        super.draw(b, a);
    }

    public void fireHorizontal(float delta){
        stateTimerHorizontal += delta;
        if(stateTimerHorizontal > .5f){
            horizontalBullets.add(new BossBullet(screen, body.getPosition().x, body.getPosition().y, player, false));
            stateTimerHorizontal = 0;
        }
    }

    public void fireVertical(float delta){
        stateTimerVertical += delta;
        if(stateTimerVertical > 2){
            verticalBullets.add(new BossBullet(screen, (float) possibleValues[r.nextInt(25)], Gdx.graphics.getHeight(), player, true));
            stateTimerVertical = 0;
        }
    }

    public Array<BossBullet> getHorizontalBullets() {
        return horizontalBullets;
    }

    public Array<BossBullet> getVerticalBullets() {
        return verticalBullets;
    }

    public short getHp(){
        return hp;
    }

    public void cheat(){
        hp = 1;
    }
}
