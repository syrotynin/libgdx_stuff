package codelabs.box2d.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codelabs.box2d.BouncingBallSample;
import codelabs.box2d.BuoyancySample;
import codelabs.box2d.DragAndDropSample;
import codelabs.box2d.GravityAccelerometerSample;
import codelabs.box2d.ImpulsesSample;
import codelabs.box2d.CollisionsSample;
import codelabs.box2d.JumpingSample;
import codelabs.box2d.SpawnBodiesSample;
import codelabs.box2d.SpritesSample;
import codelabs.utils.Sample;

public class Box2DSamples {

	@SuppressWarnings("unchecked")
	public static final List<Class<? extends Sample>> SAMPLES = new ArrayList<Class<? extends Sample>>(
			Arrays.asList(
					BouncingBallSample.class,
					SpawnBodiesSample.class,
					DragAndDropSample.class,
					ImpulsesSample.class,
					SpritesSample.class,
					GravityAccelerometerSample.class,
					CollisionsSample.class,
					BuoyancySample.class,
					JumpingSample.class
			)
	);

}
