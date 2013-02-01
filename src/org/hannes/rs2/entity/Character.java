package org.hannes.rs2.entity;

import java.util.ArrayList;
import java.util.List;

import org.hannes.rs2.entity.sync.Animation;
import org.hannes.rs2.entity.sync.Appearance;
import org.hannes.rs2.entity.sync.ChatMessage;
import org.hannes.rs2.entity.sync.DamageIdentifier;
import org.hannes.rs2.entity.sync.Graphic;
import org.hannes.rs2.entity.sync.MovementFlags;
import org.hannes.rs2.entity.sync.UpdateFlags;
import org.hannes.rs2.entity.sync.UpdateFlags.UpdateFlag;
import org.hannes.rs2.entity.sync.WalkingQueue;
import org.hannes.util.Location;

public abstract class Character extends Entity {

	/**
	 * The character's update flags
	 */
	private final UpdateFlags updateFlags = new UpdateFlags();
	
	/**
	 * The character's movement flags
	 */
	private final MovementFlags movementFlags = new MovementFlags();
	
	/**
	 * The characterr's walking queue
	 */
	private final WalkingQueue walkingQueue = new WalkingQueue(this);
	
	/**
	 * The character's teleport target
	 */
	private Location teleportTarget = new Location(Location.DEFAULT_LOCATION);
	
	/**
	 * The last updated region
	 */
	private Location lastUpdatedRegion = new Location(0, 0);
	
	/**
	 * Indicates the character has walked outside its loaded regions
	 */
	private boolean mapRegionChanged;
	
	/**
	 * Indicates the character is currently teleporting
	 */
	private boolean teleporting;
	
	/**
	 * The character's appearance
	 */
	private Appearance appearance = new Appearance();
	
	/**
	 * The character's graphic update
	 */
	private Graphic graphic;
	
	/**
	 * The character's primary damage identifier
	 */
	private DamageIdentifier primaryDamageIdentifier;

	/**
	 * The character's secondary damage identifier
	 */
	private DamageIdentifier secondaryDamageIdentifier;
	
	/**
	 * The player's chat message
	 */
	private ChatMessage chatMessage;
	
	/**
	 * The location the character is looking towards
	 */
	private Location viewLocation;
	
	/**
	 * Text that appears above the character's head
	 */
	private String text;
	
	/**
	 * The index of the mob the character transforms to
	 */
	private int transformationType;
	
	/**
	 * The acquainting character
	 */
	private Character acquaintance;
	
	/**
	 * The character's animation
	 */
	private Animation animation;

	/**
	 * The list of the local players
	 */
	private final List<Player> localPlayers = new ArrayList<>();

	/**
	 * The list of the local players
	 */
	private final List<NPC> localNPCs = new ArrayList<>();

	/**
	 * 
	 * @param index
	 */
	public Character(int index) {
		super(index);
	}

	/**
	 * Gets the index for when the character is an acquaintance of another
	 * 
	 * @return
	 */
	public abstract int getAcquaintanceIndex();

	/**
	 * Finish the synchronization
	 */
	public void finishSynchronization() {
		teleporting = false;
		teleportTarget = null;
		mapRegionChanged = false;
		updateFlags.reset();
	}

	public UpdateFlags getUpdateFlags() {
		return updateFlags;
	}

	public MovementFlags getMovementFlags() {
		return movementFlags;
	}

	public WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}

	public boolean hasMapRegionChanged() {
		return mapRegionChanged;
	}

	public void setMapRegionChanged(boolean mapRegionChanged) {
		this.mapRegionChanged = mapRegionChanged;
	}

	public boolean isTeleporting() {
		return teleporting;
	}

	public void setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
	}

	public Location getTeleportTarget() {
		return teleportTarget;
	}

	public void setTeleportTarget(Location teleportTarget) {
		this.teleportTarget = teleportTarget;
	}

	public Appearance getAppearance() {
		return appearance;
	}

	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
	}

	public Graphic getGraphic() {
		return graphic;
	}

	public DamageIdentifier getPrimaryDamageIdentifier() {
		return primaryDamageIdentifier;
	}

	public DamageIdentifier getSecondaryDamageIdentifier() {
		return secondaryDamageIdentifier;
	}

	public ChatMessage getChatMessage() {
		return chatMessage;
	}

	public Location getViewLocation() {
		return viewLocation;
	}

	public String getText() {
		return text;
	}

	public int getTransformationType() {
		return transformationType;
	}

	public Character getAcquaintance() {
		return acquaintance;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setGraphic(Graphic graphic) {
		this.graphic = graphic;
		this.updateFlags.flag(UpdateFlag.GRAPHICS);
	}

	public void setPrimaryDamageIdentifier(DamageIdentifier primaryDamageIdentifier) {
		this.primaryDamageIdentifier = primaryDamageIdentifier;
		this.updateFlags.flag(UpdateFlag.HIT);
	}

	public void setSecondaryDamageIdentifier(
			DamageIdentifier secondaryDamageIdentifier) {
		this.secondaryDamageIdentifier = secondaryDamageIdentifier;
		this.updateFlags.flag(UpdateFlag.HIT_2);
	}

	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
		this.updateFlags.flag(UpdateFlag.CHAT);
	}

	public void setViewLocation(Location viewLocation) {
		this.viewLocation = viewLocation;
		this.updateFlags.flag(UpdateFlag.FACE_COORDINATE);
	}

	public void setText(String text) {
		this.text = text;
		this.updateFlags.flag(UpdateFlag.FORCED_CHAT);
	}

	public void setTransformationType(int transformationType) {
		this.transformationType = transformationType;
		this.updateFlags.flag(UpdateFlag.TRANSFORM);
	}

	public void setAcquaintance(Character acquaintance) {
		this.acquaintance = acquaintance;
		this.updateFlags.flag(UpdateFlag.FACE_ENTITY);
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
		this.updateFlags.flag(UpdateFlag.ANIMATION);
	}

	public List<Player> getLocalPlayers() {
		return localPlayers;
	}

	public Location getLastUpdatedRegion() {
		return lastUpdatedRegion;
	}

	public void setLastUpdatedRegion(Location lastUpdatedRegion) {
		this.lastUpdatedRegion = lastUpdatedRegion;
	}

	public List<NPC> getLocalNPCs() {
		return localNPCs;
	}

}