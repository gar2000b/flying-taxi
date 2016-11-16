package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Digilogue on 04/11/2016.
 */
public class LoadingScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 1024;
    private static final float WORLD_HEIGHT = 768;
    private static final float PROGRESS_BAR_WIDTH = 100;
    private static final float PROGRESS_BAR_HEIGHT = 25;

    private Viewport viewport;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private float progress = 0;
    private final FlyingTaxiGame flyingTaxiGame;

    public LoadingScreen(FlyingTaxiGame flyingTaxiGame) {
        this.flyingTaxiGame = flyingTaxiGame;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
    }

    @Override
    public void show() {
        super.show();
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();

        flyingTaxiGame.getAssetManager().load("Taxi-Left.png", Texture.class);
        flyingTaxiGame.getAssetManager().load("Taxi-Right.png", Texture.class);
        flyingTaxiGame.getAssetManager().load("Taxi-Left-Gear.png", Texture.class);
        flyingTaxiGame.getAssetManager().load("Taxi-Right-Gear.png", Texture.class);
        flyingTaxiGame.getAssetManager().load("flying-taxi-world.tmx", TiledMap.class);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update();
        clearScreen();
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }

    private void update() {
        if (flyingTaxiGame.getAssetManager().update()) {
            flyingTaxiGame.setScreen(new GameScreen(flyingTaxiGame));
        } else {
            progress = flyingTaxiGame.getAssetManager().getProgress();
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect((WORLD_WIDTH - PROGRESS_BAR_WIDTH) / 2, WORLD_HEIGHT / 2 - PROGRESS_BAR_HEIGHT / 2,
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
        shapeRenderer.end();
    }
}
