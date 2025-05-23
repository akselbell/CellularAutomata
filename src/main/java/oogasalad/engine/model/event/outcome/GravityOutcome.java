package oogasalad.engine.model.event.outcome;

import java.util.List;
import java.util.Map;
import oogasalad.engine.model.event.CollisionHandler;
import oogasalad.engine.model.object.GameObject;

/**
 * Outcome that applies gravity to the game object
 *
 * @author Gage Garcia
 */
public class GravityOutcome implements Outcome {

  private final CollisionHandler collisionHandler;

  /**
   * requires collision handler
   *
   * @param collisionHandler interface that gives access to collisions
   */
  public GravityOutcome(CollisionHandler collisionHandler) {
    this.collisionHandler = collisionHandler;
  }

  @Override
  public void execute(GameObject gameObject,
      Map<String, String> stringParameters,
      Map<String, Double> doubleParameters) {
    double dy = doubleParameters.getOrDefault("ApplyGravityAmount", 5.0);
    List<GameObject> collisions = collisionHandler.getCollisions(gameObject);
    if (collisions.isEmpty()) {
      gameObject.setGrounded(false);
    }
    // Only apply gravity if the object is in the air (falling or jumping)
    if (!gameObject.isGrounded()) {
      gameObject.setYVelocity(gameObject.getYVelocity() + dy);
    }
  }

}
