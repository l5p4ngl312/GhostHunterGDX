/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 * @source created with LibGDX
 */

package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import edu.virginia.ghosthuntergdx.entities.Player;
import edu.virginia.ghosthuntergdx.items.Item;
import edu.virginia.ghosthuntergdx.screens.SPGame;

public class CollisionListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();

		if (a.getUserData() != null && !SPGame.bodiesToDestroy.contains(a)) {
			if (a.getUserData() instanceof Collider) {
				((Collider) a.getUserData()).OnCollisionBegin(b, contact);
			}
		}

		if (b.getUserData() != null && !SPGame.bodiesToDestroy.contains(b)) {
			if (b.getUserData() instanceof Collider) {
				((Collider) b.getUserData()).OnCollisionBegin(a, contact);
			}
		}

		pickUpCollision(a, b);
	}

	private void pickUpCollision(Body a, Body b) {

		if (a.getUserData() == null || b.getUserData() == null)
			return;

		Item i = null;
		Player p = null;

		if (a.getUserData() instanceof Player
				&& b.getUserData() instanceof Item) {
			p = (Player) a.getUserData();
			i = (Item) b.getUserData();
		}
		if (b.getUserData() instanceof Player
				&& a.getUserData() instanceof Item) {
			p = (Player) b.getUserData();
			i = (Item) a.getUserData();
		}
		if (i != null && p != null) {
			if (i.dropDisabled > i.dropDisableTime) {
				i.OnPickedUp(p);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();

		if (a.getUserData() != null && !SPGame.bodiesToDestroy.contains(a)) {
			if (a.getUserData() instanceof Collider) {
				((Collider) a.getUserData()).OnCollisionEnd(b, contact);
			}
		}

		if (b.getUserData() != null && !SPGame.bodiesToDestroy.contains(b)) {
			if (b.getUserData() instanceof Collider) {
				((Collider) b.getUserData()).OnCollisionEnd(a, contact);
			}
		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
}
