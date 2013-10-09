package com.kuan.tc.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.kuan.tc.models.Hinge;
import com.kuan.tc.models.RamBody;
import com.kuan.tc.models.RamTire;
import com.kuan.tc.models.TrailorBody;
import com.kuan.tc.models.TrailorTire;

public class TruckBuilder {
	private Hinge hingeModel;
	private RamBody ramBdModel;
	private RamTire ramTireModel;
	private TrailorBody trailorBdModel;
	private TrailorTire trailorTireModel;

	private WeldJoint tailor_hinge_Jnt;
	private RevoluteJoint trailor_tire_Jnt, ram_tireL_Jnt, ram_tireR_Jnt,
			ram_hinge_Jnt;
	private Body hinge, ramBd, ramTire_l, ramTire_r, trailorBd, trailorTire;

	// private WheelJointDef wjDef;
	private WeldJointDef wldDef;
	private RevoluteJointDef rvDef;

	private World world;

	private float torque =0f;
	private float speed =-20f;
	
	
	public TruckBuilder(Vector2 startPt, World world, RamBody ramBdModel,
			RamTire ramTireModel, TrailorBody trailorBdModel,
			TrailorTire trailorTireModel, Hinge hingeModel) {

		float relX, relY;
		float cX, cY;
		relX = startPt.x;
		relY = startPt.y;

		this.world = world;
		this.ramBdModel = ramBdModel;
		this.ramTireModel = ramTireModel;
		this.trailorBdModel = trailorBdModel;
		this.trailorTireModel = trailorTireModel;
		this.hingeModel = hingeModel;

		cX = trailorBdModel.getSprite().getWidth() / 2 + relX;
		cY = trailorBdModel.getSprite().getHeight() / 2
				+ trailorTireModel.getSprite().getHeight() / 2 + relY;
		trailorBdModel.getBdDef().position.set(cX, cY);
		trailorBd = world.createBody(trailorBdModel.getBdDef());
		trailorBd.createFixture(trailorBdModel.getFixDef());

		cX = ramBdModel.getSprite().getWidth() / 2
				+ trailorBdModel.getSprite().getWidth()
				+ hingeModel.getSprite().getWidth() + relX;
		cY = ramBdModel.getSprite().getHeight() / 2
				+ ramTireModel.getSprite().getHeight() / 2 + relY;
		ramBdModel.getBdDef().position.set(cX, cY);
		ramBd = world.createBody(ramBdModel.getBdDef());
		ramBd.createFixture(ramBdModel.getFixDef());

		cX = hingeModel.getSprite().getWidth() / 2
				+ trailorBdModel.getSprite().getWidth() + relX;
		// we do not consider the thickness of the hinge
		cY = trailorTireModel.getSprite().getHeight() / 2 + relY;
		hingeModel.getBdDef().position.set(cX, cY);
		hinge = world.createBody(hingeModel.getBdDef());
		hinge.createFixture(hingeModel.getFixDef());

		cX = trailorBdModel.getSprite().getWidth() * 0.345f + relX;
		cY = trailorTireModel.getSprite().getHeight() / 2 + relY;
		trailorTireModel.getBdDef().position.set(cX, cY);
		trailorTire = world.createBody(trailorTireModel.getBdDef());
		trailorTire.createFixture(trailorTireModel.getFixDef());

		cX = ramBdModel.getSprite().getWidth() * 0.2287f
				+ trailorBdModel.getSprite().getWidth()
				+ hingeModel.getSprite().getWidth() + relX;
		cY = ramTireModel.getSprite().getHeight() / 2 + relY;
		ramTireModel.getBdDef().position.set(cX, cY);
		ramTire_l = world.createBody(ramTireModel.getBdDef());
		ramTire_l.createFixture(ramTireModel.getFixDef());

		cX = ramBdModel.getSprite().getWidth() * 0.83f
				+ trailorBdModel.getSprite().getWidth()
				+ hingeModel.getSprite().getWidth() + relX;
		cY = ramTireModel.getSprite().getHeight() / 2 + relY;
		ramTireModel.getBdDef().position.set(cX, cY);
		ramTire_r = world.createBody(ramTireModel.getBdDef());
		ramTire_r.createFixture(ramTireModel.getFixDef());
		
		
	}

	public void joinParts(WeldJointDef wldDef, RevoluteJointDef rvDef) {

		rvDef.bodyA = trailorBd;
		rvDef.bodyB = trailorTire;
		rvDef.localAnchorA.set(trailorBdModel.getSprite().getWidth() * 0.345f
				- trailorBdModel.getSprite().getWidth() / 2f, -trailorBdModel
				.getSprite().getHeight() / 2);
		rvDef.localAnchorB.set(0, 0);
		rvDef.enableMotor = false;
		rvDef.maxMotorTorque = 0.5f;
		rvDef.motorSpeed = 1f;
		trailor_tire_Jnt = (RevoluteJoint) world.createJoint(rvDef);

		wldDef.bodyA = trailorBd;
		wldDef.bodyB = hinge;
		wldDef.localAnchorA.set(trailorBdModel.getSprite().getWidth() / 2f,
				-trailorBdModel.getSprite().getHeight() / 2);
		wldDef.localAnchorB.set(-hingeModel.getSprite().getWidth() / 2, 0);
		tailor_hinge_Jnt = (WeldJoint) world.createJoint(wldDef);

		rvDef.bodyA = ramBd;
		rvDef.bodyB = ramTire_l;
		rvDef.localAnchorA.set(ramBdModel.getSprite().getWidth() * 0.2287f
				- ramBdModel.getSprite().getWidth() / 2f, -ramBdModel
				.getSprite().getHeight() / 2);
		rvDef.localAnchorB.set(0, 0);
		
		
		
		rvDef.enableMotor = false;
		rvDef.maxMotorTorque = 0.5f;
		rvDef.motorSpeed = 1f;
		ram_tireL_Jnt = (RevoluteJoint) world.createJoint(rvDef);

		// rvDef.bodyA = ramBd;
		rvDef.bodyB = ramTire_r;
		rvDef.localAnchorA.set(ramBdModel.getSprite().getWidth() * 0.33f,
				-ramBdModel.getSprite().getHeight() / 2);
		rvDef.localAnchorB.set(0, 0);
		rvDef.enableMotor = false;
		rvDef.maxMotorTorque = 0.5f;
		rvDef.motorSpeed = 1f;
		ram_tireR_Jnt = (RevoluteJoint) world.createJoint(rvDef);

		rvDef.bodyB = hinge;
		rvDef.localAnchorA.set(-ramBdModel.getSprite().getWidth() / 2f,
				-ramBdModel.getSprite().getHeight() / 2);
		rvDef.localAnchorB.set(hingeModel.getSprite().getWidth() / 2, 0);
		rvDef.enableMotor = false;
		rvDef.maxMotorTorque = 0f;
		rvDef.motorSpeed = 0f;
		ram_hinge_Jnt = (RevoluteJoint) world.createJoint(rvDef);
		
		
		
		ram_tireL_Jnt.enableMotor(true);
		ram_tireL_Jnt.setMotorSpeed(speed);
		ram_tireL_Jnt.setMaxMotorTorque(torque);
	}

	public void drawTruck(SpriteBatch batch) {
		trailorBdModel.getSprite().setPosition(
				trailorBd.getPosition().x- trailorBdModel.getSprite().getWidth() / 2f,
				trailorBd.getPosition().y- trailorBdModel.getSprite().getHeight() / 2f);
		trailorBdModel.getSprite().setRotation(
				trailorBd.getAngle() * MathUtils.radiansToDegrees);
		trailorBdModel.getSprite().draw(batch);

		
		ramBdModel.getSprite().setPosition(
				ramBd.getPosition().x- ramBdModel.getSprite().getWidth() / 2f,
				ramBd.getPosition().y- ramBdModel.getSprite().getHeight() / 2f);
		ramBdModel.getSprite().setRotation(
				ramBd.getAngle() * MathUtils.radiansToDegrees);
		ramBdModel.getSprite().draw(batch);

		
		hingeModel.getSprite().setPosition(
				hinge.getPosition().x- hingeModel.getSprite().getWidth() / 2f,
				hinge.getPosition().y);
		hingeModel.getSprite().setRotation(
				hinge.getAngle() * MathUtils.radiansToDegrees);
		hingeModel.getSprite().draw(batch);

		
		trailorTireModel.getSprite().setPosition(
				trailorTire.getPosition().x- trailorTireModel.getSprite().getWidth() / 2f,
				trailorTire.getPosition().y- trailorTireModel.getSprite().getHeight() / 2f);
		trailorTireModel.getSprite().setRotation(
				trailorTire.getAngle() * MathUtils.radiansToDegrees);
		trailorTireModel.getSprite().draw(batch);

		
		ramTireModel.getSprite().setPosition(
				ramTire_l.getPosition().x- ramTireModel.getSprite().getWidth() / 2f,
				ramTire_l.getPosition().y- ramTireModel.getSprite().getHeight() / 2f);
		ramTireModel.getSprite().setRotation(
				ramTire_l.getAngle() * MathUtils.radiansToDegrees);
		ramTireModel.getSprite().draw(batch);

		ramTireModel.getSprite().setPosition(
				ramTire_r.getPosition().x- ramTireModel.getSprite().getWidth() / 2f,
				ramTire_r.getPosition().y- ramTireModel.getSprite().getHeight() / 2f);
		ramTireModel.getSprite().setRotation(
				ramTire_r.getAngle() * MathUtils.radiansToDegrees);
		ramTireModel.getSprite().draw(batch);
	}

	public Hinge getHingeModel() {
		return hingeModel;
	}

	public RamBody getRamBdModel() {
		return ramBdModel;
	}

	public RamTire getRamTireModel() {
		return ramTireModel;
	}

	public TrailorBody getTrailorBdModel() {
		return trailorBdModel;
	}

	public TrailorTire getTrailorTireModel() {
		return trailorTireModel;
	}

	public Body getHinge() {
		return hinge;
	}

	public Body getRamBd() {
		return ramBd;
	}

	public Body getRamTire_l() {
		return ramTire_l;
	}

	public Body getRamTire_r() {
		return ramTire_r;
	}

	public Body getTrailorBd() {
		return trailorBd;
	}

	public Body getTrailorTire() {
		return trailorTire;
	}

	public World getWorld() {
		return world;
	}

	public void setHingeModel(Hinge hingeModel) {
		this.hingeModel = hingeModel;
	}

	public void setRamBdModel(RamBody ramBdModel) {
		this.ramBdModel = ramBdModel;
	}

	public void setRamTireModel(RamTire ramTireModel) {
		this.ramTireModel = ramTireModel;
	}

	public void setTrailorBdModel(TrailorBody trailorBdModel) {
		this.trailorBdModel = trailorBdModel;
	}

	public void setTrailorTireModel(TrailorTire trailorTireModel) {
		this.trailorTireModel = trailorTireModel;
	}

	public void setHinge(Body hinge) {
		this.hinge = hinge;
	}

	public void setRamBd(Body ramBd) {
		this.ramBd = ramBd;
	}

	public void setRamTire_l(Body ramTire_l) {
		this.ramTire_l = ramTire_l;
	}

	public void setRamTire_r(Body ramTire_r) {
		this.ramTire_r = ramTire_r;
	}

	public void setTrailorBd(Body trailorBd) {
		this.trailorBd = trailorBd;
	}

	public void setTrailorTire(Body trailorTire) {
		this.trailorTire = trailorTire;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public WeldJoint getTailor_hinge_Jnt() {
		return tailor_hinge_Jnt;
	}

	public RevoluteJoint getTrailor_tire_Jnt() {
		return trailor_tire_Jnt;
	}

	public RevoluteJoint getRam_tireL_Jnt() {
		return ram_tireL_Jnt;
	}

	public RevoluteJoint getRam_tireR_Jnt() {
		return ram_tireR_Jnt;
	}

	public RevoluteJoint getRam_hinge_Jnt() {
		return ram_hinge_Jnt;
	}

	public WeldJointDef getWldDef() {
		return wldDef;
	}

	public RevoluteJointDef getRvDef() {
		return rvDef;
	}

	public void setTailor_hinge_Jnt(WeldJoint tailor_hinge_Jnt) {
		this.tailor_hinge_Jnt = tailor_hinge_Jnt;
	}

	public void setTrailor_tire_Jnt(RevoluteJoint trailor_tire_Jnt) {
		this.trailor_tire_Jnt = trailor_tire_Jnt;
	}

	public void setRam_tireL_Jnt(RevoluteJoint ram_tireL_Jnt) {
		this.ram_tireL_Jnt = ram_tireL_Jnt;
	}

	public void setRam_tireR_Jnt(RevoluteJoint ram_tireR_Jnt) {
		this.ram_tireR_Jnt = ram_tireR_Jnt;
	}

	public void setRam_hinge_Jnt(RevoluteJoint ram_hinge_Jnt) {
		this.ram_hinge_Jnt = ram_hinge_Jnt;
	}

	public void setWldDef(WeldJointDef wldDef) {
		this.wldDef = wldDef;
	}

	public void setRvDef(RevoluteJointDef rvDef) {
		this.rvDef = rvDef;
	}

	public float getTorque() {
		return torque;
	}

	public void setTorque(float torque) {
		this.torque = torque;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
