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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import mefetran.dgusev.meddocs.R

@Stable
enum class Category(@StringRes val labelRes: Int) {
    Laboratory(R.string.category_laboratory),
    Instrumental(R.string.category_instrumental),
    Ultrasound(R.string.category_ultrasound),
    XRay(R.string.category_xray),
    MRI(R.string.category_mri),
    CT(R.string.category_ct),
    ECG(R.string.category_ecg),
    Endoscopy(R.string.category_endoscopy),
    FunctionalDiagnostics(R.string.category_functional_diagnostics),
    Other(R.string.category_other);
}

@Composable
fun Category.icon(): ImageVector {
    return when (this) {
        Category.Laboratory -> Icons.Default.Biotech
        Category.Instrumental -> Icons.Default.MonitorHeart
        Category.Ultrasound -> Icons.Default.Microwave
        Category.XRay -> Icons.Default.CameraAlt
        Category.MRI -> Icons.Default.MedicalInformation
        Category.CT -> Icons.Default.MedicalServices
        Category.ECG -> Icons.Default.Favorite
        Category.Endoscopy -> Icons.Default.Search
        Category.FunctionalDiagnostics -> Icons.Default.Timeline
        Category.Other -> Icons.Default.MoreHoriz
    }
}
