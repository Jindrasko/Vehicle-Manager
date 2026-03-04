package cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics

import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill

class Statistics {

    companion object {
        fun getFuelCost(list: List<Refill>): Double {
            var result: Double = 0.0
            list.forEach {
                result += (it.totalCost ?: 0.0)
            }
            return result
        }

        fun getExpenseCostByCategory(list: List<Expense>, category: ExpenseCategory?): Double {
            var result: Double = 0.0
            list.forEach {
                if (category == it.expenseCategory){
                    result += ((it.costParts?: 0.0) + (it.costServices ?: 0.0))
                }
            }
            return result
        }

        fun getExpensesCost(list: List<Expense>): Double {
            var result: Double = 0.0
            list.forEach {
                result += ((it.costParts?: 0.0) + (it.costServices ?: 0.0))
            }
            return result
        }

        fun getTotalGas(list: List<Refill>): Double {
            var totalGas: Double = 0.0
            list.forEach {
                totalGas += it.volume ?: 0.0
            }
            return totalGas
        }

        fun getBestGasPrice(list: List<Refill>): Double {
            return list
                .filter { it.fuelCost != null }
                .minByOrNull { it.fuelCost!! }?.fuelCost ?: 0.0
        }

        fun  getWorstGasPrice(list: List<Refill>): Double {
            return list
                .filter { it.fuelCost != null }
                .maxByOrNull { it.fuelCost!! }?.fuelCost ?: 0.0
        }

        fun getAverageGasPrice(list: List<Refill>): Double {
            val filteredList: List<Refill> = list.filter { it.fuelCost != null }
            val average = filteredList.map { it.fuelCost!! }.average()
            return if (average.isNaN()) {
                0.0
            } else {
                average
            }
        }


        fun calculateAverageFuelConsumption(refills: List<Refill>): Double {
            if (refills.isEmpty()) {
                return 0.0
            }

            var totalConsumptionLiters = 0.0
            var totalDistanceKilometers = 0

            var lastFullRefillIndex: Int? = null

            var temporaryLiters = 0.0

            for (i in refills.indices) {
                val currentRefill = refills[i]

                if (currentRefill.previousMissed || currentRefill.volume == null) {
                    lastFullRefillIndex = null
                    temporaryLiters = 0.0
                }

                if (lastFullRefillIndex != null) {
                    temporaryLiters += currentRefill.volume!!
                }

                if (currentRefill.full && currentRefill.mileage != null && currentRefill.volume != null) {
                    if (lastFullRefillIndex == null) {
                        lastFullRefillIndex = i
                    } else {
                        totalDistanceKilometers += (currentRefill.mileage!! - refills[lastFullRefillIndex].mileage!!)
                        totalConsumptionLiters += temporaryLiters
                        lastFullRefillIndex = i
                        temporaryLiters = 0.0
                    }
                }

            }

            return if (totalConsumptionLiters > 0 && totalDistanceKilometers > 0) {
                ((totalConsumptionLiters * 100.0) / totalDistanceKilometers)
            } else {
                0.0
            }
        }


        fun getTotalDistance(list: List<Mileage>): Int {
            return if (list.isNotEmpty()) {
                list.last().mileage - list.first().mileage
            } else {
                0
            }
        }



    }


}