package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Background.ParallaxBackground;
import com.mygdx.game.BigBoiGame;
import com.mygdx.game.Bullets.BossBullet;
import com.mygdx.game.Bullets.PeaShooter;
import com.mygdx.game.HelperAI.ShieldDropper;
import com.mygdx.game.Tools.WorldContactListener;
import com.mygdx.game.entities.Barrier;
import com.mygdx.game.entities.Boss1;
import com.mygdx.game.entities.MovingPlatform;
import com.mygdx.game.entities.Pepe;



public class GameScreen implements Screen {

    public Stage stage;
    private BigBoiGame game;
    private OrthographicCamera camera;
    private TmxMapLoader mapLoader;
    private TiledMap initialMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Pepe pepe;
    private Viewport gamePort;
    public Pepe.PepeState playerState;
    private boolean movingRight = false;
    private int numofJumps = 0;
    private TextureAtlas atlas;
    private TextureAtlas bossAtlas;
    private TextureAtlas friendlyAiAtlas;
    private Array<MovingPlatform> movingPlatforms;
    private boolean justjumped;
    private MovingPlatform platform1;
    private MovingPlatform platform2;
    private MovingPlatform platform3;
    private ParallaxBackground parallaxBackground;
    private static final long LONG_JUMP_PRESS   = 150l;
    private Boss1 doittoemguy;
    private Array<Barrier> barriers;
    private Music music;
    private ShieldDropper sd;
    private BitmapFont font;

    public GameScreen(BigBoiGame game){
        this.game = game;
        justjumped = false;

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        stage = new Stage(new ScreenViewport());
        camera = (OrthographicCamera) stage.getViewport().getCamera();
        atlas = new TextureAtlas("Pepe.pack");
        bossAtlas = new TextureAtlas("Hadtodoittoem.pack");
        friendlyAiAtlas = new TextureAtlas("Ai.pack");
        gamePort = new FitViewport(BigBoiGame.V_WIDTH / BigBoiGame.PPM, BigBoiGame.V_HEIGHT / BigBoiGame.PPM, camera);
        playerState = Pepe.PepeState.IDLE;
        Array<Texture> textures = new Array<Texture>();

        for(int i = 1; i <=6;i++){
            textures.add(new Texture(Gdx.files.internal("img"+i+".png")));
            textures.get(textures.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(0);
        stage.addActor(parallaxBackground);

        mapLoader = new TmxMapLoader();
        initialMap = mapLoader.load("map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(initialMap);

        world = new World(new Vector2(0,0), true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        // Static platform
        for(MapObject object : initialMap.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 , rect.getHeight() / 2 );
            fdef.shape = shape;
            fdef.filter.categoryBits = BigBoiGame.PLATFORM_BIT;
            fdef.filter.maskBits = BigBoiGame.PLAYER_BIT | BigBoiGame.SHIELD_BIT;
            body.createFixture(fdef).setUserData("Static Platform");
        }

        movingPlatforms = new Array<MovingPlatform>();
        platform1 = new MovingPlatform(this, new Vector2(352, 8));
        platform2 = new MovingPlatform(this, new Vector2(512, 632));
        platform3 = new MovingPlatform(this, new Vector2(688,8));

        pepe = new Pepe(world, this);
        doittoemguy = new Boss1(this, pepe);
        world.setContactListener(new WorldContactListener());
        music = BigBoiGame.manager.get("Freeze.mp3");
        music.setLooping(true);
        music.setVolume(0.3f);
        barriers = generateBarrier();
        sd = new ShieldDropper(this, 50, Gdx.graphics.getHeight() + 50);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void update(float delta){
        handleMovement();
        world.step(1/60f, 6, 2);
        pepe.update(delta, this);
        platform1.update(delta);
        platform2.update(delta);
        platform3.update(delta);
        sd.update(delta);
        doittoemguy.update(delta);
        mapRenderer.setView(camera);
    }

    public void handleMovement(){

        if(Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            numofJumps++;
            if(numofJumps % 2 != 0) {
                pepe.playerBody.setLinearVelocity(new Vector2(0, 1000000));
                playerState = Pepe.PepeState.UP;
            } else {
                pepe.playerBody.setLinearVelocity(new Vector2(0, -1000000));
                playerState = Pepe.PepeState.DOWN;
            }

        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            //pepe.playerBody.applyLinearImpulse(new Vector2(7.25f, 0), pepe.playerBody.getWorldCenter(), true);
            pepe.playerBody.setTransform(new Vector2(pepe.playerBody.getPosition().x + 3, pepe.playerBody.getPosition().y), pepe.playerBody.getAngle());
            movingRight = true;
            parallaxBackground.setSpeed(1);
            if (pepe.playerBody.getPosition().x + pepe.getWidth()/2 > Gdx.graphics.getWidth()){
                pepe.playerBody.setTransform(new Vector2(Gdx.graphics.getWidth()/2 - pepe.getWidth(), pepe.playerBody.getPosition().y), pepe.playerBody.getAngle());
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pepe.playerBody.setTransform(new Vector2(pepe.playerBody.getPosition().x - 3, pepe.playerBody.getPosition().y), pepe.playerBody.getAngle());
            movingRight = false;
            parallaxBackground.setSpeed(-1);
            if(pepe.playerBody.getPosition().x - pepe.getWidth()/2 < 0){
                pepe.playerBody.setTransform(new Vector2(0 + pepe.getWidth()/2, pepe.playerBody.getPosition().y), pepe.playerBody.getAngle());
            }
        }

        if(!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            parallaxBackground.setSpeed(0);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            pepe.fire();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            doittoemguy.cheat();
        }
    }

    @Override
    public void render(float delta) {

        update(delta);
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
        mapRenderer.render();
        b2dr.render(world, camera.combined);
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        pepe.draw(game.batch);
        for (PeaShooter p : pepe.getShots()){
            p.draw(game.batch);
        }
        for (Barrier b: barriers){
            b.draw(game.batch);
        }

        for(BossBullet bl: doittoemguy.getHorizontalBullets()){
            bl.draw(game.batch);
        }

        for(BossBullet bl2: doittoemguy.getVerticalBullets()){
           // System.out.println(bl2.body.getPosition());
            bl2.draw(game.batch);
        }
        doittoemguy.draw(game.batch);
        sd.draw(game.batch);
        platform3.draw(game.batch);
        platform2.draw(game.batch);
        platform1.draw(game.batch);
        font.draw(game.batch, "HEALTH", Gdx.graphics.getWidth() / 2,Gdx.graphics.getHeight());
        font.draw(game.batch, String.format("%d", doittoemguy.getHp()), Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() - 20);
        game.batch.end();

        music.play();

        if(pepe.getDeath() == true){
            game.setScreen(new DeathScreen(game));
            music.stop();
            dispose();
        }

        if(doittoemguy.getHp() == 0){
            // set Screen
            game.setScreen(new VictoryScreen(game));
            music.stop();
            dispose();
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
        initialMap.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public boolean getMovementDirection(){
        return movingRight;
    }

    public World getWorld(){
        return world;
    }

    public TextureAtlas getBossAtlas() {
        return bossAtlas;
    }

    public Array<Barrier> generateBarrier(){
        Array<Barrier> barriers = new Array<Barrier>();
        for(int i = 220; i <= Gdx.graphics.getWidth(); i += 74){
            Barrier b = new Barrier(this, i, 0);
            barriers.add(b);
        }
        for(int i = 0; i <= Gdx.graphics.getWidth(); i += 74){
            Barrier b = new Barrier(this, i, Gdx.graphics.getHeight() - 40);
            barriers.add(b);
        }
        return barriers;
    }

    public TextureAtlas getFriendlyAiAtlas() {
        return friendlyAiAtlas;
    }

    public BigBoiGame getGame() {
        return game;
    }

    public Music getMusic() {
        return music;
    }
}
