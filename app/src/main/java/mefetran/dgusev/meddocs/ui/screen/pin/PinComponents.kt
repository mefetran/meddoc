package mefetran.dgusev.meddocs.ui.screen.pin

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun PinButton(
    digit: Int,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colorScheme.surfaceBright,
                shape = CircleShape,
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            ) { onClick(digit) }
    ) {
        val buttonWidth = maxWidth
        val textSize = buttonWidth.value * 0.4f

        Text(
            text = digit.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = textSize.sp
            )
        )
    }
}

@Composable
private fun PinBiometricButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colorScheme.surfaceBright,
                shape = CircleShape,
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            ) { onClick() }
    ) {
        val buttonWidth = maxWidth
        val iconSize = buttonWidth.value * 0.4f

        Icon(
            painter = rememberVectorPainter(image = Icons.Filled.Fingerprint),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(iconSize.dp)
        )
    }
}


@Composable
private fun PinClearButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colorScheme.surfaceBright,
                shape = CircleShape,
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            ) { onClick() }
    ) {
        val buttonWidth = maxWidth
        val iconSize = buttonWidth.value * 0.4f

        Icon(
            painter = rememberVectorPainter(image = Icons.Filled.Clear),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(iconSize.dp)
        )
    }
}


@Composable
private fun PinDoneButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colorScheme.surfaceBright,
                shape = CircleShape,
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false)
            ) { onClick() }
    ) {
        val buttonWidth = maxWidth
        val iconSize = buttonWidth.value * 0.4f

        Icon(
            painter = rememberVectorPainter(image = Icons.Filled.Check),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(iconSize.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9FF, locale = "ru")
@Composable
fun PinButtonPreview() {
    PinButton(
        digit = 9,
        modifier = Modifier.padding(16.dp)
    ) { digit ->

    }
}

@Composable
private fun PinRow(
    digits: List<Int>,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        digits.forEach { digit ->
            PinButton(
                digit = digit,
                onClick = onClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun PinPad(
    showBiometric: Boolean,
    isCreatePin: Boolean,
    modifier: Modifier = Modifier,
    onPinButtonClick: (Int) -> Unit,
    onBiometricClick: () -> Unit,
    onClearClick: () -> Unit,
    onDoneClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        PinRow(
            digits = listOf(1, 2, 3),
            onClick = onPinButtonClick,
        )
        PinRow(
            digits = listOf(4, 5, 6),
            onClick = onPinButtonClick,
        )
        PinRow(
            digits = listOf(7, 8, 9),
            onClick = onPinButtonClick,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PinClearButton(
                onClick = onClearClick,
                modifier = Modifier.weight(1f)
            )
            PinButton(
                digit = 0,
                onClick = onPinButtonClick,
                modifier = Modifier.weight(1f)
            )
            if (showBiometric && !isCreatePin) {
                PinBiometricButton(
                    onClick = onBiometricClick,
                    modifier = Modifier.weight(1f)
                )
            } else if (isCreatePin) {
                PinDoneButton(
                    onClick = onDoneClick,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(
                    Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9FF, locale = "ru")
@Composable
fun PinPadPreview() {
    PinPad(
        showBiometric = false,
        isCreatePin = false,
        onPinButtonClick = {},
        onBiometricClick = {},
        onClearClick = {},
        onDoneClick = {},
    )
}

@Composable
fun PinEnterDots(
    enteredDigits: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        modifier = modifier.fillMaxWidth()
    ) {
        repeat(PIN_LENGTH) { index ->
            AnimatedContent(
                targetState = index < enteredDigits
            ) { currentState ->
                when (currentState) {
                    true -> Box(
                        Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )

                    false -> Box(
                        Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9FF, locale = "ru")
@Composable
fun PinEnterDotsPreview() {
    PinEnterDots(
        enteredDigits = 2,
        modifier = Modifier.padding(16.dp)
    )
}
