package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addevent

import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import cz.mendelu.xspacek6.vehiclemanager.database.FakeVehicleRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Event
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date


class AddEventViewModelTest {

    val viewModel = AddEventViewModel(FakeVehicleRepository())

    @Test
    fun testCreateNewEvent_WithFinalDateAndFinalKm_ShouldCreateNewEvent() {
        val event = Event("Test Event", 1, 1000, 0)
        event.finalDate = Date().time
        event.finalKm = 100
        event.eventCategory = ExpenseCategory.INSURANCE
        event.note = "kj hfjsdvhv sjk esj sdjhgb srjhvb djb"

        val newEvent = viewModel.createNewEvent(event)

        assertEquals(event.title, newEvent.title)
        assertEquals(event.vehicleId, newEvent.vehicleId)
        assertEquals(event.finalDate, newEvent.initialDate)
        assertEquals(event.finalKm, newEvent.initialKm)
        assertEquals(event.note, newEvent.note)
        assertEquals(event.eventCategory, newEvent.eventCategory)
    }

    @Test
    fun testCreateNewEvent_WithoutFinalDateAndFinalKm_ShouldCreateNewEventWithDefaults() {
        val event = Event("Test Event", 1, 1000, 0)

        val newEvent = viewModel.createNewEvent(event)

        assertEquals(event.title, newEvent.title)
        assertEquals(event.vehicleId, newEvent.vehicleId)

        assertEquals(Date().time, newEvent.initialDate)
        assertEquals(0, newEvent.initialKm)
        assertEquals(event.note, newEvent.note)
        assertEquals(event.eventCategory, newEvent.eventCategory)

    }

    @Test
    fun testCreateNewEvent_WithEveryMonths_ShouldSetOnceDateAndEveryMonths() {
        val event = Event("Test Event", 1, 1000, 0)
        event.finalDate = 2000
        event.everyMonths = 3

        val newEvent = viewModel.createNewEvent(event)

        assertEquals(event.everyMonths, newEvent.everyMonths)
        assertEquals(DateUtils.addMonthsToDate(2000,3), newEvent.onceDate)

        assertEquals(null, newEvent.onceAtKm)
    }

    @Test
    fun testCreateNewEvent_WithEveryKm_ShouldSetOnceAtKmAndEveryKm() {
        val event = Event("Test Event", 1, 1000, 0)
        event.finalKm = 500
        event.everyKm = 100

        val newEvent = viewModel.createNewEvent(event)

        assertEquals(event.everyKm, newEvent.everyKm)
        assertEquals(600, newEvent.onceAtKm!!)

        assertEquals(null, newEvent.onceDate)
    }

    @Test
    fun testCreateNewEvent_WithEveryKmAndMonths_ShouldSetOnceAtKmAndEveryKm() {
        val event = Event("Test Event", 1, 1000, 0)
        event.finalKm = 500
        event.everyKm = 100
        event.finalDate = 2000
        event.everyMonths = 3

        val newEvent = viewModel.createNewEvent(event)
        assertEquals(event.everyMonths, newEvent.everyMonths)
        assertEquals(DateUtils.addMonthsToDate(2000,3), newEvent.onceDate)
        assertEquals(event.everyKm, newEvent.everyKm)
        assertEquals(600, newEvent.onceAtKm!!)
    }

}