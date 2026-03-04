package cz.mendelu.xspacek6.vehiclemanager.database

import androidx.room.*
import cz.mendelu.xspacek6.vehiclemanager.database.entities.*
import kotlinx.coroutines.flow.Flow


@Dao
interface VehiclesDao {

    // VEHICLES
    @Query("SELECT * FROM vehicles")
    fun getVehicles(): Flow<List<Vehicle>>
    @Query("SELECT * FROM vehicles WHERE vehicle_id = :vehicleId")
    suspend fun findVehicleById(vehicleId: Long): Vehicle
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: Vehicle): Long
    @Update
    suspend fun updateVehicle(vehicle: Vehicle)
    @Delete
    suspend fun deleteVehicle(vehicle: Vehicle)


    // SPECIFICATIONS
    @Query("SELECT * FROM specifications WHERE vehicle_id = :vehicleId")
    suspend fun getSpecifications(vehicleId: Long): List<Specification>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecification(specification: Specification): Long
    @Update
    suspend fun updateSpecification(specification: Specification)
    @Delete
    suspend fun deleteSpecification(specification: Specification)


    // REFILLS
    @Query("SELECT * FROM refills WHERE vehicle_id = :vehicleId ORDER BY date DESC")
    fun getRefills(vehicleId: Long): Flow<List<Refill>>
    @Query("SELECT * FROM refills WHERE vehicle_id = :vehicleId ORDER BY date ASC")
    suspend fun getAllRefills(vehicleId: Long): List<Refill>
    @Query("SELECT * FROM refills WHERE vehicle_id = :vehicleId AND (date BETWEEN :previousDate AND :todayDate) ORDER BY date ASC")
    suspend fun getLastRefills(vehicleId: Long, todayDate: Long, previousDate: Long): List<Refill>
    @Query("SELECT * FROM refills WHERE refill_id = :refillId")
    suspend fun findRefillById(refillId: Long): Refill
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRefill(refill: Refill): Long
    @Update
    suspend fun updateRefill(refill: Refill)
    @Delete
    suspend fun deleteRefill(refill: Refill)


    // EXPENSE
    @Query("SELECT * FROM expenses WHERE vehicle_id = :vehicleId ORDER BY date DESC")
    fun getExpenses(vehicleId: Long): Flow<List<Expense>>
    @Query("SELECT * FROM expenses WHERE vehicle_id = :vehicleId ORDER BY date ASC")
    suspend fun getAllExpenses(vehicleId: Long): List<Expense>
    @Query("SELECT * FROM expenses WHERE vehicle_id = :vehicleId AND (date BETWEEN :previousDate AND :todayDate) ORDER BY date ASC")
    suspend fun getLastExpenses(vehicleId: Long, todayDate: Long, previousDate: Long): List<Expense>
    @Query("SELECT * FROM expenses WHERE expense_id = :expenseId")
    suspend fun findExpenseById(expenseId: Long): Expense
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long
    @Update
    suspend fun updateExpense(expense: Expense)
    @Delete
    suspend fun deleteExpense(expense: Expense)


    // EVENT
    @Query("SELECT * FROM events WHERE vehicle_id = :vehicleId")
    fun getEvents(vehicleId: Long): Flow<List<Event>>
    @Query("SELECT * FROM events WHERE vehicle_id = :vehicleId AND done = 0 AND ((once_date <= :laterDate) OR (once_at_km <= :laterKm))")
    suspend fun getUpcomingEvents(vehicleId: Long, laterDate: Long, laterKm: Int): List<Event>
    @Query("SELECT * FROM events WHERE event_id = :eventId")
    suspend fun findEventById(eventId: Long): Event
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event): Long
    @Update
    suspend fun updateEvent(event: Event)
    @Delete
    suspend fun deleteEvent(event: Event)


    //TRIP
    @Query("SELECT * FROM trips WHERE vehicle_id = :vehicleId ORDER BY date DESC")
    fun getTrips(vehicleId: Long): Flow<List<Trip>>
    @Query("SELECT * FROM trips WHERE vehicle_id = :vehicleId ORDER BY date DESC")
    suspend fun getAllTrips(vehicleId: Long): List<Trip>
    @Query("SELECT * FROM trips WHERE trip_id = :tripId")
    suspend fun findTripById(tripId: Long): Trip
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Trip): Long
    @Update
    suspend fun updateTrip(trip: Trip)
    @Delete
    suspend fun deleteTrip(trip: Trip)

    //MILEAGE
    @Query("SELECT * FROM mileage WHERE vehicle_id = :vehicleId ORDER BY date DESC, mileage DESC")
    fun getMileages(vehicleId: Long): Flow<List<Mileage>>
    @Query("SELECT * FROM mileage WHERE vehicle_id = :vehicleId ORDER BY date DESC, mileage ASC")
    suspend fun getAllMileages(vehicleId: Long): List<Mileage>
    @Query("SELECT * FROM mileage WHERE vehicle_id = :vehicleId ORDER BY date DESC, mileage DESC LIMIT 1")
    suspend fun getLastMileage(vehicleId: Long): Mileage?
    @Query("SELECT * FROM mileage WHERE vehicle_id = :vehicleId AND (date BETWEEN :previousDate AND :todayDate) ORDER BY date ASC")
    suspend fun getLastMileages(vehicleId: Long, todayDate: Long, previousDate: Long): List<Mileage>
    @Query("SELECT * FROM mileage WHERE refill_id = :refillId")
    suspend fun findMileageByRefillId(refillId: Long): Mileage?
    @Query("SELECT * FROM mileage WHERE expense_id = :expenseId")
    suspend fun findMileageByExpenseId(expenseId: Long): Mileage?
    @Query("SELECT * FROM mileage WHERE event_id = :eventId")
    suspend fun findMileageByEventId(eventId: Long): Mileage?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMileage(mileage: Mileage): Long
    @Update
    suspend fun updateMileage(mileage: Mileage)
    @Delete
    suspend fun deleteMileage(mileage: Mileage)

}