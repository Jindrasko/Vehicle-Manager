package cz.mendelu.xspacek6.vehiclemanager.ui.activities


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import cz.mendelu.xspacek6.vehiclemanager.R
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppIntroActivity : AppIntro() {

    private val viewModel: AppIntroViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isWizardMode = true

        addSlide(
            AppIntroFragment.createInstance(
                title = getString(R.string.intro1T),
                description = getString(R.string.intro1D),
                backgroundColorRes = R.color.bgColor,
                imageDrawable = R.drawable.logo
            ))
        addSlide(
            AppIntroFragment.createInstance(
            title = getString(R.string.intro2T),
            description = getString(R.string.intro2D),
            backgroundColorRes = R.color.bgColor,
            imageDrawable = R.drawable.receipt_160
        ))
        addSlide(
            AppIntroFragment.createInstance(
            title = getString(R.string.intro3T),
            description = getString(R.string.intro3D),
            backgroundColorRes = R.color.bgColor,
            imageDrawable = R.drawable.car_repair_160
        ))
        addSlide(
            AppIntroFragment.createInstance(
                title = getString(R.string.intro4T),
                description = getString(R.string.intro4D),
                backgroundColorRes = R.color.bgColor,
                imageDrawable = R.drawable.bar_chart_160
            ))

    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        continueToMainActivity()
    }

    private fun continueToMainActivity() {
        lifecycleScope.launch {
            viewModel.setFirstRun()
        }.invokeOnCompletion {
            finish()
        }
    }

}