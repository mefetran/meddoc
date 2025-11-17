package mefetran.dgusev.meddocs.ui.components

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
import androidx.compose.ui.graphics.vector.ImageVector
import mefetran.dgusev.meddocs.R
import mefetran.dgusev.meddocs.domain.model.Category

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

@StringRes
fun Category.getLabelRes():  Int = when (this) {
    Category.Laboratory -> R.string.category_laboratory
    Category.Instrumental -> R.string.category_instrumental
    Category.Ultrasound -> R.string.category_ultrasound
    Category.XRay -> R.string.category_xray
    Category.MRI -> R.string.category_mri
    Category.CT -> R.string.category_ct
    Category.ECG -> R.string.category_ecg
    Category.Endoscopy -> R.string.category_endoscopy
    Category.FunctionalDiagnostics -> R.string.category_functional_diagnostics
    Category.Other -> R.string.category_other
}
