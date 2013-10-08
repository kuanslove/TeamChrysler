package com.kuan.tc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
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
	private ImageButton power, brake, pause;
	private Sprite gauge;

	private InputMultiplexer plex;

	private Sprite bg, ball_sp;
	private Sprite[] bgs;
	private SpriteBatch batch;

	// this is a test Body
	private Body ballBd, gnd;
	private boolean showdebug = true;
	private boolean pausegame = false;

	private Music music;

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

		setTest();

		batch = new SpriteBatch();
	}

	// this is just a test scene
	private void setTest() {
		// add a ground for testing
		BodyDef gndDef = new BodyDef();
		gndDef.type = BodyType.StaticBody;

		FixtureDef gndfixDef = new FixtureDef();
		gndfixDef.friction = 10f;
		gndfixDef.restitution = 0.01f;
		ChainShape ground = new ChainShape();
		ground.createChain(new Vector2[] { new Vector2(-50, -10),
				new Vector2(-5, -10), new Vector2(10, -10), new Vector2(40, 0),
				new Vector2(80, 5), new Vector2(150, 30) });
		gndfixDef.shape = ground;

		gnd = world.createBody(gndDef);
		gnd.createFixture(gndfixDef);

		// add a ground for barrier
		BodyDef barDef = new BodyDef();
		barDef.type = BodyType.DynamicBody;
		barDef.position.set(30, 30);

		FixtureDef barfixDef = new FixtureDef();
		barfixDef.density = 1f;
		barfixDef.friction = 3f;
		barfixDef.restitution = 0.3f;
		CircleShape ball = new CircleShape();
		ball.setRadius(3);
		barfixDef.shape = ball;

		ballBd = world.createBody(barDef);
		ballBd.createFixture(barfixDef);

		bgs = new Sprite[2];

		Texture texture1 = new Texture(Gdx.files.internal("data/scene/bg.jpg"));
		bg = new Sprite(texture1);
		bg.setPosition(-60, -20);
		bg.setSize(bg.getWidth() / 15, bg.getHeight() / 15);

		bgs[0] = bg;

		Texture texture2 = new Texture(
				Gdx.files.internal("data/scene/bg-night.jpg"));
		bg = new Sprite(texture2);
		bg.setPosition(-60 + bgs[0].getWidth(), -10);
		bg.setSize(bg.getWidth() / 15, bg.getHeight() / 15);

		bgs[1] = bg;

		Texture ball_tex = new Texture(Gdx.files.internal("data/scene/HGD.png"));
		ball_sp = new Sprite(ball_tex);
		ball_sp.setSize(6, 6);
		ball_sp.setOrigin(ball_sp.getWidth() / 2, ball_sp.getHeight() / 2);
	}

	public void drawBall(SpriteBatch batch) {
		ball_sp.setPosition(ballBd.getPosition().x - ball_sp.getWidth() / 2,
				ballBd.getPosition().y - ball_sp.getHeight() / 2);
		ball_sp.setRotation(ballBd.getAngle() * MathUtils.radiansToDegrees);

		ball_sp.draw(batch);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		// bg.draw(batch);
		for (Sprite b : bgs) {
			b.draw(batch);
		}

		builder.drawTruck(batch);

		// here is test ball
		drawBall(batch);

		batch.end();

		cam.position.set(builder.getTrailorBd().getPosition().x + 5, builder
				.getTrailorBd().getPosition().y + 4, 0);
		cam.update();


		if (!pausegame) {
			world.step(1 / 60f, 8, 3);
		
			if(!music.isPlaying()){
				music.play();
			}
		} else {
			if(music.isPlaying()){
				music.pause();
			}
		}
		
		// remember you should always render the world at last, or you will get a async effect
		// which is the texture will be a little ahead of the debug frame;
		if (showdebug) {
			debug.render(world, cam.combined);
		}

	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width / 60;
		cam.viewportHeight = height / 60;
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

			@Override
			public boolean keyUp(int keycode) {

				switch (keycode) {
				case Keys.D:
					showdebug = !showdebug;
					break;
				case Keys.P:
					pausegame = !pausegame;
					break;
				
				case Keys.BACKSPACE:
					builder.getRam_tireL_Jnt().setMaxMotorTorque(-20f);
					break;	
					
				case Keys.NUM_0:
					builder.getRam_tireL_Jnt().setMaxMotorTorque(0f);
					break;
				case Keys.NUM_1:
					builder.getRam_tireL_Jnt().setMaxMotorTorque(20f);
					break;
				case Keys.NUM_2:
					builder.getRam_tireL_Jnt().setMaxMotorTorque(40f);
					break;
				case Keys.NUM_3:
					builder.getRam_tireL_Jnt().setMaxMotorTorque(60f);
					break;
				default:
					break;
				}

				return true;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {
				showdebug = !showdebug;
				pausegame = !pausegame;
				return true;
			}

		});
		Gdx.input.setInputProcessor(plex);

		music = Gdx.audio.newMusic(Gdx.files.internal("data/scene/BGM.mp3"));
		music.setLooping(true);
		music.setVolume(15);
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
