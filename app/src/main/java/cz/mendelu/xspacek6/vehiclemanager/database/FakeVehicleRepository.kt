package cz.mendelu.xspacek6.vehiclemanager.database

import com.google.android.gms.maps.model.LatLng
import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import cz.mendelu.xspacek6.vehiclemanager.constants.GasType
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Event
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Specification
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Trip
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Vehicle
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.util.Date
import java.util.concurrent.TimeUnit



class FakeVehicleRepository: ILocalVehiclesRepository {

    var vehicles = mutableListOf<Vehicle>()
    private val specificationsList: MutableList<Specification> = mutableListOf()
    private val refillsList: MutableList<Refill> = mutableListOf()
    private val expensesList: MutableList<Expense> = mutableListOf()
    private val eventsList: MutableList<Event> = mutableListOf()
    private val tripsList: MutableList<Trip> = mutableListOf()
    private val mileagesList: MutableList<Mileage> = mutableListOf()

    init {
        vehicles.add(createVehicle(1,"Car1", "Description1", "Manufacturer1", "Model1", "ABC123", "VIN123", "Blue", 2020, 1630425600000, GasType.PETROL, 60))
        vehicles.add(createVehicle(2,"Car2", "Description2", "Manufacturer2", "Model2", "XYZ789", "VIN456", "Red", 2021, 1662048000000, GasType.DIESEL, 70))
        vehicles.add(createVehicle(3,"Car3", "Description3", "Manufacturer3", "Model3", "123XYZ", "VIN789", "Green", 2019, 1590969600000, GasType.LPG, 50))

        specificationsList.add(createFakeSpecification("Spec1", "Value1", 1))
        specificationsList.add(createFakeSpecification("Spec2", "Value2", 1))
        specificationsList.add(createFakeSpecification("Spec3", "Value3", 2))
        specificationsList.add(createFakeSpecification("Spec4", "Value4", 2))
        specificationsList.add(createFakeSpecification("Spec5", "Value5", 3))
        specificationsList.add(createFakeSpecification("Spec6", "Value6", 3))

        refillsList.add(createFakeRefill(1, System.currentTimeMillis() - 86400000, 10000, 40.0, GasType.PETROL, 2.0,  true, false, "Refill note 1"))
        refillsList.add(createFakeRefill(1, System.currentTimeMillis(), 12000, 45.0, GasType.DIESEL, 1.5,  false, true, "Refill note 2"))
        refillsList.add(createFakeRefill(2, System.currentTimeMillis() - 172800000, 8000, 30.0, GasType.LPG, 1.0,  true, false, "Refill note 3"))
        refillsList.add(createFakeRefill(2, System.currentTimeMillis(), 9500, 35.0, GasType.PETROL, 2.0,  false, false, "Refill note 4"))
        refillsList.add(createFakeRefill(3, System.currentTimeMillis() - 259200000, 11000, 50.0, GasType.DIESEL, 1.5,  true, true, "Refill note 5"))
        refillsList.add(createFakeRefill(3, System.currentTimeMillis(), 12500, 55.0, GasType.LPG, 1.0,false, false, "Refill note 6"))

        expensesList.add(createFakeExpense("Expense1", 1, System.currentTimeMillis() - 86400000, 100, ExpenseCategory.MAINTENANCE, 50.0, 30.0, "Expense note 1"))
        expensesList.add(createFakeExpense("Expense2", 1, System.currentTimeMillis(),  200, ExpenseCategory.REPAIR, 80.0, 40.0, "Expense note 2"))
        expensesList.add(createFakeExpense("Expense3", 2, System.currentTimeMillis() - 172800000, 300, ExpenseCategory.INSURANCE, 120.0, 60.0, "Expense note 3"))
        expensesList.add(createFakeExpense("Expense4", 2, System.currentTimeMillis(),  400, ExpenseCategory.TUNING, 40.0, 20.0, "Expense note 4"))
        expensesList.add(createFakeExpense("Expense5", 3, System.currentTimeMillis() - 259200000,  500, ExpenseCategory.CLEANING, 60.0, 30.0, "Expense note 5"))
        expensesList.add(createFakeExpense("Expense6", 3, System.currentTimeMillis(),  600, ExpenseCategory.TOLL, 25.0, 15.0, "Expense note 6"))

        eventsList.add(createFakeEvent("Event1", 1, System.currentTimeMillis() + 864000000, 1000, "Event note 1", true, System.currentTimeMillis() + 172800000, System.currentTimeMillis() + 172800000, 200000, ExpenseCategory.MAINTENANCE))
        eventsList.add(createFakeEvent("Event2", 1, System.currentTimeMillis(), 2000, "Event note 2", false, null, null, null, ExpenseCategory.REPAIR))
        eventsList.add(createFakeEvent("Event3", 2, System.currentTimeMillis() + 172800000, 3000, "Event note 3", true, System.currentTimeMillis() + 259200000, System.currentTimeMillis() + 259200000, null, ExpenseCategory.INSURANCE))
        eventsList.add(createFakeEvent("Event4", 2, System.currentTimeMillis(), 4000, "Event note 4", false, null, null, null, ExpenseCategory.TUNING))
        eventsList.add(createFakeEvent("Event5", 3, System.currentTimeMillis() + 259200000, 5000, "Event note 5", true, System.currentTimeMillis() + 345600000, System.currentTimeMillis() + 345600000, 35000, ExpenseCategory.CLEANING))
        eventsList.add(createFakeEvent("Event6", 3, System.currentTimeMillis(), 6000, "Event note 6", false, null, null, null,  ExpenseCategory.TOLL))

        tripsList.add(createFakeTrip("Trip1", System.currentTimeMillis() - 86400000, 1, createPathPoints(), 60.0f, 200, 3600000, "Start Location 1", "Finish Location 1", "Trip note 1"))
        tripsList.add(createFakeTrip("Trip2", System.currentTimeMillis(), 1, null, 65.0f, 250, 4500000, "Start Location 2", "Finish Location 2", "Trip note 2"))
        tripsList.add(createFakeTrip("Trip3", System.currentTimeMillis() - 172800000, 2, null, 55.0f, 180, null, "Start Location 3", "Finish Location 3", "Trip note 3"))
        tripsList.add(createFakeTrip("Trip4", System.currentTimeMillis(), 2, createPathPoints(), 70.0f, 300, null, "Start Location 4", "Finish Location 4", "Trip note 4"))
        tripsList.add(createFakeTrip("Trip5", System.currentTimeMillis() - 259200000, 3, null, 50.0f, null, 2400000, null, null, "Trip note 5"))
        tripsList.add(createFakeTrip("Trip6", System.currentTimeMillis(), 3, null, 75.0f, null, 5400000, "Start Location 6", "Finish Location 6", null))

        mileagesList.add(createFakeMileage(1000, System.currentTimeMillis() - 86400000, 1, 1))
        mileagesList.add(createFakeMileage(1500, System.currentTimeMillis(), 1))
        mileagesList.add(createFakeMileage(2000, System.currentTimeMillis() - 172800000, 2, 3))
        mileagesList.add(createFakeMileage(2500, System.currentTimeMillis(), 2, null, 4))
        mileagesList.add(createFakeMileage(3000, System.currentTimeMillis() - 259200000, 3, null, null, 5))
        mileagesList.add(createFakeMileage(3500, System.currentTimeMillis(), 3, 6))
    }


    override fun getVehicles(): Flow<List<Vehicle>> {
        return flow { emit(vehicles) }
    }

    override suspend fun findVehicleById(vehicleId: Long): Vehicle {
        return vehicles.firstOrNull { it.vehicleId == vehicleId }
            ?: throw NoSuchElementException("Vehicle not found")
    }

    override suspend fun insertVehicle(vehicle: Vehicle): Long {
        val fakeId = vehicles.size.toLong() + 1
        vehicle.vehicleId = fakeId
        vehicles.add(vehicle)
        return fakeId
    }

    override suspend fun updateVehicle(vehicle: Vehicle) {
        val existingVehicle = vehicles.find { it.vehicleId == vehicle.vehicleId }
        existingVehicle?.apply {
            name = vehicle.name
            description = vehicle.description
            manufacturer = vehicle.manufacturer
            model = vehicle.model
            licencePlate = vehicle.licencePlate
            vin = vehicle.vin
            color = vehicle.color
            yearOfManufacture = vehicle.yearOfManufacture
            dateOfPurchase = vehicle.dateOfPurchase
            fuelType = vehicle.fuelType
            fuelTankVolume = vehicle.fuelTankVolume
        }
    }

    override suspend fun deleteVehicle(vehicle: Vehicle) {
        vehicles.remove(vehicle)
    }

    override suspend fun getSpecifications(vehicleId: Long): List<Specification> {
        // Simulate getting specifications for a specific vehicle
        return specificationsList.filter { it.vehicleId == vehicleId }
    }

    override suspend fun insertSpecification(specification: Specification): Long {
        // Simulate inserting a specification and generating a fake ID
        val fakeId = specificationsList.size.toLong() + 1
        specification.specificationsId = fakeId
        specificationsList.add(specification)
        return fakeId
    }

    override suspend fun updateSpecification(specification: Specification) {
        // Simulate updating a specification
        val existingSpecification = specificationsList.find { it.specificationsId == specification.specificationsId }
        existingSpecification?.apply {
            specTitle = specification.specTitle
            specValue = specification.specValue
            vehicleId = specification.vehicleId
        }
    }

    override suspend fun deleteSpecification(specification: Specification) {
        // Simulate deleting a specification
        specificationsList.remove(specification)
    }

    override fun getRefills(vehicleId: Long): Flow<List<Refill>> {
        return flow { emit(refillsList.filter { it.vehicleId == vehicleId }) }
    }

    override suspend fun getAllRefills(vehicleId: Long): List<Refill> {
        return refillsList.filter { it.vehicleId == vehicleId }
    }

    override suspend fun getLastRefills(vehicleId: Long, previousDate: Long): List<Refill> {
        return refillsList.filter { it.vehicleId == vehicleId && it.date >= previousDate }
    }

    override suspend fun getLastTwoYearsRefills(vehicleId: Long): List<Refill> {
        val twoYearsAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365 * 2)
        return refillsList.filter { it.vehicleId == vehicleId && it.date >= twoYearsAgo }
    }

    override suspend fun findRefillById(refillId: Long): Refill {
        return refillsList.firstOrNull { it.refillId == refillId }
            ?: throw NoSuchElementException("Refill not found")
    }

    override suspend fun insertRefill(refill: Refill): Long {
        val fakeId = refillsList.size.toLong() + 1
        refill.refillId = fakeId
        refillsList.add(refill)
        return fakeId
    }

    override suspend fun updateRefill(refill: Refill) {
        val existingRefill = refillsList.find { it.refillId == refill.refillId }
        existingRefill?.apply {
            date = refill.date
            mileage = refill.mileage
            volume = refill.volume
            fuelType = refill.fuelType
            fuelCost = refill.fuelCost
            totalCost = refill.totalCost
            full = refill.full
            previousMissed = refill.previousMissed
            note = refill.note
        }
    }

    override suspend fun deleteRefill(refill: Refill) {
        runBlocking {
            refillsList.remove(refill)
        }
    }

    override fun getExpenses(vehicleId: Long): Flow<List<Expense>> {
        return flow { emit(expensesList.filter { it.vehicleId == vehicleId }) }
    }

    override suspend fun getAllExpenses(vehicleId: Long): List<Expense> {
        return expensesList.filter { it.vehicleId == vehicleId }
    }

    override suspend fun getLastExpenses(vehicleId: Long, previousDate: Long): List<Expense> {
        return expensesList.filter { it.vehicleId == vehicleId && it.date >= previousDate }
    }

    override suspend fun getLastTwoYearsExpenses(vehicleId: Long): List<Expense> {
        val twoYearsAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365 * 2)
        return expensesList.filter { it.vehicleId == vehicleId && it.date >= twoYearsAgo }
    }

    override suspend fun findExpenseById(expenseId: Long): Expense {
        return expensesList.firstOrNull { it.expenseId == expenseId }
            ?: throw NoSuchElementException("Expense not found")
    }

    override suspend fun insertExpense(expense: Expense): Long {
        val fakeId = expensesList.size.toLong() + 1
        expense.expenseId = fakeId
        expensesList.add(expense)
        return fakeId
    }

    override suspend fun updateExpense(expense: Expense) {
        val existingExpense = expensesList.find { it.expenseId == expense.expenseId }
        existingExpense?.apply {
            title = expense.title
            vehicleId = expense.vehicleId
            date = expense.date
            mileage = expense.mileage
            expenseCategory = expense.expenseCategory
            costParts = expense.costParts
            costServices = expense.costServices
            note = expense.note
        }
    }

    override suspend fun deleteExpense(expense: Expense) {
        expensesList.remove(expense)
    }

    override fun getEvents(vehicleId: Long): Flow<List<Event>> {
        return flow { emit(eventsList.filter { it.vehicleId == vehicleId }) }
    }

    override suspend fun getUpcomingEvents(vehicleId: Long, currentKm: Int): List<Event> {
        return eventsList.filter {
            it.vehicleId == vehicleId &&
                    !it.done &&
                    (if (it.onceAtKm != null) it.onceAtKm!! <= (currentKm + 10000)
                    else if (it.onceDate != null) it.onceDate!! <= DateUtils.addMonthsToDate(Date().time, 2) else false)
        }
    }

    override suspend fun findEventById(eventId: Long): Event {
        return eventsList.firstOrNull { it.eventId == eventId }
            ?: throw NoSuchElementException("Event not found")
    }

    override suspend fun insertEvent(event: Event): Long {
        val fakeId = eventsList.size.toLong() + 1
        event.eventId = fakeId
        eventsList.add(event)
        return fakeId
    }

    override suspend fun updateEvent(event: Event) {
        val existingEvent = eventsList.find { it.eventId == event.eventId }
        existingEvent?.apply {
            title = event.title
            vehicleId = event.vehicleId
            initialDate = event.initialDate
            initialKm = event.initialKm
            note = event.note
            done = event.done
            onceDate = event.onceDate
            everyMonths = event.everyMonths
            finalDate = if (event.done) event.finalDate else null
            onceAtKm = event.onceAtKm
            everyKm = event.everyKm
            finalKm = if (event.done) event.finalKm else null
            eventCategory = event.eventCategory
        }
    }

    override suspend fun deleteEvent(event: Event) {
        eventsList.remove(event)
    }

    override fun getTrips(vehicleId: Long): Flow<List<Trip>> {
        return flow { emit(tripsList.filter { it.vehicleId == vehicleId }) }
    }

    override suspend fun findTripById(tripId: Long): Trip {
        return tripsList.firstOrNull { it.tripId == tripId }
            ?: throw NoSuchElementException("Trip not found")
    }

    override suspend fun insertTrip(trip: Trip): Long {
        val fakeId = tripsList.size.toLong() + 1
        trip.tripId = fakeId
        tripsList.add(trip)
        return fakeId
    }

    override suspend fun updateTrip(trip: Trip) {
        val existingTrip = tripsList.find { it.tripId == trip.tripId }
        existingTrip?.apply {
            title = trip.title
            date = trip.date
            vehicleId = trip.vehicleId
            pathPoints = trip.pathPoints
            averageSpeed = trip.averageSpeed
            distance = trip.distance
            timeDriven = trip.timeDriven
            startLocation = trip.startLocation
            finishLocation = trip.finishLocation
            note = trip.note
        }
    }

    override suspend fun deleteTrip(trip: Trip) {
        tripsList.remove(trip)
    }

    override fun getMileages(vehicleId: Long): Flow<List<Mileage>> {
        return flow { emit(mileagesList.filter { it.vehicleId == vehicleId }) }
    }

    override suspend fun getAllMileages(vehicleId: Long): List<Mileage> {
        return mileagesList.filter { it.vehicleId == vehicleId }
    }

    override suspend fun getLastMileage(vehicleId: Long): Mileage? {
        return mileagesList.filter { it.vehicleId == vehicleId }.lastOrNull()
    }

    override suspend fun getLastMileages(vehicleId: Long, previousDate: Long): List<Mileage> {
        //"SELECT * FROM mileage WHERE vehicle_id = :vehicleId AND (date BETWEEN :previousDate AND :todayDate) ORDER BY date ASC"
        return mileagesList.filter { it.vehicleId == vehicleId && it.date >= previousDate && it.date <= Date().time }
            .sortedBy { it.date }
    }

    override suspend fun getLastTwoYearsMileages(vehicleId: Long): List<Mileage> {
        val twoYearsAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365 * 2)
        return mileagesList.filter { it.vehicleId == vehicleId && it.date > twoYearsAgo }
    }

    override suspend fun findMileageByRefillId(refillId: Long): Mileage? {
        return mileagesList.find { it.refillId == refillId }
    }

    override suspend fun findMileageByExpenseId(expenseId: Long): Mileage? {
        return mileagesList.find { it.expenseId == expenseId }
    }

    override suspend fun findMileageByEventId(eventId: Long): Mileage? {
        return mileagesList.find { it.eventId == eventId }
    }

    override suspend fun insertMileage(mileage: Mileage): Long {
        val fakeId = mileagesList.size.toLong() + 1
        mileage.mileageId = fakeId
        mileagesList.add(mileage)
        return fakeId
    }

    override suspend fun updateMileage(mileage: Mileage) {
        val existingMileage = mileagesList.find { it.mileageId == mileage.mileageId }
        existingMileage?.apply {
            this.mileage = mileage.mileage
            this.date = mileage.date
            this.vehicleId = mileage.vehicleId
            this.refillId = mileage.refillId
            this.expenseId = mileage.expenseId
            this.eventId = mileage.eventId
        }
    }

    override suspend fun deleteMileage(mileage: Mileage) {
        mileagesList.remove(mileage)
    }


    private fun createVehicle(
        vehicleId: Long,
        name: String,
        description: String?,
        manufacturer: String?,
        model: String?,
        licencePlate: String?,
        vin: String?,
        color: String?,
        yearOfManufacture: Short?,
        dateOfPurchase: Long?,
        fuelType: GasType?,
        fuelTankVolume: Int?
    ): Vehicle {
        val vehicle = Vehicle(name)
        vehicle.vehicleId = vehicleId
        vehicle.description = description
        vehicle.manufacturer = manufacturer
        vehicle.model = model
        vehicle.licencePlate = licencePlate
        vehicle.vin = vin
        vehicle.color = color
        vehicle.yearOfManufacture = yearOfManufacture
        vehicle.dateOfPurchase = dateOfPurchase
        vehicle.fuelType = fuelType
        vehicle.fuelTankVolume = fuelTankVolume
        return vehicle
    }

    private fun createFakeSpecification(
        title: String,
        value: String,
        vehicleId: Long
    ): Specification {
        // Helper function to create a fake specification
        val fakeId = specificationsList.size.toLong() + 1
        return Specification(title, value, vehicleId).apply {
            specificationsId = fakeId
        }
    }

    private fun createFakeRefill(
        vehicleId: Long,
        date: Long,
        mileage: Int,
        volume: Double,
        fuelType: GasType,
        fuelCost: Double,
        full: Boolean,
        previousMissed: Boolean,
        note: String?
    ): Refill {
        // Helper function to create a fake refill
        val fakeId = refillsList.size.toLong() + 1
        return Refill(vehicleId, date).apply {
            refillId = fakeId
            this.mileage = mileage
            this.volume = volume
            this.fuelType = fuelType
            this.fuelCost = fuelCost
            this.totalCost = volume * fuelCost
            this.full = full
            this.previousMissed = previousMissed
            this.note = note
        }
    }

    private fun createFakeExpense(
        title: String,
        vehicleId: Long,
        date: Long,
        mileage: Int?,
        expenseCategory: ExpenseCategory,
        costParts: Double?,
        costServices: Double?,
        note: String?
    ): Expense {
        // Helper function to create a fake expense
        val fakeId = expensesList.size.toLong() + 1
        return Expense(title, vehicleId, date).apply {
            expenseId = fakeId
            this.mileage = mileage
            this.expenseCategory = expenseCategory
            this.costParts = costParts
            this.costServices = costServices
            this.note = note
        }
    }

    private fun createFakeEvent(
        title: String,
        vehicleId: Long,
        initialDate: Long,
        initialKm: Int,
        note: String?,
        done: Boolean,
        onceDate: Long?,
        finalDate: Long?,
        finalKm: Int?,
        eventCategory: ExpenseCategory
    ): Event {
        // Helper function to create a fake event
        val fakeId = eventsList.size.toLong() + 1
        return Event(title, vehicleId, initialDate, initialKm).apply {
            eventId = fakeId
            this.note = note
            this.done = done
            this.onceDate = onceDate
            this.finalDate = if (done) finalDate else null
            this.finalKm = if (done) finalKm else null
            this.eventCategory = eventCategory
        }
    }

    private fun createFakeTrip(
        title: String,
        date: Long,
        vehicleId: Long,
        pathPoints: List<List<LatLng>>?,
        averageSpeed: Float,
        distance: Int?,
        timeDriven: Long?,
        startLocation: String?,
        finishLocation: String?,
        note: String?
    ): Trip {
        // Helper function to create a fake trip
        val fakeId = tripsList.size.toLong() + 1
        return Trip(title, date, vehicleId).apply {
            tripId = fakeId
            this.pathPoints = pathPoints
            this.averageSpeed = averageSpeed
            this.distance = distance
            this.timeDriven = timeDriven
            this.startLocation = startLocation
            this.finishLocation = finishLocation
            this.note = note
        }
    }

    private fun createPathPoints(): List<List<LatLng>> {
        return listOf(
            listOf(
                LatLng(37.7749, -122.4194),
                LatLng(37.7749, -122.4194),
                LatLng(37.7749, -122.4194)
            ),
            listOf(
                LatLng(34.0522, -118.2437),
                LatLng(34.0522, -118.2437),
                LatLng(34.0522, -118.2437)
            ),
            listOf(
                LatLng(40.7128, -74.0060),
                LatLng(40.7128, -74.0060),
                LatLng(40.7128, -74.0060)
            )
        )
    }

    private fun createFakeMileage(
        mileageValue: Int,
        date: Long,
        vehicleId: Long,
        refillId: Long? = null,
        expenseId: Long? = null,
        eventId: Long? = null
    ): Mileage {
        val fakeId = mileagesList.size.toLong() + 1
        return Mileage(mileageValue, date, vehicleId).apply {
            mileageId = fakeId
            this.refillId = refillId
            this.expenseId = expenseId
            this.eventId = eventId
        }
    }


}