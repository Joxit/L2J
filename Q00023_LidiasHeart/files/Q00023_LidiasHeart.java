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
package quests.Q00023_LidiasHeart;

import quests.Q00022_TragedyInVonHellmannForest.Q00022_TragedyInVonHellmannForest;
import quests.Q00024_InhabitantsOfTheForestOfTheDead.Q00024_InhabitantsOfTheForestOfTheDead;

import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.serverpackets.NpcSay;

/**
 * Lidia's Heart (23)
 * @author Joxit
 */
public class Q00023_LidiasHeart extends Quest
{
	// NPCs
	private static final int INNOCENTIN = 31328;
	private static final int VIOLET = 31386;
	private static final int TOMBSTONE = 31523;
	private static final int HELLMANN = 31524;
	private static final int BOOKSHELF = 31526;
	private static final int BOX = 31530;
	// Items
	private static final int MAP = 7063;
	private static final int KEY = 7149;
	private static final int HAIRPIN = 7148;
	private static final int DIARY = 7064;
	private static final int SPEAR = 7150;
	// Misc
	private static final int MIN_LVL = 64;
	private static int GHOST_VAR;
	private static final Location GHOST_LOC = new Location(51432, -54570, -3136);
	
	public Q00023_LidiasHeart(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(INNOCENTIN);
		addTalkId(INNOCENTIN, VIOLET, TOMBSTONE, HELLMANN, BOOKSHELF, BOX);
		registerQuestItems(KEY, DIARY, SPEAR);
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
			case "31524-03.html":
			case "31526-09.html":
			case "31526-07a.html":
			case "31526-05.html":
			case "31526-04.html":
			case "31328-18.html":
			case "31328-17.html":
			case "31328-16.html":
			case "31328-11.html":
			case "31328-10.html":
			case "31328-06.html":
			case "31328-05.html":
			{
				htmltext = event;
				break;
			}
			case "31328-03.html":
			{
				final QuestState qs = player.getQuestState(Q00022_TragedyInVonHellmannForest.class.getSimpleName());
				if ((player.getLevel() >= MIN_LVL) && (qs != null) && qs.isCompleted())
				{
					st.startQuest();
					st.giveItems(MAP, 1);
					st.giveItems(KEY, 1);
					htmltext = event;
				}
				else
				{
					htmltext = "31328-02.htm";
				}
				break;
			}
			case "31328-07.html":
			{
				if (st.isCond(1))
				{
					st.setCond(2, true);
					htmltext = event;
				}
				break;
			}
			case "31328-12.html":
			{
				if (st.isCond(4) || (st.getInt("id") == 6))
				{
					st.set("id", 6);
					st.setCond(5, true);
					htmltext = event;
				}
				break;
			}
			case "31328-13.html":
			{
				if (st.isCond(4) || (st.getInt("id") == 6))
				{
					st.set("id", 7);
					htmltext = event;
				}
				break;
			}
			case "31328-19.html":
			{
				playSound(player, QuestSound.AMBSOUND_MT_CREAK);
				htmltext = event;
				break;
			}
			case "31328-20.html":
			{
				if (st.getInt("id") == 7)
				{
					st.setCond(6, true);
					st.unset("id");
					htmltext = event;
				}
				break;
			}
			case "31328-21.html":
			{
				st.setCond(5, true);
				htmltext = event;
				break;
			}
			case "31526-02.html":
			{
				if (st.isCond(3) && st.hasQuestItems(KEY))
				{
					st.takeItems(KEY, -1);
					htmltext = event;
				}
				break;
			}
			case "31526-06.html":
			{
				if (!st.hasQuestItems(HAIRPIN))
				{
					st.giveItems(HAIRPIN, 1);
					if (st.hasQuestItems(DIARY))
					{
						st.setCond(4, true);
					}
					htmltext = event;
				}
				break;
			}
			case "31526-08.html":
			{
				playSound(player, QuestSound.ITEMSOUND_ARMOR_LEATHER);
				htmltext = event;
				break;
			}
			case "31526-10.html":
			{
				playSound(player, QuestSound.AMBSOUND_EG_DRON);
				htmltext = event;
				break;
			}
			case "31526-11.html":
			{
				if (!st.hasQuestItems(DIARY))
				{
					st.giveItems(DIARY, 1);
					if (st.hasQuestItems(HAIRPIN))
					{
						st.setCond(4, true);
					}
					htmltext = event;
				}
				break;
			}
			case "31524-02.html":
			{
				playSound(player, QuestSound.CHRSOUND_MHFIGHTER_CRY);
				htmltext = event;
				break;
			}
			case "31524-04.html":
			{
				if (st.isCond(6))
				{
					st.takeItems(DIARY, -1);
					st.setCond(7, true);
					htmltext = event;
				}
				break;
			}
			case "31523-02.html":
			{
				if (st.isCond(6) || st.isCond(7))
				{
					if (GHOST_VAR == 0)
					{
						GHOST_VAR = 1;
						final L2Npc ghost1 = addSpawn(HELLMANN, GHOST_LOC, true, 0);
						st.startQuestTimer("DESPAWN_GHOST1", 1000 * 300, ghost1);
						ghost1.broadcastPacket(new NpcSay(ghost1.getObjectId(), 0, ghost1.getId(), NpcStringId.WHO_AWOKE_ME));
						playSound(player, QuestSound.SKILLSOUND_HORROR_02);
						htmltext = event;
					}
					else
					{
						playSound(player, QuestSound.SKILLSOUND_HORROR_02);
						htmltext = "31523-03.html";
					}
				}
				break;
			}
			case "DESPAWN_GHOST1":
			{
				GHOST_VAR = 0;
				npc.deleteMe();
				break;
			}
			case "31523-06.html":
			{
				if (st.isCond(7))
				{
					st.giveItems(KEY, 1);
					st.setCond(8, true);
					htmltext = event;
				}
				break;
			}
			case "31530-02.html":
			{
				if (st.isCond(9) && st.hasQuestItems(KEY))
				{
					st.takeItems(KEY, -1);
					st.giveItems(SPEAR, 1);
					st.setCond(10, true);
					htmltext = event;
				}
				break;
			}
		}
		return htmltext;
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
				if (npc.getId() == INNOCENTIN)
				{
					final QuestState qs = talker.getQuestState(Q00022_TragedyInVonHellmannForest.class.getSimpleName());
					if ((qs != null) && qs.isCompleted())
					{
						htmltext = "31328-01.htm";
					}
					else
					{
						htmltext = "31328-01a.html";
					}
				}
				break;
			}
			case State.STARTED:
			{
				switch (npc.getId())
				{
					case INNOCENTIN:
					{
						switch (st.getCond())
						{
							case 1:
							{
								htmltext = "31328-04.html";
								break;
							}
							case 2:
							{
								htmltext = "31328-08.html";
								break;
							}
							case 4:
							{
								htmltext = "31328-09.html";
								break;
							}
							case 5:
							{
								switch (st.getInt("id"))
								{
									case 6:
									{
										htmltext = "31328-14.html";
										break;
									}
									case 7:
									{
										htmltext = "31328-15.html";
										break;
									}
								}
								break;
								
							}
							case 6:
							{
								htmltext = "31328-22.html";
								break;
							}
						}
						break;
					}
					case BOOKSHELF:
					{
						switch (st.getCond())
						{
							case 2:
							{
								if (st.hasQuestItems(KEY))
								{
									st.setCond(3, true);
									htmltext = "31526-01.html";
								}
								break;
							}
							case 3:
							{
								if (!st.hasQuestItems(KEY))
								{
									final boolean hasPin = st.hasQuestItems(HAIRPIN);
									final boolean hasDiary = st.hasQuestItems(DIARY);
									if (!hasPin && !hasDiary)
									{
										htmltext = "31526-03.html";
									}
									else if (hasPin)
									{
										htmltext = "31526-07.html";
									}
									else if (hasDiary)
									{
										htmltext = "31526-12.html";
									}
								}
								break;
							}
							case 4:
							{
								if (st.hasQuestItems(HAIRPIN) && st.hasQuestItems(DIARY))
								{
									htmltext = "31526-13.html";
								}
								break;
							}
						}
						break;
					}
					case HELLMANN:
					{
						switch (st.getCond())
						{
							case 6:
							{
								htmltext = "31524-01.html";
								break;
							}
							case 7:
							{
								htmltext = (st.hasQuestItems(KEY) ? "31524-06.html" : "31524-05.html");
								break;
							}
							case 8:
							{
								if (st.hasQuestItems(KEY))
								{
									htmltext = "31524-06.html";
								}
								break;
							}
						}
						break;
					}
					case TOMBSTONE:
					{
						switch (st.getCond())
						{
							case 6:
							{
								htmltext = "31523-01.html";
								break;
							}
							case 7:
							{
								htmltext = "31523-04.html";
								break;
							}
							case 8:
							{
								htmltext = "31523-05.html";
								break;
							}
						}
						break;
					}
					case VIOLET:
					{
						switch (st.getCond())
						{
							case 8:
							{
								if (st.hasQuestItems(KEY))
								{
									st.setCond(9, true);
									htmltext = "31386-01.html";
								}
								break;
							}
							case 10:
							{
								if (!st.hasQuestItems(SPEAR))
								{
									htmltext = "31386-02.html";
								}
								else
								{
									st.giveAdena(350000, true);
									st.addExpAndSp(456893, 42112);
									st.exitQuest(false, true);
									htmltext = "31386-03.html";
								}
								break;
							}
						}
						break;
					}
					case BOX:
					{
						switch (st.getCond())
						{
							case 9:
							{
								if (st.hasQuestItems(KEY))
								{
									htmltext = "31530-01.html";
								}
								break;
							}
							case 10:
							{
								if (st.hasQuestItems(SPEAR))
								{
									htmltext = "31530-03.html";
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
				if (npc.getId() == INNOCENTIN)
				{
					htmltext = getAlreadyCompletedMsg(talker);
				}
				else if (npc.getId() == VIOLET)
				{
					final QuestState qs = talker.getQuestState(Q00024_InhabitantsOfTheForestOfTheDead.class.getSimpleName());
					if ((qs == null) || !qs.isCompleted())
					{
						htmltext = "31386-04.html";
					}
				}
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00023_LidiasHeart(23, Q00023_LidiasHeart.class.getSimpleName(), "Lidia's Heart");
	}
}