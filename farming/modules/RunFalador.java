package scripts.farming.modules;

import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

import state.Condition;
import state.Module;
import state.State;
import state.Value;
import state.edge.AnimationPath;
import state.edge.InteractItem;
import state.edge.InteractSceneObject;
import state.edge.Timeout;
import state.edge.Walk;

public class RunFalador extends Module {

	public String toString() {
		return "Cabbage port";
	}

	public RunFalador(State INITIAL, State SUCCESS, State CRITICAL) {
		super(INITIAL, SUCCESS, CRITICAL);
		State TELEPORTED = new State();
		State TELEPORTING = new State();
		State IN_FRONT_OF_GATE = new State();
		INITIAL.add(new InteractItem(Condition.TRUE, TELEPORTING, 19760,
				"Cabbage-port"));
		TELEPORTING.add(new AnimationPath(Condition.TRUE, new Integer[] { 9984,
				9986 }, TELEPORTED, new Timeout(INITIAL, 6000)));
		TELEPORTED.add(new Walk(Condition.TRUE, new Tile(3052, 3299, 0),
				IN_FRONT_OF_GATE, new Timeout(INITIAL, 5000)));
		IN_FRONT_OF_GATE.add(new InteractSceneObject(Condition.TRUE,
				IN_FRONT_OF_GATE, new Value<SceneObject>() {
					public SceneObject get() {
						return SceneEntities.getNearest(7049);
					}
				}, "Open"));
		IN_FRONT_OF_GATE.add(new Walk(Condition.TRUE, new Tile(3056, 3307, 0),
				SUCCESS, new Timeout(INITIAL, 5000)));
	}
}