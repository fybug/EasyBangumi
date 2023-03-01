package com.heyanle.easybangumi4.ui.home.explore.source

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.heyanle.bangumi_source_api.api.Source
import com.heyanle.easy_i18n.R
import com.heyanle.easybangumi4.LocalNavController
import com.heyanle.easybangumi4.navigationSourceHome
import com.heyanle.easybangumi4.source.SourceBundle
import com.heyanle.easybangumi4.ui.common.OkImage
import com.heyanle.easybangumi4.ui.common.SourceContainer

/**
 * Created by HeYanLe on 2023/2/21 23:35.
 * https://github.com/heyanLE
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourceTopAppBar(behavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.explore)) },
        scrollBehavior = behavior
    )
}

@Composable
fun Source() {
    val nav = LocalNavController.current
    SourceContainer() { bundle ->
        LazyColumn() {
            items(bundle.sources()) {
                SourceItem(
                    sourceBundle = bundle,
                    source = it,
                    onClick = {
                        nav.navigationSourceHome(it.key)
                    },
                    onAction = {
                        nav.navigationSourceHome(it.key)
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourceItem(
    sourceBundle: SourceBundle,
    source: Source,
    onClick: (Source) -> Unit,
    onAction: (Source) -> Unit,
) {

    val icon = remember {
        sourceBundle.icon(source.key)
    }

    ListItem(
        modifier = Modifier.clickable {
            onClick(source)
        },
        headlineText = {
            Text(text = source.label)
        },
        supportingText = {
            Text(
                text = source.version,
            )
        },
        trailingContent = {
            TextButton(onClick = {
                onAction(source)
            }) {
                Text(text = stringResource(id = R.string.explore))
            }
        },
        leadingContent = {
            OkImage(
                modifier = Modifier.size(40.dp),
                image = icon?.getIconFactory()?.invoke(),
                contentDescription = source.label
            )
        }
    )

}