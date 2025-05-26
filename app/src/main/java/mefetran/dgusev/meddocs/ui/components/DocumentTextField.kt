package mefetran.dgusev.meddocs.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.R

@Composable
fun DocumentTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    innerPaddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
    label: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(8.dp),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.onSurface),
    isError: Boolean = false,
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = if (isError) textStyle.merge(color = MaterialTheme.colorScheme.error) else textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = maxLines,
        minLines = minLines,
        cursorBrush = cursorBrush,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = shape)
            .border(
                width = if(isError) 2.dp else 1.dp,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                shape = shape,
            )
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        decorationBox = @Composable { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPaddingValues)
            ) {
                if (label != null && value.text.isBlank() && !isFocused) {
                    ProvideTextStyle(textStyle.merge(color = MaterialTheme.colorScheme.outline)) {
                        label()
                    }
                }
                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
fun DocumentTextFieldPreview(modifier: Modifier = Modifier) {
    var inputValue by remember { mutableStateOf(TextFieldValue("")) }

    DocumentTextField(
        value = inputValue,
        onValueChange = { newValue -> inputValue = newValue },
        modifier = Modifier.padding(16.dp),
        label = { Text(text = stringResource(id = R.string.field_label)) },
        isError = true,
    )
}