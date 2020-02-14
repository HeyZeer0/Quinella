package net.heyzeer0.quinella.core.commands.enums

import net.heyzeer0.quinella.core.enums.Emoji

enum class CommandType(val emote: Emoji) {

    FUN(Emoji.COOKIE),
    INFORMATIVE(Emoji.BOOKMARK),
    MODERATION(Emoji.LOCK),
    MISCELLANEOUS(Emoji.DIAMOND)

}