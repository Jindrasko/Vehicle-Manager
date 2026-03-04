package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.events

import android.content.Context
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.VehicleManagerApplication
import cz.mendelu.xspacek6.vehiclemanager.database.FakeVehicleRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class EventsViewModelTest {

    @Mock
    var appContext: Context = mock(Context::class.java)

    private val viewModel = EventsViewModel(FakeVehicleRepository())

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        // Mock the behavior of getString
        `when`(appContext.getString(R.string.km)).thenReturn("km")
        `when`(appContext.getString(R.string.months)).thenReturn("mon.")
        VehicleManagerApplication.appContext = appContext
    }

    @Test
    fun testGetRepeatText_everyKmOnly_shouldReturnKmText() {
        val result = viewModel.getRepeatText(5, null)
        assertEquals("5 km", result)
    }

    @Test
    fun testGetRepeatText_everyMonthsOnly_shouldReturnMonthsText() {
        val result = viewModel.getRepeatText(null, 3)
        assertEquals("3 mon.", result)
    }

    @Test
    fun testGetRepeatText_bothEveryKmAndMonths_shouldReturnCombinedText() {
        val result = viewModel.getRepeatText(5, 3)
        assertEquals("5 km, 3 mon.", result)
    }

    @Test
    fun testGetRepeatText_noValues_shouldReturnEmptyString() {
        val result = viewModel.getRepeatText(null, null)
        assertEquals("", result)
    }


    @Test
    fun testGetRepeatText_negativeValues_shouldReturnCombinedText() {
        val result = viewModel.getRepeatText(-5, -3)
        assertEquals("-5 km, -3 mon.", result)
    }


}