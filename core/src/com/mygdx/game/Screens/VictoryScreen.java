package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.BigBoiGame;

public class VictoryScreen implements Screen{

    private Stage stage;
    private BigBoiGame game;
    private TextureAtlas atlas;
    private Music sound;

    public VictoryScreen(final BigBoiGame game){
        this.game = game;
        stage = new Stage(new ScreenViewport());
        //stage.addActor();

        Label title = new Label("WOW, YOU DID IT", BigBoiGame.deadSkin, "default");
        title.setAlignment(Align.center);

        title.setY(Gdx.graphics.getHeight() * 2/3);
        title.setWidth(Gdx.graphics.getWidth());
        Label replay = new Label("PRESS R TO RESTART", BigBoiGame.deadSkin, "default");
        replay.setY(Gdx.graphics.getHeight() * 1/3);
        replay.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);
        stage.addActor(replay);
        sound = BigBoiGame.manager.get("victory.mp3");

    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        sound.play();
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            game.setScreen(new GameScreen(game));
            sound.stop();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
