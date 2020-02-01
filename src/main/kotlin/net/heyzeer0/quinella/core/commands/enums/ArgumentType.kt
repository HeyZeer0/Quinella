package net.heyzeer0.quinella.core.commands.enums

enum class ArgumentType {

    STRING,
    NUMBER, //{-INT_MAX, INT_MAX}
    USER, //@user | user | id
    ROLE, //@role | role | id
    TEXT_CHANNEL, //#channel | channel | id
    COLOR //#hex | 255, 255, 255

}