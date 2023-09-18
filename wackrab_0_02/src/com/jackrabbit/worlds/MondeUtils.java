package com.jackrabbit.worlds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class MondeUtils {

    
    public static class BizarreChunkGenerator extends ChunkGenerator {

    	
    	
    	
    	
    	
    	
    	//-----------------------------------  WTF GLITCH BUG
    	
    	
    	
    	
        @Override
        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            ChunkData chunkData = createChunkData(world);

            // Remplissez la chunkData avec les blocs que vous voulez, par exemple de l'obsidienne et de la roche.
            for (int y = 0; y < 256; y++) {
                for (int ix = 0; ix < 16; ix++) {
                    for (int iz = 0; iz < 16; iz++) {
                        chunkData.setBlock(ix, y, iz, Material.OBSIDIAN);
                    }
                }
            }

            // Ajoutez d'autres éléments bizarres à votre chunk ici, par exemple des structures de pierre aléatoires, des trous, etc.

            return chunkData;
        }

        @Override
        public List<BlockPopulator> getDefaultPopulators(World world) {
            // Supprimez les populators par défaut pour éviter les arbres, villages, etc.
            return null;
        }
    }
    public static class CustomChunkGenerator extends ChunkGenerator {

        @Override
        public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
            ChunkData chunkData = createChunkData(world);
            
            // Générer des blocs d'obsidienne et de roche aléatoires
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 128; y++) {
                        // Blocs d'obsidienne
                        if (random.nextInt(64) == 0) {
                            if (y == 0 || y == 1) {
                                chunkData.setBlock(x, y, z, Material.BEDROCK);
                            } else {
                                chunkData.setBlock(x, y, z, Material.OBSIDIAN);
                            }
                        }
                        
                        // Blocs de roche aléatoires
                        if (random.nextInt(32) == 0) {
                            chunkData.setBlock(x, y, z, Material.STONE);
                        }
                        
                        // Définir le biome souhaité
                        biome.setBiome(x, z, Biome.PLAINS);
                    }
                }
            }
            
            // Faites d'autres modifications spécifiques selon vos besoins
            
            return chunkData;
        }
    }
	
    public static World creerMondeCasseBizarreBugObsiRock1(String nomDuMonde) {

   	 	WorldCreator worldCreator = new WorldCreator(nomDuMonde);
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.generateStructures(false);
        
        // Générer un seul biome cohérent
        worldCreator.generator(new CustomChunkGenerator());
        
        // Créer le monde avec les paramètres requis
        World world = worldCreator.createWorld();
        
        // Configurer les caractéristiques spécifiques du monde
        world.setGameRuleValue("doFireTick", "true"); // Activer le feu
        world.setGameRuleValue("doMobSpawning", "false"); // Désactiver les mobs
        world.setGameRuleValue("doDaylightCycle", "false"); // Désactiver le cycle jour/nuit
        world.setWeatherDuration(999999); // La météo ne changera pas
        world.setTime(6000); // Définir l'heure du jour
        
        System.out.println("Generation bizarre ok");
        
        return world;
        
    }
    
    
    
    
    //------------------------------------  SAND ROCK
    
    
    
    
    
    public static World createMondePlatSable(String nomMonde) {
        WorldCreator creator = new WorldCreator(nomMonde);
        creator.generator(new MondePlatChunkGenerator());

        World world = creator.createWorld();
        world.setGameRuleValue("doMobSpawning", "false"); // Désactiver les mobs
        return world;
    }

    private static class MondePlatChunkGenerator extends ChunkGenerator {

        @Override
        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            ChunkData chunkData = createChunkData(world);

            for (int y = 0; y < 256; y++) {
                for (int ix = 0; ix < 16; ix++) {
                    for (int iz = 0; iz < 16; iz++) {
                    	if (y < 12) {
	                        // Remplissez la chunkData avec de la roche et du sable
	                        chunkData.setBlock(ix, y, iz, Material.STONE); // Roche
                    	}else if (y < 21) {
                            chunkData.setBlock(ix, y, iz, Material.SAND); // Sable sous la couche 64
                        }
                    }
                }
            }

            return chunkData;
        }

        @Override
        public List<BlockPopulator> getDefaultPopulators(World world) {
            // Si vous souhaitez ajouter des collines, vous pouvez utiliser un BlockPopulator personnalisé
            return null; // Vous pouvez ajouter des populators personnalisés ici si nécessaire
        }
    }
    
    
    
    
    
    
    
    
    //  - ----------------------------------	WORLD   3
    
    
    
    
    
    
         /*
         public static World createMondeMystique(String nomMonde) {

        	 WorldCreator creator = new WorldCreator(nomMonde);
             creator.generator(new MondeMystiqueChunkGenerator());

             World world = creator.createWorld();

             return world;
         }

         private static class MondeMystiqueChunkGenerator extends ChunkGenerator {

             @Override
             public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
                 ChunkData chunkData = createChunkData(world);

                 // Remplissez la chunkData avec de la roche
                 for (int y = 0; y < 256; y++) {
                     for (int ix = 0; ix < 16; ix++) {
                         for (int iz = 0; iz < 16; iz++) {
                             chunkData.setBlock(ix, y, iz, Material.STONE);
                         }
                     }
                 }

                 // Générez des éléments mystiques
                 populateMysticalElements(world, random, x * 16, z * 16);

                 return chunkData;
             }

             @Override
             public List<BlockPopulator> getDefaultPopulators(World world) {
                 return null; // Pas de populators par défaut
             }

             private void populateMysticalElements(World world, Random random, int x, int z) {
                 int centerX = x + 8;
                 int centerZ = z + 8;

                 // Générez des éléments mystiques ici, par exemple des diamants, blocs de diamants, arbres d'émeraudes, etc.

                 // Exemple : générer des diamants aléatoirement
                 for (int i = 0; i < 10; i++) {
                     int randX = centerX + random.nextInt(16) - 8;
                     int randY = random.nextInt(64) + 1;
                     int randZ = centerZ + random.nextInt(16) - 8;

                     world.getBlockAt(randX, randY, randZ).setType(Material.DIAMOND_ORE);
                 }

                 // Exemple : générer des blocs de diamants aléatoirement
                 for (int i = 0; i < 5; i++) {
                     int randX = centerX + random.nextInt(16) - 8;
                     int randY = random.nextInt(64) + 1;
                     int randZ = centerZ + random.nextInt(16) - 8;

                     world.getBlockAt(randX, randY, randZ).setType(Material.DIAMOND_BLOCK);
                 }

                 // Exemple : générer des arbres d'émeraudes
                 for (int i = 0; i < 2; i++) {
                     int randX = centerX + random.nextInt(16) - 8;
                     int randZ = centerZ + random.nextInt(16) - 8;

                     world.generateTree(world.getBlockAt(randX, world.getHighestBlockYAt(randX, randZ), randZ).getLocation(), TreeType.valueOf("TREE")); // Changez "TREE" selon le type de l'arbre que vous voulez
                 }

                 // Ajoutez d'autres éléments mystiques selon vos préférences
                 
                 // Exemple : générer des nuages de cuivre
                 if (random.nextDouble() < 0.005) { // Ajustez la probabilité selon vos besoins
                     int cloudHeight = world.getHighestBlockYAt(centerX, centerZ) + random.nextInt(10) + 10; // Hauteur entre y+10 et y+19
                     generateCopperCloud(world, centerX, cloudHeight, centerZ, random);
                 }
                 
                 
             }
             

             private void generateCopperCloud(World world, int x, int y, int z, Random random) {
                 int cloudRadius = random.nextInt(5) + 5; // Rayon entre 5 et 9
                 Material cloudMaterial = Material.COPPER_BLOCK;

                 for (int offsetX = -cloudRadius; offsetX <= cloudRadius; offsetX++) {
                     for (int offsetY = -cloudRadius; offsetY <= cloudRadius; offsetY++) {
                         for (int offsetZ = -cloudRadius; offsetZ <= cloudRadius; offsetZ++) {
                             if (offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ <= cloudRadius * cloudRadius) {
                                 world.getBlockAt(x + offsetX, y + offsetY, z + offsetZ).setType(cloudMaterial);
                             }
                         }
                     }
                 }
             }
              
         }
         
         
     
     private static int getRandomNumberInRange(int min, int max) {

 		if (min >= max) {
 			throw new IllegalArgumentException("max must be greater than min");
 		}

 		Random r = new Random();
 		return r.nextInt((max - min) + 1) + min;
 	}
	    
     
     
     */
     
     
     
     
     
     //   WORLD 4
     
     
     
     
     
     public static World mondeAmplified(String nm) {

    	 
    	 WorldCreator worldCreator = new WorldCreator(nm);
    	 worldCreator.environment(World.Environment.NORMAL);
    	 worldCreator.type(WorldType.AMPLIFIED);
    	 worldCreator.generateStructures(false); // Désactive les structures comme les villages
    	 
    	 
    	 World world = worldCreator.createWorld();
    	 
    	 
    	 world.getPopulators().clear(); // Supprime les générateurs de structure, comme les arbres

    	 
    	 return world;
 

    }
     
     
     
     
     //  world 5
     
     
     public static World creerMonde5(String name) {
    	  WorldCreator worldCreator = new WorldCreator(name);
          worldCreator.type(WorldType.FLAT); // Vous pouvez changer le type selon vos besoins
          worldCreator.environment(World.Environment.NORMAL); // Vous pouvez changer l'environnement si nécessaire
          worldCreator.generator(new CustomChunkGenerator5()); // Utilisez votre générateur de chunks personnalisé
          return worldCreator.createWorld();
     }
     
	
	public static class CustomChunkGenerator5 extends ChunkGenerator {
	
	    @Override
	    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
	        ChunkData chunkData = createChunkData(world);
	
	        // Choisissez un biome aléatoire
	        Biome chosenBiome = Biome.values()[random.nextInt(Biome.values().length)];
	
	        // Remplissez le chunk avec le biome choisi
	        for (int x = 0; x < 16; x++) {
	            for (int z = 0; z < 16; z++) {
	                biome.setBiome(x, z, chosenBiome);
	            }
	        }
	
	        return chunkData;
	    }
	
	    @Override
	    public boolean canSpawn(World world, int x, int z) {
	        return true;
	    }
	
	    @Override
	    public List<BlockPopulator> getDefaultPopulators(World world) {
	        return new ArrayList<BlockPopulator>();
	    }
	}

	
	
	
	
	
	// WORLD GIRLY 6 TEST
	
	
	
	/*
	
	
	public static World generateGirlyWorld(String name) {
		 WorldCreator worldCreator = new WorldCreator(name);
         worldCreator.type(WorldType.FLAT);
         worldCreator.environment(World.Environment.NORMAL);
         worldCreator.generator(new GirlyBiomeGenerator());
         return worldCreator.createWorld();
	}
	
	
	public static class GirlyBiomeGenerator extends ChunkGenerator {

		@Override
        public ChunkData generateChunkData(World world, Random random, int cx, int cz, BiomeGrid biome) {
			 ChunkData chunkData = createChunkData(world);

		        // Remplir le ChunkData avec des blocs roses
		        for (int x = 0; x < 16; x++) {
		            for (int z = 0; z < 16; z++) {
		                for (int y = 0; y < world.getMaxHeight(); y++) {
		                    chunkData.setBlock(x, y, z, Material.PINK_CONCRETE);
		                }
		            }
		        }


            return chunkData;
        }

		/*
        @Override
        public List<BlockPopulator> getDefaultPopulators(World world) {
            // Si vous souhaitez ajouter des collines, vous pouvez utiliser un BlockPopulator personnalisé
            return null; // Vous pouvez ajouter des populators personnalisés ici si nécessaire
        }
        
	    @Override
	    public List<BlockPopulator> getDefaultPopulators(World world) {
	        return new ArrayList<BlockPopulator>();
	    }

	    @Override
	    public boolean canSpawn(World world, int x, int z) {
	        return true;
	    }

	    @Override
	    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
	        ChunkData chunkData = createChunkData(world);

	        // Remplir le ChunkData avec des blocs roses
	        for (int x = 0; x < 16; x++) {
	            for (int z = 0; z < 16; z++) {
	                for (int y = 0; y < world.getMaxHeight(); y++) {
	                    chunkData.setBlock(x, y, z, Material.PINK_WOOL);
	                }
	            }
	        }

	        return chunkData;
	    }*/
	
	
	
	  /*  
	}
	
	
	
	*/
	
  public static void generateTower(World world, int x, int y, int z) {
	   int rand = (int)(Math.random()*12)+4;
	   
        // Générer une tour de 5 blocs de hauteur
        for (int i = 0; i < rand; i++) {
        	
            for (int j = 0; j < i; j++) {
            

	            world.getBlockAt(x+j, y + i, z).setType(Material.GLASS);
            }
            for (int j = 0; j < i; j++) {
	            

	            world.getBlockAt(x, y + i, z+j).setType(Material.GLASS);
            }
        }
    }
	 
    public static World createMondeVideAir(String nomMonde) {
    	
        WorldCreator creator = new WorldCreator(nomMonde);
        creator.generator(new MondeVideAirChunkGenerator());

        World world = creator.createWorld();
        world.setGameRuleValue("doMobSpawning", "false"); // Désactiver les mobs
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("time", "18000"); // 18000 représente le début de la nuit
        
        for(int z = 0; z < 20; z++) {
	        
	        Random random = new Random();
	        int x = random.nextInt(4000) - 2000; // Coordonnée x entre 50 et 150
	        int zz = random.nextInt(4000) - 2000; // Coordonnée z entre 50 et 150
	        int y = random.nextInt(70) + 20; // Coordonnée z entre 50 et 150
	
	        generateTower(world, x, y, zz);

        }
        
        return world;
    }

    private static class MondeVideAirChunkGenerator extends ChunkGenerator {
    	
	 
	   
        @Override
        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            ChunkData chunkData = createChunkData(world);

            for (int y = 0; y < 256; y++) {
                for (int ix = 0; ix < 16; ix++) {
                    for (int iz = 0; iz < 16; iz++) {
                    	if (y < 16) {
	                        // Remplissez la chunkData avec de la roche et du sable
	                        chunkData.setBlock(ix, y, iz, Material.AIR); // Roche
                    	}
                    }
                }
            }
            chunkData.setBlock(x, 100, z, Material.GLASS);

            
            return chunkData;
        }

        @Override
        public List<BlockPopulator> getDefaultPopulators(World world) {
            // Si vous souhaitez ajouter des collines, vous pouvez utiliser un BlockPopulator personnalisé
            return null; // Vous pouvez ajouter des populators personnalisés ici si nécessaire
        }
    }
    
	
	// --
    
    
    public static World CreateWorld6Chao(String  nn) {
    	  // Créer un nouveau monde avec CustomChunkGenerator
        WorldCreator worldCreator = new WorldCreator(nn);
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.generator(new CustomChunkGenerator6());
        World votreMonde = Bukkit.createWorld(worldCreator);
        return votreMonde;
    }
    public static class CustomChunkGenerator6 extends ChunkGenerator {

        @Override
        public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
            ChunkData chunk = createChunkData(world);

            // Utilisation de SimplexNoise pour une génération plus intéressante
            SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
            generator.setScale(0.005);

            // Génération chaotique
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int realX = chunkX * 16 + x;
                    int realZ = chunkZ * 16 + z;

                    int y = calculateHeight(generator, realX, realZ); // Utilisation de SimplexNoise pour la hauteur

                    // Génération de différents types de blocs chaotiques
                    chunk.setBlock(x, y+0, z, Material.STONE);
                    chunk.setBlock(x, y+1, z, Material.STONE);
                    chunk.setBlock(x, y+2, z, Material.STONE);
                    chunk.setBlock(x, y-1, z, Material.BEDROCK);
                }
            }

            return chunk;
        }

        private int calculateHeight(SimplexOctaveGenerator generator, int x, int z) {
            return (int) ((generator.noise(x, z, 0.5, 0.5) * 15) + 60);
        }
    }
	
	// ---------------------------------

    public static World CreateWorld7DesertOnly(String  nn) {
    	  // Créer un nouveau monde avec CustomChunkGenerator
        WorldCreator worldCreator = new WorldCreator(nn);
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.generator(new UniqueBiomeGenerator7());
        World votreMonde = Bukkit.createWorld(worldCreator);
        return votreMonde;
    }
    
    public static class UniqueBiomeGenerator7 extends ChunkGenerator {
        @Override
        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            ChunkData chunkData = createChunkData(world);
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    biome.setBiome(i, j, Biome.DESERT); // Définir le biome pour tout le chunk
                }
            }
            return chunkData;
        }
    }
    
    
	
	// level height test:!:
    
    
    
    public static World CreateWorldWaterLevel8(String  nn) {
  	  // Créer un nouveau monde avec CustomChunkGenerator
      WorldCreator worldCreator = new WorldCreator(nn);
      worldCreator.environment(World.Environment.NORMAL);
      worldCreator.generator(new CustomWaterLevelChunkGenerator());
      World votreMonde = Bukkit.createWorld(worldCreator);
      return votreMonde;
    }
    public static class CustomWaterLevelChunkGenerator extends ChunkGenerator {

        @Override
        public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biome) {
            ChunkData chunkData = createChunkData(world);

            int maxHeight= 55;
            
            // Réduire la hauteur de l'eau dans les océans
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                	int surfaceY = world.getHighestBlockYAt(chunkX * 16 + x, chunkZ * 16 + z);
                	//Block blockY = world.getHighestBlockAt(chunkX * 16 + x, chunkZ * 16 + z);
	                //if(blockY.getType() == Material.WATER)
                    for (int y = surfaceY; y > maxHeight; y--) {
                    	Block blockY = world.getBlockAt(chunkX * 16 + x, y, chunkZ * 16 + z);
                    	if(blockY.getType() == Material.WATER)
                    		chunkData.setBlock(x, y, z, Material.AIR); // Remplacez l'eau par de l'air
                    }
                }
            }

            return chunkData;
        }
    }
	
	
	
	
	
	
	//--------------------
	
    public static World creerMonde(String nomDuMonde) {
    	
    	// default
    	// Bukkit.createWorld(WorldCreator.name(nomDuMonde));
    	// 1
    	//return creerMondeCasseBizarreBugObsiRock1(nomDuMonde);
    	// 2
    	//return createMondePlatSable(nomDuMonde);
    	// not working 3
    	//return createMondeMystique(nomDuMonde);
    	// 4
    	// return mondeAmplified(nomDuMonde);
    	// 5 NOT WORKING 
    	// return creerMonde5(nomDuMonde);
    	// 6  OK  Stone 
    	// return CreateWorld6Chao(nomDuMonde); 
    	// test desert 7 ?
    	// return CreateWorld7DesertOnly(nomDuMonde);
    	
    	//return Bukkit.createWorld(WorldCreator.name(nomDuMonde));
    	
    	//return generateGirlyWorld(nomDuMonde);
    	//return mondeAmplified(nomDuMonde);
    	
    	
    	
    	
    	
    	
    	
    	
    	//return CreateWorldWaterLevel8(nomDuMonde);
		return Bukkit.createWorld(WorldCreator.name(nomDuMonde)); //Bukkit.createWorld(WorldCreator.name(nomDuMonde));
    	
    	
    	
    	
    	
    	
    	
    	
    	//return createMondeVideAir(nomDuMonde);
    	/*		
    	int r = getRandomNumberInRange(0, 2);
    	if(r == 0)  //defualt
    		return Bukkit.createWorld(WorldCreator.name(nomDuMonde));
    	else if (r == 1)
    		return creerMondeCasseBizarreBugObsiRock1(nomDuMonde);

    	//default
		return Bukkit.createWorld(WorldCreator.name(nomDuMonde));
    	 */
    }

    private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
   
    
    
}