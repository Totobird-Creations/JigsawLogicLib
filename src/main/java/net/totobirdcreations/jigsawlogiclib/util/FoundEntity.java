package net.totobirdcreations.jigsawlogiclib.util;


import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.totobirdcreations.jigsawlogiclib.logic.LogicBlockEntity;


public record FoundEntity(LogicBlockEntity entity, ServerWorld world, StructurePlacementData data) { }
