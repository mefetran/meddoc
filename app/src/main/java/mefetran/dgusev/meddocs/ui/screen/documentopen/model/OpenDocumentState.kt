package mefetran.dgusev.meddocs.ui.screen.documentopen.model

import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document
import java.util.HashMap

data class OpenDocumentState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val document: Document = Document(
        id = "",
        title = "",
        description = "",
        date = "",
        localFilePath = "",
        file = "",
        category = Category.Other,
        priority = 0,
        content = HashMap<String, String>(),
        createdAt = "",
        updatedAt = "",
    ),
)
