package com.kuan.tc.controls;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
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
	
	private WheelJointDef wjDef;
	private DistanceJointDef distDef;
	
	private World world;
	
	public TruckBuilder(World world, RamBody ramBdModel, RamTire ramTireModel, TrailorBody trailorBdModel, TrailorTire trailorTireModel, Hinge hingeModel){
		this.world = world;
		this.ramBdModel = ramBdModel;
		this.ramTireModel = ramTireModel;
		this.trailorBdModel = trailorBdModel;
		this.trailorTireModel = trailorTireModel;
		this.hingeModel = hingeModel;

		
		
		
	}
	
}
