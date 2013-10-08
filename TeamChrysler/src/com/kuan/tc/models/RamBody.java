package com.kuan.tc.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class RamBody {

	private BodyDef bdDef;
	private FixtureDef fixDef;
	private Sprite sprite;

	private float width, height;
	private float rate = 100f;

	public RamBody() {
		InitDef();
		setSpriteSpec();
		setShapeSpec();
	}

	public void InitDef() {
		bdDef = new BodyDef();
		fixDef = new FixtureDef();

		bdDef.type = BodyType.DynamicBody;

		fixDef.density = 2.0f;
		fixDef.friction = 0.0f;
		fixDef.restitution = 1.0f;
	}

	public void setSpriteSpec() {
		TextureAtlas atlas = new TextureAtlas(
				Gdx.files.internal("data/ramtruck/ramtruck.pack"),
				Gdx.files.internal("data/ramtruck/"));
		sprite = new Sprite(atlas.findRegion("ramBody"));
		width = sprite.getWidth() / rate;
		height = sprite.getHeight() / rate;
		
//		Gdx.app.log("width", Float.toString(width));
//		Gdx.app.log("height", Float.toString(height));
//		Gdx.app.log("sep", "========================");
		
		
		sprite.setSize(width, height);
		sprite.setOrigin(width/2f, height/2f);
	}

	public void setShapeSpec() {
		PolygonShape shape = new PolygonShape();
//		shape.setAsBox(width / 2f, height / 2f);
		shape.set(new Vector2[]{
			new Vector2(width/2, -height/2f),
			new Vector2(width/2, 0),
//			new Vector2(width*(35f/47f-0.5f), height*(5f/7f-0.5f)),
			new Vector2(width*(28f/47f-0.5f), height/2f),
			new Vector2(-width*(0.5f-20f/47f), height/2f),
//			new Vector2(-width*(0.5f-20f/47f), height*(5f/7f-0.5f)),
			new Vector2(-width*(0.5f-1f/47f), height*(5f/7f-0.5f)),
			new Vector2(-width/2f, -height/2f)
		});
		
		fixDef.shape = shape;
	}

	public BodyDef getBdDef() {
		return bdDef;
	}

	public FixtureDef getFixDef() {
		return fixDef;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setBdDef(BodyDef bdDef) {
		this.bdDef = bdDef;
	}

	public void setFixDef(FixtureDef fixDef) {
		this.fixDef = fixDef;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
}
