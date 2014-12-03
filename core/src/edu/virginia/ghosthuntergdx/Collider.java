/**
 * @author Anthony Batres (alb3ee), Alexander Mazza (am7kg), David Rubin (dar3ey), Lane Spangler (las4vc)
 * @group T103-06
 * @source created with LibGDX
 */

package edu.virginia.ghosthuntergdx;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

public interface Collider {

	abstract void OnCollisionBegin(Body other, Contact c);

	abstract void OnCollisionEnd(Body other, Contact c);
}
