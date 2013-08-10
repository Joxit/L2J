/*
 * Copyright (C) 2004-2013 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests.Q00022_TragedyInVonHellmannForest;

import quests.Q00021_HiddenTruth.Q00021_HiddenTruth;

import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.QuestTimer;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.serverpackets.NpcSay;

/**
 * Tragedy in Von Hellmann Forest (22)
 * @author Joxit
 */
public class Q00022_TragedyInVonHellmannForest extends Quest
{
	// npcs
	private static final int INNOCENTIN = 31328;
	private static final int TIFAREN = 31334;
	private static final int WELL = 31527;
	private static final int GHOST_OF_PRIEST = 31528;
	private static final int GHOST_OF_ADVENTURER = 31529;
	// mobs
	private static final int[] MOBS =
	{
		21553,
		21554,
		21555,
		21556,
		21561
	};
	private static final int SOUL_OF_WELL = 27217;
	// items
	private static final int SKULL = 7142;
	private static final int CROSS = 7141;
	private static final int BOX = 7147;
	private static final int LETTER = 7143;
	private static final int JEWEL1 = 7144;
	private static final int JEWEL2 = 7145;
	private static final int SEALED_BOX = 7146;
	// misc
	private static int MIN_LVL = 63;
	private static int[] TIFAREN_VAR;
	private static int[] WELL_VAR;
	
	private static Location PRIEST_LOC = new Location(38354, -49777, -1128);
	private static Location SOUL_WELL_LOC = new Location(34706, -54590, -2054);
	
	public Q00022_TragedyInVonHellmannForest(int questId, String name, String descr)
	{
		super(questId, name, descr);
		TIFAREN_VAR = new int[2];
		WELL_VAR = new int[2];
		addKillId(MOBS);
		addKillId(SOUL_OF_WELL);
		addAttackId(SOUL_OF_WELL);
		addStartNpc(TIFAREN);
		addTalkId(INNOCENTIN, TIFAREN, WELL, GHOST_OF_PRIEST, GHOST_OF_ADVENTURER);
		registerQuestItems(SKULL, CROSS, BOX, JEWEL1, JEWEL2, SEALED_BOX);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		String htmltext = null;
		if (st == null)
		{
			return htmltext;
		}
		switch (event)
		{
			case "31529-02.html":
			case "31529-04.html":
			case "31529-05.html":
			case "31529-06.html":
			case "31529-07.html":
			case "31529-09.html":
			case "31529-13.html":
			case "31529-13a.html":
			case "31328-13.html":
			case "31328-06.html":
			case "31328-05.html":
			case "31328-02.html":
			case "31328-07.html":
			case "31328-08.html":
			case "31328-14.html":
			case "31328-15.html":
			case "31328-16.html":
			case "31328-17.html":
			case "31328-18.html":
			case "31334-12.html":
			{
				htmltext = event;
				break;
			}
			case "31334-02.htm":
			{
				if (st.isCreated())
				{
					final QuestState qt = player.getQuestState(Q00021_HiddenTruth.class.getSimpleName());
					if ((qt != null) && qt.isCompleted() && (player.getLevel() >= MIN_LVL))
					{
						htmltext = event;
					}
					else
					{
						htmltext = "31334-03.html";
					}
				}
				break;
			}
			case "31334-04.html":
			{
				st.startQuest();
				htmltext = event;
				break;
			}
			case "31334-06.html":
			{
				if (st.hasQuestItems(CROSS))
				{
					htmltext = event;
				}
				else
				{
					htmltext = "31334-07.html";
					st.setCond(2, true);
				}
				break;
			}
			case "31334-08.html":
			{
				htmltext = event;
				st.setCond(4, true);
				break;
			}
			case "31334-13.html":
			{
				if ((st.isCond(5) || st.isCond(6)) && st.hasQuestItems(CROSS) && st.hasQuestItems(SKULL))
				{
					if (TIFAREN_VAR[0] == 0)
					{
						TIFAREN_VAR[0] = 1;
						TIFAREN_VAR[1] = player.getObjectId();
						final L2Npc ghost2 = addSpawn(GHOST_OF_PRIEST, PRIEST_LOC, true, 0);
						ghost2.setScriptValue(player.getObjectId());
						st.startQuestTimer("DESPAWN_GHOST2", 1000 * 120, ghost2);
						ghost2.broadcastPacket(new NpcSay(ghost2.getObjectId(), 0, ghost2.getId(), NpcStringId.DID_YOU_CALL_ME_S1).addStringParameter(player.getName()));
						st.takeItems(SKULL, -1);
						st.setCond(7, true);
						htmltext = event;
					}
					else
					{
						htmltext = "31334-14.html";
						st.setCond(6, true);
					}
				}
				else if ((st.isCond(6) || st.isCond(7)) && st.hasQuestItems(CROSS))
				{
					if (TIFAREN_VAR[0] == 0)
					{
						TIFAREN_VAR[0] = 1;
						TIFAREN_VAR[1] = player.getObjectId();
						final L2Npc ghost2 = addSpawn(GHOST_OF_PRIEST, PRIEST_LOC, true, 0);
						ghost2.setScriptValue(player.getObjectId());
						st.startQuestTimer("DESPAWN_GHOST2", 1000 * 120, ghost2);
						ghost2.broadcastPacket(new NpcSay(ghost2.getObjectId(), 0, ghost2.getId(), NpcStringId.DID_YOU_CALL_ME_S1).addStringParameter(player.getName()));
						htmltext = event;
					}
					else
					{
						htmltext = "31334-14.html";
						st.setCond(6, true);
					}
				}
				break;
			}
			case "31528-04.html":
			{
				playSound(player, QuestSound.AMBSOUND_HORROR_03);
				htmltext = event;
				break;
			}
			case "31528-08.html":
			{
				QuestTimer qt = getQuestTimer("DESPAWN_GHOST2", npc, player);
				if (qt != null)
				{
					qt.cancelAndRemove();
					npc.setScriptValue(0);
					st.startQuestTimer("DESPAWN_GHOST2", 1000 * 3, npc);
					st.setCond(8);
					
				}
				break;
			}
			case "DESPAWN_GHOST2":
			{
				TIFAREN_VAR[0] = 0;
				if (npc.getScriptValue() != 0)
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getId(), NpcStringId.IM_CONFUSED_MAYBE_ITS_TIME_TO_GO_BACK));
				}
				
				npc.deleteMe();
				break;
			}
			case "31328-03.html":
			{
				if (st.isCond(8))
				{
					st.takeItems(CROSS, -1);
					htmltext = event;
				}
				break;
			}
			case "31328-09.html":
			{
				if (st.isCond(8))
				{
					st.giveItems(LETTER, 1);
					st.setCond(9, true);
					htmltext = event;
				}
				break;
			}
			case "31328-11.html":
			{
				if (st.isCond(14) && st.hasQuestItems(BOX))
				{
					st.takeItems(BOX, -1);
					st.setCond(15, true);
					htmltext = event;
				}
				break;
			}
			case "31328-19.html":
			{
				if (st.isCond(15))
				{
					st.setCond(16, true);
					htmltext = event;
				}
				break;
			}
			case "31527-02.html":
			{
				if (WELL_VAR[0] == 0)
				{
					final L2Npc umul_ghost = addSpawn(SOUL_OF_WELL, SOUL_WELL_LOC, true, 0);
					st.startQuestTimer("UMUL_GHOST", 1000 * 90, umul_ghost);
					st.startQuestTimer("DESPAWN_UMUL_GHOST", 1000 * 120, umul_ghost);
					umul_ghost.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
					playSound(player, QuestSound.SKILLSOUND_ANTARAS_FEAR);
					htmltext = event;
				}
				else
				{
					htmltext = "31527-03.html";
				}
				break;
			}
			case "UMUL_GHOST":
			{
				npc.setScriptValue(1);
				break;
			}
			case "DESPAWN_UMUL_GHOST":
			{
				if (!npc.isDead())
				{
					WELL_VAR[0] = 0;
				}
				npc.deleteMe();
				break;
			}
			case "31529-03.html":
			{
				if (st.isCond(9) && st.hasQuestItems(LETTER))
				{
					st.set("id", 8);
					htmltext = event;
				}
				break;
			}
			case "31529-08.html":
			{
				st.set("id", 9);
				htmltext = event;
				break;
			}
			case "31529-11.html":
			{
				st.giveItems(JEWEL1, 1);
				st.set("id", 10);
				st.setCond(10);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon)
	{
		final QuestState st = attacker.getQuestState(getName());
		
		if ((st != null))
		{
			if (st.hasQuestItems(JEWEL1))
			{
				if (st.getInt("id") == 10)
				{
					st.set("id", 11);
				}
				else if ((npc.getScriptValue() == 1) && st.isCond(10))
				{
					st.takeItems(JEWEL1, -1);
					st.giveItems(JEWEL2, 1);
					st.setCond(11, true);
					st.unset("id");
				}
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		if (npc.getId() == SOUL_OF_WELL)
		{
			WELL_VAR[0] = 0;
		}
		else
		{
			final QuestState st = killer.getQuestState(getName());
			if ((st != null) && st.hasQuestItems(CROSS) && !st.hasQuestItems(SKULL) && (getRandom(100) < 10))
			{
				st.giveItems(SKULL, 1);
				st.setCond(5, true);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker)
	{
		
		final QuestState st = talker.getQuestState(getName());
		String htmltext = getNoQuestMsg(talker);
		if (st == null)
		{
			return htmltext;
		}
		switch (st.getState())
		{
			case State.CREATED:
			{
				if (npc.getId() == TIFAREN)
				{
					htmltext = "31334-01.htm";
				}
				break;
			}
			case State.STARTED:
			{
				switch (npc.getId())
				{
					case TIFAREN:
					{
						switch (st.getCond())
						{
							case 3:
							case 1:
							{
								htmltext = "31334-05.html";
								break;
							}
							case 5:
							case 4:
							{
								if (hasQuestItems(talker, CROSS))
								{
									if (!hasQuestItems(talker, SKULL))
									{
										htmltext = "31334-09.html";
									}
									else if (hasQuestItems(talker, SKULL))
									{
										if (TIFAREN_VAR[0] == 0)
										{
											htmltext = "31334-10.html";
										}
										else
										{
											htmltext = "31334-11.html";
										}
									}
								}
								break;
							}
							case 6:
							case 7:
							{
								if (hasQuestItems(talker, CROSS))
								{
									if (TIFAREN_VAR[0] == 1)
									{
										if (TIFAREN_VAR[1] == talker.getObjectId())
										{
											htmltext = "31334-15.html";
										}
										else
										{
											htmltext = "31334-16.html";
											st.setCond(6);
										}
									}
									else
									{
										htmltext = "31334-17.html";
									}
								}
								break;
							}
							case 8:
							{
								if (hasQuestItems(talker, CROSS))
								{
									htmltext = "31334-18.html";
								}
								break;
							}
						}
						break;
					}
					case GHOST_OF_PRIEST:
					{
						playSound(talker, QuestSound.AMBSOUND_HORROR_15);
						if (npc.getScriptValue() == talker.getObjectId())
						{
							
							htmltext = "31528-01.html";
						}
						else
						{
							htmltext = "31528-03.html";
						}
						break;
					}
					case INNOCENTIN:
					{
						switch (st.getCond())
						{
							case 2:
							{
								if (!st.hasQuestItems(CROSS))
								{
									st.giveItems(CROSS, 1);
									st.setCond(3, true);
									htmltext = "31328-01.html";
								}
								break;
							}
							case 3:
							{
								if (st.hasQuestItems(CROSS))
								{
									htmltext = "31328-01b.html";
								}
								break;
							}
							case 8:
							{
								if (st.hasQuestItems(CROSS))
								{
									htmltext = "31328-02.html";
								}
								else
								{
									htmltext = "31328-04.html";
								}
								break;
							}
							case 9:
							{
								htmltext = "31328-09a.html";
								break;
							}
							case 14:
							{
								if (st.hasQuestItems(BOX))
								{
									htmltext = "31328-10.html";
								}
								break;
							}
							case 15:
							{
								htmltext = "31328-12.html";
								break;
							}
							case 16:
							{
								st.addExpAndSp(345966, 31578);
								st.exitQuest(false, true);
								if (talker.getLevel() >= MIN_LVL)
								{
									htmltext = "31328-20.html";
								}
								else
								{
									htmltext = "31328-21.html";
								}
								break;
							}
						}
						break;
					}
					case WELL:
					{
						switch (st.getCond())
						{
							case 10:
							{
								if (st.hasQuestItems(JEWEL1))
								{
									htmltext = "31527-01.html";
									playSound(talker, QuestSound.AMBSOUND_HORROR_01);
								}
								break;
							}
							case 12:
							{
								if (st.hasQuestItems(JEWEL2) && !st.hasQuestItems(SEALED_BOX))
								{
									st.giveItems(SEALED_BOX, 1);
									st.setCond(13, true);
									htmltext = "31527-04.html";
									
								}
								break;
							}
							case 13:
							case 14:
							case 15:
							case 16:
							{
								htmltext = "31527-05.html";
								break;
							}
						}
						break;
					}
					case GHOST_OF_ADVENTURER:
					{
						switch (st.getCond())
						{
							case 9:
							{
								if (st.hasQuestItems(LETTER))
								{
									if (st.getInt("id") == 0)
									{
										htmltext = "31529-01.html";
										
									}
									else if (st.getInt("id") == 8)
									{
										htmltext = "31529-03a.html";
									}
									else if (st.getInt("id") == 9)
									{
										htmltext = "31529-10.html";
									}
								}
								break;
							}
							case 10:
							{
								if (st.hasQuestItems(JEWEL1))
								{
									if (st.getInt("id") == 10)
									{
										htmltext = "31529-12.html";
									}
									else if (st.getInt("id") == 11)
									{
										htmltext = "31529-14.html";
									}
								}
								break;
							}
							case 11:
							{
								if (st.hasQuestItems(JEWEL2) && !st.hasQuestItems(SEALED_BOX))
								{
									htmltext = "31529-15.html";
									st.setCond(12, true);
								}
								break;
							}
							case 13:
							{
								if (st.hasQuestItems(JEWEL2) && st.hasQuestItems(SEALED_BOX))
								{
									st.giveItems(BOX, 1);
									st.takeItems(SEALED_BOX, -1);
									st.takeItems(JEWEL2, -1);
									st.setCond(14, true);
									htmltext = "31529-16.html";
								}
								break;
							}
							case 14:
							{
								if (st.hasQuestItems(BOX))
								{
									htmltext = "31529-17.html";
								}
								break;
							}
						}
						break;
					}
				}
				break;
			}
			case State.COMPLETED:
			{
				if (npc.getId() == TIFAREN)
				{
					htmltext = getAlreadyCompletedMsg(talker);
				}
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00022_TragedyInVonHellmannForest(22, Q00022_TragedyInVonHellmannForest.class.getSimpleName(), "Tragedy in Von Hellmann Forest");
	}
}