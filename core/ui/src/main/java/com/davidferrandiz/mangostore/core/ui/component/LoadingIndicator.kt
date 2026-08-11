package com.davidferrandiz.mangostore.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.davidferrandiz.mangostore.core.ui.theme.MangoTheme

const val LOADING_INDICATOR_TAG = "loadingIndicator"

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag(LOADING_INDICATOR_TAG),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun LoadingIndicatorPreview() {
    MangoTheme { LoadingIndicator() }
}
