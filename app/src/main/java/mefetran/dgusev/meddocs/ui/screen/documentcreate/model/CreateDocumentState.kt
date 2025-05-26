package mefetran.dgusev.meddocs.ui.screen.documentcreate.model

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import mefetran.dgusev.meddocs.data.model.Category

data class CreateDocumentState(
    val category: Category = Category.Other,
    val contentMap: SnapshotStateMap<String, String> = mutableStateMapOf(),
)
