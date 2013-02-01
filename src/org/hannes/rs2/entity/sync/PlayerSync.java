package org.hannes.rs2.entity.sync;

import java.util.Iterator;
import java.util.List;

import org.hannes.rs2.World;
import org.hannes.rs2.container.impl.Equipment;
import org.hannes.rs2.entity.Character;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.entity.sync.UpdateFlags.UpdateFlag;
import org.hannes.rs2.net.ConnectionState;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.net.MessageLength;
import org.hannes.rs2.util.ItemDefinition;
import org.hannes.rs2.util.TextUtils;
import org.hannes.util.Location;

public class PlayerSync implements CharacterSync {

	@Override
	public Message synchronize(Player player) {
		/*
		 * If the map region changed send the new one. We do this immediately as
		 * the client can begin loading it before the actual packet is received.
		 */
		if (player.hasMapRegionChanged()) {
			MessageBuilder packet = new MessageBuilder(73);
			packet.putShort((short) (player.getLastUpdatedRegion().getX()));
			packet.putShort((short) (player.getLastUpdatedRegion().getY()));
			player.getConnection().write(packet.build());
		}

		/*
		 * Get the nearby players
		 */
		List<Player> nearbyPlayers = World.getWorld().getPlayers();

		/*
		 * The update block packet holds update blocks and is send after the
		 * main packet.
		 */
		MessageBuilder updateBlock = new MessageBuilder();

		/*
		 * The main packet is written in bits instead of bytes and holds
		 * information about the local list, players to add and remove, movement
		 * and which updates are required.
		 */
		MessageBuilder packet = new MessageBuilder(81, MessageLength.VARIABLE_16_BIT);
		packet.startBitAccess();

		/*
		 * Updates this player.
		 */
		updateThisPlayerMovement(packet, player);
		updatePlayer(updateBlock, player, false); // false, true

		/*
		 * Write the current size of the player list.
		 */
		packet.putBits(8, player.getLocalPlayers().size());
		
		/*
		 * The local players
		 */
		for (Iterator<Player> iterator = player.getLocalPlayers().iterator(); iterator.hasNext();) {
			final Player otherPlayer = iterator.next();
			
			/*
			 * 
			 */
			if (otherPlayer.getConnection().getState() == ConnectionState.ACTIVE
					&& nearbyPlayers.contains(otherPlayer)) {
				/*
				 * Update the movement.
				 */
				updatePlayerMovement(packet, otherPlayer);
				
				/*
				 * Check if an update is required, and if so, send the update.
				 */
				if(otherPlayer.getUpdateFlags().isUpdateRequired()) {
					updatePlayer(updateBlock, otherPlayer, false);
				}
			} else {
				/*
				 * Remove the player from the list.
				 */
				iterator.remove();
				
				/*
				 * Tell the client to remove the player from the list.
				 */
				packet.putBits(1, 1);
				packet.putBits(2, 3);
			}
		}

		/*
		 * Loop through every player.
		 */
		for(Player otherPlayer : nearbyPlayers) {
			/*
			 * Check if there is room left in the local list.
			 */
			if(player.getLocalPlayers().size() >= 255) {
				/*
				 * There is no more room left in the local list. We cannot add
				 * more players, so we just ignore the extra ones. They will be
				 * added as other players get removed.
				 */
				break;
			}
			
			/*
			 * If they should not be added ignore them.
			 */
			if(otherPlayer == player || player.getLocalPlayers().contains(otherPlayer)) {
				continue;
			}
			
			/*
			 * Add the player to the local list if it is within distance.
			 */
			player.getLocalPlayers().add(otherPlayer);
			
			/*
			 * Add the player in the packet.
			 */
			addNewPlayer(packet, otherPlayer, player);
			
			/*
			 * Update the player, forcing the appearance flag.
			 */
			updatePlayer(updateBlock, otherPlayer, true);
		}

		/*
		 * Check if the update block is not empty.
		 */
		if (!updateBlock.isEmpty()) {
			
			/*
			 * Write a magic id indicating an update block follows.
			 */
			packet.putBits(11, 2047);
			packet.finishBitAccess();

			/*
			 * Add the update block at the end of this packet.
			 */
			packet.put(updateBlock.build());
		} else {
			/*
			 * Terminate the packet normally.
			 */
			packet.finishBitAccess();
		}

		/*
		 * Write the packet.
		 */
		return packet.build();
	}
	
	/**
	 * Updates a non-this player's movement.
	 * @param packet The packet.
	 * @param otherPlayer The player.
	 */
	public void updatePlayerMovement(MessageBuilder packet, Player otherPlayer) {
		/*
		 * Check which type of movement took place.
		 */
		if(otherPlayer.getMovementFlags().getPrimaryDirection() == null) {
			/*
			 * If no movement did, check if an update is required.
			 */
			if(otherPlayer.getUpdateFlags().isUpdateRequired()) {
				/*
				 * Signify that an update happened.
				 */
				packet.putBits(1, 1);
				
				/*
				 * Signify that there was no movement.
				 */
				packet.putBits(2, 0);
			} else {
				/*
				 * Signify that nothing changed.
				 */
				packet.putBits(1, 0);
			}
		} else if(otherPlayer.getMovementFlags().getSecondaryDirection() == null) {
			/*
			 * The player moved but didn't run. Signify that an update is
			 * required.
			 */
			packet.putBits(1, 1);
			
			/*
			 * Signify we moved one tile.
			 */
			packet.putBits(2, 1);
			
			/*
			 * Write the primary sprite (i.e. walk direction).
			 */
			packet.putBits(3, otherPlayer.getMovementFlags().getPrimaryDirection().ordinal());
			
			/*
			 * Write a flag indicating if a block update happened.
			 */
			packet.putBits(1, otherPlayer.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		} else {
			/*
			 * The player ran. Signify that an update happened.
			 */
			packet.putBits(1, 1);
			
			/*
			 * Signify that we moved two tiles.
			 */
			packet.putBits(2, 2);
			
			/*
			 * Write the primary sprite (i.e. walk direction).
			 */
			packet.putBits(3, otherPlayer.getMovementFlags().getPrimaryDirection().ordinal());
			
			/*
			 * Write the secondary sprite (i.e. run direction).
			 */
			packet.putBits(3, otherPlayer.getMovementFlags().getSecondaryDirection().ordinal());
			
			/*
			 * Write a flag indicating if a block update happened.
			 */
			packet.putBits(1, otherPlayer.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		}
	}
	
	/**
	 * Adds a new player.
	 * @param bldr The packet.
	 * @param otherPlayer The player.
	 */
	public void addNewPlayer(MessageBuilder bldr, Player otherPlayer, Player player) {
		/*
		 * Write the player index.
		 */
		bldr.putBits(11, otherPlayer.getIndex());
		
		/*
		 * Calculate the x and y offsets.
		 */
		int xPos = otherPlayer.getLocation().getX() - player.getLocation().getX();
		int yPos = otherPlayer.getLocation().getY() - player.getLocation().getY();
		
		bldr.putBits(1, 1);
		bldr.putBits(1, 1);
		
		bldr.putBits(5, yPos);
		bldr.putBits(5, xPos);
	}
	/**
	 * Updates this player's movement.
	 * 
	 * @param packet The packet.
	 */
	private void updateThisPlayerMovement(MessageBuilder packet, Player player) {
		final boolean updateRequired = player.getUpdateFlags().isUpdateRequired();
		
		/*
		 * Check if the player is teleporting.
		 */
		if(player.isTeleporting() || player.hasMapRegionChanged()) {			
			/*
			 * They are, so an update is required.
			 */
			packet.putBits(1, 1);
			
			/*
			 * This value indicates the player teleported.
			 */
			packet.putBits(2, 3);

			/*
			 * This is the new player height.
			 */
			packet.putBits(2, player.getLocation().getZ());
			
			/*
			 * This indicates that the client should discard the walking queue.
			 */
			packet.putBits(1, player.isTeleporting() ? 1 : 0);
			
			/*
			 * This flag indicates if an update block is appended.
			 */
			packet.putBits(1, updateRequired ? 1 : 0);
			
			/*
			 * These are the positions.
			 */
			packet.putBits(7, player.getLocation().getY() - (player.getLastUpdatedRegion().getY() - 6) * 8);
			packet.putBits(7, player.getLocation().getX() - (player.getLastUpdatedRegion().getX() - 6) * 8);
		} else {
			/*
			 * Otherwise, check if the player moved.
			 */
			if(player.getMovementFlags().getPrimaryDirection() == null) {
				/*
				 * The player didn't move. Check if an update is required.
				 */
				if(updateRequired) {
					/*
					 * Signifies an update is required.
					 */
					packet.putBits(1, 1);
					
					/*
					 * But signifies that we didn't move.
					 */
					packet.putBits(2, 0);
				} else {
					/*
					 * Signifies that nothing changed.
					 */
					packet.putBits(1, 0);
				}
			} else {
				/*
				 * Check if the player was running.
				 */
				if(player.getMovementFlags().getSecondaryDirection() == null) {
					/*
					 * The player walked, an update is required.
					 */
					packet.putBits(1, 1);
					
					/*
					 * This indicates the player only walked.
					 */
					packet.putBits(2, 1);
					
					/*
					 * This is the player's walking direction.
					 */
					packet.putBits(3, player.getMovementFlags().getPrimaryDirection().ordinal());
					
					/*
					 * This flag indicates an update block is appended.
					 */
					packet.putBits(1, updateRequired ? 1 : 0);
				} else {
					/*
					 * The player ran, so an update is required.
					 */
					packet.putBits(1, 1);
					
					/*
					 * This indicates the player ran.
					 */
					packet.putBits(2, 2);
					
					/*
					 * This is the walking direction.
					 */
					packet.putBits(3, player.getMovementFlags().getPrimaryDirection().ordinal());
					
					/*
					 * And this is the running direction.
					 */
					packet.putBits(3, player.getMovementFlags().getSecondaryDirection().ordinal());
					
					/*
					 * And this flag indicates an update block is appended.
					 */
					packet.putBits(1, updateRequired ? 1 : 0);
				}
			}
		}
	}

	/**
	 * Updates a player.
	 * @param packet The packet.
	 * @param otherPlayer The other player.
	 * @param forceAppearance The force appearance flag.
	 * @param noChat Indicates chat should not be relayed to this player.
	 */
	public void updatePlayer(MessageBuilder packet, Player otherPlayer, boolean forceAppearance) {
		/*
		 * If no update is required and we don't have to force an appearance
		 * update, don't write anything.
		 */
		if(!otherPlayer.getUpdateFlags().isUpdateRequired() && !forceAppearance) {
			return;
		}
		
		/*
		 * We can used the cached update block!
		 */
		synchronized(otherPlayer) {
			/*
			 * We have to construct and cache our own block.
			 */
			MessageBuilder block = new MessageBuilder();
			
			/*
			 * The player's update flas
			 */
			UpdateFlags updateFlags = otherPlayer.getUpdateFlags();
			
			/*
			 * Calculate the bitmask.
			 */
			int mask = 0;

			if (updateFlags.get(UpdateFlag.FORCE_WALK)) {
				mask |= 0x400;
			}
			if (updateFlags.get(UpdateFlag.GRAPHICS)) {
				mask |= 0x100;
			}
			if (updateFlags.get(UpdateFlag.ANIMATION)) {
				mask |= 0x8;
			}
			if (updateFlags.get(UpdateFlag.CHAT)) {
				mask |= 0x4;
			}
			if (updateFlags.get(UpdateFlag.FORCED_CHAT)) {
				mask |= 0x80;
			}
			if (updateFlags.get(UpdateFlag.FACE_ENTITY)) {
				mask |= 0x1;
			}
			if (updateFlags.get(UpdateFlag.APPEARANCE) || forceAppearance) {
				mask |= 0x10;
			}
			if (updateFlags.get(UpdateFlag.FACE_COORDINATE)) {
				mask |= 0x2;
			}
			if (updateFlags.get(UpdateFlag.HIT)) {
				mask |= 0x20;
			}
			if (updateFlags.get(UpdateFlag.HIT_2)) {
				mask |= 0x200;
			}
			
			/*
			 * Check if the bitmask would overflow a byte.
			 */
			if (mask >= 0x100) {
				/*
				 * Write it as a short and indicate we have done so.
				 */
				mask |= 0x40;
				block.put((byte) (mask & 0xFF));
				block.put((byte) (mask >> 8));
			} else {
				/*
				 * Write it as a byte.
				 */
				block.put((byte) mask);
			}

			/*
			 * Append the appropriate updates.
			 */
			if (updateFlags.get(UpdateFlag.FORCE_WALK)) {
				// TODO: Make this work
			}
			if (updateFlags.get(UpdateFlag.GRAPHICS)) {
				appendGraphicUpdate(otherPlayer, block);
			}
			if (updateFlags.get(UpdateFlag.ANIMATION)) {
				appendAnimationUpdate(otherPlayer, block);
			}
			if (updateFlags.get(UpdateFlag.CHAT)) {
				appendChatUpdate(otherPlayer, block);
			}
			if (updateFlags.get(UpdateFlag.FORCED_CHAT)) {
				appendForceChat(otherPlayer, block);
			}
			if (updateFlags.get(UpdateFlag.FACE_ENTITY)) {
				appendFaceEntity(otherPlayer, block);
			}
			if (updateFlags.get(UpdateFlag.APPEARANCE) || forceAppearance) {
				appendPlayerAppearanceUpdate(block, otherPlayer);
			}
			if (updateFlags.get(UpdateFlag.FACE_COORDINATE)) {
				appendFaceLocation(otherPlayer, block);
			}
			if (updateFlags.get(UpdateFlag.HIT)) {
				appendHitUpdate(otherPlayer, block);
			}
			if (updateFlags.get(UpdateFlag.HIT_2)) {
				appendHitUpdate2(otherPlayer, block);
			}
		
			/*
			 * And finally append the block at the end.
			 */
			packet.put(block.build());
		}
	}

	private void appendGraphicUpdate(Player player, MessageBuilder builder) {
		builder.putShort((short) player.getGraphic().getId());
		builder.putInt(player.getGraphic().getModifier());
	}

	private void appendChatUpdate(Player player, MessageBuilder builder) {
		final ChatMessage message = player.getChatMessage();
		byte[] bytes = message.getText();
		builder.put((byte) message.getColor());
		builder.put((byte) message.getEffects());
		for(int ptr = bytes.length -1; ptr >= 0; ptr--) {
			builder.put(bytes[ptr]);
		}
	}

	private void appendFaceLocation(Player player, MessageBuilder builder) {
		final Location point = player.getViewLocation();
		builder.putShort((short) (point == null ? 0 : point.getX() * 2 + 1));
		builder.putShort((short) (point == null ? 0 : point.getY() * 2 + 1));
	}

	private void appendHitUpdate(Player player, MessageBuilder builder) {
		builder.put((byte) player.getPrimaryDamageIdentifier().getDamage());
		builder.put((byte) player.getPrimaryDamageIdentifier().getHitType().ordinal());
		
		builder.put((byte) 5);
		builder.put((byte) 10);
		//FIXME:
//		builder.put(player.getSkills().getLevel(Skills.HITPOINTS));
//		builder.put(player.getSkills().getLevelForExperience(Skills.HITPOINTS));
	}

	private void appendHitUpdate2(Player player, MessageBuilder builder) {
		builder.put((byte) player.getSecondaryDamageIdentifier().getDamage());
		builder.put((byte) player.getSecondaryDamageIdentifier().getHitType().ordinal());
		
		builder.put((byte) 5);
		builder.put((byte) 10);
		//FIXME:
//		builder.put(player.getSkills().getLevel(Skills.HITPOINTS));
//		builder.put(player.getSkills().getLevelForExperience(Skills.HITPOINTS));
	}

	private void appendForceChat(Player player, MessageBuilder builder) {
		builder.putString(player.getText());
	}

	private void appendFaceEntity(Player player, MessageBuilder builder) {
		final Character character = player.getAcquaintance();
		if(character == null) {
			builder.putShort((short) 65535);
		} else {
			builder.putShort((short) character.getAcquaintanceIndex());
		}
	}

	private void appendAnimationUpdate(Player player, MessageBuilder builder) {
		builder.putShort((short) player.getAnimation().getId());
		builder.put((byte) player.getAnimation().getDelay());
	}

	private void appendPlayerAppearanceUpdate(MessageBuilder packet, Player otherPlayer) {
		Appearance app = otherPlayer.getAppearance();
		Equipment eq = otherPlayer.getEquipment();

		MessageBuilder playerProps = new MessageBuilder();
		playerProps.put((byte) app.getGender());
		playerProps.put((byte) app.getPrayerIcon());
		playerProps.put((byte) app.getMiscIcon());
		playerProps.put((byte) app.getHintIcon());
		
		if (eq.getId(Equipment.SLOT_HELM) > 1) {
			playerProps.putShort((short) (0x200 + eq.getId(Equipment.SLOT_HELM)));
		} else {
			playerProps.put((byte) 0);
		}

		if (eq.getId(Equipment.SLOT_CAPE) > 1) {
			playerProps.putShort((short) (0x200 + eq.getId(Equipment.SLOT_CAPE)));
		} else {
			playerProps.put((byte) 0);
		}

		if (eq.getId(Equipment.SLOT_AMULET) > 1) {
			playerProps.putShort((short) (0x200 + eq.getId(Equipment.SLOT_AMULET)));
		} else {
			playerProps.put((byte) 0);
		}

		if (eq.getId(Equipment.SLOT_WEAPON) > 1) {
			playerProps.putShort((short) (0x200 + eq.getId(Equipment.SLOT_WEAPON)));
		} else {
			playerProps.put((byte) 0);
		}
		
		if (eq.getId(Equipment.SLOT_CHEST) > 1) {
			playerProps.putShort((short) (0x200 + eq.getId(Equipment.SLOT_CHEST)));
		} else {
			playerProps.putShort((short) (0x100 + app.getTorso()));
		}
		
		if (eq.getId(Equipment.SLOT_SHIELD) > 1) {
			playerProps.putShort((short) (0x200 + eq.getId(Equipment.SLOT_SHIELD)));
		} else {
			playerProps.put((byte) 0);
		}
		
		if ((ItemDefinition.forId(eq.getId(Equipment.SLOT_CHEST)).getMask() & 1) == 0) {
			playerProps.putShort((short) (0x100 + app.getArms()));
		} else {
			playerProps.put((byte) 0);
		}
		
		if (eq.getId(Equipment.SLOT_BOTTOMS) > 1) {
			playerProps.putShort((short) (0x200 + eq.getId(Equipment.SLOT_BOTTOMS)));
		} else {
			playerProps.putShort((short) (0x100 + app.getLegs()));
		}
		
		if ((ItemDefinition.forId(eq.getId(Equipment.SLOT_CHEST)).getMask() & 1) == 0) {
			playerProps.putShort((short) (0x100 + app.getHead()));		
		} else {
			playerProps.put((byte) 0);
		}

		if (eq.getId(Equipment.SLOT_GLOVES) > 1) {
			playerProps.putShort((short) (0x200 + eq.getId(Equipment.SLOT_GLOVES)));
		} else {
			playerProps.putShort((short) (0x100 + app.getHands()));
		}
		
		if (eq.getId(Equipment.SLOT_BOOTS) > 1) {
			playerProps.putShort((short) (0x200 + eq.getId(Equipment.SLOT_BOOTS)));
		} else {
			 playerProps.putShort((short) (0x100 + app.getFeet()));
		}
			 
		if ((ItemDefinition.forId(eq.getId(Equipment.SLOT_CHEST)).getMask() & 2) == 0) {
			playerProps.putShort((short) (0x100 + app.getBeard()));
		} else {
			playerProps.put((byte) 0);
		}
		
		playerProps.put((byte) 7);
		playerProps.put((byte) 8);
		playerProps.put((byte) 9);
		playerProps.put((byte) 5);
		playerProps.put((byte) 0);

		playerProps.putShort((short) 0x328);
		playerProps.putShort((short) 0x337);
		playerProps.putShort((short) 0x333);
		playerProps.putShort((short) 0x334);
		playerProps.putShort((short) 0x335);
		playerProps.putShort((short) 0x336);
		playerProps.putShort((short) 0x338);
		
		playerProps.putLong(TextUtils.encode(otherPlayer.getUsername()));
		playerProps.put((byte) 3);
		playerProps.putShort((short) 0);
		
		Message props = playerProps.build();
		packet.put((byte) props.size());
		packet.put(props);
	}

}