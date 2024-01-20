package com.mygdx.gama6map;

import static com.mygdx.gama6map.utils.ApiRequests.fetchTransactionsFromDB;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
//import com.mygdx.gama6map.lang.Context;
//import com.mygdx.gama6map.lang.Renderer;
import com.mygdx.gama6map.animation.MoneyFallingActor;
import com.mygdx.gama6map.model.Block;
import com.mygdx.gama6map.model.Transaction;
import com.mygdx.gama6map.utils.ApiRequests;
import com.mygdx.gama6map.utils.Constants;
import com.mygdx.gama6map.utils.Geolocation;
import com.mygdx.gama6map.utils.MapRasterTiles;
import com.mygdx.gama6map.utils.ZoomXY;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Gama6Map extends ApplicationAdapter implements GestureDetector.GestureListener {

    private ShapeRenderer shapeRenderer;
    private Vector3 touchPosition;

    private List<Transaction> locations;

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

    private Texture markerTexture;


    // money animation
    private Texture buildingTexture;
    private Texture moneyTexture;

    // center geolocation
    private final Geolocation CENTER_GEOLOCATION = new Geolocation(46.05108, 14.50513);// ljubljana


    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();



        markerTexture = new Texture(Gdx.files.internal("home.png"));
        buildingTexture = new Texture(Gdx.files.internal("locationImage.png"));
        moneyTexture = new Texture(Gdx.files.internal("money.png"));
        locations = fetchTransactionsFromDB();

        if (locations != null) {
            for (Transaction transaction : locations) {
                System.out.println(transaction);
            }
        }


        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
        camera.position.set(Constants.MAP_WIDTH / 2f, Constants.MAP_HEIGHT / 2f, 0);
        camera.viewportWidth = Constants.MAP_WIDTH / 2f;
        camera.viewportHeight = Constants.MAP_HEIGHT / 2f;
        camera.zoom = 0.5f;
        camera.update();

        spriteBatch = new SpriteBatch();
        hudViewport = new FitViewport(Constants.HUD_WIDTH, Constants.HUD_HEIGHT);
        viewport = new FitViewport(Constants.MAP_WIDTH / 2f, Constants.MAP_HEIGHT / 2f, camera);

        touchPosition = new Vector3();
        viewport = new FitViewport(Constants.MAP_WIDTH / 2f, Constants.MAP_HEIGHT / 2f, camera);
        stage = new Stage(viewport, spriteBatch);

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
        skin = new Skin(Gdx.files.internal("ui/tracer-ui.json"));
        hudStage = new Stage(hudViewport, spriteBatch);
        hudStage.addActor(createButtons());

        Gdx.input.setInputProcessor(new InputMultiplexer(hudStage, new GestureDetector(this)));




        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(hudStage);
        inputMultiplexer.addProcessor(new GestureDetector(this));
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                camera.zoom += amountY * 0.1f; // Adjust zoom factor
                camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 2f); // Keep zoom within limits
                return true;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);






    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        handleInput();

        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        drawMarkers();

        hudStage.act(Gdx.graphics.getDeltaTime());
        stage.act(Gdx.graphics.getDeltaTime());

        hudStage.draw();
        stage.draw();




    }

    private void drawMarkers() {
        if (locations == null) return;


        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        for (Transaction transaction : locations) {
            if (transaction.getGps() != null) {
                Vector2 markerPos = MapRasterTiles.getPixelPosition(transaction.getGps().getLat(), transaction.getGps().getLng(), beginTile.x, beginTile.y);
                spriteBatch.draw(markerTexture, markerPos.x - markerTexture.getWidth() / 2, markerPos.y - markerTexture.getHeight() / 2);
            }
        }

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        hudStage.dispose();
        markerTexture.dispose();
    }

    private void addLabelRow(Table contentTable, String labelText, String valueText) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font");
        labelStyle.fontColor = Color.WHITE;

        Label label = new Label(labelText, labelStyle);
        label.setAlignment(Align.left);
        Label value = new Label(valueText, labelStyle);
        value.setAlignment(Align.right);

        Table rowTable = new Table();
        rowTable.add(label).expandX().align(Align.left).padRight(10);
        rowTable.add(value).expandX().align(Align.right);

        contentTable.add(rowTable).growX().padTop(10).padBottom(10);
        contentTable.row();
    }

    private void showLocationPrompt(Transaction transaction) {

        Texture imageTexture = new Texture(Gdx.files.internal("new-house.png"));
        Image image = new Image(imageTexture);

        Dialog dialog = new Dialog("", skin) {
            public void result(Object obj) {
                this.hide();
            }
        };

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font");
        labelStyle.fontColor = Color.WHITE;

        dialog.setSize(300, Gdx.graphics.getHeight());
        dialog.setPosition(Gdx.graphics.getWidth() - dialog.getWidth(), 0);

        dialog.getContentTable().top().pad(30);

        MoneyFallingActor moneyFallingActor = new MoneyFallingActor(buildingTexture, moneyTexture, transaction.getTransactionAmountGross());
        dialog.getContentTable().add(moneyFallingActor).size(300, 300).padBottom(30).row(); // Adjust size as needed
        dialog.getContentTable().row();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(transaction.getTransactionDate());

        dialog.getContentTable().add().padTop(20);
        dialog.getContentTable().row();
        addLabelRow(dialog.getContentTable(), "Naslov: ", transaction.getAddress());
        addLabelRow(dialog.getContentTable(), "Geografska Sirina: ", transaction.getGps().getLatString());
        addLabelRow(dialog.getContentTable(), "Geografska dolzina: ", transaction.getGps().getLngString());
        addLabelRow(dialog.getContentTable(), "Tip nepremicnine: ", transaction.getComponentType());
        addLabelRow(dialog.getContentTable(), "Cena kvadratnega metra: ", String.format("%.1f", transaction.getTransactionAmountM2()));
        addLabelRow(dialog.getContentTable(), "Ocenjena vrednost kvadratnega metra: ", String.format("%.1f", transaction.getEstimatedAmountM2()));
        addLabelRow(dialog.getContentTable(), "Velikost parcele: ", String.format("%.1f", transaction.getTransactionSumParcelSizes()));
        addLabelRow(dialog.getContentTable(), "Datum transakcije: ", formattedDate);
        addLabelRow(dialog.getContentTable(), "Transakcija: ", String.format("%.1f", transaction.getTransactionAmountGross()));
        addLabelRow(dialog.getContentTable(), "Davek na transakcijo: ", String.format("%.1f", transaction.getTransactionTax()));
        addLabelRow(dialog.getContentTable(), "Izgradnja nepremicnine: ", transaction.getBuildingYearBuilt().toString());

        dialog.getContentTable().columnDefaults(0).padRight(10);

        dialog.setZIndex(Integer.MAX_VALUE);
        dialog.getContentTable().add().expand().fill();


        dialog.button("Close", true);
        dialog.key(Input.Keys.ESCAPE, true);
        dialog.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (x < 0 || x > dialog.getWidth() || y < 0 || y > dialog.getHeight()) {
                    dialog.hide();
                    event.stop();
                }
                return false;
            }
        });
        dialog.show(hudStage);

    }



    private void showDataWindow(List<Block> blocks) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font");
        labelStyle.fontColor = Color.WHITE;

        Dialog dialog = new Dialog("", skin);
        dialog.getTitleTable().padTop(50);
        dialog.getContentTable().padTop(50);


        Label titleLabel = new Label("Sporocila", labelStyle);
        titleLabel.setAlignment(Align.center);
        dialog.getTitleTable().clear();
        dialog.getTitleTable().add(titleLabel).center().expandX().padTop(100).padBottom(100);



        dialog.setSize(400, Gdx.graphics.getHeight());

        for (Block block : blocks) {
            Label label = new Label("Indeks: " + block.getIndex() + ", Sporocilo: " + block.getData().getMessage(), labelStyle);
            dialog.getContentTable().add(label).expandX().fillX().pad(10);
            dialog.getContentTable().row();
        }

        dialog.button("Close", true);
        dialog.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (x < 0 || x > dialog.getWidth() || y < 0 || y > dialog.getHeight()) {
                    dialog.hide();
                    event.stop();
                }
                return false;
            }
        });
        dialog.show(hudStage);
        dialog.setPosition(Gdx.graphics.getWidth() - dialog.getWidth(), Gdx.graphics.getHeight() - dialog.getHeight());
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        touchPosition.set(x, y, 0);
        camera.unproject(touchPosition);

        for (Transaction transaction : locations) {
            if (transaction.getGps() != null) {
                Vector2 markerPos = MapRasterTiles.getPixelPosition(transaction.getGps().getLat(), transaction.getGps().getLng(), beginTile.x, beginTile.y);
                float markerWidth = markerTexture.getWidth();
                float markerHeight = markerTexture.getHeight();

                if (touchPosition.x >= markerPos.x - markerWidth / 2 && touchPosition.x <= markerPos.x + markerWidth / 2
                        && touchPosition.y >= markerPos.y - markerHeight / 2 && touchPosition.y <= markerPos.y + markerHeight / 2) {
                    showLocationPrompt(transaction);
                    break;
                }
            }
        }

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
            camera.zoom += 0.01;
        else
            camera.zoom -= 0.01;
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
            camera.zoom += 0.01;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.01;
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

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 2f);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, Constants.MAP_WIDTH - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, Constants.MAP_HEIGHT - effectiveViewportHeight / 2f);
    }

    private Actor createButtons() {
        Table table = new Table();
        table.defaults().pad(20);


        TextButton fetchDataButton = new TextButton("Pridobi Sporocila", skin);
        fetchDataButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                List<Block> blocks = ApiRequests.fetchBlocksFromDB();
                if (blocks != null) {
                    showDataWindow(blocks);
                }
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

        buttonTable.add(fetchDataButton).padBottom(15).fillX().row();
        buttonTable.add(quitButton).fillX();

        table.add(buttonTable);
        table.left();
        table.top();
        table.setFillParent(true);
        table.pack();

        return table;
    }
}
