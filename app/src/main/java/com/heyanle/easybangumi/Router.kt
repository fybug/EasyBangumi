package com.heyanle.easybangumi

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.heyanle.bangumi_source_api.api.entity.Bangumi
import com.heyanle.easybangumi.ui.home.Home
import com.heyanle.easybangumi.ui.player.BangumiPlayController
import com.heyanle.easybangumi.ui.search.Search
import com.heyanle.easybangumi.ui.player.Play
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Created by HeYanLe on 2023/1/7 13:38.
 * https://github.com/heyanLE
 */

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("AppNavController Not Provide")
}

const val NAV = "nav"

const val HOME = "home"
const val SEARCH = "search"

const val PLAY = "play"

// 缺省路由
const val DEFAULT = HOME

fun NavHostController.navigationSearch(keyword: String) {
    navigate("${SEARCH}?keyword=${keyword}")
}

fun NavHostController.navigationSearch(keyword: String, source: String) {
    navigate("${SEARCH}?keyword=${keyword}&source=${source}")
}

fun NavHostController.navigationPlay(bangumi: Bangumi) {
    navigationPlay(bangumi.source, bangumi.detailUrl)
}

fun NavHostController.navigationPlay(
    source: String,
    detailUrl: String,
    linesIndex: Int = -1,
    episode: Int = -1,
    startPosition: Long = -1L
) {
    val uel = URLEncoder.encode(detailUrl, "utf-8")
    navigate("${PLAY}/${source}/${uel}?linesIndex=${linesIndex}&episode=${episode}&startPosition=${startPosition}")
}

fun NavHostController.navigationPlay(source: String, detailUrl: String) {
    val uel = URLEncoder.encode(detailUrl, "utf-8")
    navigate("${PLAY}/${source}/${uel}")
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Nav() {
    val nav = rememberAnimatedNavController()
    CompositionLocalProvider(LocalNavController provides nav) {
        AnimatedNavHost(nav, DEFAULT,
            enterTransition = { slideInHorizontally(tween()) { it } },
            exitTransition = { slideOutHorizontally(tween()) { -it } + fadeOut(tween()) },
            popEnterTransition = { slideInHorizontally(tween()) { -it } },
            popExitTransition = { slideOutHorizontally(tween()) { it } })
        {

            composable(HOME) {
                Home()
            }

            composable(
                "${SEARCH}?keyword={keyword}&sourceIndex={sourceIndex}",
                arguments = listOf(
                    navArgument("keyword") { defaultValue = "" },
                    navArgument("source") {
                        defaultValue = ""
                    }
                )
            ) {
                Search(it.arguments?.getString("keyword") ?: "", it.arguments?.getString("source")?:"")
            }

            composable(
                "${PLAY}/{source}/{detailUrl}?linesIndex={linesIndex}&episode={episode}&startPosition={startPosition}",
                arguments = listOf(
                    navArgument("source") { defaultValue = "" },
                    navArgument("detailUrl") { defaultValue = "" },
                    navArgument("linesIndex") {
                        defaultValue = -1
                        type = NavType.IntType
                    },
                    navArgument("episode") {
                        defaultValue = -1
                        type = NavType.IntType
                    },
                    navArgument("startPosition"){
                        defaultValue = -1L
                        type = NavType.LongType
                    },
                ),
                deepLinks = listOf(navDeepLink {
                    uriPattern = "${NAV}://${PLAY}/{source}/{detailUrl}"
                }),
            ) {
                val source = it.arguments?.getString("source") ?: ""
                val detailUrl = it.arguments?.getString("detailUrl") ?: ""
                val linesIndex = it.arguments?.getInt("linesIndex")?:-1
                val episode = it.arguments?.getInt("episode")?:-1
                val startPosition = it.arguments?.getLong("startPosition")?:-1L

                val enterData = BangumiPlayController.EnterData(
                    sourceIndex = linesIndex,
                    episode = episode,
                    startProcess = startPosition

                )

                Play(source = source, detail = URLDecoder.decode(detailUrl, "utf-8"), enterData = enterData)
            }


        }
    }
}
