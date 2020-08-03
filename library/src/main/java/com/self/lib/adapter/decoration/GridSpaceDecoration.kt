package com.self.lib.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * GridLayoutManager下RecyclerView分割线
 */
class GridSpaceDecoration : ItemDecoration {

    private var horizontalSpace: Int
    private var verticalSpace: Int
    private var startPosition = 0
    private var endPosition = -1
    private var includeBorder = false

    /**
     * 创建水平和垂直方向相同距离的分割线
     * @param itemSpace Int     分割线宽度/高度
     * @param includeBorder Boolean  是否在RecyclerView的边界绘制分割线
     * @constructor
     */
    constructor(itemSpace: Int,includeBorder : Boolean = false) {
        horizontalSpace = itemSpace
        verticalSpace = itemSpace
        this.includeBorder = includeBorder
    }

    /**
     * 创建分割线
     * @param horizontalSpace Int  水平分割宽度
     * @param verticalSpace Int   垂直分割高度
     * @param includeBorder Boolean 是否在RecyclerView的边界绘制分割线
     * @constructor
     */
    constructor(horizontalSpace: Int, verticalSpace: Int,includeBorder : Boolean = false) {
        this.horizontalSpace = horizontalSpace
        this.verticalSpace = verticalSpace
        this.includeBorder = includeBorder
    }

    fun setStartPosition(startPosition: Int) {
        if(startPosition<0) return
        this.startPosition = startPosition
    }

    fun setEndPosition(endPosition: Int) {
        if(endPosition < 0) return
        this.endPosition = endPosition
    }

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: State
    ) {
        if (parent.adapter == null) return

        val position = parent.getChildAdapterPosition(view)
        if (position < startPosition || (endPosition > -1 && endPosition < position)) {
            return
        }

        val spanCount = when (val layoutManager = parent.layoutManager) {
            is StaggeredGridLayoutManager -> {
                layoutManager.spanCount
            }
            is GridLayoutManager -> {
                layoutManager.spanCount
            }
            else -> {
                return
            }
        }

        val itemCount = parent.adapter!!.itemCount
        //行数
        val line = itemCount / spanCount + if(itemCount % spanCount == 0) 0 else 1

        if(includeBorder){
            //Horizontal
            outRect.left = horizontalSpace * (spanCount - position % spanCount) / spanCount
            outRect.right  = horizontalSpace * (position % spanCount + 1) / spanCount
            //Vertical
            outRect.top = verticalSpace * (line - position / spanCount) / line
            outRect.bottom = verticalSpace * (position / spanCount + 1) / line
        }else{
            //Horizontal
            outRect.left = horizontalSpace * (position % spanCount) / spanCount
            outRect.right = horizontalSpace * (spanCount - 1 - position % spanCount) / spanCount
            //Vertical
            outRect.top = verticalSpace * (position / spanCount) / line
            outRect.bottom = verticalSpace * (line - 1 - position / spanCount) / line
        }
    }
}