package net.heyzeer0.quinella.core.commands.enums

enum class Emoji(val discord: String, val unicode: String?) {

    ERROR(":heavy_multiplication_x:", "\u2716"),
    ERROR2(":x:", "\u274C"),
    DICE(":game_die:", "\uD83C\uDFB2"),
    SAD(":frowning:", "\uD83D\uDE26"),
    CORRECT(":white_check_mark:", "\u2705"),
    OK(":ok_hand:", "\uD83D\uDC4C"),
    STOP(":octagonal_sign:", "\uD83D\uDED1"),
    TALKING(":speech_balloon:", "\uD83D\uDCAC"),
    CRYING(":sob:", "\uD83D\uDE2D"),
    WARNING(":warning:", "\u26a0"),
    POPPER(":tada:", "\uD83C\uDF89"),
    ZAP(":zap:", "\u26a1"),
    MEGA(":mega:", "\uD83D\uDCE3"),
    CONFUSED(":confused:", "\uD83D\uDE15"),
    WORRIED(":worried:", "\uD83D\uDE1F"),
    THINKING(":thinking:", "\uD83E\uDD14"),
    STOPWATCH(":stopwatch:", "\u23f1"),
    BUY(":inbox_tray:", "\uD83D\uDCE5"),
    SELL(":outbox_tray:", "\uD83D\uDCE4"),
    MARKET(":shopping_car:", "\uD83D\uDED2"),
    MONEY(":moneybag:", "\uD83D\uDCB0"),
    PENCIL(":pencil:", "\uD83D\uDCDD"),
    SMILE(":smile:", "\uD83D\uDE04"),
    PICK(":pick:", "\u26cf"),
    HEART(":heart:", "\u2764"),
    RUNNER(":runner:", "\uD83C\uDFC3"),
    POTION1(":milk:", "\uD83E\uDD5B"),
    POTION2(":champagne:", "\uD83C\uDF7E"),
    CREDIT_CARD(":credit_card:", "\uD83D\uDCB3"),
    POUCH(":pouch:", "\uD83D\uDC5D"),
    REP(":military_medal:", "\uD83C\uDF96"),
    MAGAZINE(":newspaper:", "\uD83D\uDCF0"),
    AXE(":hammer_pick:", "\u2692"),
    DOLLAR(":dollar:", "\uD83D\uDCB5"),
    WOOD(":bamboo:", "\uD83C\uDF8D"),
    EYES(":eyes:", "\uD83D\uDC40"),
    PENNY(":cd:", "\uD83D\uDCBF"),
    RING(":ring:", "\uD83D\uDC8D"),
    WIND(":wind_blowing_face:", "\uD83C\uDF2C"),
    BOOSTER(":runner:", "\uD83C\uDFC3"),
    JOY(":joy:", "\uD83D\uDE02"),
    CROSSED_SWORD(":crossed_sword:", "\u2694"),
    MAG(":mag_right:", "\uD83D\uDD0E"),
    KEY(":key:", "\uD83D\uDD11"),
    DOG(":dog:", "\uD83D\uDC36"),
    DOOR(":door:", "\uD83D\uDEAA"),
    LOVE_LETTER(":love_letter:", "\uD83D\uDC8C"),
    NECKLACE(":prayer_beads:", "\uD83D\uDCFF"),
    DIAMOND(":gem:", "\uD83D\uDC8E"),
    TUXEDO(":man_in_tuxedo:", "\uD83E\uDD35"),
    DRESS(":dress:", "\uD83D\uDC57"),
    COOKIE(":cookie:", "\uD83C\uDF6A"),
    CHOCOLATE(":chocolate_bar:", "\uD83C\uDF6B"),
    CLOTHES(":shirt:", "\uD83D\uDC55"),
    SHOES(":athletic_shoe:", "\uD83D\uDC5F"),
    ROSE(":rose:", "\uD83C\uDF39"),
    PARTY(":tada:", "\uD83C\uDF89"),
    ANGER(":anger:", null),
    PING_PONG(":ping_pong:", null),
    WRENCH(":wrench:", null),
    BEGINNER(":beginner:", null),
    BOOKMARK(":bookmark:", null),
    LOCK(":lock:", null),
    MUSIC(":musical_note:", null),
    STAR(":star:", "\u2b50"),

    ANIM_CORRECT("<a:animated_check_mark:565648828435398708>", null),
    QUINELLA_THINK("<:quinellaThink:673972230488719360>", null),
    QUINELLA_TREE("<:quinellaTree:673974438923993109>", null),
    EMPTY("<:empty:621781910653370380>", null);

    override fun toString():String {
        return discord
    }

    operator fun plus(s: String): String {
        return "$discord $s"
    }

}