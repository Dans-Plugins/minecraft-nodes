package phonon.nodes

/**
 * Pays one town's territory income. Generic over the town and item types
 * so the split can be tested without a running server.
 *
 * Each territory is given as its occupier (null when unoccupied) and its
 * income rates. An occupied territory pays `taxRate` of its income to the
 * occupier and the rest to `owner`; an unoccupied one pays all of it to
 * `owner`. Each receiving town's total is scaled by its own
 * `incomeModifier`, converted with `rateToAmount`, and handed to `pay`
 * under that receiving town.
 */
internal fun <T : Any, M : Any> payTerritoryIncome(
    owner: T,
    territories: Iterable<Pair<T?, Map<M, Double>>>,
    taxRate: Double,
    incomeModifier: (T) -> Double,
    rateToAmount: (Double) -> Int,
    pay: (T, M, Int) -> Unit,
) {
    val keptRate = 1.0 - taxRate
    val ownerIncome = LinkedHashMap<M, Double>()
    val townIncomes = LinkedHashMap<T, LinkedHashMap<M, Double>>()
    townIncomes[owner] = ownerIncome

    for ((occupier, income) in territories) {
        if (occupier != null) {
            val occupierIncome = townIncomes.getOrPut(occupier) { LinkedHashMap() }
            for ((item, amount) in income) {
                occupierIncome[item] = (occupierIncome[item] ?: 0.0) + (amount * taxRate)
                ownerIncome[item] = (ownerIncome[item] ?: 0.0) + (amount * keptRate)
            }
        } else {
            for ((item, amount) in income) {
                ownerIncome[item] = (ownerIncome[item] ?: 0.0) + amount
            }
        }
    }

    for ((payee, income) in townIncomes) {
        val modifier = incomeModifier(payee)
        for ((item, amount) in income) {
            val amountInt = rateToAmount(amount * modifier)
            if (amountInt > 0) {
                pay(payee, item, amountInt)
            }
        }
    }
}
