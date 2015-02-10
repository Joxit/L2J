L2J
===
This github repository contains three Java classes for the analysis of official L2 scripts.

---
Class Descriptions
----

> **HTMLCleaner:**
> 
> - Allows you to format a folder containing HTML files and stores the result in another folder. 
> - Removes duplicate spaces and newlines.
> - Add newline after `<br>` `<br1>`.
> - Removes `<head>` and `</head>`.
> - Add `</html>` if missing.

> **FreyaAiClean:** needs the file `ai-freya-symbol.txt`
> 
> - Allows you to find the name of all NPCs who are in the quest you want (`getQuestNpcs`).
> - Allows you to find the name of all items which are unsed in the quest you want (`getQuestItems`).
> - Allows you to rewrite parts of the file `ai-freya-symbol.txt` corresponding to specific NPCs. Then stores the results in a file, where each NPC will have its own file. (`aiByName`)

> **PchFinder: needs files *_pch.txt**
>  
> - Allows you to find the name or the id of any NPCs, items or quests. (`getNamesByIds` and `getIdsByNames`)
> - You can display the output in the form `name = id` in your terminal. (`printNamesAndIds`)
> - You can use FreyaAiClean methods to retrieve NPCs and items then use `printNamesAndIds` to display their ids.

---
Examples
---
Example of item_pch.txt <br>
[earing_of_strength]  =  114<br>
[earing_of_wisdom]  =  115<br>
[magic_ring]  =  116<br>
[ring_of_mana]  =  117<br>
[necklace_of_magic]  =  118<br>
[necklace_of_binding]  =  119<br>
[sword_of_reflexion]  =  120

```java
// Code
printNamesAndIds("item_pch.txt", "necklace_of_magic", "earing_of_wisdom", "120");
```
> **Output:**<br>
> earing_of_wisdom = 115<br> 
> necklace_of_magic = 118<br>
> sword_of_reflexion = 120<br>

```java
// Code
final String Q0025name = PchFinder.getNameById(freya_quest_pch, "25");
		System.out.println("The name of Q0025 is " + Q0025name);

		// print all items of Q0025
		System.out.println("All items of Q0025 : ");
		final ArrayList<String> items = FreyaAiClean.getItemsIdsByQuest(freya_aiScript, Q0025name);
		PchFinder.printNamesAndIds(freya_item_pch, items);

		// print all npc of Q0025
		System.out.println("All npcs of Q0025 : ");
		final ArrayList<String> npcs = FreyaAiClean.getQuestNpcs(freya_aiScript, Q0025name);
		PchFinder.printNamesAndIds(freya_npc_pch, npcs);

		// write on disk all ai for quest Q0025 in the folder Q0025_[name]
		System.out.println("Names of NPCs that I am writing on the disc : ");
		FreyaAiClean.aiByName(freya_aiScript, freya_srcipt_dir + "Q0025_" + Q0025name, npcs);
```

> **Output:**

> The name of Q0025 is man_behind_the_truth<br>

> **All items of Q0025 :** <br>
earing_of_blessing = 874<br>
ring_of_blessing = 905<br>
necklace_of_blessing = 936<br>
q_lost_map = 7063<br>
q_lost_contract = 7066<br>
q_ridias_dress = 7155<br>
q_triols_totem2 = 7156<br>
q_lost_jewel_key = 7157<br>
q_triols_totem3 = 7158<br>
<br>
> **All npcs of Q0025 :** <br>
triyol_zzolda = 1027218<br>
falsepriest_agripel = 1031348<br>
falsepriest_benedict = 1031349<br>
shadow_hardin = 1031522<br>
q_forest_stone2 = 1031531<br>
maid_of_ridia = 1031532<br>
broken_desk2 = 1031533<br>
broken_desk3 = 1031534<br>
broken_desk4 = 1031535<br>
q_forest_box1 = 1031536<br>
<br>
> **Names of NPCs that I am writing on the disc :** <br>
triyol_zzolda<br>
falsepriest_agripel<br>
falsepriest_benedict<br>
shadow_hardin<br>
q_forest_stone2<br>
maid_of_ridia<br>
broken_desk2<br>
broken_desk3<br>
broken_desk4<br>
q_forest_box1<br>