package scripts.farming.modules;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.concurrent.strategy.StrategyDaemon;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.bot.Bot;
import org.powerbot.game.bot.Context;

import scripts.farming.FarmingProject;
import scripts.farming.ScriptWrapper;
import scripts.state.Condition;
import scripts.state.Module;
import scripts.state.State;
import scripts.state.edge.Edge;
import scripts.state.edge.Either;
import scripts.state.edge.ExceptionSafeTask;
import scripts.state.edge.Option;
import scripts.state.edge.Task;
import scripts.state.tools.OptionSelector;

public class RunOtherScript extends Module {
	// List<Strategy> newStrategies;
	public Class<?> runningScript = null;
	public ActiveScript activeScript;

	public RunOtherScript(final FarmingProject main, State initial,
			State success, final State critical,
			OptionSelector<Class<?>> selector, final Condition run,
			final Condition interrupt) {
		super("Run other script", initial, success, critical);

		Set<Class<?>> scripts = main.loader.getScripts();

		Option<Class<?>> option = new Option<Class<?>>(run, selector);
		initial.add(option);
		for (final Class<?> script : scripts) {
			final State bankFirst = new State("SCRBF");
			final State prepared = new State("SCRPRP");
			final State cleaningUp = new State("SCRPCU");
			final State state = new State("SCRIPT");
			ScriptWrapper annotation = script
					.getAnnotation(ScriptWrapper.class);
			if (annotation.banking()) {
				option.add(script, bankFirst);
				main.banker.addSharedStates(bankFirst, prepared,
						Banker.Method.DEPOSIT, Banker.Method.IDLE);
				main.banker.addSharedStates(cleaningUp, success,
						Banker.Method.WITHDRAW, Banker.Method.IDLE);
			} else {
				option.add(script, prepared);
				cleaningUp.add(new Edge(Condition.TRUE, success));

			}

			final State interrupted = new State();

			prepared.add(new ExceptionSafeTask(Condition.TRUE, state, critical) {
				public void run() throws Exception {
					state.removeAllEdges();

					script.getDeclaredMethod("prepare").invoke(null);
					runningScript = script;
					activeScript = (ActiveScript) script.getDeclaredMethod(
							"getInstance").invoke(null);

					Field botField = Context.class.getDeclaredField("bot");
					botField.setAccessible(true);
					Bot bot = (Bot) botField.get(Context.get());
					Context newContext = new Context(bot);
					activeScript.init(newContext);

					Method setupMethod = activeScript.getClass()
							.getDeclaredMethod("setup");
					setupMethod.setAccessible(true);
					setupMethod.invoke(activeScript);

					Field executorField = ActiveScript.class
							.getDeclaredField("executor");
					executorField.setAccessible(true);
					StrategyDaemon sd = (StrategyDaemon) executorField
							.get(activeScript);
					Field strategiesField = StrategyDaemon.class
							.getDeclaredField("strategies");
					strategiesField.setAccessible(true);
					List<Strategy> strategies = (List<Strategy>) strategiesField
							.get(sd);

					// newStrategies = new ArrayList<Strategy>();

					state.add(new Edge(interrupt, interrupted));
					interrupted.add(new ExceptionSafeTask(Condition.TRUE,
							cleaningUp, critical) {
						public void run() throws Exception {
							// for (Strategy strategy : newStrategies) {
							// main.customRevoke(strategy);
							// }
							script.getDeclaredMethod("cleanup").invoke(null);
							activeScript = null;
						}
					});


					for (final Strategy strategy : strategies) {
						/*
						 * System.out.println(strategy.getClass().getName());
						 * Field policyField = Strategy.class
						 * .getDeclaredField("policy");
						 * policyField.setAccessible(true); final
						 * org.powerbot.concurrent.strategy.Condition condition
						 * = (org.powerbot.concurrent.strategy.Condition)
						 * policyField .get(strategy);
						 */
						Field tasksField = Strategy.class
								.getDeclaredField("tasks");
						tasksField.setAccessible(true);
						final org.powerbot.concurrent.Task[] tasks = (org.powerbot.concurrent.Task[]) tasksField
								.get(strategy);
						State strategyState = new State("SS");
						state.add(new Edge(new Condition() {
							public boolean validate() {
								return strategy.validate();
							}
						}, strategyState));
						strategyState.add(new ExceptionSafeTask(Condition.TRUE, state, interrupted) {
							public void run() {
								for (org.powerbot.concurrent.Task task : tasks) {
									System.out.print("<T");
									task.run();
									System.out.print("T>");
								}
							}
						});

						// main.customProvide(newStrategy = new Strategy(
						// (Condition)
						// run.and(interrupt.negate()).and(strategy), tasks));
						// newStrategies.add(newStrategy);
					}

				}
			});

		}
	}
}
