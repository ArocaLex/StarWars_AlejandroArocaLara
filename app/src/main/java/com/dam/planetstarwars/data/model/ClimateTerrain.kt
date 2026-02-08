package com.dam.planetstarwars.data.model

enum class Climate(val textoInterfaz: String) {
    ARID("Arid"),
    TEMPERATE("Temperate"),
    TROPICAL("Tropical"),
    FROZEN("Frozen"),
    HOT("Hot"),
    COLD("Cold"),
    MURKY("Murky");
}

enum class Terrain(val textoInterfaz: String) {
    DESERT("Desert"),
    GRASSLANDS("Grasslands"),
    MOUNTAINS("Mountains"),
    SWAMP("Swamp"),
    OCEAN("Ocean"),
    TUNDRA("Tundra"),
    ICE_CAVES("Ice caves");
}