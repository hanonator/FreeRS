package org.hannes.rs2.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

public class WeaponInterface {

	/**
	 * The default interface (when fighting bare-handed)
	 */
	public static final int DEFAULT_INTERFACE = 5855;

	/**
	 * Cheap hax, fix later
	 */
	private static final Map<Integer, Integer> weapons = new HashMap<>();

	/**
	 * The map
	 */
	private static final Map<Integer, WeaponInterface> map = new HashMap<>();

	/**
	 * The id of the string in the client for the weapon's name
	 */
	private final int textId;
	
	/**
	 * The sidebar interface id
	 */
	private final int interfaceId;
	
	/**
	 * The id of the client configuration value
	 */
	@Deprecated
	private final int configurationId;
	
	/**
	 * 
	 */
	private final int modelId;
	
	/**
	 * The options in the sidebar interface
	 */
	private final InterfaceOption[] options;
	
	/**
	 * @param textId
	 * @param interfaceId
	 * @param configurationId
	 * @param options
	 */
	public WeaponInterface(int textId, int interfaceId, int modelId, int configurationId, InterfaceOption[] options) {
		this.textId = textId;
		this.interfaceId = interfaceId;
		this.configurationId = configurationId;
		this.options = options;
		this.modelId = modelId;
	}

	/**
	 * Initializes all the weapon interfaces
	 * 
	 * @param document
	 * @throws Exception
	 */
	public static void initialize(Document document) throws Exception {
		/*
		 * Parse all packet information
		 */
		for (Iterator<Element> iterator = document.getRootElement().elements().iterator(); iterator.hasNext(); ) {
			Element element = iterator.next();
			
			/*
			 * Get the interface id
			 */
			int interfaceId = Integer.valueOf(element.element("interface-id").getText());
			
			/*
			 * Get the text id
			 */
			int textId = Integer.valueOf(element.element("text-id").getText());
			
			/*
			 * Get the model id
			 */
			int modelId = Integer.valueOf(element.element("model-id").getText());
			
			/*
			 * Get the size of the options
			 */
			int size = element.element("options").elements("option").size();
			
			/*
			 * Create the options array
			 */
			InterfaceOption[] options = new InterfaceOption[size];
			
			/*
			 * Add all the options
			 */
			int index = 0;
			for (Iterator<Element> it = element.element("options").elementIterator("option"); it.hasNext(); ) {
				Element optionElement = it.next();
				
				/*
				 * Get the button id
				 */
				int id = Integer.valueOf(optionElement.elementText("id"));
				
				/*
				 * Get the fighint type (crush, stab, etc)
				 */
				FightingType type = FightingType.valueOf(optionElement.elementText("style"));
				
				/*
				 * Get the training style (aggressive, controlled, etc)
				 */
				TrainingStyle style = TrainingStyle.valueOf(optionElement.elementText("type"));
				
				/*
				 * Add the option
				 */
				options[index++] = new InterfaceOption(id, type, style);
			}
			
			/*
			 * Add the weapon interface
			 */
			map.put(interfaceId, new WeaponInterface(textId, interfaceId, modelId, 0, options));
		}
		weapons.put(-1, 5855);
	}

	public static WeaponInterface get(int index) {
		int interfaceId = weapons.get(index);
		if (interfaceId == -1 || interfaceId == 0) {
			interfaceId = DEFAULT_INTERFACE;
		}
		return map.get(interfaceId);
	}

	public static void put(int key, int value) {
		weapons.put(key, value);
	}

	/**
	 * @return the textId
	 */
	public int getTextId() {
		return textId;
	}

	/**
	 * @return the interfaceId
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * @return the configurationId
	 */
	@Deprecated
	public int getConfigurationId() {
		return configurationId;
	}

	/**
	 * @return the model id
	 */
	public int getModelId() {
		return modelId;
	}
	/**
	 * @return the options
	 */
	public InterfaceOption[] getOptions() {
		return options;
	}

	public InterfaceOption getOption(int i) {
		return options[i];
	}

	public static class InterfaceOption {
		
		/**
		 * The id of the button
		 */
		private final int id;
		
		/**
		 * The style of fighting
		 */
		private final FightingType fightingType;
		
		/**
		 * The style of training
		 */
		private final TrainingStyle trainingStyle;

		/**
		 * @param id
		 * @param fightingType
		 * @param trainingStyle
		 */
		public InterfaceOption(int id, FightingType fightingType, TrainingStyle trainingStyle) {
			this.id = id;
			this.fightingType = fightingType;
			this.trainingStyle = trainingStyle;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @return the fightingType
		 */
		public FightingType getFightingType() {
			return fightingType;
		}

		/**
		 * @return the trainingStyle
		 */
		public TrainingStyle getTrainingStyle() {
			return trainingStyle;
		}
		
	}
	
	/**
	 * The 
	 * @author red
	 *
	 */
	public static enum FightingType {
		STAB, SLASH, CRUSH, ACCURATE, LONG_RANGE, RAPID_FIRE;
	}

	/**
	 * The style of training
	 * 
	 * @author red
	 *
	 */
	public static enum TrainingStyle {
		ACCURATE, AGGRESSIVE, CONTROLLED, DEFENSIVE, RANGE, RANGE_DEFENSIVE;
	}

}