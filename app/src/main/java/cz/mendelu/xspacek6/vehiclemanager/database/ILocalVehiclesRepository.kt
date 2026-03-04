package cz.mendelu.xspacek6.vehiclemanager.database

import cz.mendelu.xspacek6.vehiclemanager.database.entities.Event
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Specification
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Trip
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Vehicle
import kotlinx.coroutines.flow.Flow

interface ILocalVehiclesRepository {

    // VEHICLES
    fun getVehicles(): Flow<List<Vehicle>>
    suspend fun findVehicleById(vehicleId: Long): Vehicle
    suspend fun insertVehicle(vehicle: Vehicle): Long
    suspend fun updateVehicle(vehicle: Vehicle)
    suspend fun deleteVehicle(vehicle: Vehicle)


    // SPECIFICATIONS
    suspend fun getSpecifications(vehicleId: Long): List<Specification>
    suspend fun insertSpecification(specification: Specification): Long
    suspend fun updateSpecification(specification: Specification)
    suspend fun deleteSpecification(specification: Specification)


    // REFILLS
    fun getRefills(vehicleId: Long) : Flow<List<Refill>>
    suspend fun getAllRefills(vehicleId: Long) : List<Refill>
    suspend fun getLastRefills(vehicleId: Long, previousDate: Long): List<Refill>
    suspend fun getLastTwoYearsRefills(vehicleId: Long): List<Refill>
    suspend fun findRefillById(refillId: Long) : Refill
    suspend fun insertRefill(refill: Refill): Long
    suspend fun updateRefill(refill: Refill)
    suspend fun deleteRefill(refill: Refill)


    // EXPENSES

    fun getExpenses(vehicleId: Long): Flow<List<Expense>>
    suspend fun getAllExpenses(vehicleId: Long): List<Expense>
    suspend fun getLastExpenses(vehicleId: Long, previousDate: Long): List<Expense>
    suspend fun getLastTwoYearsExpenses(vehicleId: Long): List<Expense>

    suspend fun findExpenseById(expenseId: Long): Expense
    suspend fun insertExpense(expense: Expense): Long
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)


    // EVENTS

    fun getEvents(vehicleId: Long): Flow<List<Event>>
    suspend fun getUpcomingEvents(vehicleId: Long, currentKm: Int): List<Event>
    suspend fun findEventById(eventId: Long): Event
    suspend fun insertEvent(event: Event): Long
    suspend fun updateEvent(event: Event)
    suspend fun deleteEvent(event: Event)

    //TRIPS
    fun getTrips(vehicleId: Long): Flow<List<Trip>>
    suspend fun findTripById(tripId: Long): Trip
    suspend fun insertTrip(trip: Trip): Long
    suspend fun updateTrip(trip: Trip)
    suspend fun deleteTrip(trip: Trip)


    //MILEAGE
    fun getMileages(vehicleId: Long): Flow<List<Mileage>>
    suspend fun getAllMileages(vehicleId: Long): List<Mileage>
    suspend fun getLastMileage(vehicleId: Long): Mileage?
    suspend fun getLastMileages(vehicleId: Long, previousDate: Long): List<Mileage>
    suspend fun getLastTwoYearsMileages(vehicleId: Long): List<Mileage>
    suspend fun findMileageByRefillId(refillId: Long): Mileage?
    suspend fun findMileageByExpenseId(expenseId: Long): Mileage?
    suspend fun findMileageByEventId(eventId: Long): Mileage?
    suspend fun insertMileage(mileage: Mileage): Long
    suspend fun updateMileage(mileage: Mileage)
    suspend fun deleteMileage(mileage: Mileage)

}