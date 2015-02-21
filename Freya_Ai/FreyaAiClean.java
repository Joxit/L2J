/*
 * Copyright (C) 2013-2015 Joxit
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Joxit
 */
public class FreyaAiClean {
	final static String freya_srcipt_dir = "freya_scripts";
	final static String freya_aiScript = freya_srcipt_dir + File.separator + "ai-freya-symbol.txt";
	final static String freya_npc_pch = freya_srcipt_dir + File.separator + "npc_pch.txt";
	final static String freya_quest_pch = freya_srcipt_dir + File.separator + "quest_pch.txt";
	final static String freya_item_pch = freya_srcipt_dir + File.separator + "item_pch.txt";
	final static String freya_html_dir = freya_srcipt_dir + File.separator + "html-en";

	public static void main(final String args[]) throws IOException {
		final String Q0025name = PchFinder.getNameById(freya_quest_pch, "25");
		System.out.println("The name of Q0025 is " + Q0025name);

		// print all items of Q0025
		System.out.println("All items of Q0025 : ");
		final ArrayList<String> items = FreyaAiClean.getQuestItems(freya_aiScript, Q0025name);
		PchFinder.printNamesAndIds(freya_item_pch, items);

		// print all npc of Q0025
		System.out.println("All npcs of Q0025 : ");
		final ArrayList<String> npcs = FreyaAiClean.getQuestNpcs(freya_aiScript, Q0025name);
		PchFinder.printNamesAndIds(freya_npc_pch, npcs);

		// write on disk all ai for quest Q0025 in the folder Q0025_[name]
		System.out.println("Names of NPCs that I am writing on the disc : ");
		FreyaAiClean.aiByName(freya_aiScript, freya_srcipt_dir + "Q0025_" + Q0025name, npcs);
	}

	/**
	 * Write all class of freya ai in separate files.
	 * 
	 * @param freya_aiScript path of the freya ai script
	 * @param save_dir directory where we save files
	 * @param name of all classes of ai
	 */
	public static void aiByName(final String freya_aiScript, final String save_dir,
			final ArrayList<String> name) throws IOException {
		final FileReader r = new FileReader(freya_aiScript);
		String buff = "";
		char c = (char) r.read();
		int statut = -1;
		while (r.ready()) {
			if (((c == ' ') || (c == '\n') || (c == '>')) && !buff.equals(" ")) {
				if ((statut == -1) && buff.endsWith("class")) {
					statut = 1;
				}
				if ((statut == 1) && buff.endsWith("0")) {
					statut = 2;
				}
				if ((statut == 2) && name.contains(buff)) {
					System.out.println(buff);
					final FileWriter w = new FileWriter(save_dir + File.separator + buff + ".c");

					w.write(buff);
					int parent = 0;
					while (r.ready() && (parent == 0)) {
						c = (char) r.read();
						w.write(c);
						if (c == '{') {
							parent++;
						}
					}
					while (r.ready() && (parent != 0)) {
						c = (char) r.read();
						if (c == ' ') {
							c = (char) r.read();
							if (c == ' ') {
								w.write('\t');
							} else {
								w.write(" " + c);
							}
						} else {
							w.write(c);
						}
						if (c == '{') {
							parent++;
						} else if (c == '}') {
							parent--;
						}
						w.flush();
					}
					w.write('\n');
					statut = -1;
					w.close();
				}
				buff = "";
			} else if (buff.equals(" ")) {
				buff = "";
			} else {
				buff = buff + c;
			}
			c = (char) r.read();
		}
		r.close();
	}

	/**
	 * Give all the npc in relationship with this quest name from the Freya AI
	 * and save their scripts in a special folder.
	 *
	 * @param freya_aiScript path of the freya ai script
	 * @param save path where we will save all files (folder like Q[id]_[name]
	 *            for exemple)
	 * @param quest name of the quest (same as quest_pch)
	 * @throws IOException
	 */
	public static void aiByQuest(final String freya_aiScript, final String save, final String quest)
			throws IOException {
		final ArrayList<String> npcs = getQuestNpcs(freya_aiScript, quest);
		final File dir = new File(save);
		if (!dir.mkdir() && !dir.exists()) {
			System.out.println("File " + save + " does not exist");
			return;
		}
		aiByName(freya_aiScript, save, npcs);
	}

	/**
	 * Give all the npc IDs in relationship with this quest name. 
	 * @param freya_aiScript path of the freya ai script
	 * @param quest
	 * @throws IOException
	 */
	public static ArrayList<String> getQuestItems(final String freya_aiScript, final String quest)
			throws IOException {
		return getQuestItems(freya_aiScript, quest, null);
	}

	/**
	 * Give all the npc IDs in relationship with this quest name. 
	 * @param freya_aiScript path of the freya ai script
	 * @param quest
	 * @param questId
	 * @throws IOException
	 */
	public static ArrayList<String> getQuestItems(final String freya_aiScript, final String quest,
			final String questId) throws IOException {
		final FileReader r = new FileReader(freya_aiScript);
		String buff = "";
		final ArrayList<String> items = new ArrayList<String>();
		char c = (char) r.read();
		int statut = -1;
		/* on cherche tous les items de la quete */
		while (r.ready()) {
			if (((c == ' ') || (c == '\n') || (c == '>')) && !buff.equals(" ")) {
				if (statut == 2) {
					int parent = 0;
					while (r.ready() && (parent == 0)) {
						c = (char) r.read();
						if (c == '{') {
							parent++;
						}
					}
					int parent2 = -1;
					while (r.ready() && (parent != 0)) {
						c = (char) r.read();
						if ((c == '\n')) {
							if ((buff.contains(quest) && (buff.contains("gg::HaveMemo") || buff
									.contains("myself::SetCurrentQuestID")))
									|| buff.contains("if( ask == " + questId + " )")) {
								parent2 = 0;
							} else if ((parent2 != -1) && buff.contains("Item")
									&& !buff.contains("ItemSound")) {
								final String res = buff.replaceAll(".*@([\\w_\\d]*).*", "$1")
										.replaceAll("[\n\r ]", "");
								if (!res.isEmpty() && !items.contains(res)) {
									items.add(res);
								}
							}
							buff = "";
						} else if (c == '{') {
							if (parent2 != -1) {
								parent2++;
							}
							parent++;
						} else if (c == '}') {
							if (parent2 == 1) {
								parent2 = -1;
							} else if (parent2 != -1) {
								parent2--;
							}
							parent--;
						}
						if (buff.equals(" ")) {
							buff = "";
						}

						buff = buff + c;
					}
					statut = -1;
				}
				if ((statut == -1) && buff.endsWith("class")) {
					statut = 1;
				}
				if ((statut == 1) && buff.endsWith("0")) {
					statut = 2;
				}
				buff = "";
			} else if (buff.equals(" ")) {
				buff = "";
			} else {
				buff = buff + c;
			}
			c = (char) r.read();
		}
		r.close();
		statut = 0;
		return items;
	}

	/**
	 * Search all npcs of a quest. is known that a NPC is in a quest when 
	 * there is gg::HaveMemo( player, @quest) in his script.
	 * 
	 * @param freya_aiScript path of the freya ai script
	 * @param quest where we want all npcs
	 * @return ArrayList of all npcs
	 */
	public static ArrayList<String> getQuestNpcs(final String freya_aiScript, final String quest)
			throws IOException {
		final FileReader r = new FileReader(freya_aiScript);
		String buff = "", name = "";
		final ArrayList<String> npcs = new ArrayList<String>();
		char c = (char) r.read();
		int statut = -1;
		/* on cherche tous les npcs de la quete */
		while (r.ready()) {
			if (((c == ' ') || (c == '\n') || (c == '>')) && !buff.equals(" ")) {
				if (statut == 2) {
					name = buff;
					int parent = 0;
					while (r.ready() && (parent == 0)) {
						c = (char) r.read();
						if (c == '{') {
							parent++;
						}
					}
					String line = "";
					while (r.ready() && (parent != 0)) {
						c = (char) r.read();
						line += c;
						if ((c == ' ') || (c == '\n')) {
							if (buff.endsWith(quest) && line.contains("gg::HaveMemo")) {
								npcs.add(name);
								break;
							}
							if (c == '\n') {
								line = "";
							}
							buff = "";
						} else if (c == '{') {
							parent++;
						} else if (c == '}') {
							parent--;
						}
						if (buff.equals(" ")) {
							buff = "";
						}

						buff = buff + c;
					}
					statut = -1;
				}
				if ((statut == -1) && buff.endsWith("class")) {
					statut = 1;
				}
				if ((statut == 1) && buff.endsWith("0")) {
					statut = 2;
				}
				buff = "";
			} else if (buff.equals(" ")) {
				buff = "";
			} else {
				buff = buff + c;
			}
			c = (char) r.read();
		}
		r.close();
		statut = 0;
		return npcs;
	}
}
