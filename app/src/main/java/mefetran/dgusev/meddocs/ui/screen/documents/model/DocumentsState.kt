package mefetran.dgusev.meddocs.ui.screen.documents.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import mefetran.dgusev.meddocs.domain.model.Document

data class DocumentsState(
    val isLoading: Boolean = false,
    val documents: SnapshotStateList<Document> = mutableStateListOf()
)
