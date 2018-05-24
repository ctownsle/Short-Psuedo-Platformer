package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Background.ParallaxBackground;
import com.mygdx.game.BigBoiGame;

import java.awt.*;
import java.util.logging.FileHandler;

public class TitleScreen implements Screen {

    private Stage stage;
    private BigBoiGame game;
    private TextureAtlas atlas;

    public TitleScreen(final BigBoiGame game){
        this.game = game;
        stage = new Stage(new ScreenViewport());



        Array<Texture> textures = new Array<Texture>();

        for(int i = 1; i <=6;i++){
            textures.add(new Texture(Gdx.files.internal("img"+i+".png")));
            textures.get(textures.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        ParallaxBackground parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(0);
        ParallaxBackground background = new ParallaxBackground(textures);
        stage.addActor(background);

        Label title = new Label("BIG BOI: THE GAME", BigBoiGame.skin, "title");
        title.setAlignment(Align.center);

        title.setY(Gdx.graphics.getHeight() * 2/3);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        ImageTextButton playButton = new ImageTextButton("PLAY", BigBoiGame.skin);
        playButton.setWidth(Gdx.graphics.getWidth()/2);
        playButton.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight()/2);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                game.setScreen(new GameScreen(game));
                return true;
            }
        });
        stage.addActor(playButton);

    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
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

    public TextureAtlas getAtlas() {
        return atlas;
    }
}
