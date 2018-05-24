package com.mygdx.game.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Bullets.BossBullet;
import com.mygdx.game.Bullets.PeaShooter;
import com.mygdx.game.HelperAI.ShieldDropper;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.BigBoiGame;
import com.mygdx.game.entities.Boss1;
import com.mygdx.game.entities.Pepe;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef){
            case BigBoiGame.SHOT_BIT | BigBoiGame.PLATFORM_BIT:
                if(fixA.getFilterData().categoryBits == BigBoiGame.SHOT_BIT)
                    ((PeaShooter) fixA.getUserData()).setCollision();
                else
                    ((PeaShooter) fixB.getUserData()).setCollision();
                break;
            case BigBoiGame.SHOT_BIT | BigBoiGame.MOVING_PLATFORM_BIT:
                if(fixA.getFilterData().categoryBits == BigBoiGame.SHOT_BIT) {
                    ((PeaShooter) fixA.getUserData()).setCollision();
                    //System.out.println(cDef);
                }
                else {
                    //System.out.println(cDef);
                    ((PeaShooter) fixB.getUserData()).setCollision();
                }
                break;
            case BigBoiGame.SHOT_BIT | BigBoiGame.BOSS_BIT:
                if(fixA.getFilterData().categoryBits == BigBoiGame.SHOT_BIT){
                    ((PeaShooter) fixA.getUserData()).setCollision();
                    ((Boss1) fixB.getUserData()).gotHit(true);
                } else {
                    ((PeaShooter) fixB.getUserData()).setCollision();
                    ((Boss1) fixA.getUserData()).gotHit(true);
                }
                break;
            case BigBoiGame.BOSS_BIT | BigBoiGame.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == BigBoiGame.PLAYER_BIT){
                    ((Pepe) fixA.getUserData()).die();
                } else {
                    ((Pepe) fixB.getUserData()).die();
                }
                break;
            case BigBoiGame.PLAYER_BIT | BigBoiGame.BARRIER_BIT:
                if(fixA.getFilterData().categoryBits == BigBoiGame.PLAYER_BIT){
                    ((Pepe) fixA.getUserData()).die();
                } else {
                    ((Pepe) fixB.getUserData()).die();
                }
                break;
            case BigBoiGame.SHOT_BIT | BigBoiGame.BARRIER_BIT:
                if(fixA.getFilterData().categoryBits == BigBoiGame.SHOT_BIT){
                    ((PeaShooter) fixA.getUserData()).setCollision();
                } else {
                    ((PeaShooter) fixB.getUserData()).setCollision();
                }
                break;
            case BigBoiGame.BOSS_BULLET_BIT | BigBoiGame.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == BigBoiGame.PLAYER_BIT){
                    ((Pepe) fixA.getUserData()).die();
                    ((BossBullet) fixB.getUserData()).setCollision();
                } else {
                    ((Pepe) fixB.getUserData()).die();
                    ((BossBullet) fixA.getUserData()).setCollision();
                }
                break;
            case BigBoiGame.SHIELD_BIT | BigBoiGame.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == BigBoiGame.PLAYER_BIT){
                    ((Pepe) fixA.getUserData()).setShielded();
                    ((ShieldDropper) fixB.getUserData()).setPlayerCollision(true);
                } else {
                    ((Pepe) fixB.getUserData()).setShielded();
                    ((ShieldDropper) fixA.getUserData()).setPlayerCollision(true);
                }
                break;
            case BigBoiGame.SHIELD_BIT | BigBoiGame.PLATFORM_BIT:
                if(fixA.getFilterData().categoryBits == BigBoiGame.SHIELD_BIT){
                    ((ShieldDropper) fixA.getUserData()).setGroundCollision();
                } else
                    ((ShieldDropper) fixB.getUserData()).setGroundCollision();
                break;
            case BigBoiGame.SHIELD_BIT | BigBoiGame.MOVING_PLATFORM_BIT:
                if(fixA.getFilterData().categoryBits == BigBoiGame.SHIELD_BIT){
                    ((ShieldDropper) fixA.getUserData()).setGroundCollision();
                } else
                    ((ShieldDropper) fixB.getUserData()).setGroundCollision();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
       // System.out.println("Contactyboi end: ");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
