package com.example.workout_log.presentation.workoutlog.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.workout_log.presentation.util.extensions.onFocusSelectAll
import com.example.workout_log.ui.theme.Grey200
import com.example.workout_log.ui.theme.Indigo200
import com.example.workout_log.ui.theme.Indigo400

@Composable
fun WorkoutLogTextField(
    textFieldValue: MutableState<TextFieldValue>,
    modifier: Modifier,
    isNumberTextField: Boolean,
    colors: TextFieldColors = workoutLogTextFieldColors(),
    textStyle: TextStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
    onValueChanged: (TextFieldValue) -> Unit
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = Indigo400,
        backgroundColor = Indigo200.copy(alpha = 0.4f)
    )
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        val keyboardOptions =
            if (isNumberTextField) KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
        OutlinedTextField(
            value = textFieldValue.value,
            modifier = Modifier
                .then(modifier)
                .onFocusSelectAll(textFieldValue),
            maxLines = 1,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(8.dp),
            colors = colors,
            textStyle = textStyle,
            onValueChange = onValueChanged
        )
    }
}

@Composable
fun workoutLogTextFieldColors(
    backgroundColor: Color = Grey200,
    focusedIndicatorColor: Color = Indigo200,
    unfocusedIndicatorColor: Color = Color.Transparent,
    cursorColor: Color = Color.White
): TextFieldColors =
    TextFieldDefaults.textFieldColors(
        backgroundColor = backgroundColor,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor,
        cursorColor = cursorColor
    )
