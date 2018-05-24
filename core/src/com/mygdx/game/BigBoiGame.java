package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Screens.TitleScreen;

public class BigBoiGame extends Game {

	public static Skin skin;
	public static final int V_WIDTH = 960;
	public static final int V_HEIGHT = 640;
	public static final float PPM = 100;
	public static final short SHOT_BIT = 32;
	public static final short PLAYER_BIT = 2;
	public static final short PLATFORM_BIT = 4;
	public static final short BOSS_BIT = 8;
	public static final short MOVING_PLATFORM_BIT = 16;
	public static final short DEFAULT_BIT = 1;
	public static final short BARRIER_BIT = 64;
	public static final short BOSS_BULLET_BIT = 128;
	public static final short SHIELD_BIT = 256;
	public static Skin deadSkin;
	public SpriteBatch batch;
	public static AssetManager manager;

	@Override
	public void create() {
		skin = new Skin(Gdx.files.internal("cyberpunk-street.json"));
		deadSkin = new Skin(Gdx.files.internal("dead-skin.json"));
		manager = new AssetManager();
		manager.load("Freeze.mp3", Music.class);
		manager.load("loludied.mp3", Music.class);
		manager.load("himoof.wav", Sound.class);
		manager.load("meoof.wav", Sound.class);
		manager.load("victory.mp3", Music.class);
		manager.finishLoading();
		this.setScreen(new TitleScreen(this));
		batch = new SpriteBatch();
	}
	@Override
	public void render(){
		super.render();
		manager.update();
	}
}
