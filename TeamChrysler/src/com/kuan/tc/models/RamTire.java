package com.kuan.tc.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class RamTire {
	private BodyDef bdDef;
	private FixtureDef fixDef;
	private Sprite sprite;

	private float width, height;
	private float rate = 100f;

	public RamTire() {
		InitDef();
		setSpriteSpec();
		setShapeSpec();
	}

	public void InitDef() {
		bdDef = new BodyDef();
		fixDef = new FixtureDef();

		bdDef.type = BodyType.DynamicBody;

		fixDef.density = 5.0f;
		fixDef.friction = 2.0f;
		fixDef.restitution = 0.1f;
	}

	public void setSpriteSpec() {
		TextureAtlas atlas = new TextureAtlas(
				Gdx.files.internal("data/ramtruck/ramtruck.pack"),
				Gdx.files.internal("data/ramtruck/"));
		sprite = new Sprite(atlas.findRegion("ramTire"));
		width = sprite.getWidth() / rate;
		height = sprite.getHeight() / rate;
		sprite.setSize(width, height);
		sprite.setOrigin(width/2f, height/2f);
	}

	public void setShapeSpec() {
		CircleShape shape = new CircleShape();
		shape.setRadius( (width+height)/4f );
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
