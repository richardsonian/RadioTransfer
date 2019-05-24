# Radio Transfer
Radio Transfer is a Minecraft mod which allows players to build customizable, upgradeable multiblock radios which can wirelessly transfer items, fluids, power, and data.

## How to intitialize environment and run from the source code (Using IntelliJ IDEA)
_steps copied from the [Forge Documentation](https://mcforge.readthedocs.io/en/latest/gettingstarted/#terminal-free-intellij-idea-configuration) (Section: "Terminal-free IntelliJ IDEA configuration")_

- Clone this repo, and cd to the project directory
- Make sure you have an internet connection which won't block any websites that might be needed for initialization *cough* 
- Launch IntelliJ IDEA and choose to open/import the build.gradle file, using the default gradle wrapper choice for the project's JVM. Also make sure that you have "import modules as separate source sets" **un**checked. Wait for the gradle configuration to sync into intellij once the project is open.
- Open the gradle tab on the right side of IntelliJ. Run the _setupDecompWorkspace_ task (inside the forgegradle task group). It will take a few minutes, and use quite a bit of RAM. 
- Once the setup task is done, you will want to run the _genIntellijRuns_ task, which will configure the project’s run/debug targets.
After it’s done, you should click the blue refresh icon on the gradle panel (there’s another refresh icon on the main toolbar, but that’s not it). This will re-synchronize the IDEA project with the Gradle data, making sure that all the dependencies and settings are up to date.
Finally, assuming you use IDEA 2016 or newer, you will have to fix the classpath module. Go to Edit configurations and in both Minecraft Client and Minecraft Server, change Use classpath of module to point to the task _RadioTransfer.main_ .
If all the steps worked correctly, you should now be able to choose the Minecraft run tasks from the dropdown, and select "Minecraft Client" to run your code

## See our progress!
We've made branches at each of our milestones so you can see the major progression of the mod. So far the branches are:
- __Demo__: Learning how to write minecraft mods. Added a block, a few items, and a gui/inventory for the block. Wrote a lot of framework for the rest of the mod.
- __singleBlockFunctionalDemo__: The core functionality of the mod working! Two blocks that can wirelessly transmit items with frequency and priority selection.
- __multiblockDemo__: a demo with placeholder textures and no functionality of a master/slave system for recognition of multiblock structures.
- __working-for-pick-no-power__: Everthing working that we wanted to accomplish in our stretch stretch goal! See the "stretch goal" release for more info.

## For Mr. Poles to play (aka for n00bs)
1. Make a new single player world with creative mode
2. Press E to open the creative mode inventory
3. Navigate to the miscellaneous tab
### Demo
4. Find the Demo Block and place it anywhere in the world. It has a basic inventory that you can store items in, and that's about it. We also added a few items, but they aren't listed in the creative inventory.
### singleBlockFunctionalDemo
4. Find the Transmitter and Receiver blocks and place them in the world. Now you may interact with their frequencies and priority, as well as (of course) a fully functioning transmitter/receiver communications framework (Ask if you're interested how it works).
5. Put something in the transmitter, and watch the magic send it to the receiver(s). Make sure that they're both activated, and on the right frequency.
### multiblockDemo
4. Drag the radio, tx controller, rx controller, encoder, and decoder blocks into your inventory from the misc tab
5. Place all of the blocks adjacent to each other (in no particular order)
6. The radio block (the master) will recognize and register only one of each node block, and deregister them if they are detatched

## Current progress on master as of 5/3
6. This multiblock system is a radio setup, and now you may place items in the encoder, which sends them to the transmitter, whose settings can be adjusted in the gui. The reverse of this process occurs on the receiver/decoder side. Partly implemented, and mostly being debugged right now. If you put an item somewhere, something might happen. Right clicking on a block may or may not open a nice gui. Clicking the buttons in the gui will probably do something
