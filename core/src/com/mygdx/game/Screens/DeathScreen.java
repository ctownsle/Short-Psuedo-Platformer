package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Background.ParallaxBackground;
import com.mygdx.game.BigBoiGame;

public class DeathScreen implements Screen {

    private Stage stage;
    private BigBoiGame game;
    private TextureAtlas atlas;
    private Music music;

    public DeathScreen(final BigBoiGame game){
        this.game = game;
        stage = new Stage(new ScreenViewport());
        //stage.addActor();

        Label title = new Label("LOL, YOU DIED", BigBoiGame.deadSkin, "default");
        title.setAlignment(Align.center);

        title.setY(Gdx.graphics.getHeight() * 2/3);
        title.setWidth(Gdx.graphics.getWidth());
        Label replay = new Label("PRESS R TO RESTART", BigBoiGame.deadSkin, "default");
        replay.setY(Gdx.graphics.getHeight() * 1/3);
        replay.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);
        stage.addActor(replay);
        music = BigBoiGame.manager.get("loludied.mp3");
        music.setLooping(true);
        music.setVolume(0.5f);

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
        music.play();
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            game.setScreen(new GameScreen(game));
            music.stop();
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
