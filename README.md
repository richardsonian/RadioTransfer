# Radio Transfer
Radio Transfer is a Minecraft mod which allows players to build customizable, upgradeable radios which can wirelessly transfer items, fluids, power, and data.

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
- singleBlockFunctionalDemo: The core functionality of the mod working! Two blocks that can wirelessly transmit items with frequency and priority selection.
- multiblockDemo: a demo with placeholder textures and no functionality of a master/slave system for recognition of multiblock structures.

