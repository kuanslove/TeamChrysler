package com.kuan.tc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kuan.tc.TC;

public class SplashScreen implements Screen {

	public TC game;
	public ImageButton startBtn;
	public Stage stage;
	
	
	public SplashScreen( final TC game){
		this.game = game;
		
		Texture texture = new Texture(Gdx.files.internal("data/ui/logoUp.png"));
		Sprite up_sp = new Sprite(texture);
		texture = new Texture(Gdx.files.internal("data/ui/logoDown.png"));
		Sprite down_sp = new Sprite(texture);
		SpriteDrawable up_spd = new SpriteDrawable(up_sp);
		SpriteDrawable up_downd = new SpriteDrawable(down_sp);
		
		startBtn = new ImageButton(up_spd, up_downd);
		startBtn.setPosition((Gdx.graphics.getWidth()-startBtn.getWidth())/2f, (Gdx.graphics.getHeight()-startBtn.getHeight())/2f);
		startBtn.addListener(new InputListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				
				game.setScreen(new MainGameScreen(game));
				Gdx.app.log("touch up", "TouchUp");
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				
				return true;
			}
		});
		
		stage = new Stage();
		stage.addActor(startBtn);
		Gdx.input.setInputProcessor(stage);
		
	}
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
