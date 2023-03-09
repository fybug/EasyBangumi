package com.heyanle.easybangumi4.ui.cartoon_play

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heyanle.bangumi_source_api.api.Source
import com.heyanle.bangumi_source_api.api.entity.Cartoon
import com.heyanle.bangumi_source_api.api.entity.CartoonSummary
import com.heyanle.bangumi_source_api.api.entity.PlayLine
import com.heyanle.easybangumi4.LocalNavController
import com.heyanle.easybangumi4.R
import com.heyanle.easybangumi4.ui.common.Action
import com.heyanle.easybangumi4.ui.common.ActionRow
import com.heyanle.easybangumi4.ui.common.DetailedContainer
import com.heyanle.easybangumi4.ui.common.EmptyPage
import com.heyanle.easybangumi4.ui.common.ErrorPage
import com.heyanle.easybangumi4.ui.common.LoadingPage
import com.heyanle.easybangumi4.ui.common.OkImage
import com.heyanle.easybangumi4.ui.common.player.ControlViewModel
import com.heyanle.easybangumi4.ui.common.player.ControlViewModelFactory
import com.heyanle.easybangumi4.ui.common.player.EasyPlayerScaffold
import com.heyanle.easybangumi4.ui.common.player.LockBtn
import com.heyanle.easybangumi4.ui.common.player.ProgressBox
import com.heyanle.easybangumi4.ui.common.player.SimpleBottomBar
import com.heyanle.easybangumi4.ui.common.player.SimpleTopBar
import com.heyanle.easybangumi4.utils.TODO
import kotlinx.coroutines.launch

/**
 * Created by HeYanLe on 2023/3/4 16:34.
 * https://github.com/heyanLE
 */

@Composable
fun CartoonPlay(
    id: String,
    source: String,
    url: String,
    enterData: CartoonPlayViewModel.EnterData? = null
) {

    val summary = CartoonSummary(id, source, url)
    val owner = DetailedViewModel.getViewModelStoreOwner(summary)
    CompositionLocalProvider(
        LocalViewModelStoreOwner provides owner
    ) {
        DetailedContainer(sourceKey = source) { _, sou, det ->

            val detailedVM =
                viewModel<DetailedViewModel>(factory = DetailedViewModelFactory(summary, det))
            val cartoonPlayViewModel = viewModel<CartoonPlayViewModel>()
            CartoonPlay(
                detailedVM = detailedVM,
                cartoonPlayVM = cartoonPlayViewModel,
                cartoonSummary = summary,
                source = sou,
                enterData = enterData
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartoonPlay(
    detailedVM: DetailedViewModel,
    cartoonPlayVM: CartoonPlayViewModel,
    cartoonSummary: CartoonSummary,
    source: Source,
    enterData: CartoonPlayViewModel.EnterData? = null
) {

    val controlVM =
        viewModel<ControlViewModel>(factory = ControlViewModelFactory(CartoonPlayingManager.exoPlayer))

    val nav = LocalNavController.current

    LaunchedEffect(key1 = detailedVM.detailedState) {
        val sta = detailedVM.detailedState
        if (sta is DetailedViewModel.DetailedState.None) {
            detailedVM.load()
        } else if (sta is DetailedViewModel.DetailedState.Info) {
            // 加载好之后进入 播放环节
            cartoonPlayVM.onDetailedLoaded(cartoonSummary, sta, enterData)
        }
    }



    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        EasyPlayerScaffold(
            modifier = Modifier.fillMaxSize(),
            vm = controlVM,
            videoFloat = {
                LaunchedEffect(key1 = CartoonPlayingManager.state) {
                    when (CartoonPlayingManager.state) {
                        is CartoonPlayingManager.PlayingState.Playing -> {
                            it.onPrepare()
                        }

                        is CartoonPlayingManager.PlayingState.Loading -> {}
                        is CartoonPlayingManager.PlayingState.Error -> {}
                        else -> {}
                    }
                }
                when (val state = CartoonPlayingManager.state) {
                    is CartoonPlayingManager.PlayingState.Playing -> {}
                    is CartoonPlayingManager.PlayingState.Loading -> {
                        LoadingPage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { }
                        )
                    }

                    is CartoonPlayingManager.PlayingState.Error -> {
                        ErrorPage(
                            modifier = Modifier
                                .fillMaxSize(),
                            errorMsg = state.errMsg,
                            clickEnable = true,
                            onClick = {
                                CartoonPlayingManager.defaultScope.launch {
                                    CartoonPlayingManager.refresh()
                                }
                            }
                        )
                    }

                    else -> {}
                }

            },
            control = {
                Box(modifier = Modifier
                    .fillMaxSize()) {

                    SimpleTopBar(
                        vm = it,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                    )
                    SimpleBottomBar(
                        vm = it,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    ) {

                    }


                    LockBtn(vm = it)
                    
                    ProgressBox(vm = it)
                }
            }
        ) {
            CartoonPlayUI(
                detailedVM = detailedVM,
                cartoonPlayVM = cartoonPlayVM,
            )
        }
    }

}


@Composable
fun CartoonPlayUI(
    detailedVM: DetailedViewModel,
    cartoonPlayVM: CartoonPlayViewModel,
) {
    when (val detailedState = detailedVM.detailedState) {
        is DetailedViewModel.DetailedState.Info -> {
            CartoonPlayDetailed(
                modifier = Modifier.fillMaxSize(),
                cartoon = detailedState.detail,
                playLines = detailedState.playLine,
                selectLineIndex = cartoonPlayVM.selectedLineIndex,
                playingPlayLine = CartoonPlayingManager.state.playLine(),
                playingEpisode = CartoonPlayingManager.state.episode(),
                onLineSelect = {
                    cartoonPlayVM.selectedLineIndex = it
                },
                onEpisodeClick = { _, playLine, episode ->
                    if (CartoonPlayingManager.state.playLine() == playLine) {
                        CartoonPlayingManager.defaultScope.launch {
                            CartoonPlayingManager.changeEpisode(episode, 0L)
                        }
                    } else {
                        CartoonPlayingManager.defaultScope.launch {
                            CartoonPlayingManager.changeLine(
                                detailedState.detail.source,
                                detailedVM.cartoonSummary,
                                playLine,
                                defaultEpisode = episode,
                                defaultProgress = 0L
                            )
                        }
                    }
                },
                isStar = detailedVM.isStar,
                onStar = {
                    detailedVM.setCartoonStar(it, detailedState.detail)
                },
                onSearch = {
                    TODO("搜索同名番")
                },
                onWeb = {
                    TODO("打开原网站")
                },
                onDlna = {
                    TODO("投屏")
                }
            )
        }

        is DetailedViewModel.DetailedState.Error -> {
            ErrorPage(
                modifier = Modifier.fillMaxSize(),
                errorMsg = detailedState.errorMsg,
                clickEnable = true,
                onClick = {
                    detailedVM.load()
                },
                other = { Text(text = stringResource(id = com.heyanle.easy_i18n.R.string.click_to_retry)) }
            )
        }

        is DetailedViewModel.DetailedState.Loading -> {
            LoadingPage(
                modifier = Modifier.fillMaxSize()
            )
        }

        else -> {}
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartoonPlayDetailed(
    modifier: Modifier,
    cartoon: Cartoon,
    playLines: List<PlayLine>,
    selectLineIndex: Int,
    playingPlayLine: PlayLine?,
    playingEpisode: Int,
    onLineSelect: (Int) -> Unit,
    onEpisodeClick: (Int, PlayLine, Int) -> Unit,

    isStar: Boolean,
    onStar: (Boolean) -> Unit,
    onSearch: () -> Unit,
    onWeb: () -> Unit,
    onDlna: () -> Unit,
) {

    // 将非空的 播放线路 下标存成离散序列
    val unEmptyLinesIndex = remember(playLines) {
        arrayListOf<Int>().apply {
            playLines.forEachIndexed { index, playLine ->
                if (playLine.episode.isNotEmpty()) {
                    add(index)
                }
            }
        }
    }

    LaunchedEffect(key1 = playLines, key2 = selectLineIndex) {
        if (!unEmptyLinesIndex.contains(selectLineIndex) && unEmptyLinesIndex.isNotEmpty()) {
            onLineSelect(unEmptyLinesIndex[0])
        }
    }



    Column(modifier = modifier) {

        Text(
            modifier = Modifier.padding(8.dp, 4.dp),
            text = cartoon.title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Divider()

        LazyVerticalGrid(columns = GridCells.Adaptive(128.dp)) {
            item(
                span = {
                    // LazyGridItemSpanScope:
                    // maxLineSpan
                    GridItemSpan(maxLineSpan)
                }
            ) {
                CartoonDescCard(cartoon = cartoon)
            }

            item(
                span = {
                    // LazyGridItemSpanScope:
                    // maxLineSpan
                    GridItemSpan(maxLineSpan)
                }
            ) {
                CartoonActions(
                    isStar = isStar,
                    onStar = onStar,
                    onSearch = onSearch,
                    onWeb = onWeb,
                    onDlna = onDlna
                )
            }

            if (unEmptyLinesIndex.isEmpty()) {
                item(
                    span = {
                        // LazyGridItemSpanScope:
                        // maxLineSpan
                        GridItemSpan(maxLineSpan)
                    }
                ) {
                    EmptyPage(
                        modifier = Modifier.fillMaxWidth(),
                        emptyMsg = stringResource(id = com.heyanle.easy_i18n.R.string.no_play_line)
                    )
                }
            } else {
                item(
                    span = {
                        // LazyGridItemSpanScope:
                        // maxLineSpan
                        GridItemSpan(maxLineSpan)
                    }
                ) {
                    ScrollableTabRow(selectedTabIndex = 0.coerceAtLeast(
                        unEmptyLinesIndex.indexOf(
                            selectLineIndex
                        )
                    )
                    ) {
                        unEmptyLinesIndex.forEach { index ->
                            val playLine = playLines[index]
                            Tab(
                                selected = index == selectLineIndex,
                                onClick = {
                                    onLineSelect(index)
                                },
                                text = {
                                    Text(text = playLine.label)
                                }
                            )
                        }
                    }
                }

                if (selectLineIndex >= 0 && selectLineIndex < playLines.size && unEmptyLinesIndex.contains(
                        selectLineIndex
                    )
                ) {
                    itemsIndexed(playLines[selectLineIndex].episode) { index, item ->

                        Box(modifier = Modifier.padding(2.dp)) {
                            FilterChip(
                                modifier = Modifier.fillMaxSize(),
                                selected = playLines[selectLineIndex] == playingPlayLine && index == playingEpisode,
                                onClick = {
                                    onEpisodeClick(
                                        selectLineIndex,
                                        playLines[selectLineIndex],
                                        index
                                    )
                                },
                                label = { Text(item) },
                                colors = FilterChipDefaults.filterChipColors(),
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CartoonDescCard(
    cartoon: Cartoon
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OkImage(
            modifier = Modifier
                .width(95.dp)
                .aspectRatio(19 / 27F)
                .clip(RoundedCornerShape(4.dp)),
            image = cartoon.coverUrl,
            contentDescription = cartoon.title
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = cartoon.description ?: cartoon.intro ?: "",
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun CartoonActions(
    isStar: Boolean,
    onStar: (Boolean) -> Unit,
    onSearch: () -> Unit,
    onWeb: () -> Unit,
    onDlna: () -> Unit,
) {
    ActionRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        val starIcon =
            if (isStar) Icons.Filled.Star else Icons.Filled.StarOutline
        val starTextId =
            if (isStar) com.heyanle.easy_i18n.R.string.stared else com.heyanle.easy_i18n.R.string.click_star
        // 点击追番
        Action(
            icon = {
                Icon(
                    starIcon,
                    stringResource(id = starTextId),
                    tint = if (isStar) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground
                )
            },
            msg = {
                Text(
                    text = stringResource(id = starTextId),
                    color = if (isStar) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp
                )
            },
            onClick = {
                onStar(!isStar)
            }
        )

        // 搜索同名番
        Action(
            icon = {
                Icon(
                    Icons.Filled.Search,
                    stringResource(id = com.heyanle.easy_i18n.R.string.search_same_bangumi)
                )
            },
            msg = {
                Text(
                    text = stringResource(id = com.heyanle.easy_i18n.R.string.search_same_bangumi),
                    fontSize = 12.sp
                )
            },
            onClick = onSearch
        )

        // 打开原网站
        Action(
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_webview_24dp),
                    stringResource(id = com.heyanle.easy_i18n.R.string.open_source_url)
                )
            },
            msg = {
                Text(
                    text = stringResource(id = com.heyanle.easy_i18n.R.string.open_source_url),
                    fontSize = 12.sp
                )
            },
            onClick = onWeb
        )

        // 投屏
        Action(
            icon = {
                Icon(
                    Icons.Filled.CastConnected,
                    stringResource(id = com.heyanle.easy_i18n.R.string.screen_cast)
                )
            },
            msg = {
                Text(
                    text = stringResource(id = com.heyanle.easy_i18n.R.string.screen_cast),
                    fontSize = 12.sp
                )
            },
            onClick = onDlna
        )
    }


}






