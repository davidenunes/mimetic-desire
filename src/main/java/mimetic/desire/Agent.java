package mimetic.desire;

import mimetic.desire.behaviour.Behaviour;
import mimetic.desire.behaviour.EvoBehaviour;
import sim.app.pso.Evaluatable;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public class Agent implements Steppable {
	private static final long serialVersionUID = 1L;

	public int index;
	MutableDouble2D bestPosition = new MutableDouble2D();
	MutableDouble2D position = new MutableDouble2D();
	MutableDouble2D velocity = new MutableDouble2D();
	private MimeticDesire model;
	private Evaluatable fitnessLandscape;

	// steps executed by the agent
	public int steps;

	// evolutionary behaviour
	EvoBehaviour behaviour;

	/**
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @param vx
	 *            velocity in x
	 * @param vy
	 *            velocity in y
	 * @param model
	 *            simulation model
	 * @param f
	 *            fitness landscape
	 * @param i
	 *            current agent index = or id
	 */
	public Agent(double x, double y, double vx, double vy, MimeticDesire model,
			Evaluatable f, int i) {
		this.position.setTo(x, y);
		this.velocity.setTo(vx, vy);

		this.model = model;
		this.fitnessLandscape = f;
		this.index = i;
		this.steps = 0;

		this.behaviour = new EvoBehaviour();
		behaviour.setup(this, model);

		model.space.setObjectLocation(this, new Double2D(position));
	}

	private void stepBehaviour(EvoBehaviour evoBehaviour, MimeticDesire model) {
		evoBehaviour.update();
	}

	@Override
	public void step(SimState state) {
		stepBehaviour(behaviour, (MimeticDesire) state);
		steps++;
	}

	// get a fitness based on the fitness landscape the agent is considering
	public double getFitness() {

		return fitnessLandscape.calcFitness(position.x, position.y);

	}

	public Behaviour getBehaviour() {
		return this.behaviour;
	}

	/**
	 * Returns a copy of the agent current position
	 */
	public Double2D getPosition() {
		return new Double2D(position);
	}

	public Double2D getVelocity() {
		return new Double2D(velocity);
	}

	// sets the agent current velocity to
	public void setVelocity(Double2D newVelocity) {
		this.velocity.setTo(newVelocity);
	}

	/**
	 * Sets the position of the current agent on the model 2DContinuous Space
	 * the model is responsible for the update of the agent position based on
	 * the velocity. This could be usefull to simulate bumbing into a wall for
	 * instance or toroidal worlds, handled only by the model which controlls
	 * the space where agents interact.
	 * 
	 * @param position
	 *            the current agent position
	 * @param velocity
	 *            the agent velocity to be applied to the position
	 */
	public void updatePosition() {
		Double2D newPosition = model.updatePosition(this, getPosition(),
				getVelocity());
		this.position.setTo(newPosition);
	}

}
