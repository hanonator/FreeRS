package org.hannes.rs2.entity.sync;

import java.util.Iterator;

import org.hannes.rs2.World;
import org.hannes.rs2.entity.Character;
import org.hannes.rs2.entity.NPC;
import org.hannes.rs2.entity.Player;
import org.hannes.rs2.entity.sync.UpdateFlags.UpdateFlag;
import org.hannes.rs2.net.Message;
import org.hannes.rs2.net.MessageBuilder;
import org.hannes.rs2.net.MessageLength;
import org.hannes.util.Location;

public class NPCSync implements CharacterSync {

	@Override
	public Message synchronize(Player player) {
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
		MessageBuilder packet = new MessageBuilder(65, MessageLength.VARIABLE_16_BIT);
		packet.startBitAccess();

		/*
		 * Write the current size of the player list.
		 */
		packet.putBits(8, player.getLocalNPCs().size());
		
		/*
		 * The local players
		 */
		for (Iterator<NPC> iterator = player.getLocalNPCs().iterator(); iterator.hasNext();) {
			final NPC npc = iterator.next();
			
			/*
			 * 
			 */
			if (World.getWorld().getNPCs().contains(npc)) {
				/*
				 * Update the movement.
				 */
				updateNPCMovement(packet, npc);
				
				/*
				 * Check if an update is required, and if so, send the update.
				 */
				if(npc.getUpdateFlags().isUpdateRequired()) {
					updateNPC(updateBlock, npc);
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
		for(NPC npc : World.getWorld().getNPCs()) {
			/*
			 * Check if there is room left in the local list.
			 */
			if(npc.getLocalNPCs().size() >= 255) {
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
			if(npc == null || player.getLocalNPCs().contains(npc)) {
				continue;
			}
			
			/*
			 * Add the player to the local list if it is within distance.
			 */
			player.getLocalNPCs().add(npc);
			
			/*
			 * Add the player in the packet.
			 */
			addNewNPC(packet, npc, player);
			
			/*
			 * Update the player, forcing the appearance flag.
			 */
			if (npc.getUpdateFlags().isUpdateRequired()) {
				updateNPC(updateBlock, npc);
			}
		}

		/*
		 * Check if the update block is not empty.
		 */
		if (!updateBlock.isEmpty()) {
			
			/*
			 * Write a magic id indicating an update block follows.
			 */
			packet.putBits(14, 16383);
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

	private void addNewNPC(MessageBuilder packet, NPC npc, Player player) {

		/*
		 * Write the NPC's index.
		 */
		packet.putBits(14, npc.getIndex());

		/*
		 * Calculate the x and y offsets.
		 */
		int xPos = npc.getLocation().getX() - player.getLocation().getX();
		int yPos = npc.getLocation().getY() - player.getLocation().getY();

		/*
		 * And write them.
		 */
		packet.putBits(5, yPos);
		packet.putBits(5, xPos);

		/*
		 * Discards the client-side walk queue.
		 */
		packet.putBits(1, 0);

		/*
		 * We now write the NPC type id.
		 */
		packet.putBits(12, npc.getType());

		/*
		 * And indicate if an update is required.
		 */
		packet.putBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
	}

	private void updateNPCMovement(MessageBuilder packet, NPC npc) {
		/*
		 * Check if the NPC is running.
		 */
		if (npc.getMovementFlags().getSecondaryDirection() == null) {
			/*
			 * They are not, so check if they are walking.
			 */
			if (npc.getMovementFlags().getPrimaryDirection() == null) {
				/*
				 * They are not walking, check if the NPC needs an update.
				 */
				if (npc.getUpdateFlags().isUpdateRequired()) {
					/*
					 * Indicate an update is required.
					 */
					packet.putBits(1, 1);

					/*
					 * Indicate we didn't move.
					 */
					packet.putBits(2, 0);
				} else {
					/*
					 * Indicate no update or movement is required.
					 */
					packet.putBits(1, 0);
				}
			} else {
				/*
				 * They are walking, so indicate an update is required.
				 */
				packet.putBits(1, 1);

				/*
				 * Indicate the NPC is walking 1 tile.
				 */
				packet.putBits(2, 1);

				/*
				 * And write the direction.
				 */
				packet.putBits(3, npc.getMovementFlags().getPrimaryDirection().ordinal());

				/*
				 * And write the update flag.
				 */
				packet.putBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
			}
		} else {
			/*
			 * They are running, so indicate an update is required.
			 */
			packet.putBits(1, 1);

			/*
			 * Indicate the NPC is running 2 tiles.
			 */
			packet.putBits(2, 2);

			/*
			 * And write the directions.
			 */
			packet.putBits(3, npc.getMovementFlags().getPrimaryDirection().ordinal());
			packet.putBits(3, npc.getMovementFlags().getSecondaryDirection().ordinal());

			/*
			 * And write the update flag.
			 */
			packet.putBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		}
	}

	private void updateNPC(MessageBuilder updateBlock, NPC npc) {
		/*
		 * Calculate the mask.
		 */
		int mask = 0;

		MessageBuilder block = new MessageBuilder();

		if (npc.getUpdateFlags().get(UpdateFlag.ANIMATION)) {
			mask |= 0x10;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.HIT)) {
			mask |= 0x8;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.GRAPHICS)) {
			mask |= 0x80;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FACE_ENTITY)) {
			mask |= 0x20;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FORCED_CHAT)) {
			mask |= 0x1;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.HIT_2)) {
			mask |= 0x40;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.TRANSFORM)) {
			mask |= 0x2;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FACE_COORDINATE)) {
			mask |= 0x4;
		}

		/*
		 * And write the mask.
		 */
		updateBlock.put(mask);

		if (npc.getUpdateFlags().get(UpdateFlag.ANIMATION)) {
			appendAnimationUpdate(npc, block);
		}
		if (npc.getUpdateFlags().get(UpdateFlag.HIT)) {
			appendHitUpdate(npc, block);
		}
		if (npc.getUpdateFlags().get(UpdateFlag.GRAPHICS)) {
			appendGraphicUpdate(npc, block);
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FACE_ENTITY)) {
			appendFaceEntity(npc, block);
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FORCED_CHAT)) {
			appendForceChat(npc, block);
		}
		if (npc.getUpdateFlags().get(UpdateFlag.HIT_2)) {
			appendHitUpdate2(npc, block);
		}
		if (npc.getUpdateFlags().get(UpdateFlag.TRANSFORM)) {
			// TODO: Transform
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FACE_COORDINATE)) {
			appendFaceLocation(npc, block);
		}
	}

	private void appendGraphicUpdate(NPC npc, MessageBuilder builder) {
		builder.putShort(npc.getGraphic().getId());
		builder.putInt(npc.getGraphic().getModifier());
	}

	private void appendFaceLocation(NPC npc, MessageBuilder builder) {
		final Location point = npc.getViewLocation();
		builder.putShort(point == null ? 0 : point.getX() * 2 + 1);
		builder.putShort(point == null ? 0 : point.getY() * 2 + 1);
	}

	private void appendHitUpdate(NPC npc, MessageBuilder builder) {
//		builder.putByte(npc.getPrimaryHit().getDamage());
//		builder.putByte(npc.getPrimaryHit().getHitType().ordinal());
//		builder.putByte(npc.getHealth());
//		builder.putByte(npc.getMaxHealth());
	}

	private void appendHitUpdate2(NPC npc, MessageBuilder builder) {
//		builder.putByte(npc.getSecondaryhit().getDamage());
//		builder.putByte(npc.getSecondaryhit().getHitType().ordinal());
//		builder.putByte(npc.getHealth());
//		builder.putByte(npc.getMaxHealth());
	}

	private void appendForceChat(NPC npc, MessageBuilder builder) {
		builder.putString(npc.getText());
	}

	private void appendFaceEntity(NPC npc, MessageBuilder builder) {
		final Character character = npc.getAcquaintance();
		if(character == null) {
			builder.putShort(65535);
		} else {
			builder.putShort(character.getAcquaintanceIndex());
		}
	}

	private void appendAnimationUpdate(NPC npc, MessageBuilder builder) {
		builder.putShort((short) npc.getAnimation().getId());
		builder.put((byte) npc.getAnimation().getDelay());
	}

}