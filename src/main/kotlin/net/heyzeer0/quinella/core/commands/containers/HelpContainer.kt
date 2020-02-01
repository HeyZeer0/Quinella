package net.heyzeer0.quinella.core.commands.containers

data class HelpContainer(

    var pathName: String,
    var pathDescription: String,
    var arguments: List<String> = mutableListOf(),
    var parents: HashMap<String, HelpContainer> = HashMap()

)