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

public class Physics {

	public static Body createBoxBody( final BodyType pBodyType, final FixtureDef pFixtureDef, Sprite pSprite ) {

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
	    boxPoly.setAsBox( halfWidth, halfHeight );  // set the anchor point to be the center of the sprite

	    pFixtureDef.shape = boxPoly;        

	    final Body boxBody = SPGame.getPhysicsWorld().createBody(boxBodyDef);
	    boxBody.createFixture(pFixtureDef);
	    
	    return boxBody;
	}
	
	public static Body createCircleBody( final BodyType pBodyType, final FixtureDef pFixtureDef, Sprite pSprite ) {

	    float pRotation = 0;
	    float pWidth = pSprite.getWidth();
	    float pHeight = pSprite.getHeight();

	    final BodyDef circleBodyDef = new BodyDef();
	    circleBodyDef.type = pBodyType;

	    circleBodyDef.position.x = pSprite.getX() / Consts.BOX_TO_WORLD;
	    circleBodyDef.position.y = pSprite.getY() / Consts.BOX_TO_WORLD;

	    final CircleShape shape = new CircleShape();
	    shape.setRadius(pWidth/2/Consts.BOX_TO_WORLD);

	    pFixtureDef.shape = shape;        

	    final Body circleBody = SPGame.getPhysicsWorld().createBody(circleBodyDef);
	    circleBody.createFixture(pFixtureDef);
	    
	    return circleBody;
	}
	
	   // The pixels per tile. If your tiles are 16x16, this is set to 16f
 private static float ppt = 128f;

 public static Array<Body> buildShapes(Map map,World world,String layer) {
     MapObjects objects = map.getLayers().get(layer).getObjects();

     Array<Body> bodies = new Array<Body>();

     for(MapObject object : objects) {

         if (object instanceof TextureMapObject) {
             continue;
         }

         Shape shape;
         Gdx.app.debug("SHAPE", "RECTANGLE");
         if (object instanceof RectangleMapObject) {
             shape = getRectangle((RectangleMapObject)object);
         }
         else if (object instanceof PolygonMapObject) {
             shape = getPolygon((PolygonMapObject)object);
         }
         else if (object instanceof PolylineMapObject) {
             shape = getPolyline((PolylineMapObject)object);
         }
         else if (object instanceof CircleMapObject) {
             shape = getCircle((CircleMapObject)object);
         }
         else {
             continue;
         }

         BodyDef bd = new BodyDef();
         bd.type = BodyType.StaticBody;
         Body body = world.createBody(bd);
         body.createFixture(shape, 1);

         bodies.add(body);

         shape.dispose();
     }
     return bodies;
 }

 private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
     Rectangle rectangle = rectangleObject.getRectangle();
     PolygonShape polygon = new PolygonShape();
     Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / ppt,
                                (rectangle.y + rectangle.height * 0.5f ) / ppt);
     
     polygon.setAsBox(rectangle.width * 0.5f / ppt,
                      rectangle.height * 0.5f / ppt,
                      size,
                      0.0f);
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
     float[] vertices = polylineObject.getPolyline().getTransformedVertices();
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
