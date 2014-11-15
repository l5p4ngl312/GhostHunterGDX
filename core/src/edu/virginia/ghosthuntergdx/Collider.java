package edu.virginia.ghosthuntergdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

public interface Collider {

	abstract void OnCollisionBegin(Body other,Contact c);
	abstract void OnCollisionEnd(Body other,Contact c);
}
