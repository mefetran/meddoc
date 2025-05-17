package mefetran.dgusev.meddocs.data.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Microwave
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import mefetran.dgusev.meddocs.R

@Stable
sealed class Category(
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    data object Laboratory : Category(R.string.category_laboratory, Icons.Default.Biotech)
    data object Instrumental : Category(R.string.category_instrumental, Icons.Default.MonitorHeart)
    data object Ultrasound : Category(R.string.category_ultrasound, Icons.Default.Microwave)
    data object XRay : Category(R.string.category_xray, Icons.Default.CameraAlt)
    data object MRI : Category(R.string.category_mri, Icons.Default.MedicalInformation)
    data object CT : Category(R.string.category_ct, Icons.Default.MedicalServices)
    data object ECG : Category(R.string.category_ecg, Icons.Default.Favorite)
    data object Endoscopy : Category(R.string.category_endoscopy, Icons.Default.Search)
    data object FunctionalDiagnostics :
        Category(R.string.category_functional_diagnostics, Icons.Default.Timeline)

    data object Other : Category(R.string.category_other, Icons.Default.MoreHoriz)

    companion object {
        private val values = listOf(
            Laboratory,
            Instrumental,
            Ultrasound,
            XRay,
            MRI,
            CT,
            ECG,
            Endoscopy,
            FunctionalDiagnostics,
            Other
        )
        fun fromString(value: String) = values.firstOrNull { it.toString().lowercase() == value.lowercase() } ?: Other
    }
}