package com.kuan.tc.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Hinge {
	private BodyDef bdDef;
	private FixtureDef fixDef;
	private Sprite sprite;

	private float width, height;
	private float rate = 100f;

	public Hinge() {
		InitDef();
		setSpriteSpec();
		setShapeSpec();
	}

	public void InitDef() {
		bdDef = new BodyDef();
		fixDef = new FixtureDef();

		bdDef.type = BodyType.DynamicBody;

		fixDef.density = 0.5f;
		fixDef.friction = 0.0f;
		fixDef.restitution = 0.0f;
	}

	public void setSpriteSpec() {
		TextureAtlas atlas = new TextureAtlas(
				Gdx.files.internal("data/ramtruck/ramtruck.pack"),
				Gdx.files.internal("data/ramtruck/"));
		sprite = new Sprite(atlas.findRegion("hinge"));
		width = sprite.getWidth() / rate;
		height = sprite.getHeight() / rate;
		sprite.setSize(width, height);
		sprite.setOrigin(width/2f, height/2f);
	}

	public void setShapeSpec() {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 8);
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
