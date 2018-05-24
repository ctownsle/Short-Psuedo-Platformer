package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.BigBoiGame;
import com.mygdx.game.Bullets.PeaShooter;
import com.mygdx.game.Screens.GameScreen;

public class Pepe extends Sprite {

    public enum PepeState{
        IDLE, UP, DOWN, LEFT, RIGHT
    }


    public PepeState currentState;
    public PepeState previousState;
    private Texture pepeimg;
    public World world;
    public Body playerBody;
    private TextureRegion rightFacing;
    private TextureRegion leftFacing;
    private boolean movingRight;
    private GameScreen screen;
    private Vector2 position = new Vector2();
    private Vector2 acceleration = new Vector2();
    private Vector2 velocity = new Vector2();
    private boolean death = false;
    public Rectangle bounds = new Rectangle();
    private float stateTime = 0;
    Array<TextureRegion> frames = new Array<TextureRegion>();
    Array<PeaShooter> shots;
    int numOfShotsOnScreen;
    private Sound sound;
    private boolean shielded = false;

    public Pepe(final World world, final GameScreen gameScreen){
        super(gameScreen.getAtlas().findRegion("Pepe"));
        currentState = PepeState.IDLE;
        previousState = PepeState.IDLE;
        movingRight = true;
        this.world = world;
        this.screen = gameScreen;
        definePepe();
        rightFacing = new TextureRegion(getTexture(), 0, 32, 35, 37);
        leftFacing = new TextureRegion(getTexture(), 35, 32, 35, 37);
        frames.add(rightFacing);
        frames.add(leftFacing);
        setBounds(0, 0, 35, 37);
        setRegion(rightFacing);
        shots = new Array<PeaShooter>();
        sound = BigBoiGame.manager.get("meoof.wav");
    }

    public void definePepe(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(36, 36);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.angularDamping = 100000000100000000f;
        bdef.linearDamping = 0;
        playerBody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(20);
        fdef.filter.categoryBits =  BigBoiGame.PLAYER_BIT;
        fdef.filter.maskBits = BigBoiGame.MOVING_PLATFORM_BIT |
                BigBoiGame.PLATFORM_BIT |
                BigBoiGame.BOSS_BIT |
                BigBoiGame.BARRIER_BIT |
                BigBoiGame.BOSS_BULLET_BIT |
                BigBoiGame.SHIELD_BIT;
        fdef.shape = shape;
        playerBody.createFixture(fdef).setUserData(this);
    }

    @Override
    public void draw(Batch batch, float a){
        super.draw(batch, a);
        System.out.println("draw");
        for (PeaShooter p: shots){
            p.draw(batch);
        }
    }

    public void update(float delta, final GameScreen screen){
        movingRight = screen.getMovementDirection();
        setPosition(playerBody.getPosition().x - getWidth()/2, playerBody.getPosition().y - getHeight()/2);
        setRegion(getFrame(delta));

        for(PeaShooter shot: shots){
            shot.update(delta);
            if(shot.isHitSomething() || shot.body.getPosition().x > Gdx.graphics.getWidth() || shot.body.getPosition().x < 0){
                shots.removeValue(shot, true);
                numOfShotsOnScreen--;
            }
        }

    }

    public TextureRegion getFrame(float delta){
        currentState = getState();
        TextureRegion jeff;

        switch (currentState){
            case LEFT:
                jeff = leftFacing;
                break;
            case RIGHT:
                jeff = rightFacing;
                break;
            default:
                jeff = null;
        }

        return jeff;
    }

    public PepeState getState(){
        if(movingRight == true){
            return PepeState.RIGHT;
        } else {
            return PepeState.LEFT;
        }
    }

    public void fire(){
        if(numOfShotsOnScreen < 5) {
            shots.add(new PeaShooter(screen, playerBody.getPosition().x, playerBody.getPosition().y, movingRight ? true : false));
            numOfShotsOnScreen++;
        }
    }

    public Array<PeaShooter> getShots() {
        return shots;
    }

    public void die() {
        sound.play();
        if (shielded) {
            shielded = false;
        } else{
            death = true;
        }
    }

    public boolean getDeath(){
        return death;
    }

    public void setShielded(){
        shielded = true;
    }

    public boolean getShielded(){
        return shielded;
    }
}
