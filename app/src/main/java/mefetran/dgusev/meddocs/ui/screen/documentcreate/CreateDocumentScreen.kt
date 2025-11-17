package mefetran.dgusev.meddocs.ui.screen.documentcreate

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.R
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.ui.components.AppToolbar
import mefetran.dgusev.meddocs.ui.components.DocumentContentItem
import mefetran.dgusev.meddocs.ui.components.DocumentTextField
import mefetran.dgusev.meddocs.ui.components.ModalDatePicker
import mefetran.dgusev.meddocs.ui.components.ScreenTitle
import mefetran.dgusev.meddocs.ui.components.formatDate
import mefetran.dgusev.meddocs.ui.components.getLabelRes
import mefetran.dgusev.meddocs.ui.components.icon

@Composable
internal fun CreateDocumentScreen(
    title: TextFieldValue,
    description: TextFieldValue,
    date: TextFieldValue,
    category: Category,
    contentMap: SnapshotStateMap<String, String>,
    newField: TextFieldValue,
    newFieldValue: TextFieldValue,
    modifier: Modifier = Modifier,
    onCreateDocument: () -> Unit,
    onTitleChange: (TextFieldValue) -> Unit,
    onDescriptionChange: (TextFieldValue) -> Unit,
    onDateChange: (Long?) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onNewFieldChange: (TextFieldValue) -> Unit,
    onNewFieldValueChange: (TextFieldValue) -> Unit,
    onBackClick: () -> Unit,
    onAddContentClick: () -> Unit,
    onDeleteContentItemClick: (String) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    BackHandler { onBackClick() }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
    ) {
        Column {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                AppToolbar(
                    onBackClick = onBackClick,
                    modifier = Modifier.padding(
                        top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
                    )
                )
                Column(Modifier.padding(horizontal = 16.dp)) {
                    ScreenTitle(stringResource(id = R.string.create_document_title))
                    Spacer(Modifier.height(32.dp))
                    DocumentTextField(
                        value = title,
                        onValueChange = {
                            isError = false
                            onTitleChange(it)
                        },
                        textStyle = MaterialTheme.typography.bodyMedium.merge(color = MaterialTheme.colorScheme.onSurface),
                        label = {
                            Text(stringResource(id = R.string.title_label))
                        },
                        singleLine = true,
                        isError = isError,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    DocumentTextField(
                        value = description,
                        onValueChange = onDescriptionChange,
                        textStyle = MaterialTheme.typography.bodyMedium.merge(color = MaterialTheme.colorScheme.onSurface),
                        label = {
                            Text(stringResource(id = R.string.description_label))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { showDatePicker = true })
                    ) {
                        DocumentTextField(
                            value = date,
                            onValueChange = {},
                            textStyle = MaterialTheme.typography.bodyMedium.merge(color = MaterialTheme.colorScheme.onSurface),
                            label = {
                                Text(stringResource(id = R.string.select_date))
                            },
                            enabled = false,
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        stringResource(id = R.string.category_label),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Category.entries.forEach { currentCategory ->
                            AssistChip(
                                onClick = {
                                    onCategoryChange(currentCategory)
                                },
                                label = {
                                    Text(
                                        stringResource(id = currentCategory.getLabelRes()),
                                        style = MaterialTheme.typography.labelLarge.copy(color = if (category == currentCategory) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        currentCategory.icon(),
                                        null,
                                        tint = if (category == currentCategory) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = if (category == currentCategory) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                                ),
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        stringResource(id = R.string.additional_data_label),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    contentMap.forEach { (field, value) ->
                        DocumentContentItem(
                            title = field,
                            description = value,
                            onDeleteClick = onDeleteContentItemClick
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    DocumentTextField(
                        value = newField,
                        onValueChange = onNewFieldChange,
                        textStyle = MaterialTheme.typography.bodyMedium.merge(color = MaterialTheme.colorScheme.onSurface),
                        label = {
                            Text(stringResource(id = R.string.field_label))
                        },
                        singleLine = true,
                    )
                    Spacer(Modifier.height(8.dp))
                    DocumentTextField(
                        value = newFieldValue,
                        onValueChange = onNewFieldValueChange,
                        textStyle = MaterialTheme.typography.bodyMedium.merge(color = MaterialTheme.colorScheme.onSurface),
                        label = {
                            Text(stringResource(id = R.string.value_label))
                        },
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (newField.text.isNotBlank() && newFieldValue.text.isNotBlank()) {
                                onAddContentClick()
                            }
                        },
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Text(
                            text = stringResource(id = R.string.add_button),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
            Button(
                onClick = {
                    if (title.text.isNotBlank()) {
                        onCreateDocument()
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .imePadding()
                    .navigationBarsPadding()
            ) {
                Text(
                    text = stringResource(id = R.string.create_document_button),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }

    if (showDatePicker) {
        ModalDatePicker(
            onDateSelected = onDateChange,
            onDismiss = { showDatePicker = !showDatePicker }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
internal fun CreateDocumentScreenPreview() {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf(TextFieldValue("")) }
    var category by remember { mutableStateOf(Category.Other) }
    val contentMap by remember { mutableStateOf(SnapshotStateMap<String, String>()) }

    var newField by remember { mutableStateOf(TextFieldValue("")) }
    var newFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    CreateDocumentScreen(
        title = title,
        description = description,
        date = date,
        category = category,
        contentMap = contentMap,
        newField = newField,
        newFieldValue = newFieldValue,
        onCreateDocument = {},
        onTitleChange = { title = it },
        onDescriptionChange = { description = it },
        onDateChange = { timeMillis ->
            timeMillis?.let {
                date = TextFieldValue(formatDate(timeMillis))
            }
        },
        onCategoryChange = { category = it },
        onNewFieldChange = { newField = it },
        onNewFieldValueChange = { newFieldValue = it },
        onBackClick = {},
        onAddContentClick = {
            contentMap[newField.text] = newFieldValue.text
            newField = TextFieldValue("")
            newFieldValue = TextFieldValue("")
        },
        onDeleteContentItemClick = {
            contentMap.remove(it)
        }
    )
}
