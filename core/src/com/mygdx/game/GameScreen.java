package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Digilogue on 02/11/2016.
 */
public class GameScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 1024;
    private static final float WORLD_HEIGHT = 768;
    private static final int GRID_CELL = 32;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Taxi taxi;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private final FlyingTaxiGame flyingTaxiGame;

    private Array<Rectangle> platforms = new Array<Rectangle>();

    public GameScreen(FlyingTaxiGame flyingTaxiGame) {
        this.flyingTaxiGame = flyingTaxiGame;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
//        System.out.println(width + " x " + height);
        viewport.update(width, height);
    }

    @Override
    public void show() {
        super.show();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.BLUE);

        camera = new OrthographicCamera();
        // camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        // camera.update();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);
        // viewport.update( (int) WORLD_WIDTH, (int) WORLD_HEIGHT);

        batch = new SpriteBatch();

        taxi = new Taxi(flyingTaxiGame);

        tiledMap = flyingTaxiGame.getAssetManager().get("flying-taxi-world.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        orthogonalTiledMapRenderer.setView(camera);

        buildPlatforms();

//        batch.setProjectionMatrix(camera.projection);
//        batch.setTransformMatrix(camera.view);
//        orthogonalTiledMapRenderer.render();
    }

    private void buildPlatforms() {
        MapObjects objects = tiledMap.getLayers().get("Platforms").getObjects();

        for (MapObject object : objects) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            platforms.add(rectangle);
            System.out.println();
            System.out.println("name:" + object.getName());
            System.out.println("x: " + rectangle.getX());
            System.out.println("y: " + rectangle.getY());
            System.out.println("width: " + rectangle.getWidth());
            System.out.println("height: " + rectangle.getHeight());
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        clearScreen();
        // drawGrid();
        draw(delta);
    }

    private void update(float delta) {
        taxi.update(delta, platforms);
    }

    private void draw(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        orthogonalTiledMapRenderer.render();

        batch.begin();
        taxi.draw(batch);
        update(delta);
        batch.end();
    }

    private void clearScreen() {
        //Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
    }

    private void drawGrid() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x < viewport.getWorldWidth(); x += GRID_CELL) {
            for (int y = 0; y < viewport.getWorldHeight(); y += GRID_CELL) {
                shapeRenderer.rect(x, y, GRID_CELL, GRID_CELL);
            }
        }
        shapeRenderer.end();
    }
}
