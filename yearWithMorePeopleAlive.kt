data class Life(val birth: Int, val death: Int)

fun main() {
	val lifes: ArrayList<Life> = arrayListOf(
        Life(2003, 2008), Life(2000, 2018), Life(1900, 2018), Life(1990, 2005),
        Life(2003, 2004), Life(2004, 2018), Life(0, 33), Life(1980, 2015)
    )
    
    var lifetime = 0
    
    lifes.forEachValid { 
        lifetime = (lifetime + (it.death - it.birth))/2
    }
    
	val yearWithMorePeople = if(lifetime < kotlin.math.log2(lifes.size.toDouble())) {
     	unorderedSolution(lifes)   
    } else {
        orderedSolution(lifes)
    }
    
    println("Year with more alive people: $yearWithMorePeople")
}

/**
 * Complexity: O(n*q), where q is the average lifetime
 **/
fun unorderedSolution(lifes: ArrayList<Life>): Int {
    val peopleAlive: HashMap<Int, Int> = hashMapOf() // List<Year, CountPeopleAlive>
    
    lifes.forEachValid {
        // For each year between birth and death
        for(year in it.birth .. it.death) {
            // If peopleAlive not have a entry with given year, peopleAlive[year] = 1. Else, adds 1
            peopleAlive[year] = peopleAlive[year]?.plus(1) ?: 1
        }
    }
    
    var moreAliveYear: Int = peopleAlive.keys.first()
    var countPeopleAlive: Int = peopleAlive[moreAliveYear]!!
    
    peopleAlive.forEach { year, count ->
        if(count > countPeopleAlive) {
            moreAliveYear = year
            countPeopleAlive = count
        }
    }
    
    return moreAliveYear
}

/**
 * Complexity: O(n*log(n))
 * */
fun orderedSolution(lifes: ArrayList<Life>): Int {
    val alive: ArrayList<Pair<Int, Int>> = arrayListOf()
    var alivePeople = 0
    var aliveYear = 0
    var maxAlivePeople = 0
    
    // Creating a list with year and int that represents the number of birth (+1) or death(-1)
    //  for each life entry
    lifes.forEachValid {
        alive.add(Pair(it.birth, 1))
        alive.add(Pair(it.death+1, -1))
    }
    
    // Ordering by year
    alive.sortBy({ it.component1() })
    
    // Iterating year by year to determinate the max people alive on a determined year
    alive.forEach {
        alivePeople += it.component2()
        if(alivePeople > maxAlivePeople) {
            maxAlivePeople = alivePeople
            aliveYear = it.component1()
        }
    }
    
    return aliveYear
}


private inline fun List<Life>.forEachValid(block: (life: Life) -> Unit) {
    this.forEach{
        if(it.death < it.birth){
            println("Invalid input.")
        } else {
            block(it)
        }
    }
}
