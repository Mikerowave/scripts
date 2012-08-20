package scripts.farming;

import java.util.HashMap;
import java.util.Map;

import org.powerbot.concurrent.strategy.Condition;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;

public class ProductXYZ {
/*
	int id;

	public int getId() {
		return id;
	}

	String name;

	public String getName() {
		return name;
	}

	ProcessOption[] processOptions;

	public ProcessOption[] getProcessOptions() {
		return processOptions;
	}

	public ProcessOption selectedProcessOption;
	public static Map<String, Integer> notedProducts = new HashMap<String, Integer>();

	public ProductXYZ(int id_, String name_) {
		this(id_, name_, Drop);
	}

	public ProductXYZ(int id_, String name_, ProcessOption... options) {
		id = id_;
		name = name_;
		processOptions = options;
		selectedProcessOption = processOptions[processOptions.length - 1];
	}

	public static ProcessOption DontProcess = new ProcessOption() {
		public String toString() {
			return "Don't process";
		}

		public void run(ProductXYZ p) {
		}
	};

	public static ProcessOption Drop = new ProcessOption() {
		public String toString() {
			return "Drop";
		}

		public void run(ProductXYZ p) {
			Item item;
			while ((item = Inventory.getItem(p.id)) != null) {
				int count = Inventory.getCount(p.id);
				item.getWidgetChild().interact("Drop");
				while (count == Inventory.getCount(p.id))
					Time.sleep(10);
				Time.sleep(100);
			}
		}
	};

	public static ProcessOption GiveToLeprechaun = new ProcessOption() {
		public String toString() {
			return "Give to leprechaun for notes";
		}

		public void run(final ProductXYZ p) {
			Camera.setPitch(89);
			final Item item = Inventory.getItem(p.id);
			if (item == null)
				return;
			System.out.println(p.getName());
			final int amount = Inventory.getCount(p.id);

			Mouse.click(item.getWidgetChild().getCentralPoint(), false);
			Menu.select("Use");
			// 3021, 5808, 4965
			NPC leprechaun = NPCs.getNearest(7569, 3021, 5808, 7557, 4965);
			if (leprechaun == null) {
				Camera.setAngle((540 - Camera.getYaw()) % 360);
				leprechaun = NPCs.getNearest(7569, 3021, 5808, 7557, 4965);
			}
			if (leprechaun != null) {
				Camera.turnTo(leprechaun);
				leprechaun.interact("Use");
				Time.sleep(700);
				while (Players.getLocal().isMoving())
					Time.sleep(10);
				Time.sleep(1300);
			}
			if (Inventory.getCount(p.id) == 0) {
				if (notedProducts.containsKey(p.getName()))
					notedProducts.put(item.getName(),
							notedProducts.get(p.getName()) + amount);
				else
					notedProducts.put(p.getName(), amount);
			}

			while (Widgets.get(1189, 10).isOnScreen()) {
				System.out.println("Click!");
				Widgets.get(1189, 10).click(true);
				Time.sleep(200);
			}
		}
	};

	public static class Interact implements ProcessOption {
		String interaction;

		public Interact(String interaction_) {
			interaction = interaction_;
		}

		public String toString() {
			return interaction;
		}

		public void run(ProductXYZ p) {
			Item item;
			while ((item = Inventory.getItem(p.id)) != null) {
				item.getWidgetChild().interact(interaction);
				Time.sleep(250);
			}
		}
	}

	public static void waitFor(Condition c) {
		while (!c.validate())
			Time.sleep(1);
	}

	public static ProcessOption Clean = new ProcessOption() {
		public String toString() {
			return "Clean the herb";
		}

		public void run(ProductXYZ p) {
			for (Item item : Inventory.getItems()) {
				if (item.getId() == p.id) {
					item.getWidgetChild().interact("Clean");
					Time.sleep(200);
				}
			}
		}
	};

	public static ProcessOption Burn = new ProcessOption() {
		public String toString() {
			return "Burn the logs";
		}

		public void run(ProductXYZ p) {
			
			 //Item item; while((item = Inventory.getItem(p.id)) != null) {
			 // item.getWidgetChild().interact("Clean"); }
			 
		}
	};

	private static HashMap<Integer, ProductXYZ> loadProducts() {
		HashMap<Integer, ProductXYZ> products = new HashMap<Integer, ProductXYZ>();
		ProductXYZ[] productsPlain = {
				new ProductXYZ(24154, "Spin ticket", new Interact("Claim spin")),
				new ProductXYZ(24155, "Double spin ticket", new Interact(
						"Claim spin")),
				new ProductXYZ(14664, "Mini-event gift", Drop),
				new ProductXYZ(6055, "Weed", Drop),
				new ProductXYZ(229, "Empty vial", Drop),
				new ProductXYZ(199, "Grimy Guam", Drop, GiveToLeprechaun, Clean),
				new ProductXYZ(249, "Clean Guam", Drop, GiveToLeprechaun),
				new ProductXYZ(201, "Grimy Marrentil", Drop, GiveToLeprechaun,item
				new ProductXYZ(251, "Clean Marrentil", Drop, GiveToLeprechaun),
				new ProductXYZ(203, "Grimy Tarromin", Drop, GiveToLeprechaun,
						Clean),
				new ProductXYZ(253, "Clean Tarromin", Drop, GiveToLeprechaun),
				new ProductXYZ(205, "Grimy Harralander", Drop, GiveToLeprechaun,
						Clean),
				new ProductXYZ(255, "Clean Harralander", Drop, GiveToLeprechaun),
				new ProductXYZ(207, "Grimy Ranarr", Drop, GiveToLeprechaun, Clean),
				new ProductXYZ(257, "Clean Ranarr", Drop, GiveToLeprechaun),
				new ProductXYZ(2998, "Grimy Toadflax", Drop, GiveToLeprechaun,
						Clean),
				new ProductXYZ(2999, "Clean Toadflax", Drop, GiveToLeprechaun),
				new ProductXYZ(209, "Grimy Irit", Drop, GiveToLeprechaun, Clean),
				new ProductXYZ(259, "Clean Irit", Drop, GiveToLeprechaun),
				new ProductXYZ(14836, "Grimy Wergali", Drop, GiveToLeprechaun,
						Clean),
				new ProductXYZ(14854, "Clean Wergali", Drop, GiveToLeprechaun),
				new ProductXYZ(211, "Grimy Avantoe", Drop, GiveToLeprechaun, Clean),
				new ProductXYZ(261, "Clean Avantoe", Drop, GiveToLeprechaun),
				new ProductXYZ(213, "Grimy Kwuarm", Drop, GiveToLeprechaun, Clean),
				new ProductXYZ(263, "Clean Kwuarm", Drop, GiveToLeprechaun),
				new ProductXYZ(3051, "Grimy Snapdragon", Drop, GiveToLeprechaun,
						Clean),
				new ProductXYZ(3000, "Clean Snapdragon", Drop, GiveToLeprechaun),
				new ProductXYZ(215, "Grimy Cadantine", Drop, GiveToLeprechaun,
						Clean),
				new ProductXYZ(265, "Clean Cadantine", Drop, GiveToLeprechaun),
				new ProductXYZ(2485, "Grimy Lantadyme", Drop, GiveToLeprechaun,
						Clean),
				new ProductXYZ(2481, "Clean Lantadyme", Drop, GiveToLeprechaun),
				new ProductXYZ(217, "Grimy Dwarf weed", Drop, GiveToLeprechaun,
						Clean),
				new ProductXYZ(267, "Clean Dwarf weed", Drop, GiveToLeprechaun),
				new ProductXYZ(12174, "Grimy Spirit weed", Drop, GiveToLeprechaun,
						Clean),
				new ProductXYZ(12172, "Clean Spirit weed", Drop, GiveToLeprechaun),
				new ProductXYZ(219, "Grimy Torstol", Drop, GiveToLeprechaun, Clean),
				new ProductXYZ(269, "Clean Torstol", Drop, GiveToLeprechaun),
				new ProductXYZ(21626, "Grimy Fellstalk", Drop, GiveToLeprechaun,
						Clean),
				new ProductXYZ(21624, "Clean Fellstalk", Drop, GiveToLeprechaun),
				new ProductXYZ(1942, "Raw potato", Drop, GiveToLeprechaun),
				new ProductXYZ(1957, "Onion", GiveToLeprechaun),
				new ProductXYZ(1965, "Cabbage", Drop, GiveToLeprechaun),
				new ProductXYZ(1982, "Tomato", Drop, GiveToLeprechaun),
				new ProductXYZ(5986, "Sweetcorn", Drop, GiveToLeprechaun),
				new ProductXYZ(5504, "Strawberry", Drop, GiveToLeprecha60un),
				new ProductXYZ(5982, "Watermelon", Drop, GiveToLeprechaun),
				new ProductXYZ(6010, "Marigold", Drop, GiveToLeprechaun),
				new ProductXYZ(1793, "Woad leaf", Drop, GiveToLeprechaun),
				new ProductXYZ(225, "Limpwurt root", Drop, GiveToLeprechaun),
				new ProductXYZ(14583, "White lily", Drop, GiveToLeprechaun),
				new ProductXYZ(6006, "Barley", Drop, GiveToLeprechaun),
				new ProductXYZ(5994, "Hammerstone", Drop, GiveToLeprechaun),
				new ProductXYZ(5996, "Asgarnian", Drop, GiveToLeprechaun),
				new ProductXYZ(5931, "Jute fibre", Drop, GiveToLeprechaun),
				new ProductXYZ(5998, "Yanillian", Drop, GiveToLeprechaun),
				new ProductXYZ(6000, "Krandorian", Drop, GiveToLeprechaun),
				new ProductXYZ(6002, "Wildblood", Drop, GiveToLeprechaun),
				new ProductXYZ(1951, "Redberries", Drop, GiveToLeprechaun),
				new ProductXYZ(753, "Cadava berries", Drop, GiveToLeprechaun),
				new ProductXYZ(2126, "Dwellberries", Drop, GiveToLeprechaun),
				new ProductXYZ(247, "Jangerberries", Drop, GiveToLeprechaun),
				new ProductXYZ(239, "White berries", Drop, GiveToLeprechaun),
				new ProductXYZ(6018, "Poisonivy berries", Drop, GiveToLeprechaun),
				new ProductXYZ(1955, "Cooking apple", Drop, GiveToLeprechaun),
				new ProductXYZ(1963, "Banana", Drop, GiveToLeprechaun),
				new ProductXYZ(2108, "Orange", Drop, GiveToLeprechaun),
				new ProductXYZ(2011, "Curry", Drop, GiveToLeprechaun),
				new ProductXYZ(2114, "Pineapple", Drop, GiveToLeprechaun),
				new ProductXYZ(5972, "Papaya", Drop, GiveToLeprechaun),
				new ProductXYZ(5974, "Coconut", Drop, GiveToLeprechaun),
				new ProductXYZ(12134, "Evil turnip", Drop, GiveToLeprechaun),
				new ProductXYZ(6004, "Bittercap mushroom", Drop, GiveToLeprechaun),
				new ProductXYZ(6016, "Cactus spine", Drop, GiveToLeprechaun),
				new ProductXYZ(2398, "Nightshade", Drop, GiveToLeprechaun),
				new ProductXYZ(5980, "Calquat fruit", Drop, GiveToLeprechaun),
				new ProductXYZ(1521, "Oak logs", Drop, Burn),
				new ProductXYZ(1519, "Willow logs", Drop, Burn),
				new ProductXYZ(1517, "Maple logs", Drop, Burn),
				new ProductXYZ(1515, "Yew logs", Drop, Burn),
				new ProductXYZ(1513, "Magic logs", Drop, Burn),
				new ProductXYZ(6043, "Oak roots", Drop),
				new ProductXYZ(6045, "Willow roots", Drop),
				new ProductXYZ(6047, "Maple roots", Drop),
				new ProductXYZ(6049, "Yew roots", Drop),
				new ProductXYZ(6051, "Magic roots", Drop)

		};
		for (ProductXYZ product : productsPlain) {
			products.put(product.getId(), product);
		}
		return products;
	}

	public static HashMap<Integer, ProductXYZ> products = loadProducts();*/
}
