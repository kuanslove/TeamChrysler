package com.kuan.tc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.kuan.tc.TC;
import com.kuan.tc.controls.TruckBuilder;
import com.kuan.tc.controls.TruckController;
import com.kuan.tc.models.Hinge;
import com.kuan.tc.models.RamBody;
import com.kuan.tc.models.RamTire;
import com.kuan.tc.models.TrailorBody;
import com.kuan.tc.models.TrailorTire;

public class MainGameScreen implements Screen {

	public TC game;

	private Box2DDebugRenderer debug;
	private OrthographicCamera cam;

	private World world;
	private TruckBuilder builder;
	private WeldJointDef wldDef;
	private RevoluteJointDef rvDef;

	private Stage stage;
	// this is for that greenRam and redRam
	private ImageButton power, brake;

	private InputMultiplexer plex;

	private Sprite bg;
	private SpriteBatch batch;

	public MainGameScreen(TC game) {
		this.game = game;
		debug = new Box2DDebugRenderer();
		cam = new OrthographicCamera();

		world = new World(new Vector2(0, -9.81f), true);
		builder = new TruckBuilder(new Vector2(0, -5), world, new RamBody(),
				new RamTire(), new TrailorBody(), new TrailorTire(),
				new Hinge());
		wldDef = new WeldJointDef();
		rvDef = new RevoluteJointDef();
		builder.joinParts(wldDef, rvDef);

		
		//add a ground for testing
		BodyDef gndDef = new BodyDef();
		gndDef.type = BodyType.StaticBody;
		
		FixtureDef gndfixDef = new FixtureDef();
		gndfixDef.friction = 10f;
		gndfixDef.restitution = 0.01f;
		ChainShape ground = new ChainShape();
		ground.createChain(new Vector2[]{
			new Vector2(-50, -10),
			new Vector2(-5, -10),
			new Vector2(10, -10),
			new Vector2(40, 0),
			new Vector2(80, 5)
		});
		gndfixDef.shape = ground;
		
		world.createBody(gndDef).createFixture(gndfixDef);
		
		//add a ground for barrier
		BodyDef boxDef = new BodyDef();
		boxDef.type = BodyType.DynamicBody;
		boxDef.position.set(30, 10);
		
		FixtureDef boxfixDef = new FixtureDef();
		boxfixDef.friction = 5f;
		boxfixDef.restitution = 0.01f;
		PolygonShape box = new PolygonShape();
		box.setAsBox(5, 5);
		boxfixDef.shape = box;
		
		world.createBody(boxDef).createFixture(boxfixDef);
		
		Texture texture = new Texture(Gdx.files.internal("data/scene/bg.jpg"));
		bg = new Sprite(texture);
		bg.setPosition(-50, -20);
		bg.setSize(bg.getWidth() / 20, bg.getHeight() / 20);
		
		Gdx.app.log("", Float.toString(bg.getX()));
		
		batch = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		bg.draw(batch);

		builder.drawTruck(batch);
		batch.end();

		cam.position.set(builder.getTrailorBd().getPosition().x+10, builder
				.getTrailorBd().getPosition().y, 0);
		cam.update();
		debug.render(world, cam.combined);
		world.step(1 / 60f, 8, 3);
	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width / 50;
		cam.viewportHeight = height / 50;
		cam.update();
	}

	@Override
	public void show() {

		plex = new InputMultiplexer();
		plex.addProcessor(new TruckController() {

			@Override
			public boolean scrolled(int amount) {
				cam.zoom += amount / 25f;
				return true;
			}

		});
		Gdx.input.setInputProcessor(plex);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
