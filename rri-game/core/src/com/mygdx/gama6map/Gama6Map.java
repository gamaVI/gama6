package com.mygdx.gama6map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.gama6map.BoatAnimation;
//import com.mygdx.gama6map.lang.Context;
//import com.mygdx.gama6map.lang.Renderer;
import com.mygdx.gama6map.utils.Constants;
import com.mygdx.gama6map.utils.Geolocation;
import com.mygdx.gama6map.utils.MapRasterTiles;
import com.mygdx.gama6map.utils.ZoomXY;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Gama6Map extends ApplicationAdapter implements GestureDetector.GestureListener {

	private ShapeRenderer shapeRenderer;
	private Vector3 touchPosition;

	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private OrthographicCamera camera;

	private Texture[] mapTiles;
	private ZoomXY beginTile;   // top left tile

	private SpriteBatch spriteBatch;

	// buttons
	private FitViewport hudViewport;
	private Stage hudStage;
	private Skin skin;
	private boolean showLangExample = false;

	// animation
	private Stage stage;
	private FitViewport viewport;

	// boat animation
	Geolocation[] boatCoordinates = {
			new Geolocation(46.5602f, 15.625186f),
			new Geolocation(46.5580f, 15.632482f),
			new Geolocation(46.5560f, 15.639112f),
			new Geolocation(46.5555f, 15.647974f),
			new Geolocation(46.5553f, 15.657566f)
	};
	BoatAnimation boatAnimation;

	// center geolocation
	private final Geolocation CENTER_GEOLOCATION = new Geolocation(46.557314, 15.637771);

	// test marker
	private final Geolocation MARKER_GEOLOCATION = new Geolocation(46.559070, 15.638100);

	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
		camera.position.set(Constants.MAP_WIDTH / 2f, Constants.MAP_HEIGHT / 2f, 0);
		camera.viewportWidth = Constants.MAP_WIDTH / 2f;
		camera.viewportHeight = Constants.MAP_HEIGHT / 2f;
		camera.zoom = 2f;
		camera.update();

		spriteBatch = new SpriteBatch();
		hudViewport = new FitViewport(Constants.HUD_WIDTH, Constants.HUD_HEIGHT);
		viewport = new FitViewport(Constants.MAP_WIDTH / 2f, Constants.MAP_HEIGHT / 2f, camera);

		touchPosition = new Vector3();

		try {
			//in most cases, geolocation won't be in the center of the tile because tile borders are predetermined (geolocation can be at the corner of a tile)
			ZoomXY centerTile = MapRasterTiles.getTileNumber(CENTER_GEOLOCATION.lat, CENTER_GEOLOCATION.lng, Constants.ZOOM);
			mapTiles = MapRasterTiles.getRasterTileZone(centerTile, Constants.NUM_TILES);
			//you need the beginning tile (tile on the top left corner) to convert geolocation to a location in pixels.
			beginTile = new ZoomXY(Constants.ZOOM, centerTile.x - ((Constants.NUM_TILES - 1) / 2), centerTile.y - ((Constants.NUM_TILES - 1) / 2));
		} catch (IOException e) {
			e.printStackTrace();
		}

		tiledMap = new TiledMap();
		MapLayers layers = tiledMap.getLayers();

		TiledMapTileLayer layer = new TiledMapTileLayer(Constants.NUM_TILES, Constants.NUM_TILES, MapRasterTiles.TILE_SIZE, MapRasterTiles.TILE_SIZE);
		int index = 0;
		for (int j = Constants.NUM_TILES - 1; j >= 0; j--) {
			for (int i = 0; i < Constants.NUM_TILES; i++) {
				TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
				cell.setTile(new StaticTiledMapTile(new TextureRegion(mapTiles[index], MapRasterTiles.TILE_SIZE, MapRasterTiles.TILE_SIZE)));
				layer.setCell(i, j, cell);
				index++;
			}
		}
		layers.add(layer);

		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		// buttons
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		hudStage = new Stage(hudViewport, spriteBatch);
		hudStage.addActor(createButtons());

		Gdx.input.setInputProcessor(new InputMultiplexer(hudStage, new GestureDetector(this)));

		// boat
		boatAnimation = new BoatAnimation(boatCoordinates, beginTile, 5);
		stage = new Stage(viewport, spriteBatch);
		stage.addActor(boatAnimation.create());
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);

		handleInput();

		camera.update();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		hudStage.act(Gdx.graphics.getDeltaTime());
		stage.act(Gdx.graphics.getDeltaTime());

		hudStage.draw();
		stage.draw();

		drawMarkers();

		// lang
		/*
		if(showLangExample){
			Renderer renderer = new Renderer();
			try {
				renderer.render(new FileInputStream(new File("program.txt")), new Context(shapeRenderer, camera, beginTile));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}*/
	}

	private void drawMarkers() {
		Vector2 marker = MapRasterTiles.getPixelPosition(MARKER_GEOLOCATION.lat, MARKER_GEOLOCATION.lng, beginTile.x, beginTile.y);

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.circle(marker.x, marker.y, 10);
		shapeRenderer.end();

		// boat positions
        /*for(int i=0; i<boatAnimation.getInterpolatedPositions().length; i++){
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(boatAnimation.getInterpolatedPositions()[i].x, boatAnimation.getInterpolatedPositions()[i].y, 10);
            shapeRenderer.end();
        }*/
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
		hudStage.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		touchPosition.set(x, y, 0);
		camera.unproject(touchPosition);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		camera.translate(-deltaX, deltaY);
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		if (initialDistance >= distance)
			camera.zoom += 0.02;
		else
			camera.zoom -= 0.02;
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop() {

	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			camera.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, 3, 0);
		}

		camera.zoom = MathUtils.clamp(camera.zoom, 0.5f, 2f);

		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, Constants.MAP_WIDTH - effectiveViewportWidth / 2f);
		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, Constants.MAP_HEIGHT - effectiveViewportHeight / 2f);
	}

	private Actor createButtons() {
		Table table = new Table();
		table.defaults().pad(20);

		TextButton langButton = new TextButton("Lang", skin, "toggle");
		langButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				showLangExample = !showLangExample;
			}
		});

		TextButton animButton = new TextButton("Animation", skin);
		animButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addActor(boatAnimation.create());
			}
		});

		TextButton quitButton = new TextButton("Quit", skin);
		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		Table buttonTable = new Table();
		buttonTable.defaults().padLeft(30).padRight(30);

		buttonTable.add(langButton).padBottom(15).expandX().fill().row();
		buttonTable.add(animButton).padBottom(15).fillX().row();
		buttonTable.add(quitButton).fillX();


		table.add(buttonTable);
		table.left();
		table.top();
		table.setFillParent(true);
		table.pack();

		return table;
	}
}
