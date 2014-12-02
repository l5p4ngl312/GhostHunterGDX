/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 */

package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import edu.virginia.ghosthuntergdx.assets.Consts;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class Physics {

	public final static short OBSTACLE = 1; // 1 - everything without a mask
	public final static short PLAYER = 1 << 1;
	public final static short PICKUP = 1 << 3;
	public final static short LIGHT = 1 << 4;
	public final static short SENSOR = 1 << 5;
	public final static short ENEMYATTACK = 1 << 6;
	public final static short ARTIFACT = 1 << 7;
	public final static short ENEMYBODY = 1 << 8;
	public final static short GHOST = 1 << 9;

	public final static short LIGHT_GROUP = 1;
	public final static short NO_GROUP = 0;
	// masks
	public final static short MASK_LIGHTS = OBSTACLE | PICKUP | ENEMYBODY;
	public final static short MASK_PLAYER = OBSTACLE | PICKUP | SENSOR | PLAYER
			| ENEMYBODY | ENEMYATTACK | ARTIFACT;
	public final static short MASK_PICKUP = OBSTACLE;
	public final static short MASK_SENSOR = PLAYER | ENEMYBODY | OBSTACLE
			| ARTIFACT;
	public final static short MASK_ENEMYATTACK = PLAYER;
	public final static short MASK_ENEMYBODY = LIGHT | PLAYER | SENSOR
			| OBSTACLE | ENEMYBODY | ARTIFACT;
	public final static short MASK_GHOST = OBSTACLE | SENSOR | GHOST;
	public final static short MASK_ARTIFACT = PLAYER | SENSOR | ENEMYBODY;

	// Creates a box physics body based on a sprite
	public static Body createBoxBody(final BodyType pBodyType,
			final FixtureDef pFixtureDef, Sprite pSprite, boolean bullet) {

		float pRotation = 0;
		float pWidth = pSprite.getWidth();
		float pHeight = pSprite.getHeight();

		final BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = pBodyType;

		boxBodyDef.position.x = pSprite.getX() / Consts.BOX_TO_WORLD;
		boxBodyDef.position.y = pSprite.getY() / Consts.BOX_TO_WORLD;

		// Temporary Box shape of the Body
		final PolygonShape boxPoly = new PolygonShape();
		final float halfWidth = pWidth * 0.5f / Consts.BOX_TO_WORLD;
		final float halfHeight = pHeight * 0.5f / Consts.BOX_TO_WORLD;
		boxPoly.setAsBox(halfWidth, halfHeight); // set the anchor point to be
													// the center of the sprite

		pFixtureDef.shape = boxPoly;
		boxBodyDef.bullet = bullet;
		final Body boxBody = SPGame.getPhysicsWorld().createBody(boxBodyDef);
		boxBody.createFixture(pFixtureDef);

		return boxBody;
	}

	// Creates a circular physics body based on a sprite
	public static Body createCircleBody(final BodyType pBodyType,
			final FixtureDef pFixtureDef, Sprite pSprite, boolean fixedRot) {

		float pRotation = 0;
		float pWidth = pSprite.getWidth() / Consts.BOX_TO_WORLD;
		float pHeight = pSprite.getHeight() / Consts.BOX_TO_WORLD;

		final BodyDef circleBodyDef = new BodyDef();
		circleBodyDef.type = pBodyType;
		circleBodyDef.fixedRotation = fixedRot;

		circleBodyDef.position.x = pSprite.getX() / Consts.BOX_TO_WORLD;
		circleBodyDef.position.y = pSprite.getY() / Consts.BOX_TO_WORLD;

		final CircleShape shape = new CircleShape();
		shape.setRadius(pWidth / 2);

		pFixtureDef.shape = shape;
		pFixtureDef.density = 1;
		final Body circleBody = SPGame.getPhysicsWorld().createBody(
				circleBodyDef);
		circleBody.createFixture(pFixtureDef);

		return circleBody;
	}

	public static Body createCircleBody(final BodyType pBodyType,
			final FixtureDef pFixtureDef, final FixtureDef sensorDef,
			Sprite pSprite, boolean fixedRot) {

		float pRotation = 0;
		float pWidth = pSprite.getWidth() / Consts.BOX_TO_WORLD;
		float pHeight = pSprite.getHeight() / Consts.BOX_TO_WORLD;

		final BodyDef circleBodyDef = new BodyDef();
		circleBodyDef.type = pBodyType;
		circleBodyDef.fixedRotation = fixedRot;

		circleBodyDef.position.x = pSprite.getX() / Consts.BOX_TO_WORLD;
		circleBodyDef.position.y = pSprite.getY() / Consts.BOX_TO_WORLD;

		CircleShape shape = new CircleShape();
		CircleShape shape2 = new CircleShape();
		shape2.setRadius(pWidth / 4);
		pFixtureDef.shape = shape2;
		pFixtureDef.density = 1;
		shape.setRadius(pWidth / 2);
		sensorDef.shape = shape;
		sensorDef.density = 1;
		final Body circleBody = SPGame.getPhysicsWorld().createBody(
				circleBodyDef);
		circleBody.createFixture(pFixtureDef);
		circleBody.createFixture(sensorDef);

		return circleBody;
	}

	private static float ppt = Consts.BOX_TO_WORLD;

	// Builds bodies from a map
	public static Array<Body> buildShapes(Map map, World world, String layer) {
		MapObjects objects = map.getLayers().get(layer).getObjects();

		Array<Body> bodies = new Array<Body>();

		for (MapObject object : objects) {

			if (object instanceof TextureMapObject) {
				continue;
			}

			Shape shape;
			Gdx.app.debug("SHAPE", "RECTANGLE");
			if (object instanceof RectangleMapObject) {
				shape = getRectangle((RectangleMapObject) object);
			} else if (object instanceof PolygonMapObject) {
				shape = getPolygon((PolygonMapObject) object);
			} else if (object instanceof PolylineMapObject) {
				shape = getPolyline((PolylineMapObject) object);
			} else if (object instanceof CircleMapObject) {
				shape = getCircle((CircleMapObject) object);
			} else {
				continue;
			}

			BodyDef bd = new BodyDef();
			bd.type = BodyType.StaticBody;
			Body body = world.createBody(bd);
			FixtureDef fDef = new FixtureDef();
			fDef.shape = shape;
			fDef.density = 1;
			fDef.filter.categoryBits = OBSTACLE;
			fDef.filter.groupIndex = 0;
			fDef.filter.maskBits = LIGHT | PLAYER | OBSTACLE | PICKUP
					| ENEMYBODY | SENSOR | GHOST;
			body.createFixture(fDef);

			bodies.add(body);

			shape.dispose();
		}
		return bodies;
	}

	public static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
		Rectangle rectangle = rectangleObject.getRectangle();
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2(
				(rectangle.x + rectangle.width * 0.5f) / ppt,
				(rectangle.y + rectangle.height * 0.5f) / ppt);

		polygon.setAsBox(rectangle.width * 0.5f / ppt, rectangle.height * 0.5f
				/ ppt, size, 0.0f);
		return polygon;
	}

	private static CircleShape getCircle(CircleMapObject circleObject) {
		Circle circle = circleObject.getCircle();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(circle.radius / ppt);
		circleShape.setPosition(new Vector2(circle.x / ppt, circle.y / ppt));
		return circleShape;
	}

	private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
		PolygonShape polygon = new PolygonShape();
		float[] vertices = polygonObject.getPolygon().getTransformedVertices();

		float[] worldVertices = new float[vertices.length];

		for (int i = 0; i < vertices.length; ++i) {
			System.out.println(vertices[i]);
			worldVertices[i] = vertices[i] / ppt;
		}

		polygon.set(worldVertices);
		return polygon;
	}

	private static ChainShape getPolyline(PolylineMapObject polylineObject) {
		float[] vertices = polylineObject.getPolyline()
				.getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];

		for (int i = 0; i < vertices.length / 2; ++i) {
			worldVertices[i] = new Vector2();
			worldVertices[i].x = vertices[i * 2] / ppt;
			worldVertices[i].y = vertices[i * 2 + 1] / ppt;
		}

		ChainShape chain = new ChainShape();
		chain.createChain(worldVertices);
		return chain;
	}
}
