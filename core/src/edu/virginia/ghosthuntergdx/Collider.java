package edu.virginia.ghosthuntergdx;
import com.badlogic.gdx.physics.box2d.Body;

public interface Collider {

	abstract void OnCollisionBegin(Body other);
	abstract void OnCollisionEnd(Body other);
}
