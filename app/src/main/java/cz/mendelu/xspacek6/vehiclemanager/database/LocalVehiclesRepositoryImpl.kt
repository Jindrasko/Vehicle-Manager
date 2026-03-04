package cz.mendelu.xspacek6.vehiclemanager.database

import cz.mendelu.xspacek6.vehiclemanager.database.entities.Event
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Specification
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Trip
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Vehicle
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import java.util.Date

class LocalVehiclesRepositoryImpl(private val dao: VehiclesDao) : ILocalVehiclesRepository {

    // VEHICLE
    override fun getVehicles(): Flow<List<Vehicle>> {
        return dao.getVehicles()
    }
    override suspend fun findVehicleById(vehicleId: Long): Vehicle {
        return dao.findVehicleById(vehicleId)
    }
    override suspend fun insertVehicle(vehicle: Vehicle): Long {
        return dao.insertVehicle(vehicle)
    }
    override suspend fun updateVehicle(vehicle: Vehicle) {
        dao.updateVehicle(vehicle)
    }
    override suspend fun deleteVehicle(vehicle: Vehicle) {
        dao.deleteVehicle(vehicle)
    }


    //SPECIFICATIONS
    override suspend fun getSpecifications(vehicleId: Long): List<Specification> {
        return dao.getSpecifications(vehicleId)
    }
    override suspend fun insertSpecification(specification: Specification): Long {
        return dao.insertSpecification(specification)
    }
    override suspend fun updateSpecification(specification: Specification) {
        dao.updateSpecification(specification)
    }
    override suspend fun deleteSpecification(specification: Specification) {
        dao.deleteSpecification(specification)
    }


    // REFILLS
    override fun getRefills(vehicleId: Long): Flow<List<Refill>> {
        return dao.getRefills(vehicleId)
    }

    override suspend fun getAllRefills(vehicleId: Long): List<Refill> {
        return dao.getAllRefills(vehicleId)
    }

    override suspend fun getLastRefills(vehicleId: Long, previousDate: Long): List<Refill> {
        return dao.getLastRefills(vehicleId, Date().time, previousDate)
    }

    override suspend fun getLastTwoYearsRefills(vehicleId: Long): List<Refill> {
        return dao.getLastRefills(vehicleId, Date().time,DateUtils.addMonthsToDate(Date().time, -24))
    }

    override suspend fun findRefillById(refillId: Long): Refill {
        return dao.findRefillById(refillId)
    }
    override suspend fun insertRefill(refill: Refill): Long {
        return dao.insertRefill(refill)
    }
    override suspend fun updateRefill(refill: Refill) {
        dao.updateRefill(refill)
    }
    override suspend fun deleteRefill(refill: Refill) {
        dao.deleteRefill(refill)
    }


    //EXPENSES
    override fun getExpenses(vehicleId: Long): Flow<List<Expense>> {
        return dao.getExpenses(vehicleId)
    }

    override suspend fun getAllExpenses(vehicleId: Long): List<Expense> {
        return dao.getAllExpenses(vehicleId)
    }

    override suspend fun getLastExpenses(vehicleId: Long, previousDate: Long): List<Expense> {
        return dao.getLastExpenses(vehicleId, Date().time, previousDate)
    }

    override suspend fun getLastTwoYearsExpenses(vehicleId: Long): List<Expense> {
        return dao.getLastExpenses(vehicleId, Date().time, DateUtils.addMonthsToDate(Date().time, -24))
    }

    override suspend fun findExpenseById(expenseId: Long): Expense {
        return dao.findExpenseById(expenseId)
    }
    override suspend fun insertExpense(expense: Expense): Long {
        return dao.insertExpense(expense)
    }
    override suspend fun updateExpense(expense: Expense) {
        dao.updateExpense(expense)
    }
    override suspend fun deleteExpense(expense: Expense) {
        dao.deleteExpense(expense)
    }


    // EVENTS
    override fun getEvents(vehicleId: Long): Flow<List<Event>> {
        return dao.getEvents(vehicleId)
    }

    override suspend fun getUpcomingEvents(vehicleId: Long, currentKm: Int): List<Event> {
        return dao.getUpcomingEvents(vehicleId, DateUtils.addMonthsToDate(Date().time, 2), currentKm + 10000)
    }

    override suspend fun findEventById(eventId: Long): Event {
        return dao.findEventById(eventId)
    }
    override suspend fun insertEvent(event: Event): Long {
        return dao.insertEvent(event)
    }
    override suspend fun updateEvent(event: Event) {
        dao.updateEvent(event)
    }
    override suspend fun deleteEvent(event: Event) {
        dao.deleteEvent(event)
    }


    //TRIPS
    override fun getTrips(vehicleId: Long): Flow<List<Trip>> {
        return dao.getTrips(vehicleId)
    }

    override suspend fun findTripById(tripId: Long): Trip {
        return dao.findTripById(tripId)
    }

    override suspend fun insertTrip(trip: Trip): Long {
        return dao.insertTrip(trip)
    }

    override suspend fun updateTrip(trip: Trip) {
        dao.updateTrip(trip)
    }

    override suspend fun deleteTrip(trip: Trip) {
        dao.deleteTrip(trip)
    }


    //MILEAGE
    override fun getMileages(vehicleId: Long): Flow<List<Mileage>> {
        return dao.getMileages(vehicleId)
    }

    override suspend fun getAllMileages(vehicleId: Long): List<Mileage> {
        return dao.getAllMileages(vehicleId)
    }

    override suspend fun getLastMileage(vehicleId: Long): Mileage? {
        return dao.getLastMileage(vehicleId)
    }

    override suspend fun getLastMileages(vehicleId: Long, previousDate: Long): List<Mileage> {
        return dao.getLastMileages(vehicleId, Date().time, previousDate)
    }

    override suspend fun getLastTwoYearsMileages(vehicleId: Long): List<Mileage> {
        return dao.getLastMileages(vehicleId, Date().time, DateUtils.addMonthsToDate(Date().time, -24))
    }

    override suspend fun findMileageByRefillId(refillId: Long): Mileage? {
        return dao.findMileageByRefillId(refillId)
    }
    override suspend fun findMileageByExpenseId(expenseId: Long): Mileage? {
        return dao.findMileageByExpenseId(expenseId)
    }
    override suspend fun findMileageByEventId(eventId: Long): Mileage? {
        return dao.findMileageByEventId(eventId)
    }
    override suspend fun insertMileage(mileage: Mileage): Long {
        return dao.insertMileage(mileage)
    }
    override suspend fun updateMileage(mileage: Mileage) {
        dao.updateMileage(mileage)
    }
    override suspend fun deleteMileage(mileage: Mileage) {
        dao.deleteMileage(mileage)
    }



}