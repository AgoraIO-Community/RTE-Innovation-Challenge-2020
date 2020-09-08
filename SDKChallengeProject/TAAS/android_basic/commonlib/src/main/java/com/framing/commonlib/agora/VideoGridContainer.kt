package com.framing.commonlib.agora

import android.os.Handler
import android.util.SparseArray
import android.view.ViewGroup
import com.framing.commonlib.agora.status.StatsManager
import java.util.*

/*
 * Des  
 * Author Young
 * Date 
 */
open class VideoGridContainer {

    private val MAX_USER = 4
    private val STATS_REFRESH_INTERVAL = 2000
    private val STAT_LEFT_MARGIN = 34
    private val STAT_TEXT_SIZE = 10

    private val mUserViewList = SparseArray<ViewGroup>(MAX_USER)
    private val mUidList: List<Int> = ArrayList(MAX_USER)
    private val mStatsManager: StatsManager? = null
    private val mHandler: Handler? = null
    private val mStatMarginBottom = 0
}